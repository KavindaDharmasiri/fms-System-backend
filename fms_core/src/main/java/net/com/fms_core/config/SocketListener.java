/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.config;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.controller.RiskController;
import net.com.fms_core.dto.message.IsoMessageDTO;
import net.com.fms_core.util.SocketMethods;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.packager.GenericPackager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
@Slf4j
@Component
@RequiredArgsConstructor
public class SocketListener implements CommandLineRunner {
    private final RiskController riskController;
    private final SocketMethods SocketMethods;
    int port = 5000;
    @Override
    public void run(String... args) {
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress("0.0.0.0", port));
            riskController.setRules();
            log.info("✅ FMS Receiver is listening on port {}", port);
            while (true) {
                Socket socket = serverSocket.accept();
                log.info("🔗 Connected to sender: {}", socket.getInetAddress());
                new Thread(() -> handleClient(socket)).start();
            }
        } catch (Exception e) {
            log.error("❌ Error in FMS Receiver: {}", e.getMessage(), e);
        }
    }
    private void handleClient(Socket socket) {
        try (DataInputStream input = new DataInputStream(socket.getInputStream())) {
            while (true) {
                byte[] lengthBytes = new byte[4];
                input.readFully(lengthBytes);
                int messageLength = Integer.parseInt(new String(lengthBytes).trim());
                byte[] messageBytes = new byte[messageLength];
                input.readFully(messageBytes);
                String message = new String(messageBytes).trim();
                log.info("📨 Received raw message: {}", message);
                handleMessage(message);
            }
        } catch (Exception e) {
            log.error("❌ Error while reading message from {}: {}", socket.getInetAddress(), e.getMessage(), e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                log.error("❌ Failed to close socket: {}", e.getMessage(), e);
            }
        }
    }
    public String handleMessage(String message) {
        try{
            log.info("Received packet: {}", message);
            log.info("Message length: {}", message.length());
            
            byte[] packetBytes = ISOUtil.hex2byte(message);
            log.info("Packet bytes length: {}", packetBytes.length);
            log.info("First 20 bytes: {}", ISOUtil.hexString(packetBytes, 0, Math.min(20, packetBytes.length)));
            
            ClassPathResource resource = new ClassPathResource("isoXML/visapack.xml");
            InputStream is = resource.getInputStream();
            GenericPackager packager = new GenericPackager(is);
            
            ISOMsg m = new ISOMsg();
            m.setPackager(packager);
            m.unpack(packetBytes);
            
            IsoMessageDTO isoMessageDTO = SocketMethods.mapToDto(m);
            riskController.executeTransactions(isoMessageDTO);
            log.info("Processed message: {}", isoMessageDTO.toString());
            return "fms Core Service received: " + message;
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}

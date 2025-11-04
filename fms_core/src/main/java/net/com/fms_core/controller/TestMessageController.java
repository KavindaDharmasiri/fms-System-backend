/**
 * ALL RIGHT RESERVED By kavinda_d
 *
 * @AUTHOR : kavinda_d
 * @PROJECT : fms backend
 */
package net.com.fms_core.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.com.fms_core.dto.VisaPacketDTO;
import net.com.fms_core.dto.message.*;
import net.com.fms_core.service.DynamicDroolsService;
import net.com.fms_core.util.SocketMethods;
import net.com.fms_core.util.SysConfigValues;
import net.com.fms_core.util.UtilMethods;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.packager.GenericPackager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
@Controller
public class TestMessageController {
    private final RiskController riskController;
    private final SocketMethods SocketMethods;
    public TestMessageController(RiskController riskController, net.com.fms_core.util.SocketMethods socketMethods) {
        this.riskController = riskController;
        SocketMethods = socketMethods;
    }
    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public String handleMessage(String message) {
        try{
            log.info("Received packet: {}", message);
            System.out.println("message buffer1:" + message);
            byte[] packetBytes = ISOUtil.hex2byte(message);
            System.out.println("message buffer3:" + packetBytes);
            ClassPathResource resource = new ClassPathResource("isoXML/visapack.xml");
            InputStream is = resource.getInputStream();
            GenericPackager packager;
            packager  = new GenericPackager(is);
            ISOMsg m = new ISOMsg();
            m.setPackager(packager);
            m.unpack(packetBytes);
            IsoMessageDTO isoMessageDTO = SocketMethods.mapToDto(m);
            riskController.executeTransactions(isoMessageDTO);
            System.out.println(isoMessageDTO.toString());
        return "fms Core Service received: " + message;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

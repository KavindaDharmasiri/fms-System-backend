package net.com.fms_core.config;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class JasperReportsConfig {

    @Bean
    public DefaultJasperReportsContext jasperReportsContext() {
        DefaultJasperReportsContext context = DefaultJasperReportsContext.getInstance();
        
        // Configure JasperReports properties
        JRPropertiesUtil propertiesUtil = JRPropertiesUtil.getInstance(context);
        
        // Set default font to handle Unicode characters
        propertiesUtil.setProperty("net.sf.jasperreports.default.font.name", "DejaVu Sans");
        propertiesUtil.setProperty("net.sf.jasperreports.default.pdf.font.name", "DejaVu Sans");
        propertiesUtil.setProperty("net.sf.jasperreports.default.pdf.encoding", "UTF-8");
        propertiesUtil.setProperty("net.sf.jasperreports.default.pdf.embedded", "true");
        
        // Enable font extensions
        propertiesUtil.setProperty("net.sf.jasperreports.extension.registry.factory.fonts", 
            "net.sf.jasperreports.engine.fonts.SimpleFontExtensionRegistryFactory");
        
        // Configure image handling
        propertiesUtil.setProperty("net.sf.jasperreports.awt.ignore.missing.font", "true");
        
        log.info("JasperReports context configured successfully");
        return context;
    }
}
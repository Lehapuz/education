package main.config;

import main.model.ModerationStatus;
import main.model.PostModerationStatus;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new PostModerationStatus.StringToEnumConverter());
        registry.addConverter(new ModerationStatus.StringToEnumConverter());
    }
}

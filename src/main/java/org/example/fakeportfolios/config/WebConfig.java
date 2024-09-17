package org.example.fakeportfolios.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://my-angular-app.com")  // Allow requests from this domain
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true);  // Allow cookies if using sessions
    }
}

package com.crossover.siteactivity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

import static springfox.documentation.builders.PathSelectors.any;

@SpringBootApplication
@EnableScheduling
@EnableSwagger2
public class SiteActivityApplication {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.crossover.siteactivity")).paths(any()).build()
                .apiInfo(new ApiInfo("Site Activity Reporting Service API's",
                        "API's for Site Activity Reporting Service", "1.0", null,
                        new Contact("Crossover ","https://www.crossover.com", ""),
                        null, null, new ArrayList()));
    }
    public static void main(String[] args) {
        SpringApplication.run(SiteActivityApplication.class, args);
    }

}

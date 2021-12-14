package com.cobaltcourse.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan(basePackages = "com.cobaltcourse.web")
@EnableWebMvc
public class WebConfiguration {
}

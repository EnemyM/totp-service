package com.mservice.totp.api.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by anton on 15/12/16.
 */
@Configuration
@EnableWebMvc
@ComponentScan({"com.mservice.totp"})
@PropertySource("classpath:totp.properties")
public class ApplicationContext {
}

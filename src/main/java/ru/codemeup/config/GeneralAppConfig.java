package ru.codemeup.config;

import org.springframework.context.annotation.*;

@ComponentScan("ru.codemeup")
@Configuration
@PropertySource("classpath:application.properties")
public class GeneralAppConfig {
}

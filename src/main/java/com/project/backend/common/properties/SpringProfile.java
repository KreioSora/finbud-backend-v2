package com.project.backend.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("spring.properties")
@Getter
@Setter
public class SpringProfile {

    private String activeEnv;

}

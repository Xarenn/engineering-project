package com.rlchat.server.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("server")
public class ServerProperties {

    private String signingKey;
    private String stripeWebhookEvent;

    //IN SECONDS
    private Integer accessTokenValidity;

    private String adminLogin;
    private String adminPassword;

}

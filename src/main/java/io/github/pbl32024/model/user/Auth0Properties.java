package io.github.pbl32024.model.user;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("backend.user.auth0")
public class Auth0Properties {

	private String clientId;

	private String clientSecret;

	private String url;

}

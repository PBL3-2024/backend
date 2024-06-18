package io.github.pbl32024.model.user;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("backend.user.auth0")
public class Auth0Properties {

	public String clientId;

	public String clientSecret;

	public String url;

}

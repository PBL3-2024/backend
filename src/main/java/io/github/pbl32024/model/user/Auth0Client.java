package io.github.pbl32024.model.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Auth0Client {

	private final Auth0Properties auth0Properties;

	public void deleteUserProfile(String id) {

	}

}

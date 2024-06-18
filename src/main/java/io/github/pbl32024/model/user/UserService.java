package io.github.pbl32024.model.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

	private final Auth0Client auth0Client;

	private final ProfileDAO profileDAO;

	public Profile updateUserProfile(Profile userProfile) {
		return null;
	}

	public Profile getUserProfile(String id) {
		return null;
	}

	public void deleteUserProfile(String id) {

	}

}

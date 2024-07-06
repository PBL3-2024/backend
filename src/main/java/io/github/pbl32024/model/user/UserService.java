package io.github.pbl32024.model.user;


import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

	private final ProfileDAO profileDAO;

	public Profile updateUserProfile(Profile userProfile) {
		userProfile.setNew(getUserProfile(userProfile.getId()).isEmpty());
		return profileDAO.save(userProfile);
	}

	public Optional<Profile> getUserProfile(String id) {
		return profileDAO.getProfileById(id);
	}

	public void deleteUserProfile(String id) {

	}

}

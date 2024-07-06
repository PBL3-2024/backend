package io.github.pbl32024.model.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProfileDAO {

	private final ProfileRepository profileRepository;

	public Profile save(Profile userProfile) {
		return profileRepository.save(userProfile);
	}

	public Optional<Profile> getProfileById(String id) {
		return profileRepository.findById(id);
	}

	public void deleteProfileById(String id) {

	}

}

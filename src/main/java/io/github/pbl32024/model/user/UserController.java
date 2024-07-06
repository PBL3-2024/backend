package io.github.pbl32024.model.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class UserController {

	private final UserService userService;

	@PreAuthorize("#principal.subject.equals(#userProfile.id) || #userProfile.id == null")
	@PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Profile updateUserProfile(@AuthenticationPrincipal Jwt principal, @RequestBody Profile userProfile) {
		userProfile.setId(principal.getSubject());
		return userService.updateUserProfile(userProfile);
	}

	@GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Profile> getCurrentUserProfile(@AuthenticationPrincipal Jwt principal) {
		return userService.getUserProfile(principal.getSubject())
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping(path = "/")
	public void deleteCurrentUserProfile() {

	}

}

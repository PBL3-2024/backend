package io.github.pbl32024.model.user;

import lombok.Data;

@Data
public class Profile {

	private String id;

	private String name;

	private String email;

	private String postalCode;

	private String currentSocCode;

	private String goalSocCode;

	private boolean termsOfUseConsent;

}

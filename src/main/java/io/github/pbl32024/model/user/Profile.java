package io.github.pbl32024.model.user;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("profile")
public class Profile implements Persistable<String> {

	@Id
	private String id;

	private String name;

	private String email;

	@Column("postal_code")
	private String postalCode;

	@Column("current_soc")
	private String currentSocCode;

	@Column("goal_soc")
	private String goalSocCode;

	@Column("tos_consent")
	private boolean termsOfUseConsent;

	@Transient
	private boolean isNew;

	@Override
	public boolean isNew() {
		return isNew;
	}
}

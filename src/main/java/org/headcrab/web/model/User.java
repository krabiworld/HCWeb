package org.headcrab.web.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Length(message = "Please enter more than 2 characters.", min = 2)
	@NotEmpty(message = "Please enter username.")
	@Column(name = "username", nullable = false)
	private String username;

	@Length(message = "Please enter more than 8 characters.", min = 8)
	@NotEmpty(message = "Please enter password.")
	@Column(name = "password", nullable = false)
	private String password;

	@Transient
	@Length(message = "Please enter more than 8 characters.", min = 8)
	private String retypePassword;

	@Length(message = "Please enter more than 4 characters.", min = 4)
	@NotEmpty(message = "Please enter email.")
	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "enabled", nullable = false)
	private boolean enabled = false;

	@Column(name = "role", nullable = false)
	private String role = "USER";

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

}

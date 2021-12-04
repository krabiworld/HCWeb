package org.headcrab.web.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "confirmation_tokens")
@Getter
@Setter
public class Token {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
	private User user;

	@Column(name = "token", nullable = false)
	private String token;

	@Column(name = "type", nullable = false)
	private String type;

}

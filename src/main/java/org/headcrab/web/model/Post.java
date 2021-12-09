package org.headcrab.web.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "posts")
@Setter
@Getter
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Length(message = "Please enter more than 2 characters.", min = 2)
	@NotEmpty(message = "Please enter post title.")
	@Column(name = "title", nullable = false)
	private String title;

	@Length(message = "Please enter more than 10 characters.", min = 10)
	@NotEmpty(message = "Please enter post content.")
	@Column(name = "content", nullable = false)
	private String content;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date", nullable = false, updatable = false)
	private Date date;

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
	private User user;

}

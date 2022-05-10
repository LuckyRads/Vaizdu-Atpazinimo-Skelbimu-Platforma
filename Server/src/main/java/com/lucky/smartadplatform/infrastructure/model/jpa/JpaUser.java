package com.lucky.smartadplatform.infrastructure.model.jpa;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class JpaUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String username;

	@NotNull
	private String email;

	@NotNull
	private String password;

	@NotNull
	@ManyToMany
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<JpaRole> roles = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "owner", orphanRemoval = true)
	private List<JpaItem> items;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "owner", orphanRemoval = true)
	private List<JpaImage> images;

	public JpaUser(String email, String hashedPassword) {
		this.email = email;
		this.password = hashedPassword;
	}

	public void addItem(JpaItem item) {
		this.items.add(item);
	}

	public void addImage(JpaImage jpaImage) {
		this.images.add(jpaImage);
	}

}

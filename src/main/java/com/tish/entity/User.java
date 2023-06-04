package com.tish.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {

	private Integer id;
	private String login;
	private String password;
	private String name;
	private String currentMaterial;
	private Boolean isLoggedIn;

	public User() {
	}
}

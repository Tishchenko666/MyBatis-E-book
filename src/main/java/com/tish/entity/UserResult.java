package com.tish.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResult {

	private Integer id;
	private Integer userId;
	private String material;
	private Integer questionId;
	private String question;
	private String answer;
	private Integer result;

	public UserResult() {
	}
}

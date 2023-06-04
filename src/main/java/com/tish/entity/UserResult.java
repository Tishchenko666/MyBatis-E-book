package com.tish.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResult {

	private Integer id;
	private Integer userId;
	private Integer questionId;
	private String answer;
	private Boolean result;

	public UserResult() {
	}
}

package com.tish.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Test {

	private Integer id;
	private Integer materialId;
	private Integer type;
	private String question;
	private String variants;
	private String answer;

	public Test() {
	}
}

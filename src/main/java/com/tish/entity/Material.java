package com.tish.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Material {

	private Integer id;
	private String order;
	private String title;
	private String data;

	public Material() {
	}
}

package com.sparta.barointern.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Role {

	USER,
	ADMIN;

	public String getRole() {
		return name();
	}
}

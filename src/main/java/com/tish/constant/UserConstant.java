package com.tish.constant;

public class UserConstant {

	public static final String CHECK_USER_BY_EMAIL = "select * from users where login = ?";
	public static final String LOGIN_USER = "update users set isLoggedIn = 1 where id = ?";
	public static final String CHECK_LOGGED_USER = "select * from users where isLoggedIn = 1";
	public static final String REGISTER_USER = "insert into users(login, password, name, current_material, isLoggedIn) values (?,?,?,?,?)";
}

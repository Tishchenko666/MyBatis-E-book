package com.tish.constant;

public class UserConstant {

	public static final String CHECK_USER_BY_EMAIL = "select * from users where login = ?";
	public static final String LOGIN_USER = "update users set isLoggedIn = 1 where id = ?";
	public static final String LOGOUT_USER = "update users set isLoggedIn = 0 where login = ?";
	public static final String CHECK_LOGGED_USER = "select * from users where isLoggedIn = 1";
	public static final String REGISTER_USER = "insert into users(login, password, name, current_material, isLoggedIn) values (?,?,?,?,?)";
	public static final String READ_USER_RESULTS = "select ur.id, ur.user_id, m.title, t.question, ur.answer, ur.result from users_results ur inner join tests t on t.id = ur.question_id inner join materials m on m.\"order\" = t.material_order where ur.user_id = ?";
	public static final String UPDATE_USER_DATA = "update users set name = ?, password = ? where login = ?";
}

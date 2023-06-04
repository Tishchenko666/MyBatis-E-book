package com.tish.mapper;

import com.tish.entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {
	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();

		user.setId(rs.getInt("id"));
		user.setLogin(rs.getString("login"));
		user.setPassword(rs.getString("password"));
		user.setName(rs.getString("name"));
		user.setCurrentMaterial(rs.getString("current_material"));
		user.setIsLoggedIn(rs.getInt("isLoggedIn") == 1);

		return user;
	}
}

package com.tish.dao;

import com.tish.constant.UserConstant;
import com.tish.entity.User;
import com.tish.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;

@Repository
public class UserDaoImpl implements UserDao {

	private final JdbcTemplate jdbcTemplate;

	public UserDaoImpl(@Autowired JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public User checkIfUserExists(String email) {
		return jdbcTemplate.queryForObject(UserConstant.CHECK_USER_BY_EMAIL, new Object[]{email}, new int[]{Types.VARCHAR}, new UserMapper());
	}

	@Override
	public void loginByUser(User user) {
		jdbcTemplate.update(UserConstant.LOGIN_USER, user.getId());
	}

	@Override
	public User checkIfLoggedUserExists() {
		return jdbcTemplate.queryForObject(UserConstant.CHECK_LOGGED_USER, new UserMapper());
	}

	@Override
	public void registerUser(User user) {
		jdbcTemplate.update(UserConstant.REGISTER_USER, user.getLogin(), user.getPassword(), user.getName(), "0", 0);
	}
}

package com.tish.dao;

import com.tish.constant.UserConstant;
import com.tish.entity.Test;
import com.tish.entity.User;
import com.tish.entity.UserResult;
import com.tish.mapper.TestMapper;
import com.tish.mapper.UserMapper;
import com.tish.mapper.UserResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

	private final JdbcTemplate jdbcTemplate;

	public UserDaoImpl(@Autowired JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public User checkIfUserExists(String email) {
		try {
			return jdbcTemplate.queryForObject(UserConstant.CHECK_USER_BY_EMAIL, new Object[]{email}, new int[]{Types.VARCHAR}, new UserMapper());
		} catch (DataAccessException e) {
			return null;
		}
	}

	@Override
	public void loginByUser(User user) {
		jdbcTemplate.update(UserConstant.LOGIN_USER, user.getId());
	}

	@Override
	public void logoutByEmail(String email) {
		jdbcTemplate.update(UserConstant.LOGOUT_USER, email);
	}

	@Override
	public User checkIfLoggedUserExists() {
		try {
			return jdbcTemplate.queryForObject(UserConstant.CHECK_LOGGED_USER, new UserMapper());
		} catch (DataAccessException e) {
			return null;
		}
	}

	@Override
	public void registerUser(User user) {
		jdbcTemplate.update(UserConstant.REGISTER_USER, user.getLogin(), user.getPassword(), user.getName(), "0", 0);
	}

	@Override
	public List<UserResult> readUserResultsById(Integer id) {
		return jdbcTemplate.query(UserConstant.READ_USER_RESULTS, new Object[]{id}, new int[]{Types.INTEGER}, new UserResultMapper());
	}

	@Override
	public void updateUserData(User user) {
		jdbcTemplate.update(UserConstant.UPDATE_USER_DATA, user.getName(), user.getPassword(), user.getLogin());
	}
}

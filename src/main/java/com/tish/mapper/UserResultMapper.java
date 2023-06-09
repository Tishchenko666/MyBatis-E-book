package com.tish.mapper;

import com.tish.entity.UserResult;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserResultMapper implements RowMapper<UserResult> {
	@Override
	public UserResult mapRow(ResultSet rs, int rowNum) throws SQLException {

		UserResult userResult = new UserResult();

		userResult.setId(rs.getInt("id"));
		userResult.setUserId(rs.getInt("user_id"));
		userResult.setMaterial(rs.getString("title"));
		userResult.setQuestion(rs.getString("question"));
		userResult.setAnswer(rs.getString("answer"));
		userResult.setResult(rs.getInt("result"));

		return userResult;
	}
}

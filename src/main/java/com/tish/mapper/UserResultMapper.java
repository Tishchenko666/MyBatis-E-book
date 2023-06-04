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
		userResult.setQuestionId(rs.getInt("question_id"));
		userResult.setAnswer(rs.getString("answer"));
		userResult.setResult(rs.getInt("result") != 0);

		return userResult;
	}
}

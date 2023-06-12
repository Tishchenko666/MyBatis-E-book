package com.tish.mapper;

import com.tish.entity.Test;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TestMapper implements RowMapper<Test> {
	@Override
	public Test mapRow(ResultSet rs, int rowNum) throws SQLException {

		Test test = new Test();

		test.setId(rs.getInt("id"));
		test.setMaterialId(rs.getInt("material_id"));
		test.setType(rs.getInt("type"));
		test.setQuestion(rs.getString("question"));
		test.setVariants(rs.getString("variants"));
		test.setAnswer(rs.getString("answer"));

		return test;
	}
}

package com.tish.dao;

import com.tish.constant.TestConstant;
import com.tish.entity.Test;
import com.tish.entity.UserResult;
import com.tish.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;

@Repository
public class TestDaoImpl implements TestDao {

	private final JdbcTemplate jdbcTemplate;

	public TestDaoImpl(@Autowired JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


	@Override
	public List<Test> readTestsByMaterialId(Integer materialId) {
		return jdbcTemplate.query(TestConstant.READ_TESTS_BY_ID, new Object[]{materialId}, new int[]{Types.INTEGER}, new TestMapper());
	}

	@Override
	public void saveTestResult(UserResult userResult) {
		jdbcTemplate.update(TestConstant.SAVE_TEST_RESULT, userResult.getUserId(), userResult.getQuestionId(), userResult.getAnswer(), userResult.getResult());
	}
}

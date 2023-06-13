package com.tish.dao;

import com.tish.entity.Test;
import com.tish.entity.UserResult;

import java.util.List;

public interface TestDao {

	List<Test> readTestsByMaterialId(Integer materialId);

	void saveTestResult(UserResult userResult);
}

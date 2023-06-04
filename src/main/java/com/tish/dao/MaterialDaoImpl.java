package com.tish.dao;

import com.tish.constant.MaterialConstant;
import com.tish.entity.Material;
import com.tish.mapper.MaterialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;

@Repository
public class MaterialDaoImpl implements MaterialDao {

	private final JdbcTemplate jdbcTemplate;

	public MaterialDaoImpl(@Autowired JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


	@Override
	public Material readMaterialById(Integer id) {
		return jdbcTemplate.queryForObject(MaterialConstant.READ_MATERIAL_QUERY, new Object[]{id}, new int[]{Types.INTEGER}, new MaterialMapper());
	}
}

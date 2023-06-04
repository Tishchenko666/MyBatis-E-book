package com.tish.mapper;

import com.tish.entity.Material;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MaterialMapper implements RowMapper<Material> {
	@Override
	public Material mapRow(ResultSet rs, int rowNum) throws SQLException {

		Material material = new Material();

		material.setId(rs.getInt("id"));
		material.setOrder(rs.getString("order"));
		material.setTitle(rs.getString("title"));
		material.setData(rs.getString("data"));

		return material;
	}
}

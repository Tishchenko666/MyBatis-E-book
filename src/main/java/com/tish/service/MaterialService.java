package com.tish.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tish.dao.MaterialDao;
import com.tish.entity.Material;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Service
public class MaterialService {

	private final MaterialDao materialDao;
	private final ObjectMapper mapper;

	public MaterialService(@Autowired MaterialDao materialDao, @Autowired ObjectMapper mapper) {
		this.materialDao = materialDao;
		this.mapper = mapper;
	}

	public LinkedHashMap<String, String> readMaterialById(Integer id) {
		try {
			Material material = materialDao.readMaterialById(id);
			LinkedHashMap<String, String> map = mapper.readValue(material.getData(), LinkedHashMap.class);
			map.put("title", material.getTitle());
			return map;
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

}

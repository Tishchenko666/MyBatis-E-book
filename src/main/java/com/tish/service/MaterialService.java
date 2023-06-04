package com.tish.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tish.dao.MaterialDao;
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
			return mapper.readValue(materialDao.readMaterialById(id).getData(), LinkedHashMap.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

}

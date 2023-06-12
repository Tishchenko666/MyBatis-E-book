package com.tish.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tish.dao.MaterialDaoImpl;
import com.tish.entity.Material;
import com.tish.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

@Controller
//@RestController
public class TestController {
	private final ObjectMapper mapper;
	private final JdbcTemplate jdbcTemplate;
	private final MaterialDaoImpl materialDao;

	private final TestService testService;

	public TestController(@Autowired ObjectMapper objectMapper,
						  @Autowired JdbcTemplate jdbcTemplate,
						  @Autowired MaterialDaoImpl materialDao, TestService testService) {
		this.mapper = objectMapper;
		this.jdbcTemplate = jdbcTemplate;
		this.materialDao = materialDao;
		this.testService = testService;
	}

	@GetMapping("/test/{id}")
	public String openTest(Model model, @PathVariable Integer id) {

		Map<String, String> map = testService.openTest();
		Boolean isPublished = testService.publishForm(map.get("formId"), map.get("token"));

		return "redirect:https://docs.google.com/forms/d/" + map.get("formId");
	}


	@GetMapping("/")
	public String getAnswer(Model model) {
		Material material = materialDao.readMaterialById(1);
		LinkedHashMap<String, String> map;
		try {
			map = mapper.readValue(material.getData(), LinkedHashMap.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		/*map.put("surname1", "Tish");
		map.put("surname2", "Dan");
		map.put("name2", "Kate");
		map.put("name1", "Sofiia");*/
		model.addAttribute("myMap", map);
		return "index";
	}

	/*@GetMapping("/db")
	public List<User> testDB() {
		return jdbcTemplate.query("select * from users", new UserMapper());
	}*/

	@GetMapping("/materials")
	public Material testMaterials() {
		return materialDao.readMaterialById(1);
	}

}

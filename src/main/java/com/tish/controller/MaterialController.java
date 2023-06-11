package com.tish.controller;

import com.tish.entity.User;
import com.tish.service.MaterialService;
import com.tish.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = {"/material"})
public class MaterialController {

	private final MaterialService materialService;
	private final UserService userService;

	public MaterialController(@Autowired MaterialService materialService, @Autowired UserService userService) {
		this.materialService = materialService;
		this.userService = userService;
	}

	@GetMapping("/index")
	public String openFirstPage(Model model) {

		checkLoggedUser(model);
		model.addAttribute("materialMap", materialService.readMaterialById(1));

		return "index";
	}

	@GetMapping("/{id}")
	public String openMaterialPage(Model model, @PathVariable Integer id) {

		checkLoggedUser(model);

		model.addAttribute("materialMap", materialService.readMaterialById(id));

		return "material-page";
	}

	private void checkLoggedUser(Model model) {
		User user = userService.checkIfLoggedUserExists();
		boolean isLoggedIn = false;

		if (user != null) {
			isLoggedIn = true;
			model.addAttribute("userLogin", user.getLogin());
		}

		model.addAttribute("isLoggedIn", isLoggedIn);
	}

}

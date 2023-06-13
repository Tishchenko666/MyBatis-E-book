package com.tish.controller;

import com.tish.constant.TestConstant;
import com.tish.entity.User;
import com.tish.service.MaterialService;
import com.tish.service.TestService;
import com.tish.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping(path = {"/material"})
public class MaterialController {

	private final MaterialService materialService;
	private final UserService userService;
	private final TestService testService;

	public MaterialController(@Autowired MaterialService materialService,
							  @Autowired UserService userService,
							  @Autowired TestService testService) {
		this.materialService = materialService;
		this.userService = userService;
		this.testService = testService;
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

		Map<String, String> materialMap = materialService.readMaterialById(id);
		model.addAttribute("materialMap", materialMap);

		if (!TestConstant.currentFormId.isEmpty() && !TestConstant.currentToken.isEmpty()) {
			testService.readResponses(TestConstant.currentFormId, TestConstant.currentToken, model.containsAttribute("userLogin") ? model.getAttribute("userLogin").toString() : "", id);
		}

		if (!materialMap.get("title").contains("Creative")) {
			Map<String, String> map = testService.openTest(id);
			if (map != null) {
				Boolean isPublished = testService.publishForm(map.get("formId"), map.get("token"));
				TestConstant.currentFormId = map.get("formId");
				TestConstant.currentToken = map.get("token");
				model.addAttribute("formId", map.get("formId"));
			}
		} else {
			TestConstant.currentFormId = "";
			TestConstant.currentToken = "";
		}

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

package org.headcrab.web.controller;

import org.headcrab.web.model.User;
import org.headcrab.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class AdminController {

	private final UserService userService;

	@Autowired
	public AdminController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/admin")
	public String adminPage(Model model) {
		model.addAttribute("user", new User());
		return "admin";
	}

	@PostMapping("/admin/changeuserrole")
	public String changeUserRolePageLogic(@ModelAttribute User data, Model model) {
		Optional<User> optionalUser = userService.findByUsername(data.getUsername());

		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			user.setRole(data.getRole());

			userService.save(user);

			model.addAttribute("msg", "Done! New Role: " + data.getRole());
		} else {
			model.addAttribute("msg", "Error: Username not found.");
		}
		return "admin";
	}

}

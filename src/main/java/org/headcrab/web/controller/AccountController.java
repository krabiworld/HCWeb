package org.headcrab.web.controller;

import org.headcrab.web.model.User;
import org.headcrab.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
@SessionAttributes("user")
public class AccountController {

	private final UserService userService;

	@Autowired
	public AccountController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/account")
	public String accountPage(Model model, Principal principal) {
		Optional<User> optionalUser = userService.findByUsername(principal.getName());

		if (optionalUser.isPresent()) {
			model.addAttribute("user", optionalUser.get());
			return "account";
		} else {
			model.addAttribute("error", "User not found.");
			return "error";
		}
	}

	@PostMapping("/account/changeusername")
	public String changeUsernamePageLogic(@ModelAttribute User user, Model model, RedirectAttributes attributes) {
		Optional<User> optionalUser = userService.findByUsername(user.getUsername());

		if (optionalUser.isEmpty()) {
			userService.save(user);

			SecurityContextHolder.clearContext();

			attributes.addAttribute("msg", "Done! New username: " + user.getUsername());
			return "redirect:/login";
		} else {
			model.addAttribute("msg", "Error: This username already exists in the database.");
			return "account";
		}
	}

}

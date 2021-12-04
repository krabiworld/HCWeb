package org.headcrab.web.controller;

import org.headcrab.web.model.Token;
import org.headcrab.web.model.User;
import org.headcrab.web.service.TokenService;
import org.headcrab.web.service.UserService;
import org.headcrab.web.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class RecoveryController {

	private final UserService userService;
	private final TokenService tokenService;

	@Autowired
	public RecoveryController(UserService userService, TokenService tokenService) {
		this.userService = userService;
		this.tokenService = tokenService;
	}

    @GetMapping("/recovery")
    public String recoveryPage(Model model) {
        model.addAttribute("recoveryData", new User());
        return "recovery";
    }

    @PostMapping("/recovery")
    public String recoveryPageLogic(@ModelAttribute User user, Model model) {
		Optional<User> optionalUser = userService.findByEmail(user.getEmail());

		if (optionalUser.isPresent()) {
			String token = Utils.generateUUID();

			Token tokenObject = new Token();
			tokenObject.setUser(optionalUser.get());
			tokenObject.setToken(token);
			tokenObject.setType("recovery");
			tokenService.save(tokenObject);

			Utils.sendEmail("Recovery: http://localhost:8080/recovery/done?id=" + tokenObject.getId() + "&token=" + token);

			model.addAttribute("done", "Link send to email: " + user.getEmail());
			return "done";
		} else {
			model.addAttribute("msg", "Error: Email not found.");
			return "recovery";
		}
    }

	// Password recovery step two
	@GetMapping(value = "/recovery/done")
	public String recoverPassword(@RequestParam int id, @RequestParam String token, Model model) {
		Optional<Token> optionalToken = tokenService.findByIdAndTokenAndType(id, token, "recovery");

		if (optionalToken.isPresent()) {
			id = optionalToken.get().getUser().getId();

			tokenService.delete(optionalToken.get());

			model.addAttribute("id", id);
			model.addAttribute("user", new User());
			return "passwordrecovery";
		} else {
			model.addAttribute("error", "Token invalid.");
			return "error";
		}
	}

	@PostMapping(value = "/recovery/done")
	public String recoverPasswordLogic(@RequestParam int id, @ModelAttribute User user, Model model) {
		Optional<User> optionalUser = userService.findById(id);

		if (optionalUser.isPresent()) {
			User userObject = optionalUser.get();

			if (!user.getPassword().equals(user.getRetypePassword())) {
				model.addAttribute("id", id);
				model.addAttribute("msg", "Error: Password mismatch.");
				return "passwordrecovery";
			}

			userObject.setPassword(Utils.hashPassword(user.getPassword()));

			userService.save(userObject);

			model.addAttribute("toLogin", "Your password changed.");
			return "done";
		} else {
			model.addAttribute("error", "User not found.");
			return "error";
		}
	}

}

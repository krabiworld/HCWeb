package org.headcrab.web.controller;

import org.headcrab.web.model.Token;
import org.headcrab.web.model.User;
import org.headcrab.web.service.TokenService;
import org.headcrab.web.service.UserService;
import org.headcrab.web.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@SessionAttributes("user")
public class SignupController {

	private final UserService userService;
	private final TokenService tokenService;

	@Autowired
	public SignupController(UserService userService, TokenService tokenService) {
		this.userService = userService;
		this.tokenService = tokenService;
	}

	@GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupPageLogic(@Valid @ModelAttribute User user, BindingResult bindingResult, RedirectAttributes attributes) {
		if (!user.getPassword().equals(user.getRetypePassword())) {
			//model.addAttribute("msg", "Error: Password mismatch.");
			//return "signup";
			bindingResult.rejectValue("retypePassword", "error.retypePassword", "Password mismatch.");
		}

		user.setPassword(Utils.hashPassword(user.getPassword()));

		if (userService.findByUsername(user.getUsername()).isPresent()) {
			//model.addAttribute("msg", "Error: This username is already registered.");
			//return "signup";
			bindingResult.rejectValue("username", "error.username", "This username is already registered.");
		}

		if (userService.findByEmail(user.getEmail()).isPresent()) {
			//model.addAttribute("msg", "Error: This email is already registered.");
			//return "signup";
			bindingResult.rejectValue("email", "error.email", "This email is already registered.");
		}

		if (bindingResult.hasErrors()) {
			return "signup";
		}

		userService.save(user);

		String tokenUUID = Utils.generateUUID();

		Token token = new Token();
		token.setUser(user);
		token.setToken(tokenUUID);
		token.setType("signup");

		tokenService.save(token);

		Utils.sendEmail("Sign Up: http://localhost:8080/signup/done?id=" + token.getId() + "&token=" + tokenUUID);

        //model.addAttribute("done", "Link send to email: " + user.getEmail());
		attributes.addAttribute("done", "Link send to email: " + user.getEmail());
        return "redirect:/done";
    }

	@GetMapping("/signup/done")
	public String signupDonePage(@RequestParam int id, @RequestParam String token, Model model) {
		Optional<Token> optionalToken = tokenService.findByIdAndTokenAndType(id, token, "signup");

		if (optionalToken.isPresent()) {
			Token tokenObject = optionalToken.get();

			User user = tokenObject.getUser();
			user.setEnabled(true);

			userService.save(user);
			tokenService.delete(tokenObject);

			model.addAttribute("toLogin", "Your account activated!");
			return "done";
		} else {
			model.addAttribute("error", "Token invalid.");
			return "error";
		}
	}

}

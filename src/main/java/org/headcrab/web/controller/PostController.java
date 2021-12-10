package org.headcrab.web.controller;

import org.headcrab.web.model.Post;
import org.headcrab.web.model.User;
import org.headcrab.web.service.PostService;
import org.headcrab.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
@SessionAttributes("post")
public class PostController {

	private final PostService postService;
	private final UserService userService;

	@Autowired
	public PostController(PostService postService, UserService userService) {
		this.postService = postService;
		this.userService = userService;
	}

	@GetMapping("/post/{id}")
	public String postPage(@PathVariable int id, Model model, Principal principal) {
		String authUsername = "";
		if (principal != null) {
			authUsername = principal.getName();
		}

		Optional<Post> optionalPost = postService.getById(id);

		if (optionalPost.isPresent()) {
			Post post = optionalPost.get();
			model.addAttribute("post", post);
			if (post.getUser().getUsername().equals(authUsername)) {
				model.addAttribute("isOwner", true);
			}
			return "post";
		} else {
			model.addAttribute("error", "Post not found.");
			return "error";
		}
	}

	@GetMapping("/post/create")
	public String createPost(Model model, Principal principal) {
		Optional<User> optionalUser = userService.findByUsername(principal.getName());

		if (optionalUser.isPresent()) {
			Post post = new Post();
			post.setUser(optionalUser.get());
			model.addAttribute("post", post);
			return "createPost";
		} else {
			model.addAttribute("error", "User not found.");
			return "error";
		}
	}

	@PostMapping("/post/create")
	public String createPostLogic(@Valid @ModelAttribute Post post, BindingResult bindingResult, SessionStatus status) {
		if (bindingResult.hasErrors()) {
			return "createPost";
		}

		postService.save(post);
		status.setComplete();
		return "redirect:/post/" + post.getId();
	}

	@GetMapping("/post/delete/{id}")
	public String deletePost(@PathVariable int id, Model model, Principal principal) {
		Optional<Post> optionalPost = postService.getById(id);

		if (optionalPost.isPresent()) {
			Post post = optionalPost.get();
			if (!post.getUser().getUsername().equals(principal.getName())) {
				model.addAttribute("error", "You are not the owner of the post.");
				return "error";
			}
			postService.delete(post);

			model.addAttribute("done", "Post deleted.");
			return "done";
		} else {
			model.addAttribute("error", "Post not found.");
			return "error";
		}
	}

}

package org.headcrab.web.controller;

import org.headcrab.web.model.Post;
import org.headcrab.web.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.Optional;

@Controller
public class PostController {
	private final PostService postService;

	@Autowired
	public PostController(PostService postService) {
		this.postService = postService;
	}

	@GetMapping("/post/{id}")
	public String postPage(@PathVariable int id, Model model, Principal principal) {
		String authName = "";
		if (principal != null) {
			authName = principal.getName();
		}

		Optional<Post> optionalPost = this.postService.getById(id);

		if (optionalPost.isPresent()) {
			Post post = optionalPost.get();
			model.addAttribute("post", post);
			if (post.getUser().getUsername().equals(authName)) {
				model.addAttribute("isOwner", true);
			}
			return "post";
		} else {
			model.addAttribute("error", "Post not found.");
			return "error";
		}
	}
}

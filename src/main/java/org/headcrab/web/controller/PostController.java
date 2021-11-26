package org.headcrab.web.controller;

import org.headcrab.web.model.Post;
import org.headcrab.web.model.User;
import org.headcrab.web.service.PostService;
import org.headcrab.web.service.UserService;
import org.headcrab.web.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
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

		Optional<Post> optionalPost = this.postService.getById(id);

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
	public String createPost(Model model) {
		Post post = new Post();
		model.addAttribute("post", post);
		return "createPost";
	}

	@PostMapping("/post/create")
	public String createPostLogic(@ModelAttribute Post post, Model model, Principal principal) {
		String authUsername = "";
		if (principal != null) {
			authUsername = principal.getName();
		}

		Optional<User> optionalUser = this.userService.findByUsername(authUsername);

		if (optionalUser.isPresent()) {
			post.setUser(optionalUser.get());
			this.postService.save(post);
			return "redirect:/post/" + post.getId();
		} else {
			model.addAttribute("error", "User not found.");
			return "error";
		}
	}

	@GetMapping("/post/delete/{id}")
	public String deletePost(@PathVariable int id, Model model, Principal principal) {
		String authUsername = "";
		if (principal != null) {
			authUsername = principal.getName();
		}

		Optional<Post> optionalPost = this.postService.getById(id);

		if (optionalPost.isPresent()) {
			Post post = optionalPost.get();
			if (!post.getUser().getUsername().equals(authUsername)) {
				model.addAttribute("error", "You are not the owner of the post.");
				return "error";
			}
			this.postService.delete(post);

			model.addAttribute("done", "Post deleted.");
			return "done";
		} else {
			model.addAttribute("error", "Post not found.");
			return "error";
		}
	}
}

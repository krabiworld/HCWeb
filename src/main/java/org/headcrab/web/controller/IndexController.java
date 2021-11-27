package org.headcrab.web.controller;

import org.headcrab.web.model.Post;
import org.headcrab.web.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@Controller
public class IndexController {

	private final PostService postService;

	@Autowired
	public IndexController(PostService postService) {
		this.postService = postService;
	}

	@GetMapping("/")
	public String indexPage(Model model) {
		Collection<Post> posts = this.postService.getAll();

		model.addAttribute("posts", posts);
		return "index";
	}

}

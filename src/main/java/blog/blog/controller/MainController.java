package blog.blog.controller;

import blog.blog.domain.User;
import blog.blog.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Controller
@RequestMapping("/")
public class MainController {
	private final ArticleRepository articleRepository;

	@Autowired
	public MainController(ArticleRepository articleRepository) {
		this.articleRepository = articleRepository;
	}

	@GetMapping
	public String main(Model model, @AuthenticationPrincipal User user) {
		HashMap<Object, Object> data = new HashMap<>();

		data.put("profile", user);
		data.put("articles", articleRepository.findAll());

		model.addAttribute("frontendData", data);

		return "index";
	}
}
package blog.blog.controller;

import blog.blog.domain.Article;
import blog.blog.repository.ArticleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("article")
public class ArticleController {
	private final ArticleRepository articleRepo;

	@Autowired
	public ArticleController(ArticleRepository articleRepo) {
		this.articleRepo = articleRepo;
	}

	@GetMapping
	public List<Article> list() {
		return articleRepo.findAll();
	}

	@GetMapping("{id}")
	public Article getOne(@PathVariable("id") Article article) {
		return article;
	}

	@PostMapping
	public Article create(@RequestBody Article article) {
		article.setCreationDate(LocalDateTime.now());
		return articleRepo.save(article);
	}

	@PutMapping("{id}")
	public Article update(
			@PathVariable("id") Article articleFromDb,
			@RequestBody Article article
	) {
		BeanUtils.copyProperties(article, articleFromDb, "id");

		return articleRepo.save(articleFromDb);
	}

	@DeleteMapping("{id}")
	public @ResponseBody void delete(@PathVariable("id") Article article) {
		articleRepo.delete(article);
	}
}
package blog.blog.controller;

import blog.blog.domain.Article;
import blog.blog.domain.CurrentUser;
import blog.blog.domain.User;
import blog.blog.repository.ArticleRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
		article.setLastModifiedDate(LocalDateTime.now());
		article.setAuthorId(CurrentUser.Instance.getName());
		return articleRepo.save(article);
	}

	@PutMapping("{id}")
	public Article update(@PathVariable("id") Long id, @RequestBody Article article
	) throws Exception {

		final Article articleFromDb = getOneIfExists(id);
		BeanUtils.copyProperties(article, articleFromDb, "id");
		return articleRepo.save(articleFromDb);
	}

	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") Long id) throws Exception {
		Article article = getOneIfExists(id);
		articleRepo.delete(article);
	}

	private Article getOneIfExists(Long id) throws Exception {
		final Optional<Article> optional = articleRepo.findById(id);
		final Article article = optional.orElse(null);
		if (article == null)
			throw new Exception("Article with id = " + id + " not found");

		return article;
	}
}
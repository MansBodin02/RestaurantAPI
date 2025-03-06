package com.example.restaurantapi.Controller;

import com.example.restaurantapi.Models.News.News;
import com.example.restaurantapi.Repo.NewsRepo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * NewsController - A REST API controller for managing news articles in the restaurant system.
 * Provides endpoints to create, retrieve, update, and delete news entries.
 */
@RestController
@RequestMapping("/api/news")
public class NewsController {
    private final NewsRepo newsRepo;

    public NewsController(NewsRepo newsRepo) {
        this.newsRepo = newsRepo;
    }

    /**
     * Retrieve all news from the database.
     * @return List of all news articles.
     */
    @GetMapping("/")
    public List<News> getAllNews() {
        return newsRepo.findAll();
    }

    /**
     * Retrieve a news article by title and date.
     * @param newsTitle The title of the news.
     * @param newsDate The date of the news article.
     * @return Optional containing the news article if found.
     */
    @GetMapping("/{newsTitle}/{newsDate}")
    public Optional<News> getNewsByTitleAndDate(@PathVariable String newsTitle, @PathVariable LocalDateTime newsDate) {
        return newsRepo.findNewsByNewsTitleAndNewsDate(newsTitle, newsDate);
    }

    /**
     * Save a single news entry to the database.
     * @param news The news object to be saved.
     * @return Success or error message.
     */
    @PostMapping("/")
    public String saveOneNews(@RequestBody News news) {
        try {
            newsRepo.save(news);
            return "News Saved!";
        } catch (DataIntegrityViolationException e) {
            return "Error: A news already exists for title " + news.getNewsTitle() + " and date " + news.getNewsDate();
        }
    }

    /**
     * Save multiple news entries to the database in a batch operation.
     * @param news List of news articles to be saved.
     * @return Success or error message.
     */
    @PostMapping("/batch")
    public String saveManyNews(@RequestBody List<News> news) {
        try {
            newsRepo.saveAll(news);
            return "News Saved";
        } catch (DataIntegrityViolationException e) {
            return "Error: Duplicate news for the same title and date";
        }
    }

    /**
     * Update an existing news entry by title and date.
     * @param newsTitle The title of the news article.
     * @param newsDate The date of the news article.
     * @param news The updated news details.
     * @return Success message.
     */
    @PutMapping("/{newsTitle}/{newsDate}")
    public String updateNews(@PathVariable String newsTitle, @PathVariable LocalDateTime newsDate, @RequestBody News news) {
        Optional<News> optionalNews = newsRepo.findNewsByNewsTitleAndNewsDate(newsTitle, newsDate);
        if(optionalNews.isEmpty()) {
            return "News not found for title " + newsTitle + ", date " + newsDate;
        }
        News updateNews = optionalNews.get();
        updateNews.setNewsTitle(news.getNewsTitle());
        updateNews.setNewsInfo(news.getNewsInfo());
        updateNews.setNewsDate(news.getNewsDate());
        updateNews.setNewsImage(news.getNewsImage());

        newsRepo.save(updateNews);
        return "News updated successfully!";
    }

    /**
     * Delete a news article by title and date.
     * @param newsTitle The title of the news article.
     * @param newsDate The date of the news article.
     * @return Success or error message.
     */
    @DeleteMapping("/{newsTitle}/{newsDate}")
    public String deleteNews(@PathVariable String newsTitle, @PathVariable LocalDateTime newsDate) {
        Optional<News> optionalNews = newsRepo.findNewsByNewsTitleAndNewsDate(newsTitle, newsDate);
        if (optionalNews.isEmpty()) {
            return "News not found for title " + newsTitle + ", date " + newsDate;
        }

        newsRepo.delete(optionalNews.get());
        return "News deleted successfully!";
    }
}

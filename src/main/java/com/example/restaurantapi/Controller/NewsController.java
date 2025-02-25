package com.example.restaurantapi.Controller;

import com.example.restaurantapi.Models.News.News;
import com.example.restaurantapi.Repo.NewsRepo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/news")
public class NewsController {
    private final NewsRepo newsRepo;

    public NewsController(NewsRepo newsRepo) {
        this.newsRepo = newsRepo;
    }

    @GetMapping("/")
    public List<News> getAllNews() { return newsRepo.findAll(); }

    @GetMapping("/{newsTitle}/{newsDate}")
    public Optional<News> getNewsByTitleAndDate(@PathVariable String newsTitle, @PathVariable LocalDateTime newsDate) {
        return newsRepo.findNewsByNewsTitleAndNewsDate(newsTitle, newsDate);
    }

    @PostMapping("/")
    public String saveOneNews(@RequestBody News news) {
        try {
            newsRepo.save(news);
            return "News Saved!";
        } catch (DataIntegrityViolationException e) {
            return "Error: A news already exists for title " + news.getNewsTitle() + "and date" + news.getNewsDate();
        }
    }

    @PostMapping("/batch")
    public String saveManyNews(@RequestBody List<News> news) {
        try {
            newsRepo.saveAll(news);
            return "News Saved";
        } catch (DataIntegrityViolationException e) {
            return "Error: Duplicate news for the same title and date";
        }
    }

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

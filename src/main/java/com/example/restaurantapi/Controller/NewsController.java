package com.example.restaurantapi.Controller;

import com.example.restaurantapi.Models.News.News;
import com.example.restaurantapi.Repo.NewsRepo;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/news")
public class NewsController {
    private final NewsRepo newsRepo;
    private final String imageDirectory = "src/main/resources/static/images/";

    public NewsController(NewsRepo newsRepo) {
        this.newsRepo = newsRepo;
    }



    @GetMapping("/")
    public List<News> getAllNews() { return newsRepo.findAll(); }

    @GetMapping("/{newsTitle}/{newsDate}")
    public Optional<News> getNewsByTitleAndDate(@PathVariable String newsTitle, @PathVariable LocalDate newsDate) {
        return newsRepo.findNewsByNewsTitleAndNewsDate(newsTitle, newsDate);
    }

    @PostMapping("/")
    public ResponseEntity<String> saveOneNews(
            @RequestParam("newsTitle") String newsTitle,
            @RequestParam("newsInfo") String newsInfo,
            @RequestParam("newsDate") String newsDate,
            @RequestParam("image") MultipartFile image) {
        try {
            String fileName = image.getOriginalFilename();
            Path imagePath = Paths.get(imageDirectory, fileName);
            Files.write(imagePath, image.getBytes());

            News news = new News();
            news.setNewsTitle(newsTitle);
            news.setNewsInfo(newsInfo);
            news.setNewsDate(LocalDate.parse(newsDate));
            news.setNewsImage("/images/" + fileName);

            newsRepo.save(news);
            return ResponseEntity.ok("News and image saved successfully!");

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error saving image: " + e.getMessage());
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
    public String updateNews(@PathVariable String newsTitle, @PathVariable LocalDate newsDate, @RequestBody News news) {
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
    public String deleteNews(@PathVariable String newsTitle, @PathVariable LocalDate newsDate) {
        Optional<News> optionalNews = newsRepo.findNewsByNewsTitleAndNewsDate(newsTitle, newsDate);
        if (optionalNews.isEmpty()) {
            return "News not found for title " + newsTitle + ", date " + newsDate;
        }

        newsRepo.delete(optionalNews.get());
        return "News deleted successfully!";
    }
}

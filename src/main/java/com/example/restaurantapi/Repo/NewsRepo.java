package com.example.restaurantapi.Repo;


import com.example.restaurantapi.Models.News.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Optional;

public interface NewsRepo extends JpaRepository<News, Long> {
    Optional<News> findNewsByNewsTitleAndNewsDate(String newsTitle, LocalDateTime newsDate);
}

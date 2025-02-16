package com.example.restaurantapi.Models.News;


import jakarta.persistence.*;
import java.time.DateTimeException;

/**
 * Ingen avancerad modell för Lunch
 */
@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"news_title", "news_date"})
)
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newsId;

    @Column(nullable = false)
    private String newsTitle;

    @Column(nullable = false)
    private String newsInfo;

    @Column(nullable = false)
    private DateTimeException newsDate;

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsInfo() {
        return newsInfo;
    }

    public void setNewsInfo(String newsInfo) {
        this.newsInfo = newsInfo;
    }

    public DateTimeException getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(DateTimeException newsDate) {
        this.newsDate = newsDate;
    }
}

package com.example.restaurantapi.Models.News;


import jakarta.persistence.*;
import java.time.DateTimeException;
import java.time.LocalDateTime;

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

    @Column
    private String newsInfo;

    @Column(nullable = false)
    private LocalDateTime newsDate;

    @Column
    private String newsURLImage;

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

    public LocalDateTime getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(LocalDateTime newsDate) {
        this.newsDate = newsDate;
    }

    public String getNewsURLImage() {return newsURLImage;}

    public void setNewsURLImage(String newsURLImage) {this.newsURLImage = newsURLImage;}
}

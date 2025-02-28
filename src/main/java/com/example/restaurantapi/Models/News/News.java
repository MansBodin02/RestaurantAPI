package com.example.restaurantapi.Models.News;


import jakarta.persistence.*;
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
    private String newsImage;

    public News(Long newsId, String newsTitle, String newsInfo, LocalDateTime newsDate, String newsImage) {
        this.newsId = newsId;
        this.newsTitle = newsTitle;
        this.newsInfo = newsInfo;
        this.newsDate = newsDate;
        this.newsImage = newsImage;
    }

    public News() {

    }

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

    public String getNewsImage() {return newsImage;}

    public void setNewsImage(String newsImage) {this.newsImage = newsImage;}
}

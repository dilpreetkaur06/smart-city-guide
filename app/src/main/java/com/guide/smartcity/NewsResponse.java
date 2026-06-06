package com.guide.smartcity;

import java.util.List;

public class NewsResponse {

    public List<Article> articles;

    public static class Article {

        public String title;
        public String url;
    }
}
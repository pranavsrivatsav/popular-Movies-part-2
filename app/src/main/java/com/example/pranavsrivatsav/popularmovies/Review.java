package com.example.pranavsrivatsav.popularmovies;

/**
 * Created by Pranav Srivatsav on 2/12/2017.
 */

public class Review {

    private String Author;
    private String Content;

    public Review(String author, String content) {
        Author = author;
        Content = content;
    }

    public String getAuthor() {
        return Author;
    }

    public String getContent() {
        return Content;
    }

    @Override
    public String toString() {
        /*return "Review{" +
                "Author='" + Author + '\'' +
                ", Content='" + Content + '\'' +
                '}';
        */
        return "\n" + Author + "\n" + Content + "\n";
    }
}

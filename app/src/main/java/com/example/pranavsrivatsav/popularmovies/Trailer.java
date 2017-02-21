package com.example.pranavsrivatsav.popularmovies;

/**
 * Created by Pranav Srivatsav on 2/18/2017.
 */

public class Trailer {
    private String Title;
    private String Source;

    public Trailer(String title, String source) {
        Title = title;
        Source = source;
    }

    public String getTitle() {
        return Title;
    }

    public String getSource() {
        return Source;
    }
}

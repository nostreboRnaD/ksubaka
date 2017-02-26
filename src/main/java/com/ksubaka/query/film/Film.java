package com.ksubaka.query.film;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ksubaka.query.Media;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Film implements Media {
    private final String title;
    private final Integer year;
    private final String director;
    private final Pattern yearAndDirector = Pattern.compile("([0-9]{4}), +<.*>(.+)</a>");

    @JsonCreator
    public Film(@JsonProperty("description") String description,
                @JsonProperty("title") String title) {
        Matcher m = yearAndDirector.matcher(description);
        // Filter out entries with these null values later
        if (!m.matches()) {
            year = null;
            director = null;
        }
        else {
            year = Integer.valueOf(m.group(1));
            director = m.group(2);
        }
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Integer getYear() {
        return year;
    }

    public String getDirector() {
        return director;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s", title, year, director);
    }
}

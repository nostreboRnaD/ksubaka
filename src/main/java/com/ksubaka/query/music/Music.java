package com.ksubaka.query.music;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ksubaka.query.Media;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Music implements Media {

    private final String title;
    private final String artist;
    private final String date;

    @JsonCreator
    public Music(@JsonProperty("title") String title,
                 @JsonProperty("artist-credit") List<Map<String, Object>> artistMap,
                 @JsonProperty("date") String date) {
        this.title = title;
        this.date = date;
        artist = ((Map<String, String>) artistMap.get(0).get("artist")).get("name");
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getYear() {
        return date;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((artist == null) ? 0 : artist.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Music other = (Music) obj;
        if (artist == null) {
            if (other.artist != null)
                return false;
        } else if (!artist.equals(other.artist))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s - %s, %s", artist, title, date);
    }
}

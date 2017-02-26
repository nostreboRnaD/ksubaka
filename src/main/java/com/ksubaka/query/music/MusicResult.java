package com.ksubaka.query.music;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Wrapper class to get Jackson to play nicely with the fields we don't care about
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MusicResult {

    private final List<Music> releases;

    @JsonCreator
    public MusicResult(@JsonProperty("releases") List<Music> releases) {
        this.releases = releases;
    }

    public List<Music> getMusic() {
        return releases;
    }
}

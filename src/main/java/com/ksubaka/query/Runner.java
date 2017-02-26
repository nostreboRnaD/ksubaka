package com.ksubaka.query;

import java.util.List;

import com.ksubaka.query.film.HttpFilmDao;
import com.ksubaka.query.music.HttpMusicDao;

public class Runner {

    public static void main(String[] args) {
        HttpDao dao = null;
        String api = System.getProperty("api");
        if ("imdb".equalsIgnoreCase(api)) {
            dao = new HttpFilmDao();
        }
        else {
            if ("musicbrainz".equalsIgnoreCase(api)) {
                dao = new HttpMusicDao();
            }
            else {
                System.out.println("Unrecognised API supplied: " + api);
            }
        }
        List<Media> media = dao.get(System.getProperty("title"));
        if (media.isEmpty()) {
            System.out.println("No results found");
        }
        for (Media medium : media) {
            System.out.println(medium);
        }
    }
}

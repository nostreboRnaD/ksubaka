package com.ksubaka.query.music;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class MusicTest {
    private final HttpMusicDao musicDao = new HttpMusicDao();

    @Test
    public void testNoDuplicates() {
        // Raw data includes the original 2008 release and 2013 re-issue
        List<Music> music = musicDao.get("Lammendam");
        assertEquals(1, music.size());
        assertEquals("2008-04-18", music.get(0).getYear());
    }

    @Test
    public void testSortedByArtist() {
        List<Music> music = musicDao.get("massive addictive");
        assertTrue(music.size() > 1);
        String artist = music.get(0).getArtist();
        for (Music release : music) {
            assertTrue(artist.compareTo(release.getArtist()) <= 0);
            artist = release.getArtist();
        }
    }

    @Test
    public void testNoSearchTerm() {
        List<Music> music = musicDao.get(null);
        assertEquals(0, music.size());

        music = musicDao.get("");
        assertEquals(0, music.size());
    }

    @Test
    public void testNoMatches() {
        List<Music> music = musicDao.get("asdfkjahdfgk");
        assertEquals(0, music.size());
    }
}

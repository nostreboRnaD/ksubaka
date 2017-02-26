package com.ksubaka.query.film;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class FilmTest {
    private final HttpFilmDao filmDao = new HttpFilmDao();

    /**
     * The raw results for this search include a TV series; make sure it isn't included
     */
    @Test
    public void testExactly4Films() {
        List<Film> films = filmDao.get("Indiana Jones");
        assertEquals(4, films.size());
        // If this passes, we can probably assume we've found the right things
        for (Film film : films) {
            assertEquals("Steven Spielberg", film.getDirector());
        }
    }

    @Test
    public void testSortedByYear() {
        List<Film> films = filmDao.get("Indiana Jones");
        int year = 0;
        for (Film film : films) {
            assertTrue(year <= film.getYear());
            year = film.getYear();
        }
    }

    @Test
    public void testNoSearchTerm() {
        List<Film> films = filmDao.get(null);
        assertEquals(0, films.size());

        films = filmDao.get("");
        assertEquals(0, films.size());
    }

    @Test
    public void testNoMatches() {
        List<Film> films = filmDao.get("asdfkjahdfgk;j");
        assertEquals(0, films.size());
    }
}
package com.ksubaka.query.music;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ksubaka.query.HttpDao;

public class HttpMusicDao implements HttpDao<Music> {
    // The API uses very loose matching on search terms, so 10 releases is more than enough
    private final int LIMIT = 10;
    private final String API = "http://musicbrainz.org/ws/2/release/?query=release:%s&fmt=json&limit=%d";
    private final ObjectMapper mapper;

    public HttpMusicDao() {
        mapper = new ObjectMapper();
        mapper.disable(MapperFeature.USE_GETTERS_AS_SETTERS);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<Music> get(String title) {
        if (title == null || "".equals(title)) {
            return new ArrayList<Music>();
        }
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(String.format(API, URLEncoder.encode(title, "UTF-8"), LIMIT));
            final ResponseHandler<MusicResult> responseHandler = new ResponseHandler<MusicResult>() {
                @Override
                public MusicResult handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                    final int status = response.getStatusLine().getStatusCode();
                    if (status != 200) {
                        throw new IOException("MusicBrainz API returned non-200 status: " + status);
                    }
                    InputStream i = response.getEntity().getContent();
                    return mapper.readValue(i, MusicResult.class);
                }
            };
            MusicResult rawResults = client.execute(get, responseHandler);
            if (rawResults == null || rawResults.getMusic() == null || rawResults.getMusic().isEmpty()) {
                return new ArrayList<Music>();
            }
            return sortAndFilterReleases(rawResults.getMusic());
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to query MusicBrainz API", e);
        }
    }

    /**
     * Only return the first distinct result per album
     */
    private List<Music> sortAndFilterReleases(List<Music> music) {
        Comparator<Music> comparator = Comparator.comparing(Music::getArtist)
                                                 .thenComparing(Music::getYear);
        return music.stream().sorted(comparator).distinct().collect(Collectors.toList());
    }
}

package com.ksubaka.query.film;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ksubaka.query.HttpDao;

public class HttpFilmDao implements HttpDao<Film> {
    private final String API = "http://www.imdb.com/xml/find?json=1&nr=1&tt=on&q=";
    private final ObjectMapper mapper;

    public HttpFilmDao() {
        mapper = new ObjectMapper();
        mapper.disable(MapperFeature.USE_GETTERS_AS_SETTERS);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<Film> get(String title) {
        if (title == null || "".equals(title)) {
            return new ArrayList<Film>();
        }
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(API + URLEncoder.encode(title, "UTF-8"));
            final ResponseHandler<Map<String, List<Film>>> responseHandler = new ResponseHandler<Map<String, List<Film>>>() {
                @Override
                public Map<String, List<Film>> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                    final int status = response.getStatusLine().getStatusCode();
                    if (status != 200) {
                        throw new IOException("IMDb API returned non-200 status: " + status);
                    }
                    InputStream i = response.getEntity().getContent();
                    return mapper.readValue(i, new TypeReference<Map<String, List<Film>>>() { });
                }
            };
            Map<String, List<Film>> rawResults = client.execute(get, responseHandler);
            if (rawResults == null || rawResults.isEmpty()) {
                return new ArrayList<Film>();
            }
            return rawResults.get("title_popular").stream().filter(o -> o.getYear() != null)
                             .sorted(Comparator.comparing(Film::getYear))
                             .collect(Collectors.toList());
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to query IMDb API", e);
        }
    }
}

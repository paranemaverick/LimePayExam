package com.limepay.limepayexam.service;

import com.limepay.limepayexam.model.Movie;
import com.limepay.limepayexam.model.MovieResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MovieService {

    @Value("${movie.api.url}")
    private String movieApiUrl;

    private final RestTemplate restTemplate;

    public MovieService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Movie> getAllMovies() {

        List<Movie> allMovies = new ArrayList<>();
        int page = 1;
        boolean hasMorePages;

        do {
            URI requestUrl = UriComponentsBuilder.fromUriString(movieApiUrl)
                    .queryParam("page", page)
                    .build()
                    .toUri();
            log.info("fetching movies from URL: {}", requestUrl);

            MovieResponse response = restTemplate.getForObject(requestUrl, MovieResponse.class);

            if (response != null && response.getData() != null) {
                allMovies.addAll(response.getData());
                hasMorePages = page < response.getTotalPages();
                page++;
            } else {
                hasMorePages = false;
            }
        } while (hasMorePages);

        log.info("total movies fetched: {}", allMovies.size());
        return allMovies;
    }
}

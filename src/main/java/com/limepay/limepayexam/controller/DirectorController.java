package com.limepay.limepayexam.controller;

import com.limepay.limepayexam.model.Movie;
import com.limepay.limepayexam.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class DirectorController {

    private final MovieService movieService;

    public DirectorController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/api/directors")
    public Map<String, List<String>> getDirectors(@RequestParam int threshold) {

        List<Movie> allMovies = movieService.getAllMovies();
        log.info("fetched movies: {}", allMovies);

        Map<String, Long> directorMovieCountMap = allMovies.stream()
                .collect(Collectors.groupingBy(Movie::getDirector, Collectors.counting()));

        log.info("director movie count: {}", directorMovieCountMap);

        List<String> directors = directorMovieCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() > threshold)
                .map(Map.Entry::getKey)
                .sorted()
                .toList();

        Map<String, List<String>> response = new HashMap<>();
        response.put("directors", directors);
        return response;
    }
}

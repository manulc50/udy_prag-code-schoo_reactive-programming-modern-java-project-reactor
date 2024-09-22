package com.mlorenzo.service;

import com.mlorenzo.domain.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MovieReactiveServiceTest {
    MovieReactiveService movieReactiveService;

    @BeforeEach
    void setUp() {
        MovieInfoService movieInfoService = new MovieInfoService();
        ReviewService reviewService = new ReviewService();
        RevenueService revenueService = new RevenueService();
        movieReactiveService = new MovieReactiveService(movieInfoService, reviewService, revenueService);
    }

    @Test
    void getAllMoviesTest() {
        // when
        Flux<Movie> fluxOfMovies = movieReactiveService.getAllMovies();

        // then
        StepVerifier.create(fluxOfMovies)
                .assertNext(movie -> {
                    assertEquals("Batman Begins", movie.getMovie().getName());
                    assertEquals(2, movie.getReviewList().size());
                })
                .assertNext(movie -> {
                    assertEquals("The Dark Knight", movie.getMovie().getName());
                    assertEquals(2, movie.getReviewList().size());
                })
                .assertNext(movie -> {
                    assertEquals("Dark Knight Rises", movie.getMovie().getName());
                    assertEquals(2, movie.getReviewList().size());
                })
                .verifyComplete();
    }

    @Test
    void getMovieByIdWithZipWithTest() {
        // given
        long movieId = 100L;

        // when
        Mono<Movie> monoOfMovie = movieReactiveService.getMovieByIdWithZipWith(movieId);

        // then
        StepVerifier.create(monoOfMovie)
                .assertNext(movie -> {
                    assertEquals("Batman Begins", movie.getMovie().getName());
                    assertEquals(2, movie.getReviewList().size());
                })
                .expectComplete()
                .verify();
    }

    @Test
    void getMovieByIdWithFlatMapTest() {
        // given
        long movieId = 100L;

        // when
        Mono<Movie> monoOfMovie = movieReactiveService.getMovieByIdWithFlatMap(movieId);

        // then
        StepVerifier.create(monoOfMovie)
                .assertNext(movie -> {
                    assertEquals("Batman Begins", movie.getMovie().getName());
                    assertEquals(2, movie.getReviewList().size());
                })
                .expectComplete()
                .verify();
    }

    @Test
    void getMovieByIdWithRevenueTest() {
        // given
        long movieId = 100L;

        // when
        Mono<Movie> monoOfMovie = movieReactiveService.getMovieByIdWithRevenue(movieId);

        // then
        StepVerifier.create(monoOfMovie)
                .assertNext(movie -> {
                    assertEquals("Batman Begins", movie.getMovie().getName());
                    assertEquals(2, movie.getReviewList().size());
                    assertNotNull(movie.getRevenue());
                })
                .expectComplete()
                .verify();
    }
}

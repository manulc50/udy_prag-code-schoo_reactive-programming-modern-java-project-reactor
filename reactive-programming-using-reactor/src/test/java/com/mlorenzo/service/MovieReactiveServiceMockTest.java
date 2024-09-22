package com.mlorenzo.service;

import com.mlorenzo.exception.NetworkException;
import com.mlorenzo.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieReactiveServiceMockTest {

    @Mock
    MovieInfoService movieInfoService;

    @Mock
    ReviewService reviewService;

    // Anotación que crea una instancia de MovieReactiveService e inyecta en ella los Mocks anteriores.
    @InjectMocks
    MovieReactiveService movieReactiveService;

    @Test
    void getAllMovies1Test() {
        // given
        when(movieInfoService.retrieveMoviesFlux()).thenCallRealMethod();
        when(reviewService.retrieveReviewsFlux(anyLong())).thenCallRealMethod();

        // when
        var fluxOfMovies = movieReactiveService.getAllMovies();

        // then
        StepVerifier.create(fluxOfMovies)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void getAllMovies2Test() {
        // given
        var errorMessage = "Exception occurred in ReviewService";

        when(movieInfoService.retrieveMoviesFlux()).thenCallRealMethod();
        when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenThrow(new RuntimeException(errorMessage));

        // when
        var fluxOfMovies = movieReactiveService.getAllMovies();

        // then
        StepVerifier.create(fluxOfMovies)
                //.expectError(MovieException.class)
                .expectErrorMessage(errorMessage)
                .verify();
    }

    @Test
    void getAllMoviesWithRetryTest() {
        // given
        var errorMessage = "Exception occurred in ReviewService";

        when(movieInfoService.retrieveMoviesFlux()).thenCallRealMethod();
        when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenThrow(new RuntimeException(errorMessage));

        // when
        var fluxOfMovies = movieReactiveService.getAllMoviesWithRetry();

        // then
        StepVerifier.create(fluxOfMovies)
                //.expectError(MovieException.class)
                .expectErrorMessage(errorMessage)
                .verify();

        verify(reviewService, times(4)).retrieveReviewsFlux(anyLong());
    }

    @Test
    void getAllMoviesWithRetryWhen1Test() {
        // given
        var errorMessage = "Exception occurred in ReviewService";

        when(movieInfoService.retrieveMoviesFlux()).thenCallRealMethod();
        when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenThrow(new NetworkException(errorMessage));

        // when
        var fluxOfMovies = movieReactiveService.getAllMoviesWithRetryWhen();

        // then
        StepVerifier.create(fluxOfMovies)
                //.expectError(MovieException.class)
                .expectErrorMessage(errorMessage)
                .verify();

        verify(reviewService, times(4)).retrieveReviewsFlux(anyLong());
    }

    @Test
    void getAllMoviesWithRetryWhen2Test() {
        // given
        var errorMessage = "Exception occurred in ReviewService";

        when(movieInfoService.retrieveMoviesFlux()).thenCallRealMethod();
        when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenThrow(new ServiceException(errorMessage));

        // when
        var fluxOfMovies = movieReactiveService.getAllMoviesWithRetryWhen();

        // then
        StepVerifier.create(fluxOfMovies)
                //.expectError(MovieException.class)
                .expectErrorMessage(errorMessage)
                .verify();

        verify(reviewService, times(1)).retrieveReviewsFlux(anyLong());
    }

    @Test
    void getAllMoviesWithRepeatTest() {
        // given
        when(movieInfoService.retrieveMoviesFlux()).thenCallRealMethod();
        when(reviewService.retrieveReviewsFlux(anyLong())).thenCallRealMethod();

        // when
        var fluxOfMovies = movieReactiveService.getAllMoviesWithRepeat();

        // then
        StepVerifier.create(fluxOfMovies)
                .expectNextCount(6)
                // Solo queremos verificar que la subscripción se repite una vez más. Por lo tanto, luego tenemos que
                // cancelar la subscripción al publicador para que no haya más repeticiones ya que está configurado que
                // publicador se repita infinitamente.
                .thenCancel()
                .verify();

        verify(reviewService, times(6)).retrieveReviewsFlux(anyLong());
    }

    @Test
    void getAllMoviesWithRepeatNTimesTest() {
        // given
        var repeatTimes = 2L;

        when(movieInfoService.retrieveMoviesFlux()).thenCallRealMethod();
        when(reviewService.retrieveReviewsFlux(anyLong())).thenCallRealMethod();

        // when
        var fluxOfMovies = movieReactiveService.getAllMoviesWithRepeatNTimes(repeatTimes);

        // then
        StepVerifier.create(fluxOfMovies)
                .expectNextCount(9)
                .verifyComplete();

        verify(reviewService, times(9)).retrieveReviewsFlux(anyLong());
    }
}

package com.mlorenzo.service;

import com.mlorenzo.domain.Movie;
import com.mlorenzo.domain.MovieInfo;
import com.mlorenzo.domain.Revenue;
import com.mlorenzo.domain.Review;
import com.mlorenzo.exception.MovieException;
import com.mlorenzo.exception.NetworkException;
import com.mlorenzo.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;
import java.util.List;

@Slf4j
public class MovieReactiveService {
    private final MovieInfoService movieInfoService;
    private final ReviewService reviewService;
    private final RevenueService revenueService;

    public MovieReactiveService(MovieInfoService movieInfoService, ReviewService reviewService,
                                RevenueService revenueService) {
        this.movieInfoService = movieInfoService;
        this.reviewService = reviewService;
        this.revenueService = revenueService;
    }

    public Flux<Movie> getAllMovies() {
        return movieInfoService.retrieveMoviesFlux()
                .flatMap(movieInfo -> {
                    Mono<List<Review>> monoReviewsList = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                            .collectList();

                    return monoReviewsList.map(reviewsList -> new Movie(movieInfo, reviewsList));
                })
                // Si se produce alguna excepción, la convertimos en nuestra excepción de negocio MovieException.
                .onErrorMap(ex -> {
                    log.error("Exception is: ", ex);

                    return new MovieException(ex.getMessage());
                })
                .log();
    }

    public Flux<Movie> getAllMoviesWithRetry() {
        return movieInfoService.retrieveMoviesFlux()
                .flatMap(movieInfo -> {
                    Mono<List<Review>> monoReviewsList = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                            .collectList();

                    return monoReviewsList.map(reviewsList -> new Movie(movieInfo, reviewsList));
                })
                // Si se produce alguna excepción, la convertimos en nuestra excepción de negocio MovieException.
                .onErrorMap(ex -> {
                    log.error("Exception is: ", ex);

                    return new MovieException(ex.getMessage());
                })
                // Este operador o método se utiliza para reintetar indefinidamente el envío de un elemento que
                // ocasionó un error o excepción. Se suele usar pasándole un número determinado de reintentos.
                //.retry()
                .retry(3)
                .log();
    }

    public Flux<Movie> getAllMoviesWithRetryWhen() {
        return movieInfoService.retrieveMoviesFlux()
                .flatMap(movieInfo -> {
                    Mono<List<Review>> monoReviewsList = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                            .collectList();

                    return monoReviewsList.map(reviewsList -> new Movie(movieInfo, reviewsList));
                })
                // Si se produce alguna excepción, la convertimos en nuestra excepción de negocio MovieException.
                .onErrorMap(ex -> {
                    log.error("Exception is: ", ex);

                    if(ex instanceof NetworkException)
                        throw new MovieException(ex.getMessage());
                    else
                        throw new ServiceException(ex.getMessage());
                })
                // Este operador o método es una versión más avanzada que el operador o método "retry" ya que nos
                // permite configurar los reintentos pasándole un objeto de una clase que extienda de Retry.
                .retryWhen(getRetryBackoffSpec())
                .log();
    }

    public Flux<Movie> getAllMoviesWithRepeat() {
        return movieInfoService.retrieveMoviesFlux()
                .flatMap(movieInfo -> {
                    Mono<List<Review>> monoReviewsList = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                            .collectList();

                    return monoReviewsList.map(reviewsList -> new Movie(movieInfo, reviewsList));
                })
                // Si se produce alguna excepción, la convertimos en nuestra excepción de negocio MovieException.
                .onErrorMap(ex -> {
                    log.error("Exception is: ", ex);

                    if(ex instanceof NetworkException)
                        throw new MovieException(ex.getMessage());
                    else
                        throw new ServiceException(ex.getMessage());
                })
                // Este operador o método es una versión más avanzada que el operador o método "retry" ya que nos
                // permite configurar los reintentos pasándole un objeto de una clase que extienda de Retry.
                .retryWhen(getRetryBackoffSpec())
                // Este operador o método se utiliza para repetir subscripciones al publicador siempre y cuando el
                // publicador no emita ningún elemento que sea un error o excepción. En caso contrario, la repetición
                // de la subscripción se cancelará. En este caso, el número de repeticiones es infinito porque no le
                // estamos pasando un número de repetciones.
                .repeat()
                .log();
    }

    public Flux<Movie> getAllMoviesWithRepeatNTimes(long repeatTimes) {
        return movieInfoService.retrieveMoviesFlux()
                .flatMap(movieInfo -> {
                    Mono<List<Review>> monoReviewsList = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                            .collectList();

                    return monoReviewsList.map(reviewsList -> new Movie(movieInfo, reviewsList));
                })
                // Si se produce alguna excepción, la convertimos en nuestra excepción de negocio MovieException.
                .onErrorMap(ex -> {
                    log.error("Exception is: ", ex);

                    if(ex instanceof NetworkException)
                        throw new MovieException(ex.getMessage());
                    else
                        throw new ServiceException(ex.getMessage());
                })
                // Este operador o método es una versión más avanzada que el operador o método "retry" ya que nos
                // permite configurar los reintentos pasándole un objeto de una clase que extienda de Retry.
                .retryWhen(getRetryBackoffSpec())
                // Este operador o método se utiliza para repetir subscripciones al publicador siempre y cuando el
                // publicador no emita ningún elemento que sea un error o excepción. En caso contrario, la repetición
                // de la subscripción se cancelará. En este caso, el número de repeticiones es "repeatTimes".
                .repeat(repeatTimes)
                .log();
    }

    public Mono<Movie> getMovieByIdWithZipWith(Long movieId) {
        Mono<MovieInfo> monoOfMovieInfo = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
        Mono<List<Review>> monoOfListReviews = reviewService.retrieveReviewsFlux(movieId).collectList();

        // Primera forma
        /*return monoOfMovieInfo.zipWith(monoOfListReviews)
                .map(tuple2 -> new Movie(tuple2.getT1(), tuple2.getT2())).log();*/

        // Segunda forma
        // Versión simplificada de la expresión "(movieInfo, listReviews) -> new Movie(movieInfo, listReviews)"
        return monoOfMovieInfo.zipWith(monoOfListReviews, Movie::new).log();
    }

    public Mono<Movie> getMovieByIdWithFlatMap(Long movieId) {
        return movieInfoService.retrieveMovieInfoMonoUsingId(movieId)
                .flatMap(movieInfo -> {
                    Mono<List<Review>> monoReviewsList = reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                            .collectList();

                    return monoReviewsList.map(reviewsList -> new Movie(movieInfo, reviewsList));
                })
                .log();
    }

    public Mono<Movie> getMovieByIdWithRevenue(Long movieId) {
        Mono<MovieInfo> monoOfMovieInfo = movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
        Mono<List<Review>> monoOfListReviews = reviewService.retrieveReviewsFlux(movieId).collectList();

        // El método "getRevenue" es un método bloqueante porque tiene un "delay" o retraso añadido para simular una
        // llamada a otro servicio a través de la red o un acceso a la base de datos. Por esta razón, envolvemos el
        // resultado de dicho método en un flujo reactivo Mono para poder usar a continuación el operador o método
        // "subscribeOn" y, de esta forma, derivar la ejecución de esta tarea bloqueante a otro hilo para evitar
        // bloquear el hilo que inició la subscripción al publicador y que ejecuta su pipeline.
        Mono<Revenue> monoOfRevenue = Mono.fromCallable(() -> revenueService.getRevenue(movieId))
                .subscribeOn(Schedulers.boundedElastic());

        return monoOfMovieInfo.zipWith(monoOfListReviews)
                .map(tuple2 -> new Movie(tuple2.getT1(), tuple2.getT2()))
                .zipWith(monoOfRevenue, (movie, revenue) -> {
                    movie.setRevenue(revenue);

                    return movie;
                }).log();
    }

    private RetryBackoffSpec getRetryBackoffSpec() {
        // Creamos un reintento de tipo "backoff", es decir, el tiempo que transcurre en cada reintento crece
        // exponencialmente.
        //return Retry.backoff(3, Duration.ofMillis(500))
        // Creamos un reintento de tipo "fixedDelay", es decir, el tiempo que transcurre en cada reintento es siempre
        // el mismo.
        return Retry.fixedDelay(3, Duration.ofMillis(500))
                // Este operador o método es para filtrar las excepciones sobre las cuales queremos realizar reintentos.
                // Si no se indica, por defecto se realiza los reintentos para todas las excepciones.
                .filter(ex -> ex instanceof MovieException)
                // Por defecto, el operador o método "retryWhen" convierte la excepción ocurrida a otra de tipo
                // RetryExhaustedException. Por esta razón, en este opeador o método "RetryExhaustedException"
                // indicamos que propage la excepción original ocurrida sin que la convierta.
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> Exceptions.propagate(retrySignal.failure()));
    }
}

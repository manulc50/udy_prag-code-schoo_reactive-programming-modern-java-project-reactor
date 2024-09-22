package com.mlorenzo.service;

import com.mlorenzo.exception.ReactorException;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

class FluxAndMonoGeneratorServiceTest {
    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    void getFluxOfNamesTest() {
        // when
        var fluxOfNames = fluxAndMonoGeneratorService.getFluxOfNames();

        // then
        StepVerifier.create(fluxOfNames)
                //.expectNext("alex", "ben", "chloe")
                //.expectNextCount(3)
                .expectNext("alex")
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void getFluxOfNamesWithMapAndFilterTest() {
        // given
        int nameSize = 3;

        // when
        var fluxOfNames = fluxAndMonoGeneratorService.getFluxOfNamesWithMapAndFilter(nameSize);

        // then
        StepVerifier.create(fluxOfNames)
                .expectNext("4-ALEX", "5-CHLOE")
                .verifyComplete();
    }

    @Test
    void getFluxOfNamesWithFlatMapAndFilterTest() {
        // given
        int nameSize = 3;

        // when
        var fluxOfLetters = fluxAndMonoGeneratorService.getFluxOfNamesWithFlatMapAndFilter(nameSize);

        // then
        StepVerifier.create(fluxOfLetters)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void getFluxOfNamesWithFlatMapAsyncAndFilterTest() {
        // given
        int nameSize = 3;

        // when
        var fluxOfLetters = fluxAndMonoGeneratorService.getFluxOfNamesWithFlatMapAsyncAndFilter(nameSize);

        // then
        StepVerifier.create(fluxOfLetters)
                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void getFluxOfNamesWithConcatMapAndFilterTest() {
        // given
        int nameSize = 3;

        // when
        var fluxOfLetters = fluxAndMonoGeneratorService.getFluxOfNamesWithConcatMapAndFilter(nameSize);

        // then
        StepVerifier.create(fluxOfLetters)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void getFluxOfLettersWithFlatMapManyAndFilterTest() {
        // given
        int nameSize = 3;

        // when
        var fluxOfLetters = fluxAndMonoGeneratorService.getFluxOfLettersWithFlatMapManyAndFilter(nameSize);

        // then
        StepVerifier.create(fluxOfLetters)
                .expectNext("A", "L", "E", "X")
                .expectComplete()
                .verify();
    }

    @Test
    void getFluxOfLettersWithTransformAndDefaultIfEmptyTest() {
        // given
        int nameSize = 3;

        // when
        var fluxOfLetters = fluxAndMonoGeneratorService.getFluxOfLettersWithTransformAndDefaultIfEmpty(nameSize);

        // then
        StepVerifier.create(fluxOfLetters)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void getFluxOfDefaultWithTransformAndDefaultIfEmptyTest() {
        // given
        int nameSize = 6;

        // when
        var fluxOfDefault = fluxAndMonoGeneratorService.getFluxOfLettersWithTransformAndDefaultIfEmpty(nameSize);

        // then
        StepVerifier.create(fluxOfDefault)
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void getFluxOfDefaultWithTransformAndSwitchIfEmptyTest() {
        // given
        int nameSize = 6;

        // when
        var fluxOfLetters = fluxAndMonoGeneratorService.getFluxOfLettersWithTransformAndSwitchIfEmpty(nameSize);

        // then
        StepVerifier.create(fluxOfLetters)
                .expectNext("D", "E", "F", "A", "U", "L", "T")
                .verifyComplete();
    }

    @Test
    void getFluxOfStringWithConcatTest() {
        // when
        var concatFlux = fluxAndMonoGeneratorService.getFluxOfStringWithConcat();

        // then
        StepVerifier.create(concatFlux)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void getFluxOfStringWithConcatWithTest() {
        // when
        var concatFlux = fluxAndMonoGeneratorService.getFluxOfStringWithConcatWith();

        // then
        StepVerifier.create(concatFlux)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void getFluxOfStringWithMonosAndConcatWithTest() {
        // when
        var concatFlux = fluxAndMonoGeneratorService.getFluxOfStringWithMonosAndConcatWith();

        // then
        StepVerifier.create(concatFlux)
                .expectNext("A", "B")
                .verifyComplete();
    }

    @Test
    void getFluxOfStringWithMergeTest() {
        // when
        var mergeFlux = fluxAndMonoGeneratorService.getFluxOfStringWithMerge();

        // then
        StepVerifier.create(mergeFlux)
                .expectNext("A", "D", "B", "E", "C", "F")
                .verifyComplete();
    }

    @Test
    void getFluxOfStringWithMergeWithTest() {
        // when
        var mergeFlux = fluxAndMonoGeneratorService.getFluxOfStringWithMergeWith();

        // then
        StepVerifier.create(mergeFlux)
                .expectNext("A", "D", "B", "E", "C", "F")
                .verifyComplete();
    }

    @Test
    void getFluxOfStringWithMonosAndMergeWithTest() {
        // when
        var mergeFlux = fluxAndMonoGeneratorService.getFluxOfStringWithMonosAndMergeWith();

        // then
        StepVerifier.create(mergeFlux)
                .expectNext("A","B")
                .verifyComplete();
    }

    @Test
    void getFluxOfStringWithMergeSequentialTest() {
        // when
        var mergeFlux = fluxAndMonoGeneratorService.getFluxOfStringWithMergeSequential();

        // then
        StepVerifier.create(mergeFlux)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void getFluxOfStringWithZip1Test() {
        // when
        var zipFlux = fluxAndMonoGeneratorService.getFluxOfStringWithZip1();

        // then
        StepVerifier.create(zipFlux)
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }

    @Test
    void getFluxOfStringWithZip2Test() {
        // when
        var zipFlux = fluxAndMonoGeneratorService.getFluxOfStringWithZip2();

        // then
        StepVerifier.create(zipFlux)
                .expectNext("AD14", "BE25", "CF36")
                .verifyComplete();
    }

    @Test
    void getFluxOfStringWithZipWithTest() {
        // when
        var zipFlux = fluxAndMonoGeneratorService.getFluxOfStringWithZipWith();

        // then
        StepVerifier.create(zipFlux)
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }

    @Test
    void getFluxOfStringWithException1Test() {
        // when
        var fluxOfStrings = fluxAndMonoGeneratorService.getFluxOfStringWithException();

        // then
        StepVerifier.create(fluxOfStrings)
                .expectNext("A", "B", "C")
                .expectError()
                .verify();
    }

    @Test
    void getFluxOfStringWithException2Test() {
        // when
        var fluxOfStrings = fluxAndMonoGeneratorService.getFluxOfStringWithException();

        // then
        StepVerifier.create(fluxOfStrings)
                .expectNext("A", "B", "C")
                // Opcionalmente, podemos indicar el tipo de error o excepción que debe emitir el publicador
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void getFluxOfStringWithException3Test() {
        // when
        var fluxOfStrings = fluxAndMonoGeneratorService.getFluxOfStringWithException();

        // then
        StepVerifier.create(fluxOfStrings)
                .expectNext("A", "B", "C")
                // En vez de indicar el tipo de excepción que debe emitir el publicador, podemos indicar el mensaje
                // de error de la excepción que debe emitir.
                .expectErrorMessage("Exception occurred!")
                .verify();
    }

    @Test
    void getFluxOfStringWithExceptionAndOnErrorReturnTest() {
        // when
        var fluxOfStrings = fluxAndMonoGeneratorService.getFluxOfStringWithExceptionAndOnErrorReturn();

        // then
        StepVerifier.create(fluxOfStrings)
                .expectNext("A", "B", "C", "D")
                .verifyComplete();
    }

    @Test
    void getFluxOfStringWithExceptionAndOnErrorResume1Test() {
        // given
        IllegalArgumentException ex = new IllegalArgumentException("Not a valid state");

        // when
        var fluxOfStrings = fluxAndMonoGeneratorService.getFluxOfStringWithExceptionAndOnErrorResume(ex);

        // then
        StepVerifier.create(fluxOfStrings)
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    void getFluxOfStringWithExceptionAndOnErrorResume2Test() {
        // given
        RuntimeException ex = new RuntimeException("Not a valid state");

        // when
        var fluxOfStrings = fluxAndMonoGeneratorService.getFluxOfStringWithExceptionAndOnErrorResume(ex);

        // then
        StepVerifier.create(fluxOfStrings)
                .expectNext("A", "B", "C")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void getFluxOfStringWithExceptionAndOnErrorContinueTest() {
        // when
        var fluxOfStrings = fluxAndMonoGeneratorService.getFluxOfStringWithExceptionAndOnErrorContinue();

        // then
        StepVerifier.create(fluxOfStrings)
                .expectNext("A", "C", "D")
                .verifyComplete();
    }

    @Test
    void getFluxOfStringWithExceptionAndOnErrorMapTest() {
        // when
        var fluxOfStrings = fluxAndMonoGeneratorService.getFluxOfStringWithExceptionAndOnErrorMap();

        // then
        StepVerifier.create(fluxOfStrings)
                .expectNext("A")
                .expectError(ReactorException.class)
                .verify();
    }

    @Test
    void getFluxOfStringWithExceptionAndDoOnErrorTest() {
        // when
        var fluxOfStrings = fluxAndMonoGeneratorService.getFluxOfStringWithExceptionAndDoOnError();

        // then
        StepVerifier.create(fluxOfStrings)
                .expectNext("A", "B", "C")
                .expectError(IllegalStateException.class)
                .verify();
    }

    @Test
    void getFluxOfNamesImmutabilityTest() {
        // when
        var fluxOfNames = fluxAndMonoGeneratorService.getFluxOfNamesImmutability();

        // then
        StepVerifier.create(fluxOfNames)
                .expectNext("alex", "ben", "chloe")
                .verifyComplete();
    }

    @Test
    void getMonoOfNameTest() {
        // when
        var monoOfName = fluxAndMonoGeneratorService.getMonoOfName();

        // then
        StepVerifier.create(monoOfName)
                .expectNext("alex")
                // Los 2 últimos métodos son equivalentes a usar el método "verifyComplete"
                .expectComplete()
                .verify();
    }

    @Test
    void getMonoOfNameWithMapAndFilterTest() {
        // given
        int nameSize = 3;

        // when
        var monoOfName = fluxAndMonoGeneratorService.getMonoOfNameWithMapAndFilter(nameSize);

        // then
        StepVerifier.create(monoOfName)
                .expectNextCount(1)
                .expectComplete()
                .verify();
    }

    @Test
    void getMonoOfNameWithFlatMapAndFilterTest() {
        // given
        int nameSize = 3;

        // when
        var monoOfListOfLetters = fluxAndMonoGeneratorService.getMonoOfNameWithFlatMapAndFilter(nameSize);

        // then
        StepVerifier.create(monoOfListOfLetters)
                .expectNext(List.of("A", "L", "E", "X"))
                .expectComplete()
                .verify();
    }

    @Test
    void getMonoOfDefaultWithMapFilterAndDefaultIfEmptyTest() {
        // given
        int nameSize = 4;

        // when
        var monoOfDefault = fluxAndMonoGeneratorService.getMonoOfDefaultWithMapFilterAndDefaultIfEmpty(nameSize);

        // then
        StepVerifier.create(monoOfDefault)
                .expectNext("default")
                .expectComplete()
                .verify();
    }

    @Test
    void getMonoOfDefaultWithMapFilterAndSwitchIfEmptyTest() {
        // given
        int nameSize = 4;

        // when
        var monoOfDefault = fluxAndMonoGeneratorService.getMonoOfDefaultWithMapFilterAndSwitchIfEmpty(nameSize);

        // then
        StepVerifier.create(monoOfDefault)
                .expectNext("default")
                .expectComplete()
                .verify();
    }

    @Test
    void getMonoOfStringWithZipWithTest() {
        // when
        var zipMono = fluxAndMonoGeneratorService.getMonoOfStringWithZipWith();

        // then
        StepVerifier.create(zipMono)
                .expectNext("AB")
                .expectComplete()
                .verify();
    }

    @Test
    void getMonoOfStringWithExceptionAndOnErrorReturnTest() {
        // when
        var monoOfString = fluxAndMonoGeneratorService.getMonoOfStringWithExceptionAndOnErrorReturn();

        // then
        StepVerifier.create(monoOfString)
                .expectNext("abc")
                .expectComplete()
                .verify();
    }

    @Test
    void getMonoOfStringWithExceptionAndOnErrorMapTest() {
        // when
        var monoOfString = fluxAndMonoGeneratorService.getMonoOfStringWithExceptionAndOnErrorMap();

        // then
        StepVerifier.create(monoOfString)
                .expectError(ReactorException.class)
                .verify();
    }

    @Test
    void getMonoOfStringWithExceptionAndOnErrorContinue1Test() {
        // given
        var name = "abc";

        // when
        var monoOfString = fluxAndMonoGeneratorService.getMonoOfStringWithExceptionAndOnErrorContinue(name);

        // then
        StepVerifier.create(monoOfString)
                .expectComplete()
                .verify();
    }

    @Test
    void getMonoOfStringWithExceptionAndOnErrorContinue2Test() {
        // given
        var name = "reactor";

        // when
        var monoOfString = fluxAndMonoGeneratorService.getMonoOfStringWithExceptionAndOnErrorContinue(name);

        // then
        StepVerifier.create(monoOfString)
                .expectNext(name)
                .expectComplete()
                .verify();
    }
}

package com.mlorenzo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

// Nota: La funcionalidad "BackPressure" solo tiene lugar en la programación reactiva y consiste en dar a un subscriptor
// de un publicador la capacidad de controlar el número de elementos que desea recibir de ese publicador. Por ejemplo,
// puede solicitar al publicador 2 elementos, luego puede cancelar la subscripción para no recibir más elementos o
// puede solicitar al publicador los siguientes n elementos(n se corresponde con otra cantidad de elementos que solicita
// el subscriptor).

@Slf4j
class BackpressureTest {

    @Test
    void testBackPressure1() {
        var fluxOfRange = Flux.range(1, 100).log();

        // Sin usar la funcionalidad "BackPressure", es decir, en este caso, el publicador emitirá sus elementos al
        // subscriptor de golpe
        //fluxOfRange.subscribe(num -> log.info("Number is: {}", num));

        // Para usar la funcilidad "BackPressure", tenemso que sobrescribir el comportamiento por defecto de una
        // subscripción.
        fluxOfRange.subscribe(new BaseSubscriber<Integer>() {

            // En este caso, justo después de producirse la subscripción, se solicta al publicador los 2 primeros
            // elementos.
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                request(2);
            }

            // Tarea a realizar cuando se emite un elemento.
            @Override
            protected void hookOnNext(Integer value) {
                log.info("hookOnNext: {}", value);

                // Se cancela la subscripción cuando se han emitido 2 elementos
                if(value == 2)
                    cancel();
            }

            @Override
            protected void hookOnComplete() {
                //super.hookOnComplete();
            }

            @Override
            protected void hookOnError(Throwable throwable) {
                //super.hookOnError(throwable);
            }

            // Tarea a realizar cuando se cancela una subscripción.
            @Override
            protected void hookOnCancel() {
                log.info("Inside OnCancel");
            }
        });
    }

    @Test
    void testBackPressure2() throws InterruptedException {
        var fluxOfRange = Flux.range(1, 100).log();

        // Usamos este CountDownLatch como una forma para verificar que se han emitido 50 elementos de 2 en 2 mediante
        // una aserción.
        var latch = new CountDownLatch(1);

        // Sin usar la funcionalidad "BackPressure", es decir, en este caso, el publicador emitirá sus elementos al
        // subscriptor de golpe
        //fluxOfRange.subscribe(num -> log.info("Number is: {}", num));

        // Para usar la funcilidad "BackPressure", tenemso que sobrescribir el comportamiento por defecto de una
        // subscripción.
        fluxOfRange.subscribe(new BaseSubscriber<Integer>() {

            // En este caso, justo después de producirse la subscripción, se solicta al publicador los 2 primeros
            // elementos.
            @Override
            protected void hookOnSubscribe(Subscription subscription) {
                request(2);
            }

            // Tarea a realizar cuando se emite un elemento.
            @Override
            protected void hookOnNext(Integer value) {
                log.info("hookOnNext: {}", value);

                // Queremos recibir 50 elementos de 2 en 2 y después cancelar la subscripción.
                if(value < 50) {
                    if(value % 2 == 0)
                        request(2);
                }
                else
                    cancel();
            }

            @Override
            protected void hookOnComplete() {
                //super.hookOnComplete();
            }

            @Override
            protected void hookOnError(Throwable throwable) {
                //super.hookOnError(throwable);
            }

            // Tarea a realizar cuando se cancela una subscripción.
            @Override
            protected void hookOnCancel() {
                log.info("Inside OnCancel");

                latch.countDown();
            }
        });

        assertTrue(latch.await(5L, TimeUnit.SECONDS));
    }
}

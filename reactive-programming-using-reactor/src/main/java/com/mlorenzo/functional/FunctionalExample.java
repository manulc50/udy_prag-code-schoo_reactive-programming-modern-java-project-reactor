package com.mlorenzo.functional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FunctionalExample {

    public static void main(String[] args) {
        var namesList = List.of("alex", "ben", "chloe", "adam", "adam");

        System.out.println("newNamesList: " + getNamesGreaterThanTheSize(namesList, 3));

    }

    private static List<String> getNamesGreaterThanTheSize(List<String> namesList, int size) {
        return namesList.stream()
                .filter(name -> name.length() > size)
                // Versión simplificada de la expresión "name -> name.toUpperCase()"
                .map(String::toUpperCase)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}

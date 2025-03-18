package ar.edu.utnfrc.backend;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {
    public static void main(String[] args) throws URISyntaxException, IOException {
        URL location = App.class.getResource("/texto.txt");
        String text = Files.readString(Paths.get(location.toURI()));

        // elimino signos de puntuacion y caracteres especiales
        text = text.replaceAll("[^a-zA-Z0-9 ]", " ");

        
        Map<String, Long> wordCounts = Stream.of(text.split("\\s+")) // divido el texto en palabras usando los espacios que genere arriba
                .filter(word -> !word.trim().isEmpty()) // elimino palabras vacias
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())); // agrupo y cuento

        // obtengo las 10 palabras mas frecuentes y las muestro en consola
        wordCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()) // orden descendente
                .limit(10)
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
    }
}

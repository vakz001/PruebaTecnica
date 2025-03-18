package ar.edu.utnfrc.backend;

import ar.edu.utnfrc.backend.entities.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) throws URISyntaxException, IOException {

        URL location = App.class.getResource("/libros.csv");

        List<String> lines = Files.readAllLines(Paths.get(location.toURI()));

        List<Book> books = lines.stream()
                .skip(1) // Saltar el encabezado
                .map(App::parseBook)
                .filter(Objects::nonNull)
                .distinct()
                .sorted(Comparator.comparing(Book::getTitle))// Ordeno por titulo
                .collect(Collectors.toList());

        Map<String, List<Book>> booksByAuthor = books.stream()
                .collect(Collectors.groupingBy(Book::getAuthor)); // Agrupo por autor

        Map<Integer, List<Book>> booksByAnio = books.stream()
                .collect(Collectors.groupingBy(Book::getYear)); // Agrupo por autor

        System.out.println("Lista de libros ordenados por titulo:");
        books.forEach(book -> {
            System.out.println(book.toString());
        });

        System.out.println("Libros agrupados por autor:");
        booksByAuthor.forEach((author, bookList) -> {
            System.out.println("\n Author: " + author);
            bookList.forEach(book ->
                    System.out.println(" - " + book.getTitle() + " (" + book.getYear() + ")"));
        });

        System.out.println("Libros agrupados por año:");
        booksByAnio.forEach((year, bookList) -> {
            System.out.println("\n Year: " + year);
            bookList.forEach(book ->
                    System.out.println(" - " + book.getTitle() + " (" + book.getAuthor() + ")"));
        });

    }

    private static Book parseBook(String line) {
        String[] parts = line.split(",");
        if (parts.length != 3) return null;

        String title = parts[0].trim();
        if (title.isEmpty()) return null; // Descartar libros sin titulo

        String author = parts[1].trim().isEmpty() ? "Author Unknown" : parts[1].trim();

        int year;
        try {
            year = Integer.parseInt(parts[2].trim());
            if (year < 0 || year > 2025) year = 0; // Año invalido
        } catch (NumberFormatException e) {
            year = 0;
        }

        return new Book(title,author,year);
    }
}
package com.denisjulio.bookstoread;

import static java.util.stream.Collectors.toList;

import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BookShelf {

    private final List<Book> books = new ArrayList<>();

    public List<Book> books() {
        return Collections.unmodifiableList(books);
    }

    public void add(Book... booksToAdd) {
        Arrays.stream(booksToAdd).forEach(books::add);
    }

    public List<Book> arrange(Comparator<Book> criteria) {
        return books.stream().sorted(criteria).collect(toList());
    }

    public List<Book> arrange() {
        return arrange(Comparator.naturalOrder());
    }

    public Map<Year, List<Book>> groupByPublicationYear() {
        return groupBy(book -> Year.of(book.publishedOn().getYear()));
    }

    public <K> Map<K, List<Book>> groupBy(Function<Book, K> function) {
        return books.stream().collect(Collectors.groupingBy(function));
    }

    public Progress progress() {
        if (books.isEmpty())
            return Progress.OF_EMPTY_SHELF;
        var booksRead = Long.valueOf(books.stream().filter(Book::isRead).count()).intValue();
        var booksToRead = books.size() - booksRead;
        var percentageCompleted = booksRead * 100 / books.size();
        var percentageToRead = booksToRead * 100 / books.size();
        return new Progress(percentageCompleted, percentageToRead, 0);
    }

    public List<Book> findBooksByTitle(String title) {
        return findBooksByTitle(title, b -> true);
    }

    public List<Book> findBooksByTitle(String title, BookFilter filter) {
        return books.stream()
                .filter(filter::apply)
                .filter(b -> b.title().toLowerCase().contains(title.toLowerCase()))
                .collect(toList());
    }

}

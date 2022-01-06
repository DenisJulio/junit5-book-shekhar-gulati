package com.denisjulio.bookstoread;

import java.time.LocalDate;

public class BookPublishedYearFilter{
    
    private BookPublishedYearFilter() {}

    public static BookFilter after(int year) {
        var startDate = LocalDate.of(year, 12, 31);
        return book -> book.publishedOn().isAfter(startDate);
    }

    public static BookFilter before(int year) {
        var startDate = LocalDate.of(year, 1, 1);
        return book -> book.publishedOn().isBefore(startDate); 
    }
}

package com.denisjulio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.Map;

import com.denisjulio.bookstoread.Book;
import com.denisjulio.bookstoread.BookShelf;
import com.denisjulio.bookstoread.Progress;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@DisplayName("a BookShelf progress")
@ExtendWith(BooksParameterResolver.class)
public class BookShelfProgressSpec {

    private BookShelf shelf;
    private Book effectiveJava;
    private Book codeComplete;
    private Book mythicalManMonth;
    private Book cleanCode;
    private Book refactoring;

    @BeforeEach
    void init(Map<String, Book> books) throws Exception {
        shelf = new BookShelf();
        effectiveJava = books.get("Effective Java");
        codeComplete = books.get("Code Complete");
        mythicalManMonth = books.get("The Mythical Man-Month");
        cleanCode = books.get("Clean Code");
        refactoring = books.get("Refactoring");
        books.values().stream().forEach(shelf::add);
    }

    @Test
    @DisplayName("is 0% completed and 100% to-read when no book is read yet")
    void progress100PercentUnread() throws Exception {
        Progress progress = shelf.progress();
        assertAll(
                () -> assertThat(progress.completed()).isEqualTo(0),
                () -> assertThat(progress.toRead()).isEqualTo(100));
    }

    @Test
    @DisplayName("is 40% completed and 60% to-read when 2 books are finished and 3 books not read yet")
    void progressWithCompleteAndToReadPercentages() throws Exception {
        effectiveJava.startedReadingOn(LocalDate.of(2016, 7, 01));
        effectiveJava.finishedReadingOn(LocalDate.of(2016, 7, 31));
        cleanCode.startedReadingOn(LocalDate.of(2016, 8, 01));
        cleanCode.finishedReadingOn(LocalDate.of(2016, 8, 31));
        var progress = shelf.progress();
        assertAll(
                () -> assertThat(progress.completed()).isEqualTo(40),
                () -> assertThat(progress.toRead()).isEqualTo(60));
    }

    @Test
    void testName() throws Exception {
      
    }
}

package com.denisjulio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import com.denisjulio.bookstoread.Book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BookSpec {

    private Book bookOne;
    private Book bookTwo;
    private Book bookThree;

    @BeforeEach
    void init() throws Exception {
        bookOne = new Book("Book one", "Author one", LocalDate.of(2011, 11, 11));
        bookTwo = new Book("Book one", "Author one", LocalDate.of(2011, 11, 11));
        bookThree = new Book("Book one", "Author one", LocalDate.of(2011, 11, 12));
    }

    @Test
    void equalsIsWorking() throws Exception {
        assertEquals(bookOne, bookTwo);
    }

    @Test
    void compareToConsistentWithEquals() throws Exception {
        assertEquals(0, bookOne.compareTo(bookTwo));
    }

    @Test
    void compareDatesWorking() throws Exception {
        assertNotEquals(0, bookOne.compareTo(bookThree));
    }

    @Test
    @DisplayName("parse accepts the provided correct formmat of book string")
    void parseAcceptsValidBookString() throws Exception {
        var correctStr = "Book Name_01-2:Author Name-XI:2021-05-01";
        assertDoesNotThrow(() -> Book.parse(correctStr));
    }

    @Test
    @DisplayName("parse throws at invalid book strings")
    void parseThrowsAtInvalidString() throws Exception {
        var titlNauthStr = "Book One_1900-03:Denis Julio-I";
        var emptyTitlAuth = " : :2021-08-25";
        var emptyAuth = "Book Name: :2021-08-25";
        var singlDigitMontDay = "Book Name_01-2:Author Name-XI:2021-1-1";
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> Book.parse(titlNauthStr)),
                () -> assertThrows(IllegalArgumentException.class, () -> Book.parse(emptyTitlAuth)),
                () -> assertThrows(IllegalArgumentException.class, () -> Book.parse(emptyAuth)),
                () -> assertThrows(IllegalArgumentException.class, () -> Book.parse(singlDigitMontDay)));
    }

    @Test
    @DisplayName("parse returns a valid Book instance")
    void parseReturnsBook() throws Exception {
        var parsed = Book.parse("Book One_1900-03:Denis Julio-I:2021-12-25");
        var other = new Book("Book One_1900-03", "Denis Julio-I", LocalDate.of(2021, 12, 25));
        assertAll(
                () -> assertThat(parsed).isInstanceOf(Book.class),
                () -> assertThat(parsed).isEqualByComparingTo(other),
                () -> assertThat(parsed).isEqualTo(other));
    }
}

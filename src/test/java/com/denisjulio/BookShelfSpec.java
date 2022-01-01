package com.denisjulio;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Year;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.denisjulio.bookstoread.Book;
import com.denisjulio.bookstoread.BookShelf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DisplayName("A bookShelf")
public class BookShelfSpec {

    final Logger logger = LoggerFactory.getLogger(BookShelfSpec.class);

    private BookShelf shelf;
    private Book effectiveJava;
    private Book codeComplete;
    private Book mythicalManMonth;
    private Book cleanCode;

    @BeforeEach
    void init() throws Exception {
        shelf = new BookShelf();
        effectiveJava = Book.parse("Effective Java:Joshua Bloch:2008-05-08");
        codeComplete = Book.parse("Code Complete:Steve McConnel:2004-06-09");
        mythicalManMonth = Book.parse("The Mythical Man-Month:Frederick Phillips Brooks:1975-01-01");
        cleanCode = Book.parse("Clean Code:Robert C. Martin:2008-08-01");
    }

    @Nested
    @DisplayName("is empty")
    class isEmpty {

        @Test
        @DisplayName("when no book is added to it")
        void whenNoBookAdded(TestInfo testInfo) throws Exception {
            var books = shelf.books();
            assertTrue(books.isEmpty(), () -> "BookShelf should be empty");
        }

        @Test
        @DisplayName("when add is called without arguments")
        void whenAddIsCalledWithoutArgs() throws Exception {
            shelf.add();
            var books = shelf.books();
            assertTrue(books.isEmpty(), () -> "BookShelf should be empty");
        }
    }

    @Nested
    @DisplayName("after adding books")
    class afterAddingBooks {

        @Test
        @DisplayName("contains the numbers of books that are added")
        void shelfContainsBooksWhenAdded() throws Exception {
            shelf.add(effectiveJava, codeComplete);
            var books = shelf.books();
            assertEquals(2, books.size(), () -> "Bookshelf should have two books");
        }

        @Test
        @DisplayName("the books exposed to clients should be immutable")
        void booksReturnedFromBookShelfIsImutable() throws Exception {
            shelf.add(effectiveJava, codeComplete);
            var books = shelf.books();
            try {
                books.add(mythicalManMonth);
                fail(() -> "Should not be able to add book to books");
            } catch (Exception e) {
                assertTrue(e instanceof UnsupportedOperationException,
                        () -> "Should throw UnsupportedOperationException");
            }
        }
    }

    @Nested
    @DisplayName("is arranged")
    class isArranged {

        @Test
        @DisplayName("by book title when called without args")
        void byBookTitle() throws Exception {
            shelf.add(effectiveJava, codeComplete, mythicalManMonth);
            var books = shelf.arrange();
            assertThat(books).isSortedAccordingTo(Comparator.naturalOrder());
        }

        @Test
        @DisplayName("by user provided criteria(book title lexicographically descending order")
        void byUserProvidedCriteria() throws Exception {
            shelf.add(effectiveJava, mythicalManMonth, codeComplete);
            var reversed = Comparator.<Book>naturalOrder().reversed();
            var books = shelf.arrange(reversed);
            assertThat(books).isSortedAccordingTo(reversed);
        }

        @Test
        @DisplayName("by publication date in ascending order")
        void byPublicationDate() throws Exception {
            shelf.add(codeComplete, effectiveJava, mythicalManMonth, cleanCode);
            var books = shelf.arrange((b1, b2) -> b1.publishedOn().compareTo(b2.publishedOn()));
            assertThat(books).isSortedAccordingTo((b1, b2) -> b1.publishedOn().compareTo(b2.publishedOn()));
        }

        @Test
        @DisplayName("and books in bookshelf maintain their insertion order")
        void booksInBookShelfAreInInsertedOrderAfterArrange() throws Exception {
            shelf.add(effectiveJava, codeComplete, mythicalManMonth);
            shelf.arrange();
            var books = shelf.books();
            assertEquals(List.of(effectiveJava, codeComplete, mythicalManMonth), books,
                    () -> "Books in bookshelf are in insertion order");
        }
    }

    // ----------------------------[groupBy]-------------------------------------------------------

    @Test
    @DisplayName("books are grouped by publication year")
    void groupByPublicationYear() throws Exception {
        shelf.add(effectiveJava, codeComplete, mythicalManMonth, cleanCode);
        Map<Year, List<Book>> booksByPublicationYear = shelf.groupByPublicationYear();
        assertAll(
                () -> assertThat(booksByPublicationYear).containsKey(Year.of(2008))
                        .containsValues(List.of(effectiveJava, cleanCode)),
                () -> assertThat(booksByPublicationYear).containsKey(Year.of(2004))
                        .containsValues(singletonList(codeComplete)),
                () -> assertThat(booksByPublicationYear).containsKey(Year.of(1975))
                        .containsValues(singletonList(mythicalManMonth)));
    }

    @Test
    @DisplayName("books are grouped by user provided criteria(Author Name)")
    void groupByUserCriteria() throws Exception {
        shelf.add(effectiveJava, codeComplete, mythicalManMonth, cleanCode);
        Map<String, List<Book>> booksByAuthor = shelf.groupBy(Book::author);
        assertAll(
                () -> assertThat(booksByAuthor).containsKey("Joshua Bloch")
                        .containsValues(singletonList(effectiveJava)),
                () -> assertThat(booksByAuthor).containsKey("Steve McConnel")
                        .containsValues(singletonList(codeComplete)),
                () -> assertThat(booksByAuthor).containsKey("Frederick Phillips Brooks")
                        .containsValues(singletonList(mythicalManMonth)),
                () -> assertThat(booksByAuthor).containsKey("Robert C. Martin")
                        .containsValues(singletonList(cleanCode)));
    }
}

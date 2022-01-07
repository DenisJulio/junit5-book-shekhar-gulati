package com.denisjulio;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.Year;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.denisjulio.bookstoread.Book;
import com.denisjulio.bookstoread.BookShelf;
import com.denisjulio.bookstoread.Progress;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DisplayName("A bookShelf...")
@ExtendWith(BooksParameterResolver.class)
public class BookShelfSpec {

    final Logger logger = LoggerFactory.getLogger(BookShelfSpec.class);

    private BookShelf shelf;
    private Book effectiveJava;
    private Book codeComplete;
    private Book mythicalManMonth;
    private Book cleanCode;

    @BeforeEach
    void init(Map<String, Book> books) throws Exception {
        shelf = new BookShelf();
        effectiveJava = books.get("Effective Java");
        codeComplete = books.get("Code Complete");
        mythicalManMonth = books.get("The Mythical Man-Month");
        cleanCode = books.get("Clean Code");
    }

    @Nested
    @DisplayName("...is empty...")
    class isEmpty {

        @Test
        @DisplayName("...when no book is added to it")
        void whenNoBookAdded() throws Exception {
            var books = shelf.books();
            assertTrue(books.isEmpty(), () -> "BookShelf should be empty");
        }

        @Test
        @DisplayName("...when add is called without arguments")
        void whenAddIsCalledWithoutArgs() throws Exception {
            shelf.add();
            var books = shelf.books();
            assertTrue(books.isEmpty(), () -> "BookShelf should be empty");
        }
    }

    @Nested
    @DisplayName("...after adding books...")
    class afterAddingBooks {

        @BeforeEach
        void init() {
            shelf.add(effectiveJava, codeComplete);
        }

        @Test
        @DisplayName("...contains the numbers of books that are added")
        void shelfContainsBooksWhenAdded() throws Exception {
            var books = shelf.books();
            assertEquals(2, books.size(), () -> "Bookshelf should have two books");
        }

        @Test
        @DisplayName("...the books exposed to clients should be immutable")
        void booksReturnedFromBookShelfIsImutable() throws Exception {
            var books = shelf.books();
            assertThatThrownBy(() -> books.add(mythicalManMonth))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    @Nested
    @DisplayName("...is arranged...")
    class isArranged {

        @BeforeEach
        void init() {
            shelf.add(effectiveJava, codeComplete, mythicalManMonth);
        }

        @Test
        @DisplayName("...by book title when called without args")
        void byBookTitle() throws Exception {
            var books = shelf.books();
            books = shelf.arrange();
            assertThat(books).isSortedAccordingTo(Comparator.naturalOrder());
        }

        @Test
        @DisplayName("...by user provided criteria(book title lexicographically descending order")
        void byUserProvidedCriteria() throws Exception {
            var reversed = Comparator.<Book>naturalOrder().reversed();
            var books = shelf.arrange(reversed);
            assertThat(books).isSortedAccordingTo(reversed);
        }

        @Test
        @DisplayName("...by publication date in ascending order")
        void byPublicationDate() throws Exception {
            var books = shelf.arrange((b1, b2) -> b1.publishedOn().compareTo(b2.publishedOn()));
            assertThat(books).isSortedAccordingTo((b1, b2) -> b1.publishedOn().compareTo(b2.publishedOn()));
        }

        @Test
        @DisplayName("...and books in bookshelf maintain their insertion order")
        void booksInBookShelfAreInInsertedOrderAfterArrange() throws Exception {
            shelf.arrange();
            var books = shelf.books();
            assertThat(List.of(effectiveJava, codeComplete, mythicalManMonth)).isEqualTo(books);
        }
    }

    // ----------------------------[groupBy]-------------------------------------------------------

    @Nested
    @DisplayName("...is grouped by...")
    class groupedBy {

        @BeforeEach
        void init() {
            shelf.add(effectiveJava, codeComplete, mythicalManMonth, cleanCode);
        }

        @Test
        @DisplayName("...publication year")
        void publicationYear() throws Exception {
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
        @DisplayName("...user provided criteria(Author Name)")
        void userCriteria() throws Exception {
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

    @Nested
    @DisplayName("...when asked for its progress...")
    class whenAskedForProgress {

        @Test
        @DisplayName("...returns 0 for all Progress fields if shelf is empty")
        void andShelfIsEmpty() throws Exception {
            Progress progress = shelf.progress();
            assertAll(
                    () -> assertThat(shelf.books()).isEmpty(),
                    () -> assertThat(progress.completed()).isZero(),
                    () -> assertThat(progress.toRead()).isZero(),
                    () -> assertThat(progress.inProgress()).isZero());
        }
    }

    @Nested
    @DisplayName("...can be searched by...")
    class search {

        @BeforeEach
        void setup() {
            shelf.add(effectiveJava, codeComplete, mythicalManMonth, cleanCode);
        }

        @Test
        @DisplayName("...substring of book title")
        void shouldFindBooksWithTitleContainingText() throws Exception {
            List<Book> books = shelf.findBooksByTitle("code");
            assertThat(books.size()).isEqualTo(2);
        }

        @Test
        @DisplayName("...substring of book title and user provided filter criteria(Publised Date)")
        void shouldFilterSearchedBooksBasedOnPublishedDate() throws Exception {
            List<Book> books = shelf.findBooksByTitle("code",
                    b -> b.publishedOn().isBefore(LocalDate.of(2014, 12, 31)));
            assertThat(books.size()).isEqualTo(2);
        }
    }
}

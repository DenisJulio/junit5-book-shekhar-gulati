package com.denisjulio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DisplayName("BookShelf Specification")
public class BookShelfSpec {

    final Logger logger = LoggerFactory.getLogger(BookShelfSpec.class);

    private BookShelf shelf;

    @BeforeEach
    void init() throws Exception {
        shelf = new BookShelf();
    }

    @Test
    @DisplayName("is empty when no book is added to it")
    void shelfEmptyWhenNobodyAdded(TestInfo testInfo) throws Exception {
        var books = shelf.books();
        assertTrue(books.isEmpty(), () -> "BookShelf should be empty");
    }

    @Test
    @DisplayName("contains the numbers of books that are added")
    void bookShelfContainsBooksWhenAdded() throws Exception {
        shelf.add("Effective Java", "Code Complete");
        var books = shelf.books();
        assertEquals(2, books.size(), () -> "Bookshelf should have two books");
    }

    @Test
    @DisplayName("the BookShelf should be empty when add is called without arguments")
    void emptyBookShelfWhenAddIsCalledWithoutArgs() throws Exception {
        shelf.add();
        var books = shelf.books();
        assertTrue(books.isEmpty(), () -> "BookShelf should be empty");
    }

    @Test
    @DisplayName("the books exposed to clients should be immutable")
    void booksReturnedFromBookShelfIsImutable() throws Exception {
        shelf.add("Effective Java", "Code Complete");
        var books = shelf.books();
        try {
            books.add("The Mythical Man-Month");
            fail(() -> "Should not be able to add book to books");
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException, () -> "Should throw UnsupportedOperationException");
        }
    }
}

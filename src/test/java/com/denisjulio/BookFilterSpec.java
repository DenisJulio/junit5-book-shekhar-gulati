package com.denisjulio;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import com.denisjulio.bookstoread.Book;
import com.denisjulio.bookstoread.BookFilter;
import com.denisjulio.bookstoread.BookPublishedYearFilter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@DisplayName("A book filter...")
@ExtendWith(BooksParameterResolver.class)
public class BookFilterSpec {
    
    private Book cleanCode;
    private Book codeComplete;

    @BeforeEach
    void init(Map<String, Book> books) {
        codeComplete = books.get("Code Complete");
        cleanCode = books.get("Clean Code");
    }
    
    @Nested
    @DisplayName("...of book published date...")
    class BookPublisedDatePostAskedYear {

        @Test
        @DisplayName("...is after specified year")
        void validateBookPublishedDatePostAskedYear() throws Exception {
          BookFilter filter = BookPublishedYearFilter.after(2007);
          assertTrue(filter.apply(cleanCode));
          assertFalse(filter.apply(codeComplete));
        }

        @Test
        void validateBookPublishedDateBeforeAskedYear() throws Exception {
          BookFilter filter = BookPublishedYearFilter.before(2007);
          assertAll(
              () -> assertThat(filter.apply(codeComplete)).isTrue(),
              () -> assertThat(filter.apply(cleanCode)).isFalse()
          );
        }
    }

}

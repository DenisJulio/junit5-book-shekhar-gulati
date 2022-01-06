package com.denisjulio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Map;

import com.denisjulio.bookstoread.Book;
import com.denisjulio.bookstoread.BookFilter;
import com.denisjulio.bookstoread.BookPublishedYearFilter;
import com.denisjulio.bookstoread.CompositeFilter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        @DisplayName("...is before specified year")
        void validateBookPublishedDateBeforeAskedYear() throws Exception {
            BookFilter filter = BookPublishedYearFilter.before(2007);
            assertAll(
                    () -> assertThat(filter.apply(codeComplete)).isTrue(),
                    () -> assertThat(filter.apply(cleanCode)).isFalse());
        }
    }
    @Nested
    @ExtendWith(MockitoExtension.class)
    @DisplayName("...of composite criteria...")
    class CompositeCriteria implements BookFilterBoundaryTests {

        private CompositeFilter compositeFilter;
        @Mock private BookFilter firstFilter;
        @Mock private BookFilter secondFilter;

        @Override
        public BookFilter filter() {
            return firstFilter;
        }

        @BeforeEach
        void init() {
            compositeFilter = new CompositeFilter();
        }

        @Test
        @DisplayName("...should apply multiple filters")
        void shouldFilterOnMultiplesCriteria() throws Exception {
            when(firstFilter.apply(cleanCode)).thenReturn(false);
            compositeFilter.addFilter(firstFilter);
            assertThat(compositeFilter.apply(cleanCode)).isFalse();
        }

        @DisplayName("...invokes subsequent filters after first failure")
        @Test
        void invokeAllAfterFirstFailure() throws Exception {
            when(firstFilter.apply(cleanCode)).thenReturn(false);
            compositeFilter.addFilter(firstFilter);

            when(secondFilter.apply(cleanCode)).thenReturn(true);
            compositeFilter.addFilter(secondFilter);

            assertThat(compositeFilter.apply(cleanCode)).isFalse();
            verify(firstFilter).apply(cleanCode);
            verify(secondFilter).apply(cleanCode);
        }
    
        @Test
        @DisplayName("...invokes all filters")
        void shouldInvokeAllFilters() throws Exception {
            when(firstFilter.apply(cleanCode)).thenReturn(true);
            compositeFilter.addFilter(firstFilter);

            when(secondFilter.apply(cleanCode)).thenReturn(true);
            compositeFilter.addFilter(secondFilter);
            
            assertThat(compositeFilter.apply(cleanCode)).isTrue();
        }
    }
}

package com.denisjulio;

import static org.assertj.core.api.Assertions.assertThat;

import com.denisjulio.bookstoread.BookFilter;

import org.junit.jupiter.api.Test;

public interface BookFilterBoundaryTests {
    
    BookFilter filter();

    @Test
    default void returnsFalseForNullBook() throws Exception {
      assertThat(filter().apply(null)).isFalse();
    }
}

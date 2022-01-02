package com.denisjulio;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.denisjulio.bookstoread.Book;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class BooksParameterResolver implements ParameterResolver {

    private Map<String, Book> books;

    public BooksParameterResolver() {
        this.books = new HashMap<>();
        books.put("Effective Java", Book.parse("Effective Java:Joshua Bloch:2008-05-08"));
        books.put("Code Complete", Book.parse("Code Complete:Steve McConnel:2004-06-09"));
        books.put("The Mythical Man-Month", Book.parse("The Mythical Man-Month:Frederick Phillips Brooks:1975-01-01"));
        books.put("Clean Code", Book.parse("Clean Code:Robert C. Martin:2008-08-01"));
        books.put("Refactoring", Book.parse("Refactoring:Martin Fowler:2002-03-09"));
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        var store = extensionContext.getStore(ExtensionContext.Namespace.create(Book.class));
        return store.getOrComputeIfAbsent(books, b -> b);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        var param = parameterContext.getParameter();
        return Objects.equals(param.getParameterizedType().getTypeName(),
                "java.util.Map<java.lang.String, com.denisjulio.bookstoread.Book>");
    }

}

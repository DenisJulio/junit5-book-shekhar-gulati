package com.denisjulio.bookstoread;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class Book implements Comparable<Book> {

    private final String title;
    private final String author;
    private final LocalDate publishedOn;
    @EqualsAndHashCode.Exclude private LocalDate startedReadingOn;
    @EqualsAndHashCode.Exclude private LocalDate finishedReadingOn;

    public Book(String title, String author, LocalDate publishedOn) {
        this.title = title;
        this.author = author;
        this.publishedOn = publishedOn;
    }

    public String title() {
        return this.title;
    }

    public String author() {
        return this.author;
    }

    public LocalDate publishedOn() {
        return this.publishedOn;
    }

    @Override
    public int compareTo(Book other) {
        var t = this.title.compareTo(other.title);
        var a = this.author.compareTo(other.author);
        if (t != 0)
            return t;
        if (a != 0)
            return a;
        return this.publishedOn.compareTo(other.publishedOn);
    }

    /**
     * Creates a book instance through the provided string.
     * <p>
     * The valid formmat is:
     * <p>
     * {@code "Book Name:Author Name:2011-01-01"}
     * <p>
     * It must contain the Book name, Author name and publish date separeted by
     * {@code :} with no whitespaces
     * in the start of each field. For Book and Author name {@code _-} are allowed.
     * 
     * @param bookString - A string of the valid formmat
     * @return A new {@code Book} instance.
     * @throws IllegalArgumentException if the string doesn't match the valid
     *                                  formmat
     * @throws DateTimeParseException   if the date cannot be parsed
     */
    public static Book parse(String bookString) {
        var regex = "(\\b\\S[\\w\s-.]+):(\\b\\S[\\w\s-.]+):([\\d]{4}+)-([\\d]{2}+)-([\\d]{2}+)";
        if (!bookString.matches(regex))
            throw new IllegalArgumentException("The bookString doesn??t match the required formmat");
        var args = bookString.split(":");
        return new Book(args[0], args[1], LocalDate.parse(args[2]));
    }

    public void startedReadingOn(LocalDate startedOn) {
        this.startedReadingOn = startedOn;
    }

    public void finishedReadingOn(LocalDate finishedOn) {
        this.finishedReadingOn = finishedOn;
    }

    public boolean isRead() {
        return startedReadingOn != null && finishedReadingOn != null;
    }
}

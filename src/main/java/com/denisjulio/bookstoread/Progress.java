package com.denisjulio.bookstoread;

public record Progress(int completed, int toRead, int inProgress) {
    public static Progress OF_EMPTY_SHELF = new Progress(0, 0, 0);
}

package com.walgreens.readerboard;

import java.io.Serializable;

/**
 * Readerboard
 * com.walgreens.readerboard
 * Copyright 2016 Adam Treadway
 * Remarks:
 *
 * @author Adam Treadway
 * @since 2/22/2016
 */
class Board implements Serializable {
    private final String name;
    private char[] message;
    Board(String name) {
        this.name = name;
    }

    void setMessage(String message) {
        this.message = message.toCharArray();
    }

    char[] getMessage() {
        return message;
    }
}

package com.walgreens.readerboard;

import java.io.Serializable;
import java.util.ArrayList;

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
    String name;
    ArrayList<char[]> messages;
    Board(String name) {
        this.name = name;
        this.messages = new ArrayList<>();
    }
}

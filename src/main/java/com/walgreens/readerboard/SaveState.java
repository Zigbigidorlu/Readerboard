package com.walgreens.readerboard;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Readerboard
 * com.walgreens.readerboard
 * Copyright 2016 Adam Treadway
 * Remarks:
 *
 * @author Adam Treadway
 * @since 2/22/2016
 */
public class SaveState implements Serializable {
    public List<Board> boards;
    SaveState() {
        boards = new ArrayList<>();
    }

    public SaveState load(int boardCount) throws IOException, ClassNotFoundException {
        File file = new File("rba.db");
        if(file.exists()) {
            FileInputStream in = new FileInputStream(file);
            ObjectInputStream oin = new ObjectInputStream(in);
            return (SaveState) oin.readObject();
        }
        else {
            if(file.createNewFile()) {
                for(int i = 0; i < boardCount; i++) {
                    boards.add(new Board("Untitled Board"));
                }

                FileOutputStream out = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(out);
                oos.writeObject(this);

                return this;
            }
            else {
                throw new IOException("Unable to write save state");
            }
        }
    }
}

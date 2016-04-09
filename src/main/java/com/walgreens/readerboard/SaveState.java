package com.walgreens.readerboard;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
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
class SaveState implements Serializable {
    static final long serialVersionUID = 1000L;

    private int week, year;
    int default_board = 0;
    final List<Board> boards;
    SaveState() {
        boards = new ArrayList<>();
    }

    SaveState load() throws IOException, ClassNotFoundException {
        // Get saves directory
        File dir = new File("saves");

        // Make save directory if it doesn't exist
        if(!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Unable to write save state directory");
        }

        // Find the most recently modified file (this week's)
        File file = getRecent(dir);

        return load(file);
    }

    File getRecent(File dir) {
        // Get listing of files
        File[] files = dir.listFiles(File::isFile);

        File file = null;
        long lastMod = Long.MIN_VALUE;
        if (files != null) {
            for (File f : files) {
                if (f.lastModified() > lastMod) {
                    if (f.getName().endsWith(".rb")) {
                        file = f;
                        lastMod = f.lastModified();
                    }
                }
            }
        }
        return file;
    }

    SaveState load(File file) throws IOException, ClassNotFoundException {
        if(file != null && file.exists()) {
            FileInputStream in = new FileInputStream(file);
            ObjectInputStream oin = new ObjectInputStream(in);
            return (SaveState) oin.readObject();
        }
        else {
            // Build dummy boards
            for (int i = 0; i < Main.boardCount; i++) {
                Board b = new Board("Untitled Board");
                for(int j = 0; j < Main.lineCount; j++) {
                    String message = "Default Message";
                    b.messages.add(message.toCharArray());
                }
                boards.add(b);
            }

            return save();
        }
    }

    SaveState save() throws IOException {
        // Build file name based on week
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        week = calendar.get(Calendar.WEEK_OF_YEAR);

        // Make save file
        File f = new File("saves/" + year + "." + week + ".rb");
        if (f.exists() || f.createNewFile()) {
            FileOutputStream out = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(this);

            return this;
        }
        else {
            throw new IOException("Unable to write save state");
        }
    }

    void setDefault(Board board) throws IOException {
        default_board = boards.indexOf(board);
        save();
    }
}

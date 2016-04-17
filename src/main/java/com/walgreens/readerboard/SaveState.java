package com.walgreens.readerboard;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
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
    final List<Board> boards;
    int default_board = 0;
    private int week, year;

    SaveState() {
        boards = new ArrayList<>();
    }

    SaveState load() throws IOException, ClassNotFoundException {
        // Get saves directory
        File dir = new File("saves");

        // Make save directory if it doesn't exist
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Unable to write save state directory");
        }

        // Find the most recently modified file (this week's)
        File file = getRecent(dir);

        return load(file);
    }

    private File getRecent(File dir) {
        return getRecent(dir, false);
    }

    File getRecent(File dir, boolean lastWeek) {
        File file = null;

        // Get listing of files
        File[] files = dir.listFiles(File::isFile);

        ArrayList<File> rbFiles = new ArrayList<>();
        if (files != null) {
            // Build file listing with .rb extension
            for (File f : files) {
                if (f.getName().endsWith(".rb")) {
                    rbFiles.add(f);
                }
            }

            // If files exist...
            if (rbFiles.size() > 0) {
                File[] fileList = rbFiles.toArray(new File[rbFiles.size()]);

                // Sort files by last modified (most recent first)
                Arrays.sort(fileList, (f1, f2) -> Long.valueOf(f2.lastModified()).compareTo(f1.lastModified()));

                // Get file, last week if flagged.
                if (fileList.length > 1) {
                    file = fileList[lastWeek ? 1 : 0];
                } else {
                    file = fileList[0];
                }
            }
        }

        return file;
    }

    SaveState load(File file) throws IOException, ClassNotFoundException {
        if (file != null && file.exists()) {
            FileInputStream in = new FileInputStream(file);
            ObjectInputStream oin = new ObjectInputStream(in);
            return (SaveState) oin.readObject();
        } else {
            // Build dummy boards
            for (int i = 0; i < Main.boardCount; i++) {
                Board b = new Board("Untitled Board");
                for (int j = 0; j < Main.lineCount; j++) {
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
        } else {
            throw new IOException("Unable to write save state");
        }
    }

    void setDefault(Board board) throws IOException {
        default_board = boards.indexOf(board);
        save();
    }

    ArrayList<Character> getCharacters() {
        ArrayList<Character> characters = new ArrayList<>();
        for (Board board : boards) {
            characters.addAll(board.getCharacters());
        }
        return characters;
    }
}

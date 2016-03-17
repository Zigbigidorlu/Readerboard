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
public class SaveState implements Serializable {
    public List<Board> boards;
    SaveState() {
        boards = new ArrayList<>();
    }

    public SaveState load(int boardCount) throws IOException, ClassNotFoundException {
        // Build file name based on week
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int week = calendar.get(Calendar.WEEK_OF_YEAR);

        File dir = new File("saves");
        File[] files = dir.listFiles(File::isFile);

        long lastMod = Long.MIN_VALUE;
        File file = null;
        if(files != null) {
            for (File f : files) {
                if (f.lastModified() > lastMod) {
                    file = f;
                    lastMod = f.lastModified();
                }
            }
        }

        if(file != null && file.exists()) {
            FileInputStream in = new FileInputStream(file);
            ObjectInputStream oin = new ObjectInputStream(in);
            return (SaveState) oin.readObject();
        }
        else {
            // Make save directory
            if(!dir.exists() && !dir.mkdirs()) {
                throw new IOException("Unable to write save state directory");
            }

            // Make save file
            File f = new File("saves/" + year + "." + week + ".rb");
            if (f.createNewFile()) {
                for (int i = 0; i < boardCount; i++) {
                    boards.add(new Board("Untitled Board"));
                }

                FileOutputStream out = new FileOutputStream(f);
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

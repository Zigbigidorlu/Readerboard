package com.walgreens.readerboard;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Readerboard
 * com.walgreens.readerboard
 * Copyright 2016 Adam Treadway
 * Remarks:
 *
 * @author Adam Treadway
 * @since 4/10/2016
 */
public class ComparisonPrinter implements Printable {
    private SaveState saveState;
    ComparisonPrinter(SaveState saveState) {
        this.saveState = saveState;

        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat format = new PageFormat();
        format.setOrientation(PageFormat.PORTRAIT);
        job.setPrintable(this, format);
        try {
            if (job.printDialog()) {
                job.print();
            }
        }
        catch (PrinterException e) {
            new CrashHandler(e);
        }
    }

    private void buildComparison() {
        try {
            // Load last week's board
            File lastWeek = saveState.getRecent(new File("saves"), true);
            SaveState lastSaveState = new SaveState().load(lastWeek);

            // Get characters from this week's boards
            ArrayList<Character> characters = saveState.getCharacters();

            // Build letters to keep
            for(Board board : lastSaveState.boards) {
                for(Character c : board.getCharacters()) {
                    for(Character ch : characters) {
                        if(c == ch) {
                            board.keep.add(c);
                            break;
                        }
                    }
                }
            }

            // TODO: Build printable document

        }
        catch (IOException | ClassNotFoundException e) {
            new CrashHandler(e);
        }
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        return 0;
    }
}

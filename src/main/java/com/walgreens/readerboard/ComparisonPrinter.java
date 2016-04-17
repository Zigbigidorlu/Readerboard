package com.walgreens.readerboard;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;
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
                //job.print();
                buildComparison();
            }
        }
        //catch (PrinterException e) {
        catch(Exception e) {
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
                    for(int i = 0; i < characters.size(); i++) {
                        Character ch = characters.get(i);
                        if(c == ch && c != ' ') {
                            board.keep.add(c);
                            characters.remove(i);
                            break;
                        }
                    }
                }
            }

            // TODO: Build printable document

            for(Board board : lastSaveState.boards) {
                System.out.println(board.name + ": " + board.keep);
            }

            printPreview(new File("test.html"));

        }
        catch (IOException | ClassNotFoundException e) {
            new CrashHandler(e);
        }
    }

    private void printPreview(File tempFile) throws IOException {
        String contents;

        // Read temp file to string
        BufferedReader br = new BufferedReader(new FileReader(tempFile));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            contents = sb.toString();

        } finally {
            br.close();
        }

        // Create dialog
        JDialog jd = new JDialog();
        jd.setModal(true);
        jd.setPreferredSize(new Dimension(650,475));

        // Build preview pane
        JEditorPane previewArea = new JEditorPane();
        previewArea.setEditable(false);
        previewArea.setBackground(Color.WHITE);

        // Assign text to pane
        previewArea.setContentType("text/html");
        previewArea.setText(contents);

        // Create a scroll pane, add preview to it
        JScrollPane scrollPane = new JScrollPane(previewArea);
        jd.add(scrollPane);

        jd.pack();
        jd.setVisible(true);
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        return 0;
    }
}

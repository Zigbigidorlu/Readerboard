package com.walgreens.readerboard;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static java.awt.print.Printable.NO_SUCH_PAGE;

/**
 * Readerboard
 * com.walgreens.readerboard
 * Copyright 2016 Adam Treadway
 * Remarks:
 *
 * @author Adam Treadway
 * @since 4/10/2016
 */
class ComparisonPrinter extends JDialog implements ActionListener {
    private SaveState saveState;

    ComparisonPrinter(SaveState saveState) {
        try {
            this.saveState = saveState;

            // Create keep array
            SaveState compared = buildComparison();

            // Get paper for sizing
            Paper paper = new Paper();

            // Build printable panel
            setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
            getContentPane().setBackground(Color.WHITE);

            // Build contents
            for(Board board : compared.boards) {
                JLabel boardName = new JLabel(board.name);
                Font font = boardName.getFont().deriveFont(Font.BOLD, 16);
                boardName.setFont(font);
                add(boardName);

                // Keep letters label
                JLabel keepLetters = new JLabel("Keep letters: ");
                keepLetters.setFont(keepLetters.getFont().deriveFont(Font.ITALIC,14));
                add(keepLetters);

                // Build a label with the kept characters
                String keep = StringUtils.join(board.keep," ").toUpperCase();
                JLabel keeping = new JLabel(keep);
                keeping.setFont(keeping.getFont().deriveFont(12f));
                Dimension size = keeping.getSize();
                size.width = (int) paper.getImageableWidth();
                keeping.setPreferredSize(size);
                add(keeping);

                // Spacer
                add(Box.createVerticalStrut(5));

                // Messages label
                JLabel messages = new JLabel("Messages: ");
                messages.setFont(messages.getFont().deriveFont(Font.ITALIC,14));
                add(messages);

                // Build the new messages
                for(char[] message : board.messages) {
                    String line = StringUtils.join(ArrayUtils.toObject(message), "").toUpperCase();
                    JLabel lineLabel = new JLabel("<HTML>" + line + "</HTML>");
                    lineLabel.setFont(lineLabel.getFont().deriveFont(12f));
                    add(lineLabel);
                }

                // Spacer
                add(Box.createVerticalStrut(10));
            }

            pack();
            doPrint();
        }
        catch (IOException | ClassNotFoundException e) {
            new CrashHandler(e);
        }
    }

    private SaveState buildComparison() throws IOException, ClassNotFoundException {
        // Load last week's board
        File lastWeek = saveState.getRecent(new File("saves"), true);
        SaveState lastSaveState = new SaveState().load(lastWeek);

        // Get characters from this week's boards
        ArrayList<Character> characters = saveState.getCharacters();

        // Build letters to keep
        for (Board board : lastSaveState.boards) {
            for (Character c : board.getCharacters()) {
                for (int i = 0; i < characters.size(); i++) {
                    Character ch = characters.get(i);
                    if (c == ch && c != ' ') {
                        board.keep.add(c);
                        characters.remove(i);
                        break;
                    }
                }
            }
        }

        return lastSaveState;
    }

    private void doPrint() {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setJobName("Readerboard Assistant");

        // Set page format
        PageFormat format = printerJob.defaultPage();
        format.setOrientation(PageFormat.PORTRAIT);

        // Set printable
        printerJob.setPrintable((graphics, pageFormat, pageIndex) -> {
            // Print only one copy
            if (pageIndex > 0) {
                return NO_SUCH_PAGE;
            }

            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.translate(pageFormat.getImageableX(),
                    (pageFormat.getImageableY() - (pageIndex * pageFormat.getImageableHeight())));

            getContentPane().printAll(graphics2D);
            return Printable.PAGE_EXISTS;
        }, printerJob.validatePage(format));

        if (printerJob.printDialog()) {
            try {
                printerJob.print();
            } catch (PrinterException e) {
                new CrashHandler(e);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "print":
                doPrint();
                break;
        }
    }
}

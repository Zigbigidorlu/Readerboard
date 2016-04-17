package com.walgreens.readerboard;

import j2html.tags.ContainerTag;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static com.walgreens.readerboard.UserInterface.wagRed;
import static j2html.TagCreator.*;

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
    private JEditorPane previewArea;

    ComparisonPrinter(SaveState saveState) {
        this.saveState = saveState;
        buildComparison();
    }

    private void buildComparison() {
        try {
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

            // Build an HTML page for printing (using j2html)
            ContainerTag body = body();
            for (Board board : lastSaveState.boards) {
                // Header
                ContainerTag header = h2(board.name);

                // Keep Label
                ContainerTag pre = pre();
                pre.with(label("Keep: " + board.keep), br());

                // Build new messages
                for (char[] chars : board.messages) {
                    for (char aChar : chars) {
                        pre.with(label(aChar + " "));
                    }
                    pre.with(br());
                }

                // Paragraph
                ContainerTag paragraph = p();
                paragraph.with(header, pre);

                // Add to body
                body.with(paragraph);
            }

            // HTML container
            ContainerTag html = html().with(body);

            //File with contents
            File temp = File.createTempFile("readerboard", ".rbml");
            FileUtils.writeStringToFile(temp, html.toString());
            System.out.println(temp.getCanonicalPath());

            // Display print preview
            printPreview(temp);
        } catch (IOException | ClassNotFoundException e) {
            new CrashHandler(e);
        }
    }

    private void printPreview(File tempFile) throws IOException {
        String contents;

        // Read temp file to string
        try (BufferedReader br = new BufferedReader(new FileReader(tempFile))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            contents = sb.toString();

        }

        // Create dialog
        setModal(true);
        setPreferredSize(new Dimension(650, 475));
        setIconImages(UserInterface.icons);
        setTitle("Print Preview");

        // Build base pane
        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        // Build preview pane
        previewArea = new JEditorPane();
        previewArea.setEditable(false);
        previewArea.setBackground(Color.WHITE);

        // Assign text to pane
        previewArea.setContentType("text/html");
        previewArea.setText(contents);

        // Create a scroll pane, add preview to it
        JScrollPane scrollPane = new JScrollPane(previewArea);
        add(scrollPane, BorderLayout.CENTER);

        // Build a simple toolbar
        add(buildToolbar(), BorderLayout.NORTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JToolBar buildToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setLayout(new BorderLayout());

        // Set dimensions
        Dimension dimension = new Dimension();
        dimension.height = 45;
        toolBar.setPreferredSize(dimension);
        toolBar.setBackground(wagRed);

        // Add print button
        ImageButton print = new ImageButton("Print", Color.WHITE, "print_sm.png", this, "print", true);
        toolBar.add(print, BorderLayout.WEST);

        return toolBar;
    }

    private void doPrint() {
        PrinterJob pj = PrinterJob.getPrinterJob();
        if (pj.printDialog()) {
            try {
                previewArea.print();
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

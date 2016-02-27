package com.walgreens.readerboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Readerboard
 * com.walgreens.readerboard
 * Copyright 2016 Adam Treadway
 * Remarks:
 *
 * @author Adam Treadway
 * @since 2/22/2016
 */
public class CrashHandler extends JDialog {
    CrashHandler(Exception e) {
        // Set properties
        setTitle(Main.name + " has crashed!");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // Build dimensions
        Dimension dimension = new Dimension(500,250);
        setPreferredSize(dimension);
        setResizable(false);
        setModal(true);

        // Build the content panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10,10,10,10));
        add(panel);

        // North panel
        JPanel north = new JPanel();
        north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
        JLabel oops = new JLabel("Oops!", JLabel.CENTER);
        JLabel explanation = new JLabel(Main.name + " has crashed!", JLabel.CENTER);
        JLabel explanation2 = new JLabel("Please provide this information to the developer for analysis:", JLabel.CENTER);

        // Alignment
        oops.setAlignmentX(Component.CENTER_ALIGNMENT);
        explanation.setAlignmentX(Component.CENTER_ALIGNMENT);
        explanation2.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Font
        Font regular = new Font("Arial", Font.PLAIN, 14);
        oops.setFont(new Font("Arial", Font.BOLD, 18));
        explanation.setFont(regular);
        explanation2.setFont(regular);

        north.add(oops);
        north.add(explanation);
        north.add(explanation2);

        north.setBorder(new EmptyBorder(0, 0, 10, 0));
        panel.add(north, BorderLayout.NORTH);

        // Center panel
        JTextPane textArea = new JTextPane();
        textArea.setEditable(false);

        Document document = textArea.getDocument();
        try {
            // Add message
            SimpleAttributeSet bold = new SimpleAttributeSet();
            bold.addAttribute(StyleConstants.CharacterConstants.Bold, true);
            StyleConstants.setFontSize(bold,14);
            document.insertString(0, e.getMessage() + "\n\n", bold);

            // Write stack trace
            SimpleAttributeSet italics = new SimpleAttributeSet();
            bold.addAttribute(StyleConstants.CharacterConstants.Italic,true);
            for (StackTraceElement element : e.getStackTrace()) {
                document.insertString(document.getLength()," -> " +
                        element.getFileName() +
                        ":" + element.getMethodName() +
                        ":" + element.getLineNumber() +
                        "\n", italics);
            }
        }
        catch (BadLocationException ble) {
            ble.printStackTrace();
        }

        // Keep scroll up top
        textArea.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(textArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        panel.add(scrollPane, BorderLayout.CENTER);

        // Add "Copy to Clipboard" button
        JPanel south = new JPanel();
        JButton button = new JButton();
        button.setText("Copy message to clipboard and close");
        button.addActionListener(e1 -> {
            StringSelection contents = new StringSelection(textArea.getText());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(contents,null);
            dispose();
        });
        south.add(button);

        // Add basic close button
        JButton close = new JButton();
        close.setText("Close without copying message");
        close.addActionListener(e1 -> dispose());
        south.add(close);

        // Default to close button
        getRootPane().setDefaultButton(close);

        // Add border for spacing
        south.setBorder(new EmptyBorder(10,10,0,10));

        // Add south layout
        panel.add(south, BorderLayout.SOUTH);

        // Add content panel
        add(panel);

        // Pack contents
        pack();

        // Center in screen
        setLocationRelativeTo(null);

        // Run kill operation when the window is disposed
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(-1);
            }
        });

        // Show
        setVisible(true);
    }
}
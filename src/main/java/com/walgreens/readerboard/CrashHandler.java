package com.walgreens.readerboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
public class CrashHandler {
    CrashHandler(Exception e) {
        // Build dialog
        JDialog dialog = new JDialog();
        dialog.setTitle("Readerboard Assistant has crashed!");
        dialog.setAlwaysOnTop(true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // Build dimensions
        Dimension dimension = new Dimension(500,200);
        dialog.setPreferredSize(dimension);
        dialog.setResizable(false);
        dialog.setModal(true);

        // Build the content panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10,10,10,10));
        dialog.add(panel);

        // North panel
        JPanel north = new JPanel();
        north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
        JLabel oops = new JLabel(
                "Oops!", JLabel.CENTER);
        JLabel explanation = new JLabel(
                "Readerboard Assistant has crashed!", JLabel.CENTER);
        JLabel explanation2 = new JLabel(
                "Please provide this information to the developer for analysis:", JLabel.CENTER);

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
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);

        // Add message
        textArea.setText(e.getMessage());
        textArea.append("\n");

        // Write stack trace
        for(StackTraceElement element : e.getStackTrace()) {
            textArea.append(" -> " + element.getFileName() + ":" + element.getMethodName() + ":" + element.getLineNumber());
        }

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
            dialog.dispose();
        });
        south.add(button);

        // Add basic close button
        JButton close = new JButton();
        close.setText("Close without copying message");
        close.addActionListener(e1 -> dialog.dispose());
        south.add(close);

        // Default to close button
        dialog.getRootPane().setDefaultButton(close);

        // Add border for spacing
        south.setBorder(new EmptyBorder(10,10,0,10));

        // Add south layout
        panel.add(south, BorderLayout.SOUTH);

        // Add content panel
        dialog.add(panel);

        // Pack contents
        dialog.pack();

        // Center in screen
        dialog.setLocationRelativeTo(null);

        // Run kill operation when the window is disposed
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(-1);
            }
        });

        // Show
        dialog.setVisible(true);
    }
}
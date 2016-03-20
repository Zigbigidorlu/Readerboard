package com.walgreens.readerboard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Readerboard
 * com.walgreens.readerboard
 * Copyright 2016 Adam Treadway
 * Remarks:
 *
 * @author Adam Treadway
 * @since 3/15/2016
 */
class Debug {
    private static JPanel contents;
    private static final ArrayList<StackTraceElement[]> traceElements;
    private static final ArrayList<String> log;

    static {
        traceElements = new ArrayList<>();
        log = new ArrayList<>();
    }

    static void log(String message) {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        traceElements.add(stack);
        log.add(message);

        if(contents != null) {
            addLogElement(message,stack);
        }
    }

    static void console() {
        JDialog debugConsole = new JDialog();
        debugConsole.setTitle("Debug Console");
        debugConsole.setPreferredSize(new Dimension(500,650));
        debugConsole.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);

        contents = new JPanel();
        contents.setLayout(new BoxLayout(contents,BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(contents);
        scrollPane.setAutoscrolls(true);
        debugConsole.add(scrollPane);

        for(int i = 0; i < log.size(); i++) {
            addLogElement(log.get(i), traceElements.get(i));
        }

        debugConsole.pack();
        debugConsole.setLocationRelativeTo(null);
        debugConsole.setVisible(true);
    }

    private static void addLogElement(String message, StackTraceElement[] trace) {
        JLabel element = new JLabel(message);
        contents.add(element);
        contents.revalidate();
        contents.repaint();
    }
}

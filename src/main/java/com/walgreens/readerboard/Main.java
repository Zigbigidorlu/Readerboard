package com.walgreens.readerboard;

import org.ini4j.Ini;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.Properties;

/**
 * Readerboard
 * com.walgreens.readerboard
 * Copyright 2016 Adam Treadway
 * Remarks:
 *
 * @author Adam Treadway
 * @since 2/22/2016
 */
public class Main {
    // Defaults
    private static final int default_boardCount, default_lineCount, default_maxLength,
            default_windowWidth, default_windowHeight;

    // Local INI file
    private Ini ini;

    // Global variables
    static int boardCount, lineCount, maxLength, windowWidth, windowHeight;
    static String name, version;
    static boolean writeLog;
    private static boolean testRun;

    static {
        default_windowWidth = 600;
        default_windowHeight = 375;
        default_boardCount = 4;
        default_lineCount = 4;
        default_maxLength = 16;
    }


    private Main() {
        // Set look and feel of swing windows
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );
        } catch (ClassNotFoundException |
                InstantiationException |
                IllegalAccessException |
                UnsupportedLookAndFeelException f) {
            // Ignore
        }

        // Check against application lock
        try {
            new ServerSocket(51603);

            // Load data and start GUI
            try {
                getProperties();
                loadINI();
                startGUI();
            } catch (IOException | ClassNotFoundException e) {
                new CrashHandler(e);
            }
        }

        // Application already open
        catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An instance of this application is already open.",
                    "Oops!", JOptionPane.WARNING_MESSAGE);
        }
    }

    // Initialize class
    public static void main(String[] args) {
        writeLog = Arrays.asList(args).contains("-log");
        testRun = Arrays.asList(args).contains("-test");
        new Main();
    }

    private void getProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(UserInterface.class.getClassLoader().getResourceAsStream("project.properties"));
        name = properties.getProperty("name");
        version = properties.getProperty("version");
    }

    // Load configuration file
    private void loadINI() throws IOException {
        // Call for file
        File iniFile = new File("config.ini");

        // Check if file exists
        if (iniFile.exists()) {
            // Set ini global
            ini = new Ini(iniFile);

            // Load variables into memory
            Ini.Section section = ini.get("Config");
            windowWidth = Integer.parseInt(
                    section.getOrDefault("gui.window_width", String.valueOf(default_windowWidth))
            );
            windowHeight = Integer.parseInt(
                    section.getOrDefault("gui.window_height", String.valueOf(default_windowHeight))
            );
            boardCount = Integer.parseInt(
                    section.getOrDefault("rb.boards_count", String.valueOf(default_boardCount))
            );
            lineCount = Integer.parseInt(
                    section.getOrDefault("rb.lines_per_board", String.valueOf(default_lineCount))
            );
            maxLength = Integer.parseInt(
                    section.getOrDefault("rb.max_length", String.valueOf(default_maxLength))
            );
        }

        // If not, build one
        else {
            buildINI(iniFile);
        }
    }

    // Build initial INI file
    private void buildINI(File file) throws IOException {
        // Temporary ini for test runs
        if(testRun) {
            file = File.createTempFile("rb_", ".ini", new File("."));
            file.deleteOnExit();
        }

        if (    (testRun && file.exists()) ||
                file.createNewFile()) {
            // Set default values
            windowWidth = default_windowWidth;
            windowHeight = default_windowHeight;
            boardCount = default_boardCount;
            lineCount = default_lineCount;
            maxLength = default_maxLength;

            // Write ini file contents
            ini = new Ini(file);
            ini.put("Config", "gui.window_width", default_windowWidth);
            ini.put("Config", "gui.window_height", default_windowHeight);
            ini.put("Config", "rb.boards_count", default_boardCount);
            ini.put("Config", "rb.lines_per_board", default_lineCount);
            ini.put("Config", "rb.max_length", default_maxLength);
            ini.store();
        } else {
            throw new IOException("Unable to write config file");
        }
    }

    private void startGUI() throws IOException, ClassNotFoundException {
        SaveState saveState = new SaveState().load();
        new UserInterface(saveState);
    }
}

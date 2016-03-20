package com.walgreens.readerboard;

import org.ini4j.Ini;

import javax.swing.*;
import java.io.*;
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
    private Ini ini;
    private int boardCount;
    static int lineCount, maxLength;
    static String name, version;

    private final int
            default_boardCount = 4;
    private final int default_lineCount = 4;
    private final int default_maxLength = 16;

    // Initialize class
    public static void main(String[] args) {
        new Main();
    }

    private Main() {
        // Set look and feel of swing windows
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );
        }
        catch ( ClassNotFoundException |
                InstantiationException |
                IllegalAccessException |
                UnsupportedLookAndFeelException f) {
            // Ignore
        }

        // Load data and start GUI
        try {
            getProperties();
            loadINI();
            loadSaveState();
            startGUI();
        }
        catch (IOException|ClassNotFoundException e) {
            new CrashHandler(e);
        }
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
        if(iniFile.exists()) {
            // Set ini global
            ini = new Ini(iniFile);

            // Load variables into memory
            Ini.Section section = ini.get("Config");
            boardCount = Integer.parseInt(
                    section.getOrDefault("rb.boards_count",String.valueOf(default_boardCount))
            );
            lineCount = Integer.parseInt(
                    section.getOrDefault("rb.lines_per_board",String.valueOf(default_lineCount))
            );
            maxLength = Integer.parseInt(
                    section.getOrDefault("rb.max_length",String.valueOf(default_maxLength))
            );
        }

        // If not, build one
        else {
            buildINI(iniFile);
        }
    }

    // Build initial INI file
    private void buildINI(File file) throws IOException {
        if(file.createNewFile()) {
            // Set default values
            boardCount = default_boardCount;
            lineCount = default_lineCount;
            maxLength = default_maxLength;

            // Write ini file contents
            ini = new Ini(file);
            ini.put("Config","rb.boards_count",default_boardCount);
            ini.put("Config","rb.lines_per_board",default_lineCount);
            ini.put("Config","rb.max_length",default_maxLength);
            ini.store();
        }
        else {
            throw new IOException("Unable to write config file");
        }
    }

    private void loadSaveState() throws IOException, ClassNotFoundException {
        SaveState savestate = new SaveState().load(boardCount);
    }

    private void startGUI() {
        new UserInterface();
    }
}

package com.walgreens.readerboard;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Readerboard
 * com.walgreens.readerboard
 * Copyright 2016 Adam Treadway
 * Remarks:
 *
 * @author Adam Treadway
 * @since 3/1/2016
 */
class GuiBoard extends JPanel implements ActionListener {
    private Board board;
    private SaveState saveState;
    private final List<GuiBoardLine> lines = new ArrayList<>();
    final static List<Character> filterList = new ArrayList<>();
    static final Color board_default = new Color(225,235,235);
    private JLabel name;

    GuiBoard(Board board, SaveState saveState) {
        super();

        // Set the board
        this.board = board;

        // Set the saveState
        this.saveState = saveState;

        setFocusable(true);
        setFocusCycleRoot(true);
        setLayout(new BorderLayout());

        // Allow focus loss on fields
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                grabFocus();
            }

            @Override public void mousePressed(MouseEvent e) {}
            @Override public void mouseReleased(MouseEvent e) {}
            @Override public void mouseEntered(MouseEvent e) {}
            @Override public void mouseExited(MouseEvent e) {}
        });

        // Build filter list
        char[] filter = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4',
                '5', '6', '7', '8', '9', '0', '$', '.', ',', '\'',
                ':', '!', '/', '?', '&', '%','-',' '};
        for (char aFilter : filter) {
            filterList.add(aFilter);
        }

        buildGui();
    }

    private void buildGui() {
        add(buildToolbar(), BorderLayout.NORTH);
        add(createBoardGui(), BorderLayout.CENTER);
    }

    private JToolBar buildToolbar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setOpaque(false);
        Dimension dimension = new Dimension();
        dimension.height = 52;
        toolbar.setPreferredSize(dimension);
        toolbar.setBorder(new EmptyBorder(3,5,3,5));

        JPanel contents = new JPanel();
        contents.setLayout(new BorderLayout());
        toolbar.add(contents);

        JPanel nameMenu = new JPanel();
        nameMenu.setLayout(new BoxLayout(nameMenu, BoxLayout.X_AXIS));
        contents.add(nameMenu, BorderLayout.WEST);

        name = new JLabel(board.name);
        Font font = name.getFont();
        Font newFont = font.deriveFont(Font.BOLD, 18);
        name.setFont(newFont);
        name.setBorder(new EmptyBorder(0,0,0,6));
        nameMenu.add(name);

        ImageButton rename = new ImageButton(Color.BLACK,"rename.png",this,"rename");
        rename.setToolTipText("Rename Board");
        nameMenu.add(rename);

        ImageButton def = new ImageButton(Color.BLACK,"default.png",this,"default");
        def.setToolTipText("Set as default board");
        nameMenu.add(def);

        JPanel actions = new JPanel();
        contents.add(actions,BorderLayout.EAST);

        ImageButton save = new ImageButton(Color.BLACK,"save.png",this,"save");
        save.setToolTipText("Save messages");
        actions.add(save);

        ImageButton clear = new ImageButton(Color.BLACK,"clear.png",this,"clear");
        clear.setToolTipText("Clear board");
        actions.add(clear);

        return toolbar;
    }

    private JPanel createBoardGui() {
        JPanel boardPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel decor = new JPanel();
        decor.setLayout(new BoxLayout(decor, BoxLayout.Y_AXIS));
        decor.setBackground(board_default);
        decor.setOpaque(true);
        Color highlight = new Color(196,206,207);
        Color shadow = new Color(176,186,187);
        decor.setBorder(
                new CompoundBorder(
                    new CompoundBorder(
                        BorderFactory.createMatteBorder(1,1,0,0,highlight),
                        BorderFactory.createMatteBorder(0,0,2,2,shadow)
                    ), new EmptyBorder(10,10,10,10)
                )
        );

        // If messages is null, build some default
        if(board.messages.size() == 0) {
            for(int i = 0; i < Main.lineCount; i++) {
                String message = "Default Message";
                board.messages.add(message.toCharArray());
            }
        }

        for (int i = 0; i < board.messages.size(); i++) {
            GuiBoardLine line = new GuiBoardLine(new String(board.messages.get(i)));
            line.addKeyListener(new KeyEventListener(line));
            line.addMouseListener(new MouseEventListener(line));
            line.addFocusListener(new FocusEventListener(line));
            decor.add(line);
            lines.add(line);
        }

        boardPanel.add(decor, gbc);
        return boardPanel;
    }

    void save() {
        try {
            ArrayList<char[]> messages = board.messages;
            messages.clear();
            for (GuiBoardLine line : lines) {
                char[] characters = line.get();
                messages.add(characters);
            }
            saveState.save();
        }
        catch (IOException e) {
            new CrashHandler(e);
        }
    }

    private void clear() {
        lines.forEach(GuiBoardLine::clear);
    }

    private void setDefault() {
        try {
            saveState.setDefault(board);
        }
        catch (IOException e) {
            new CrashHandler(e);
        }
    }

    private void rename() {
        try {
            String newName = JOptionPane.showInputDialog(this, "Input a new name for this board:");
            if(newName != null && newName.trim().length() > 0) {
                board.name = newName;
                name.setText(board.name);
                name.revalidate();
                saveState.save();
            }
        }
        catch (IOException e) {
            new CrashHandler(e);
        }
    }

    boolean isSaved() {
        boolean isSaved = true;
        ArrayList<char[]> messages = board.messages;
        for (int i = 0; i < messages.size(); i++) {
            char[] characters = lines.get(i).get();
            if(!Arrays.equals(characters,messages.get(i))) {
                isSaved = false;
            }
        }
        return isSaved;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()) {
            case "save":
                save();
                break;
            case "clear":
                clear();
                break;
            case "default":
                setDefault();
                break;
            case "rename":
                rename();
                break;
        }
    }

    private class KeyEventListener implements KeyListener {
        final GuiBoardLine panel;
        KeyEventListener(GuiBoardLine panel) {
            this.panel = panel;
        }

        @Override
        public void keyReleased(KeyEvent e) {
            char character = Character.toLowerCase(e.getKeyChar());
            if (filterList.contains(character)) {
                if(!panel.addChar(character)) {
                    Debug.log("Character not added, limit exceeded");
                }
            }

            // Backspace key
            else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                if(!panel.delChar()) {
                    Debug.log("Character deletion error");
                }
            }

            // Delete key
            else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                panel.clear();
            }

            // Enter key
            else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                panel.transferFocus();
            }

            else {
                Debug.log("Invalid character: " + e.getKeyChar());
            }
        }

        @Override public void keyTyped(KeyEvent e) {}
        @Override public void keyPressed(KeyEvent e) {}
    }

    private class MouseEventListener implements MouseListener {
        final JPanel panel;
        MouseEventListener(JPanel panel) {
            this.panel = panel;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            panel.grabFocus();
        }

        @Override public void mouseEntered(MouseEvent e) {
            if(!panel.hasFocus()) {
                Color focus = new Color((board_default.getRed() + 20),
                        (board_default.getGreen() + 20),
                        (board_default.getBlue() + 20)
                );
                panel.setBackground(focus);
            }
        }
        @Override public void mouseExited(MouseEvent e) {
            if(!panel.hasFocus()) {
                panel.setBackground(board_default);
            }
        }

        @Override public void mousePressed(MouseEvent e) {}
        @Override public void mouseReleased(MouseEvent e) {}
    }

    private class FocusEventListener implements FocusListener {
        final JPanel panel;
        FocusEventListener(JPanel panel) {
            this.panel = panel;
        }

        @Override
        public void focusLost(FocusEvent e) {
            panel.setBackground(board_default);
        }

        @Override public void focusGained(FocusEvent e) {
            Color focus = new Color((board_default.getRed() + 20),
                    (board_default.getGreen() + 20),
                    (board_default.getBlue() + 10)
            );
            panel.setBackground(focus);
        }
    }
}

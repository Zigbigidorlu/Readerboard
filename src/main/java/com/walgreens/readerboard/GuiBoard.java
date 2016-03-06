package com.walgreens.readerboard;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
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
public class GuiBoard extends JPanel {
    final private List<Character> filterList = new ArrayList<>();
    private char[] filter = {   'a','b','c','d','e','f','g','h','i','j',
                                'k','l','m','n','o','p','q','r','s','t',
                                'u','v','w','x','y','z','1','2','3','4',
                                '5','6','7','8','9','0','$','.',',','\'',
                                ':','!','/','?','&',' '};
    private Color board_default     = new Color(194,204,203);

    GuiBoard() {
        super();
        setFocusable(true);
        setFocusCycleRoot(true);

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
        for (char aFilter : filter) {
            filterList.add(aFilter);
        }

        for(int i = 0; i < Main.lineCount; i++) {
            GuiBoardLine line = new GuiBoardLine("Foo 123! Bingo.");
            line.addKeyListener(new KeyEventListener(line));
            line.addMouseListener(new MouseEventListener(line));
            line.addFocusListener(new FocusEventListener(line));
            add(line);
        }
    }

    class KeyEventListener implements KeyListener {
        GuiBoardLine panel;
        KeyEventListener(GuiBoardLine panel) {
            this.panel = panel;
        }

        @Override
        public void keyReleased(KeyEvent e) {
            char character = Character.toLowerCase(e.getKeyChar());
            if (filterList.contains(character)) {
                if(panel.addChar(character)) {
                    System.out.println("Added Successfully");
                }
                else {
                    System.err.println("Failed to add");
                }
            }

            // Backspace key
            else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                if(panel.delChar()) {
                    System.out.println("Removed Successfully");
                }
                else {
                    System.err.println("Failed to remove");
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
                System.err.println("Bad Char: " + e.getKeyChar());
            }
        }

        @Override public void keyTyped(KeyEvent e) {}
        @Override public void keyPressed(KeyEvent e) {}
    }

    class MouseEventListener implements MouseListener {
        JPanel panel;
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

    class FocusEventListener implements FocusListener {
        JPanel panel;
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
                    (board_default.getBlue() + 20)
            );
            panel.setBackground(focus);
        }
    }
}

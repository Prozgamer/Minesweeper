package com.Bohdan;

import javax.swing.*;
import java.awt.*;

public class Cell extends JButton {
    private int value;
    private boolean is_mine;
    private boolean is_revealed;
    private boolean is_marked;
    private Level parent;

    private static final int icon_size = 50;

    private static final ImageIcon icon_mine = load_scaled_icon("/Mine.png", icon_size);
    private static final ImageIcon icon_flag = load_scaled_icon("/Flag.jpg", icon_size);
    private static final ImageIcon icon_hidden = load_scaled_icon("/Cell.jpg", icon_size);
    private static final ImageIcon icon_empty = load_scaled_icon("/Empty.jpg", icon_size);

    public Cell(Level parent, int value, boolean is_mine) {
        this.parent = parent;
        this.value = value;
        this.is_mine = is_mine;
        this.is_revealed = false;
        this.is_marked = false;

        setPreferredSize(new Dimension(icon_size, icon_size));
        setIcon(icon_hidden);
        setBackground(Color.lightGray);
        setFont(new Font("Arial", Font.BOLD, 20));
        setHorizontalTextPosition(JButton.CENTER);
        setVerticalTextPosition(JButton.CENTER);

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    reveal();
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    toggleFlag();
                }
            }
        });
    }

    public static ImageIcon load_scaled_icon(String path, int size) {
        java.net.URL resource = Cell.class.getResource(path);
        if (resource == null) {
            System.err.println("Не знайдено ресурс: " + path);
            return new ImageIcon(); // Повертаємо пусту іконку
        }
        Image img = new ImageIcon(resource).getImage();
        Image scaled = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private void reveal() {
        if (is_revealed || is_marked) return;
        is_revealed = true;

        if (is_mine) {
            setIcon(icon_mine);
            parent.game_over();
        } else {
            if (value > 0) {
                setText(String.valueOf(value));
                setIcon(icon_empty);
                int red = Math.min(255, value * 45);
                int green = Math.max(0, 255 - value * 65);
                int blue = 0;

                setForeground(new Color(red, green, blue));
            } else {
                setIcon(icon_empty);
            }
        }
        parent.increase_revealed();
        parent.game_won();
    }

    private void toggleFlag() {
        if (is_revealed) return;
        is_marked = !is_marked;
        setIcon(is_marked ? icon_flag : icon_hidden);
    }
}


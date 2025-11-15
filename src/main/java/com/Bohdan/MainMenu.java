package com.Bohdan;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {
    private int unlocked_level;
    private ProgressManager main_progress_maneger;

    public MainMenu(int Lvl, ProgressManager menu_progress_manager) {
        this.unlocked_level = Lvl;
        this.main_progress_maneger = menu_progress_manager;
        setTitle("Minesweeper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Створюємо модель та кнопки
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton new_game_button = new JButton("Нова гра");
        JButton load_game_button = new JButton("Продовжити");
        JButton exit_button = new JButton("Вихід");

        panel.add(new_game_button);
        panel.add(load_game_button);
        panel.add(exit_button);

        add(panel);
        setVisible(true);

        // Обробник кнопок
        new_game_button.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                new Level(1, 6, 6, 200, main_progress_maneger);
            });
            dispose();
        });

        load_game_button.addActionListener(e -> {
            JDialog continue_dialog = new JDialog(this, "Продовжити гру", true);
            continue_dialog.setLayout(new GridLayout(4, 1, 10, 10));
            continue_dialog.setResizable(false);

            JButton lvl1_button = new JButton("Рівень 1");
            lvl1_button.setEnabled(unlocked_level >= 1);
            lvl1_button.addActionListener(ev -> {
                SwingUtilities.invokeLater(() -> {
                    new Level(1, 6, 6, 200, main_progress_maneger);
                });
                dispose();
            });

            JButton lvl2_button = new JButton("Рівень 2");
            lvl2_button.setEnabled(unlocked_level >= 2);
            lvl2_button.addActionListener(ev -> {
                SwingUtilities.invokeLater(() -> {
                    new Level(2, 7, 7, 300, main_progress_maneger);
                });
                dispose();
            });

            JButton lvl3_button = new JButton("Рівень 3");
            lvl3_button.setEnabled(unlocked_level >= 3);
            lvl3_button.addActionListener(ev -> {
                SwingUtilities.invokeLater(() -> {
                    new Level(3, 8, 8, 400, main_progress_maneger);
                });
                dispose();
            });

            JButton own_lvl_button = new JButton("Свій рівень");
            own_lvl_button.setEnabled(unlocked_level >= 4);
            own_lvl_button.addActionListener(ev -> {
                SwingUtilities.invokeLater(() -> {
                    JTextField width_field = new JTextField(5);
                    JTextField height_field = new JTextField(5);
                    JTextField time_field = new JTextField(5);

                    JPanel own_panel = new JPanel(new GridLayout(3, 2, 5, 5));
                    own_panel.add(new JLabel("Ширина:"));
                    own_panel.add(width_field);
                    own_panel.add(new JLabel("Висота:"));
                    own_panel.add(height_field);
                    own_panel.add(new JLabel("Час:"));
                    own_panel.add(time_field);

                    int result = JOptionPane.showConfirmDialog(
                            this, own_panel, "Свій рівень",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
                    );
                    if (result == JOptionPane.OK_OPTION) {
                        try {
                            int rows = Integer.parseInt(width_field.getText());
                            int columns = Integer.parseInt(height_field.getText());
                            int time = Integer.parseInt(time_field.getText());

                            new Level(4, rows, columns, time, main_progress_maneger);
                            dispose();
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "Введіть коректні числа!");
                        }
                    }
                });
                dispose();
            });

            continue_dialog.add(lvl1_button);
            continue_dialog.add(lvl2_button);
            continue_dialog.add(lvl3_button);
            continue_dialog.add(own_lvl_button);
            continue_dialog.pack();
            continue_dialog.setLocationRelativeTo(this);
            continue_dialog.setVisible(true);
        });

        exit_button.addActionListener(e -> System.exit(0));
    }
}
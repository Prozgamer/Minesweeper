package com.Bohdan;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Level extends JFrame {
    //Поля
    private int num;
    private int columns;
    private int rows;
    private int[][] field;
    private int[][] ready_field;
    private boolean game_over = false;
    private int time_left = 300; // секунд за умовчуванням
    private Timer timer;
    private JLabel timer_label;
    private int mines = 0;
    private int num_of_revealed_cells = 0;
    private JLabel num_mines;
    private SoundPlayer bg_music;
    private SoundPlayer explosion;
    private SoundPlayer victory;
    private JPanel grid_panel;
    private boolean is_paused = false;
    private static final int ICON_SIZE = 50;
    private static final ImageIcon ICON_Pause = Cell.load_scaled_icon("/Pause.jpg", ICON_SIZE);
    private ProgressManager main_progress_manager;
    private int unlocked_level;

    public Level(int num,int columns, int rows, int timeLim, ProgressManager lvl_progress_manager) {
        this.num = num;
        this.columns = columns;
        this.rows = rows;
        this.time_left = timeLim;
        this.main_progress_manager = lvl_progress_manager;
        this.unlocked_level = main_progress_manager.load();

        setTitle("Minesweeper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        //Кнопка паузи
        JButton pause_button = new JButton();
        pause_button.setIcon(ICON_Pause);
        pause_button.addActionListener(e -> {
            game_pause();
        });
        //Звуки
        JSlider sound_slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        sound_slider.setMajorTickSpacing(25);
        sound_slider.setPaintTicks(true);
        sound_slider.setPaintLabels(true);
        JPanel sound_panel = new JPanel(new BorderLayout());
        sound_panel.add(new JLabel("Гучність"), BorderLayout.WEST);
        sound_panel.add(sound_slider, BorderLayout.CENTER);
        add(sound_panel, BorderLayout.SOUTH);
        sound_panel.add(sound_slider);
        add(sound_panel, BorderLayout.SOUTH);
        bg_music = new SoundPlayer("/saundtrack.wav");
        explosion = new SoundPlayer("/explosion.wav");
        victory = new SoundPlayer("/victory.wav");
        sound_slider.addChangeListener(e -> {
            int value = sound_slider.getValue();//Обробляємо повзунок
            float volume = value / 100f;
            bg_music.setVolume(volume);
            explosion.setVolume(volume * 0.7f);
            victory.setVolume(volume * 0.7f);
        });
        bg_music.play(true);

        //Генератор рівнів
        field = new int[rows][columns];
        ready_field = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (Math.random() < 0.7) {
                    field[i][j] = 0;
                } else {
                    field[i][j] = 1;
                    mines += 1;
                }
            }
        }
        //Перетворення в звичну систему, обрахування скільки мін навколо
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (field[i][j] != 1) {
                    int left_x = j - 1;
                    int right_x = j + 1;
                    int high_y = i - 1;
                    int low_y = i + 1;
                    if (j == 0) {
                        left_x = j;
                    }
                    if (j == columns - 1) {
                        right_x = j;
                    }
                    if (i == 0) {
                        high_y = i;
                    }
                    if (i == rows - 1) {
                        low_y = i;
                    }
                    for (int y = high_y; y <= low_y; y++) {
                        for (int x = left_x; x <= right_x; x++) {
                            if (field[y][x] == 1) {
                                ready_field[i][j] += 1;
                            }
                        }
                    }
                    System.out.print(ready_field[i][j] + " ");
                } else {
                    System.out.print("* ");
                    continue;
                }
            }
            System.out.println();
        }
        //Кількість мін та скільки клітинок розкрито + додавання таймеру до панелі + кнопка паузи
        JPanel top_panel = new JPanel(new BorderLayout());
        num_mines = new JLabel("<html>Мін: " + mines + "<br><font color='gray'>Розкрито: " + num_of_revealed_cells + "</font></html>");
        num_mines.setHorizontalAlignment(SwingConstants.CENTER); // вирівнюємо по центру
        num_mines.setFont(new Font("Arial", Font.BOLD, 14));
        top_panel.add(num_mines);

        timer_label = new JLabel("Час: " + time_left);
        timer_label.setHorizontalAlignment(SwingConstants.RIGHT);
        top_panel.add(timer_label, BorderLayout.EAST);

        top_panel.add(pause_button, BorderLayout.WEST);

        add(top_panel, BorderLayout.NORTH);

        //Обробка таймеру
        timer = new Timer(1000, (ActionEvent e) -> {
            if (!game_over) {
                time_left--;
                timer_label.setText("Час " + time_left / 60 + ":" + time_left % 60);
                if (time_left <= 0) {
                    game_over = true;
                    timer.stop();
                    JOptionPane.showMessageDialog(this, "Час вийшов!");
                    dispose();
                }
            }
        });
        timer.start();
        setVisible(true);

        //Побудова поля
        grid_panel = new JPanel(new GridLayout(rows, columns));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                boolean is_mine = field[i][j] == 1;
                int value = ready_field[i][j];
                Cell cell = new Cell(this, value, is_mine);
                grid_panel.add(cell);
            }
        }
        JPanel center_panel = new JPanel(new GridBagLayout()); // центруємо
        center_panel.add(grid_panel);
        add(center_panel, BorderLayout.CENTER);
    }

    public void increase_revealed() {
        if (!game_over) {
            num_of_revealed_cells += 1;
            num_mines.setText("<html>Мін: " + mines + "<br><font color='gray'>Розкрито: " + num_of_revealed_cells + "</font></html>");
        }
    }

    public void game_pause() {
        if (game_over) return;

        if (!is_paused) {
            is_paused = true;
            timer.stop();
            if (bg_music != null) bg_music.stop();

            if (grid_panel != null) {
                for (Component c : grid_panel.getComponents()) {
                    c.setEnabled(false);
                }
            }

            JDialog pause_dialog = new JDialog(this, "Пауза", true);
            pause_dialog.setLayout(new GridLayout(2, 1, 10, 10));
            pause_dialog.setResizable(false);

            JButton continue_button = new JButton("Продовжити");
            JButton exit_button = new JButton("Вихід");

            pause_dialog.add(continue_button);
            pause_dialog.add(exit_button);

            continue_button.addActionListener(ev -> {
                is_paused = false;
                timer.start();
                if (bg_music != null) bg_music.play(true);

                if (grid_panel != null) {
                    for (Component c : grid_panel.getComponents()) {
                        c.setEnabled(true);
                    }
                }

                pause_dialog.dispose();
            });

            exit_button.addActionListener(ev -> System.exit(0));

            pause_dialog.pack();
            pause_dialog.setLocationRelativeTo(this);
            pause_dialog.setVisible(true);
        }
    }

    public void game_over() {
        if (!game_over) {
            game_over = true;
            timer.stop();
            bg_music.stop(); //Зупинити музику
            explosion.play(false);//Звук вибуху
            JOptionPane.showMessageDialog(this, "Вибух!");
            dispose();
            SwingUtilities.invokeLater(() -> {
                new MainMenu(unlocked_level, main_progress_manager); // відкриває головне меню
            });
        }
    }

    public void game_won() {
        if (!game_over) {
            if (num_of_revealed_cells == rows * columns - mines) {
                timer.stop();
                bg_music.stop(); //Зупинити музику
                victory.play(false); //Звук вигращу
                JOptionPane.showMessageDialog(this, "Ви виграли!");
                main_progress_manager.save(num + 1);
                dispose();
                SwingUtilities.invokeLater(() -> {
                    new MainMenu(main_progress_manager.load(), main_progress_manager); // відкриває головне меню
                });
            }
        }
    }
}
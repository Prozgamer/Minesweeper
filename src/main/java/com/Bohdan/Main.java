package com.Bohdan;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProgressManager main_progress_manager = new ProgressManager();
            int unlockedLevel = main_progress_manager.load();
            new MainMenu(unlockedLevel, main_progress_manager);
        });
    }
}



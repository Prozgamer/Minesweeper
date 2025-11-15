package com.Bohdan;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Scanner;

public class ProgressManager {
    File file = new File("Progress.txt");

    public ProgressManager(){
        try {
            if (file.createNewFile()) {
                ;
            } else {
                ;
            }
        } catch (IOException e) {
            System.err.println("Помилка при створенні файла: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void save(int num){
        int current = load();
        int progress = Math.max(num, current);
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println(progress);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int load() {
        try (Scanner sc = new Scanner(file)) {
            if (sc.hasNextInt()) {
                return sc.nextInt();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }
}


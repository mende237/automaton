package com.automate.inputOutput;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Scheduler{
    
    public static void DOWN_SEM_REQUEST() throws IOException{
        Configuration config = Configuration.getConfiguration();
        int S = 0;
        do {
            File file = new File(config.getPathSemRequest());
            Scanner reader = new Scanner(file);
            S = Integer.parseInt(reader.nextLine());
            S -= 1;
            FileWriter fw = new FileWriter(file);
            fw.write(String.valueOf(S));
            fw.close();
            reader.close();
        } while (S < 0);
    }

    public static void UP_SEM_REQUEST() throws IOException {
        Configuration config = Configuration.getConfiguration();
        File file = new File(config.getPathSemRequest());
        FileWriter fw = new FileWriter(file);
        fw.write('1');
        fw.close();
    }

    public static void DOWNS_SEM_RESPONSE() throws IOException {
        Configuration config = Configuration.getConfiguration();
        int S = 0;
        do {
            File file = new File(config.getPathSemResponse());
            Scanner reader = new Scanner(file);
            S = Integer.parseInt(reader.nextLine());
            S -= 1;
            FileWriter fw = new FileWriter(file);
            fw.write(String.valueOf(S));
            fw.close();
            reader.close();
        } while (S < 0);

    }

    public static void UP_SEM_RESPONSE() throws IOException {
        Configuration config = Configuration.getConfiguration();
        File file = new File(config.getPathSemResponse());
        FileWriter fw = new FileWriter(file);
        fw.write('1');
        fw.close();
    }


}

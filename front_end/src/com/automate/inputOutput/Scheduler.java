package com.automate.inputOutput;

public class Scheduler {
    private static int S = 1;

    public static void DOWN() {

        while (Scheduler.S <= 0) {
            Scheduler.S -= 1;
        }
    }

    public static void UP() {
        Scheduler.S = 1;
    }

}

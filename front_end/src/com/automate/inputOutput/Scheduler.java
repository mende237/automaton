package com.automate.inputOutput;

public class Scheduler{
    private static int S1 = 1;//controle l'envoie et la reception des messages
    private static int S2 = 1;//controle l'affichage


    public static void DOWNS1() {
        while (Scheduler.S1 - 1 < 0) {
            System.out.println("bloquerrrrrr!!!!!!!!!!!!!");
        } 
        Scheduler.S1 -= 1;
    }

    public static void UPS1() {
        Scheduler.S1 = 1;
    }

    public static void DOWNS2() {
        while (Scheduler.S2 - 1 < 0) {

        }
        Scheduler.S2 -= 1;
    }

    public static void UPS2() {
        Scheduler.S2 = 1;
    }


}

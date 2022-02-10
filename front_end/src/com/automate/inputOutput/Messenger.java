package com.automate.inputOutput;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.json.JSONObject;

public class Messenger implements Runnable {
    private static int S1 = 1;
    private static int S2 = 1;
    private String receptionPath;
    private String sendingPath;
    private long delay = 300000;
    private long begin;
    private boolean response;

    private void DOWNS1() {
        while (Messenger.S1 - 1 < 0) {

        }
        Messenger.S1 -= 1;
    }

    private void UPS1() {
        Messenger.S1 = 1;
    }


    private void DOWNS2() {
        while (Messenger.S1 - 1 < 0) {

        }
        Messenger.S2 -= 1;
    }

    private void UPS2() {
        Messenger.S2 = 1;
    }

    /************* methods **********************/
    public boolean sendInstruction(Instruction instruction, String sendingPath, String receptionPath, long delay)
            throws FileNotFoundException {
        DOWNS1();
        this.sendingPath = sendingPath;
        this.receptionPath = receptionPath;
        this.delay = delay;

        JSONObject obj = new JSONObject();
        obj.put("id", instruction.getID());
        obj.put("name", instruction.getName());
        obj.put("data path", instruction.getDataPath());
        obj.put("reception path", this.receptionPath);

        StringWriter strW = new StringWriter();
        obj.write(strW);
        String jsonText = strW.toString();
        PrintWriter writer = new PrintWriter(this.sendingPath);
        writer.println(jsonText);
        writer.close();
        this.begin = System.currentTimeMillis();
        run();
        return true;

        // faire un up lorsqu'on recoit la reponse a l'instruction envoyé
    }

    @Override
    public void run() {
        boolean rep = false;

        while (rep == false && System.currentTimeMillis() - this.begin <= this.delay) {
            
        }

        if (rep == true) {
            this.response = true;
            UPS1();
        } else {
            this.response = false;
        }
    }

    public static void main(String[] args) {
        System.out.println("enter!!!!!!!!!!!!!!!");
        Messenger m1 = new Messenger();
        m1.DOWNS1();
        System.out.println(Messenger.S1);
        m1.DOWNS1();
        System.out.println(Messenger.S1);
        m1.DOWNS1();
        System.out.println(Messenger.S1);
        System.out.println("enter!!!!!!!!!!!!!!!!!!");
    }

}

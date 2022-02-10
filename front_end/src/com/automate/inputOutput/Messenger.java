package com.automate.inputOutput;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.json.JSONObject;

public class Messenger implements Runnable {
    private String receptionPath;
    private String sendingPath;
    private long delay = 300000;
    private long begin;
    private boolean response;



    /************* methods **********************/
    public boolean sendInstruction(Instruction instruction, String sendingPath, String receptionPath, long delay)
            throws FileNotFoundException {
        Scheduler.DOWNS1();
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
            Scheduler.UPS1();
        } else {
            this.response = false;
        }
    }

    public static void main(String[] args) {
        System.out.println("enter!!!!!!!!!!!!!!!");
        Scheduler.DOWNS1();
        System.out.println("pass1");
        Scheduler.DOWNS1();
        System.out.println("pass2");
        Scheduler.DOWNS1();
        System.out.println("pass3");
        System.out.println("enter!!!!!!!!!!!!!!!!!!");
    }

}

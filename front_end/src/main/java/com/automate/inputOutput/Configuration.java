package com.automate.inputOutput;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.json.JSONObject;

public class Configuration {
    public static Configuration configuration = null;

    private static String requestPath;
    private static String responsePath;
    private static String dataRequestPath;
    private static String dataResponsePath;
    private static String imagePath;
    private static String pathSemRequest;
    private static String pathSemResponse;

    
   
    private Configuration() {

    }

    public static Configuration getConfiguration(String path) throws FileNotFoundException {
        if (Configuration.configuration == null) {
            Configuration.configuration = new Configuration();
            Scanner reader = new Scanner(new File(path)).useDelimiter("\\Z");
            String content = reader.next();
            reader.close();
            JSONObject obj = new JSONObject(content);
            Configuration.requestPath = obj.getString("request path");
            Configuration.responsePath = obj.getString("response path");
            Configuration.dataRequestPath = obj.getString("data request path");
            Configuration.dataResponsePath = obj.getString("data reponse path");
            Configuration.pathSemRequest = obj.getString("path semaphore request");
            Configuration.pathSemResponse = obj.getString("path semaphore response");
            Configuration.imagePath = obj.getString("image path");
        }

        return Configuration.configuration;
    }

    public static Configuration getConfiguration() {
        return Configuration.configuration;
    }

    public String getPathSemRequest() {
        return Configuration.pathSemRequest;
    }

    public void setPathSemRequest(String pathSemRequest) {
        Configuration.pathSemRequest = pathSemRequest;
    }

    public String getPathSemResponse() {
        return Configuration.pathSemResponse;
    }

    public void setPathSemResponse(String pathSemResponse) {
        Configuration.pathSemResponse = pathSemResponse;
    }

    public String getImagePath() {
        return Configuration.imagePath;
    }

    public void setimagePath(String imagePath) {
        Configuration.imagePath = imagePath;
    }


    public String getRequestPath() {
        return Configuration.requestPath;
    }

    public void setRequestPath(String requestPath) {
        Configuration.requestPath = requestPath;
    }

    public String getResponsePath() {
        return Configuration.responsePath;
    }

    public void setResponsePath(String responsePath) {
        Configuration.responsePath = responsePath;
    }

    public String getDataRequestPath() {
        return Configuration.dataRequestPath;
    }

    public void setDataRequestPath(String dataRequestPath) {
        Configuration.dataRequestPath = dataRequestPath;
    }

    public String getDataResponsePath() {
        return Configuration.dataResponsePath;
    }

    public void setDataResponsePath(String dataResponsePath) {
        Configuration.dataResponsePath = dataResponsePath;
    }

    @Override
    public String toString(){
        return "data request ->" + Configuration.dataRequestPath  + "\ndata response ->" 
        + Configuration.dataResponsePath +"\nrequest -> " + Configuration.requestPath+ "\nresponse -> " 
        + Configuration.responsePath +"\nimage data -> " + Configuration.imagePath + 
        "\npath semaphore request "+Configuration.pathSemRequest + "\npath semaphore reponse " + 
        Configuration.pathSemResponse +"\n";
    }
}

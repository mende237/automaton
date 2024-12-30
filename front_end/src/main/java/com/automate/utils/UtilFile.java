package com.automate.utils;

import java.io.File;

import com.automate.inputOutput.Configuration;
import com.automate.structure.AutomateType;

public class UtilFile {

    public static boolean isFileExist(AutomateType automateType, String fileName) {
        String folderToSearch = null;
        switch (automateType) {
            case AFD:
                folderToSearch = Configuration.getConfiguration().getAfdFolderName();
                break;
            case AFN:
                folderToSearch = Configuration.getConfiguration().getAfnFolderName();
                break;
            default:
                folderToSearch = Configuration.getConfiguration().getEp_afnFolderName();
                break;
        }
        System.out.println("configuration " + Configuration.getConfiguration().getSavePath() + "/" + folderToSearch + "/" + fileName + ".json");
        File file = new File(
                Configuration.getConfiguration().getSavePath() + "/" + folderToSearch + "/" + fileName + ".json");
        return file.exists();
    }
}

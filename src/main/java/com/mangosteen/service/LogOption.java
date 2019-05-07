package com.mangosteen.service;

import java.io.*;

/**
 * @author guochang.xie
 * @Description: TODO
 * @date 2019/5/64:33 PM
 */
public class LogOption {

    private String filePath;
    public LogOption(String filePath){
        this.filePath=filePath;
    }
    public  void writeLog(String log){

        BufferedWriter bwriter = null;
        try {
            File dir = new File(filePath);
            if(!dir.exists() && !dir.isDirectory()) {
                dir.mkdirs();
            }
            File logFile = new File(filePath + "/" + "build.log");
            if(!logFile.exists()) {
                logFile.createNewFile();
            }
            bwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile, true)));
            bwriter.write(log);
            bwriter.newLine();
            bwriter.flush();
        }catch (Exception exception){

        }
        finally{
            if(bwriter != null){
                try {
                    bwriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

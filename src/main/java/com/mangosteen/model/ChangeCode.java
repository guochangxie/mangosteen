package com.mangosteen.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author guochang.xie
 * @Description: TODO
 * @date 2019/2/253:29 PM
 */
public class ChangeCode {
    public String fileName;

    public List<String > methodNameList =new ArrayList<>();

    public List<String> changeLineList =new ArrayList<>();

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<String> getMethodNameList() {
        return methodNameList;
    }

    public void addMethodName(String methodName) {
        methodNameList.add(methodName);
    }

    public List<String> getChangeLineList() {
        return changeLineList;
    }

    public void addChangeLine(String changeLine) {
        changeLineList.add(changeLine);
    }
}

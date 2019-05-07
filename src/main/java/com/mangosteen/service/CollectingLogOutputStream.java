package com.mangosteen.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.LogOutputStream;

import java.util.LinkedList;
import java.util.List;

/**
 * @author guochang.xie
 * @Description: TODO
 * @date 2019/5/62:03 PM
 */
@Slf4j
public class CollectingLogOutputStream extends LogOutputStream {
    private final List<String> lines = new LinkedList<String>();
    private final boolean logToStandardOut;

    public CollectingLogOutputStream(boolean logToStandardOut) {
        this.logToStandardOut = logToStandardOut;
    }

    @Override
    protected void processLine(String line, int level) {
        if (logToStandardOut) {
            log.info(line);
        }
        lines.add(line);
    }

    public List<String> getLines() {
        return lines;
    }

    public String getLine(){
        StringBuffer stringBuffer=new StringBuffer();
        for(String line:lines){
            stringBuffer.append(line);
            stringBuffer.append("\r\n");
        }
        return stringBuffer.toString();
    }
}

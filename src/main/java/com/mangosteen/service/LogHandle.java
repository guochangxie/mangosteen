package com.mangosteen.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author guochang.xie
 * @Description: TODO
 * @date 2019/3/275:32 PM
 */
@ServerEndpoint("/log")
@Component
public class LogHandle {

    private Process process;
    private InputStream inputStream;

    /**
     * 新的WebSocket请求开启
     */
    @OnOpen
    public void onOpen(Session session) {
        try {
            // 执行tail -f命令
            process = Runtime.getRuntime().exec("tail -f /opt/mangosteenWorkSpace/logs/spring.log");
            inputStream = process.getInputStream();
            BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
            ExecutorService executorService = Executors.newFixedThreadPool(2);
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    String line;
                    try {
                        while((line = reader.readLine()) != null) {
                            // 将实时日志通过WebSocket发送给客户端，给每一行添加一个HTML换行
                            session.getBasicRemote().sendText(line + "<br>");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * WebSocket请求关闭
     */
    @OnClose
    public void onClose() {
        try {
            if(inputStream != null)
                inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(process != null)
            process.destroy();
    }

    @OnError
    public void onError(Throwable thr) {
        thr.printStackTrace();
    }
}

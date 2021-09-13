package edu.njnu.reproducibility.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: ZhangYiming
 * @Date: 2021/08/17/16:07
 * @Description:
 */
@Component
@ServerEndpoint("/notice")
public class NoticeWebSocket {

    private static Map<String, Session> clients = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("客户端连接：" + session.getId());
        clients.put(session.getId(), session);
    }

    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发生错误");
        error.printStackTrace();
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("断开连接" + session.getId());
        System.out.println(session);
    }
}

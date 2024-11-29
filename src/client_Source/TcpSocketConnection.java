package client_Source;

import java.io.*;
import java.net.Socket;

public class TcpSocketConnection {
    private static final int PORT = 8189;
    private Socket socket;
    private TCPSend client; // SenderViewModel 인스턴스
    

    
    public void startClient(String serverIP) {
        try {
            socket = new Socket(serverIP, PORT);
            client = new TCPSend(socket);
            
            //UDP Broad메시지를 수신하였지 체크하는 스레드 생성 
            UDPCheckThread udpCheckThread = new UDPCheckThread(this,client);
            Thread udpCheck = new Thread(udpCheckThread);
            udpCheck.start();
            
            System.out.println("Server: " + serverIP + " is connected by TCP");
            System.out.println("My IP: " + socket.getLocalAddress());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // UDPCheckThread에서 바로 TCPSend의 메소드르 호출하여 Ack를 전송하도록 수정하였음
    
    /*
    // TCP 에코 메시지를 전송하는 메서드
    public void sendAckMessage(String message) {
        if (client != null) {
            client.sendMessage_tcp(message); // Client_Tcp을 사용하여 메시지 전송
        } else {
        	System.out.println("TCPSender is null");
        }
    }
    //byte배열의 TCP check 메시지를 전송하는 메서드
    public void sendAckMessage(byte[] message) {
        if (client != null) {
            client.sendMessage_tcp(message); // Client_Tcp을 사용하여 메시지 전송
        } else {
        	System.out.println("TCPSender is null");
        }
    }
    
    //byte배열의 TCP check 메시지를 전송하는 메서드
    public void sendAckMessage_alltrue(boolean message) {
        if (client != null) {
            client.sendMessage_tcp_alltrue(message); // Client_Tcp을 사용하여 메시지 전송
        } else {
        	System.out.println("TCPSender is null");
        }
    }
    public void sendAckObject(byte[] byteArray) {
    	if (client != null) {
            client.sendAckObject(byteArray);
        } else {
            System.out.println("TCPSender is null");
        }
    }
    
    */
    
    // 소켓 종료 메서드 추가
    public void closeSocket() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("TCP 소켓이 닫혔습니다.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

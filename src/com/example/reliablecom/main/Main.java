package com.example.reliablecom.main;
import server_Source.*;
import client_Source.*;
import GUI.GUI;

import java.net.InetAddress;
import java.util.Timer;



import java.util.TimerTask;





public class Main {
	/*Server*/
	public static ClientManager clientManager = new ClientManager();
	public static int clients_tcp_index = 0; // 에코메시지의 배열의 인덱스
	
    private static UDPBroadcastSend sender_udp;
    private static TcpConnectionAccepter tcp_accepter;
    private static Timer udpTimer_IP; //SETUP 과정을 위한 UDP broadcast를 위한 타이머
    private static Thread senderThread;// UDP 전송을 위한 타이머
    private static Thread accepterThread;
    public static int SIZE = 61440;
    /*Client*/
    public static UDPReceive receiver_udp;
    private static TcpSocketConnection tcp_connection;
    
	private Main() {
		
	}
	
	public static String ComFindBroadcastIp() {
		InetAddress BroadcastAddress = BroadcastAddressFinder.getWiFiBroadcastAddress();
  		if (BroadcastAddress != null) {
  			return BroadcastAddress.getHostAddress();

  		}
  		return null;
	}
	
	public static void ComSetup () {
		//수정한 이유: 병렬스레드처리로 하지않으면 socket.accept하는 부분에서 멈추게 된다.
    	if (tcp_accepter == null) {
    		tcp_accepter = new TcpConnectionAccepter();
    		accepterThread = new Thread(tcp_accepter);
   		 	accepterThread.start();
            GUI.consoleArea.append("TCP Connection Accepter thread started.\n");
    	}
    	else {
    		GUI.consoleArea.append("TCP Connection Accepter thread already running.\n");
    	}
        
        String broadIP = GUI.inputIp_udpBroad.getText();
        
        
        //TCP 소켓을 열고, UDP Broad 전송, stopUDPsend 버튼 누르면 송신 중지 -> 추후 연결되면 멈추도록하는 매커니즘으로 변경
        
        UDPBeaconbroadcast sender_udp_ip = new UDPBeaconbroadcast();
        udpTimer_IP = new Timer();
        udpTimer_IP.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
            	sender_udp_ip.startSend(broadIP);
            }
        }, 0, 500); // 500ms 간격으로 실행

        
        GUI.consoleArea.append("Connection Setup Ready \n");
        
	}
	
	
	public static void StopSetup() {
		if (udpTimer_IP != null) {
            udpTimer_IP.cancel();  // 타이머 중지
            udpTimer_IP = null;    // 타이머 객체를 null로 설정하여 상태 초기화
            GUI.consoleArea.append("SETUP이 중지되었습니다.\n");
        }
	}

	public static void ComBroadcastSend () {
		//byte[] Data,int Size,int Option 추후 업데이트
        
        String serverIP = GUI.inputIp_udpBroad.getText();
        if(sender_udp == null && senderThread == null) {
        	sender_udp = new UDPBroadcastSend(serverIP);
            //sender_udp 스레드화
            senderThread = new Thread(sender_udp);
            senderThread.start();
        }
        else {
        	GUI.consoleArea.append("UDP 메시지 이미 송신 중..\n");
        }
        
        
	} 
	public static void StopBroadcastSend() {
		if (sender_udp != null) {
        	senderThread.interrupt();  // 타이머 중지
            senderThread = null;    // 타이머 객체를 null로 설정하여 상태 초기화
            sender_udp = null;
            GUI.consoleArea.append("UDP 메시지 전송이 중지되었습니다.\n");
        }
	}

	public static void ComReset() {
		//Client List에서 모든  Client 객체 삭제
    	//System.out.println("Reset Program completed 1");
    	if (tcp_accepter != null) {
    		tcp_accepter.closeTcpSocket();
            accepterThread.interrupt();
          //tcp_accepter null로 초기화 후 모든 메세지 창 초기화
            tcp_accepter = null;
    	}
    	
        //System.out.println("Reset Program completed 2");
        GUI.consoleArea.setText("Reset Program");
        GUI.receivedMessagesArea.setText("");
        GUI.sendMessageArea.setText("");
        //Client 초기화
        clientManager.AllClientsReset();
        clients_tcp_index = 0;
        System.out.println("TcpConnectionAccepter sets null");
        //Udp sender 초기화
        UDPBroadcastSend.resetCount();
        if(senderThread != null&& sender_udp != null) {
        	senderThread = null;
            sender_udp = null;
        }
        
	}
	
	/*   The methods are necessary by client */
	public static void ComSetupResponse () {
		tcp_connection = new TcpSocketConnection();
        receiver_udp = new UDPReceive();
        String serverIP = receiver_udp.startConnect_to_tcp();
        tcp_connection.startClient(serverIP);
        GUI.consoleArea.append("Client: "+serverIP+"가 TCP 소켓과 연결되었습니다. \n");
	}

	public static void ComReceive() {
		receiver_udp = new UDPReceive();
        new Thread(() -> receiver_udp.startServer()).start();
        GUI.consoleArea.append("UDP 수신 대기 중...\n");
        
	}

}

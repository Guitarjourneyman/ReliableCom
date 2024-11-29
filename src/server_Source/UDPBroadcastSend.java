package server_Source;

import GUI.GUI;
import main.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.swing.JTextArea;

public class UDPBroadcastSend implements Runnable  {
    private static final int PORT = 1996;
    private static final int PACKET_SIZE = 1024; // 단위 패킷 크기 (1KB)
    private String serverIP;
    private JTextArea sendMessageArea;
    private JTextArea consoleArea;
    //edit: Defined count as a static  
    private static int sentMessageCount = 0;       // 메시지의 번호 
    private static int sentMessageCount_actual = 0; //실제 전송 메시지 카운
    private int messageSize;
    private DatagramSocket socket = null;
    
    public UDPBroadcastSend(String serverIP) {
    	this.serverIP = serverIP;
    	this.sendMessageArea = GUI.sendMessageArea; 
    	this.consoleArea = GUI.consoleArea;
    	this.messageSize = Main.SIZE; // messageSize는 보내고자 하는 메시지의 크기(60KB로 설정)
    }
    /*
    public void startSend(String serverIP, int messageNum, int messageSize) { 
        DatagramSocket socket = null;

        try {
            socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName(serverIP);
            //System.out.println("UDP is connected.");

            // 지정된 크기의 연속된 "A" 문자 생성
            StringBuilder messageBuilder = new StringBuilder(messageSize);
            for (int i = 0; i < messageSize; i++) {
                messageBuilder.append('A');
            }
            String fullMessage = messageBuilder.toString(); // 전체 메시지 생성
            
            // 메시지를 바이트 배열로 변환
            byte[] messageBytes = fullMessage.getBytes();
            int offset = 0;
            int packetCount = 0; // 패킷 번호 카운터 추가

            // 메시지가 1024바이트를 초과할 경우, 1024바이트 단위로 분할하여 전송
            while (offset < messageBytes.length) { 
            	// 한 패킷의 길이 1024byte(-10은 헤더 길이)와 (메시지 전체 길이 - offset)의 최솟값
                int length = Math.min(PACKET_SIZE-10, messageBytes.length - offset); 
                byte[] buffer = new byte[length+10]; // 패킷 번호를 저장할 공간(헤더) 추가
                
                // 패킷 번호를 헤더에 삽입
                String packetHeader = messageNum + "_" + (packetCount + 1); // 1부터 시작하는 패킷 번호
                byte[] headerBytes = packetHeader.getBytes();
                System.arraycopy(headerBytes, 0, buffer, 0, headerBytes.length); // 헤더를 버퍼에 복사
                
                // 배열 복사(복사할 원본 배열, 원본 배열에서 복사를 시작할 인덱스, 복사할 대사 배열, 대상 배열에서 데이터를 복사할 시작 인덱스, 복사할 데이터 길이)
                System.arraycopy(messageBytes, offset, buffer, headerBytes.length, length); 
                
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, PORT);
                socket.send(packet);

                offset += length;
                packetCount++; // 패킷 번호 증가
                //System.out.println(packetCount + "번째 패킷 전송 완료"); // 패킷 전송 완료 메시지 출력
            }

            //System.out.println("메시지가 전송되었습니다.");

        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }*/

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		
        
        try {
			// 설정한 시간 동안 대기
			long interval = 50;

			while (true) {
				if (Main.clientManager.checkAllClientsNewMessage()) {

					consoleArea.append("모든 클라이언트로부터 " + "[" + sentMessageCount + "]의 에코 메시지를 받았으므로 브로드캐스트 중지\n");
					Main.clientManager.AllClientsSetFalse(); // 에코메시지 수신여부 초기화
					sentMessageCount++; // 전송 메시지 카운트 증가

				}
				if (sentMessageCount == 0)
					sentMessageCount++; // 첫 메시지 발송때만 카운트 증가

				startSend(); // 50ms마다 UDP 메시지 전송

				// sendMessageArea에 보내는 메시지 추가
				sentMessageCount_actual++;
				sendMessageArea.append("[" + sentMessageCount_actual + "][" + sentMessageCount
						+ "] message via UDP: 'A' * 61440 bytes\n");

				Thread.sleep(interval);
			}

            }catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            }

            //System.out.println("메시지가 전송되었습니다.");

          
    
		
	}
    
	
	
	public void startSend() throws Exception { // messageSize는 보내고자 하는 메시지의 크기(60KB로 설정)
        

        
            socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName(serverIP);
            //System.out.println("UDP is connected.");

            // 지정된 크기의 연속된 "A" 문자 생성
            StringBuilder messageBuilder = new StringBuilder(messageSize);
            for (int i = 0; i < messageSize; i++) {
                messageBuilder.append('A');
            }
            String fullMessage = messageBuilder.toString(); // 전체 메시지 생성
            
            // 메시지를 바이트 배열로 변환
            byte[] messageBytes = fullMessage.getBytes();
            int offset = 0;
            int packetCount = 0; // 패킷 번호 카운터 추가

            // 메시지가 1024바이트를 초과할 경우, 1024바이트 단위로 분할하여 전송
            while (offset < messageBytes.length) { 
            	// 한 패킷의 길이 1024byte(-10은 헤더 길이)와 (메시지 전체 길이 - offset)의 최솟값
                int length = Math.min(PACKET_SIZE-10, messageBytes.length - offset); 
                byte[] buffer = new byte[length+10]; // 패킷 번호를 저장할 공간(헤더) 추가
                
                // 패킷 번호를 헤더에 삽입
                String packetHeader = sentMessageCount + "_" + (packetCount + 1); // 1부터 시작하는 패킷 번호
                byte[] headerBytes = packetHeader.getBytes();
                System.arraycopy(headerBytes, 0, buffer, 0, headerBytes.length); // 헤더를 버퍼에 복사
                
                // 배열 복사(복사할 원본 배열, 원본 배열에서 복사를 시작할 인덱스, 복사할 대사 배열, 대상 배열에서 데이터를 복사할 시작 인덱스, 복사할 데이터 길이)
                System.arraycopy(messageBytes, offset, buffer, headerBytes.length, length); 
                
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, PORT);
                socket.send(packet);

                offset += length;
                packetCount++; // 패킷 번호 증가
                //System.out.println(packetCount + "번째 패킷 전송 완료"); // 패킷 전송 완료 메시지 출력
            }

            //System.out.println("메시지가 전송되었습니다.");

          
    }
	public static void resetCount () {
		sentMessageCount = 0;
		sentMessageCount_actual =0;
	}
    
}

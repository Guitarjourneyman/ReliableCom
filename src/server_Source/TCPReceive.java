package server_Source;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;

import javax.swing.JTextArea;

import main.Ack;

public class TCPReceive {
    /* 메시지를 받기만 하는 기능 구현 */
    private Socket socket;
    private final TcpConnectionAccepter.ClientHandler handler;
    
    private static final int TOTAL_PACKETS = 61;
    private JTextArea receivedMessagesArea;  // GUI의 receive message 창
    private volatile boolean newAckReceived_tcp = false; // 에코 메시지 수신 여부
    private int receive_message_num = 0;
    
    public int array_index =0;
    public int ignored_bits =0;
    
    private AckCheck startCheck;
    //public byte[] checkNewMessage;
    // 생성자에서 JTextArea 전달 받음
    
    public static int calculateBits(int total_packets, int mode) {
    	if (mode == 0) {
    		//mode가 0이면 byte 배열 인덱스 계산
    		return (total_packets+7)/8;
    	}else if(mode ==1) {
    		return 8-(total_packets % 8);
    	}else {
    		throw new IllegalArgumentException("Invalid mode: mode should be 0 or 1");
    	}
    }
    
    
    
    public TCPReceive(Socket socket,TcpConnectionAccepter.ClientHandler clientHandler ,JTextArea receivedMessagesArea) {
        this.socket = socket;
        handler = clientHandler;
        this.receivedMessagesArea = receivedMessagesArea;
        
        startCheck = new AckCheck(handler);
    }
    
    
    
    
    public void reset_message_num() {
        receive_message_num = 0;
    }
    
    public boolean hasNewEchoMessage() {
        return newAckReceived_tcp;
    }

    public void resetNewEchoMessageFlag() {
        newAckReceived_tcp = false;
    }
    //기존의 바이트 배열을 받는 메소드 
    /*
    public void startReceiving() throws IOException { //예외 throw하여 ClientHandler에서 처리하도록함
    	
    	StartTCPCheck startCheck = new StartTCPCheck(this, handler);
    	
    	
        BufferedReader in = null;
        PrintWriter out = null;
        // 데이터 수신을 위한 DataInputStream 사용
        DataInputStream dataInputStream = null;
        array_index = calculateBits(TOTAL_PACKETS,0);
        ignored_bits = calculateBits(TOTAL_PACKETS,1);
        checkNewMessage = new byte[array_index];
        try {
            // BufferedReader를 사용하여 데이터를 송수신
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            dataInputStream =new DataInputStream(socket.getInputStream());
            
            
            
            
            String clientIP = socket.getInetAddress().getHostAddress();
            System.out.println("Server_TCP is open");

            
            
            while (!socket.isClosed()) {//클라이언트와의 연결을 유지하면서 메시지를 지속적으로 수신
                // 수신된 메시지 처리
            	// byte 배열 수신
                int byteArrayLength = dataInputStream.readInt(); // 먼저 수신할 배열의 길이를 읽음
                byte[] receivedData = new byte[byteArrayLength];
                dataInputStream.readFully(receivedData); // byte 배열 수신
                String receivedMessage = dataInputStream.readUTF();
                checkNewMessage = receivedData;
            	//클라이언트로부터 받은 바이트배열을 append 
            	StringBuilder temp = printByteArrayAsBinary(checkNewMessage);
            	receive_message_num++;
                receivedMessagesArea.append("[" + receive_message_num + "] 수신된 메시지 from " + clientIP + ": " +temp +receivedMessage +"\n" );
                //newAckReceived_tcp = true; // 에코 메시지를 받았을 경우
                System.out.println("newAckMessage was coming");
                
                //notify() 대신 checking메소드 호출
                startCheck.startChecking();
                
                	
                
            }
        } finally {
            // Exception을 throw하여 외부에서 처리하도록 함.
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
            System.out.println("TCP 소켓이 닫혔습니다.");
        }
    }*/
    
    //boolean 받는 코드
    
    
    public void startReceiving() throws IOException { // 예외를 throw하여 ClientHandler에서 처리하도록 설계
        

        DataInputStream dataInputStream = null; // 데이터 수신을 위한 DataInputStream
       

        try {
            // DataInputStream 초기화
            dataInputStream = new DataInputStream(socket.getInputStream());

            String clientIP = socket.getInetAddress().getHostAddress(); // 클라이언트 IP 가져오기
            System.out.println("Server_TCP is open");

            while (!socket.isClosed()) { // 클라이언트와 연결이 유지되는 동안 메시지 수신
                // **1비트 데이터 수신**
                byte receivedBit = dataInputStream.readByte(); // 1비트를 수신 , 자바에서는 1비트만 보내는 것이 불가 1byte로 보내게 됨
                boolean receivedBoolean = (receivedBit == 1); // 1 -> true, 0 -> false로 변환

                // **UTF 메시지 수신**
                String receivedMessage = dataInputStream.readUTF();

                

                // **수신 메시지 출력**
                receive_message_num++;
                receivedMessagesArea.append("[" + receive_message_num + "] 수신된 메시지 from " + clientIP + 
                                            ": " + receivedBoolean + " - " + receivedMessage + "\n");

                System.out.println("새로운 확인 메시지가 수신되었습니다: " + receivedBoolean);
                
                startCheck.startChecking();
                //모든 바이트배열이 1이었다면, StartTCPCheck의 checking 메서드 호출
                
                
            }
        } finally {
            // 자원 정리
            if (dataInputStream != null) dataInputStream.close();
            if (socket != null && !socket.isClosed()) socket.close();
            System.out.println("TCP 소켓이 닫혔습니다.");
        }
    }
	
    
    
    //Object 읽는 코드 
    /*
    public void startReceiving() throws IOException {
        ObjectInputStream objectInputStream = null; // Ack 객체를 수신하기 위한 ObjectInputStream

        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            String clientIP = socket.getInetAddress().getHostAddress();
            System.out.println("Server_TCP is open");

            while (!socket.isClosed()) {
                try {
                    // **Ack 객체 수신**
                    Ack receivedAck = (Ack) objectInputStream.readObject();

                    // **수신된 메시지 처리**
                    receive_message_num++;
                    StringBuilder temp = printByteArrayAsBinary(receivedAck.getMessage());
                    receivedMessagesArea.append("[" + receive_message_num + "] 수신된 Ack from " + clientIP + ":\n" +
                                                " - isCompleted: " + receivedAck.isCompleted() + "\n" +
                                                " - Size of Message: " + receivedAck.getSizeOfMessage() + "\n" +
                                                " - byteMessage: " + temp + "\n" +
                                                " - Time: " + receivedAck.getTime() + "\n");

                    System.out.println("새로운 Ack 메시지가 수신되었습니다: " + receivedAck);

                    // 완료 메시지인 경우 추가 처리
                    if (receivedAck.isCompleted()) {
                        startCheck.startChecking();
                    }
                } catch (EOFException e) {
                    // 클라이언트가 연결을 닫은 경우
                    System.out.println("EOFException: 클라이언트가 연결을 닫았습니다. IP: " + clientIP);
                    break;
                } catch (ClassNotFoundException e) {
                    // 수신된 객체를 Ack로 변환할 수 없는 경우
                    System.err.println("ClassNotFoundException: Ack 객체 변환 실패. IP: " + clientIP);
                    e.printStackTrace();
                } catch (IOException e) {
                    // I/O 예외의 상세 정보를 출력
                    System.err.println("IOException 발생: 메시지 읽기 실패");
                    System.err.println(" - 원인: " + e.getMessage());
                    System.err.println(" - 소켓 상태: isClosed=" + socket.isClosed() + ", isConnected=" + socket.isConnected());
                    System.err.println(" - 클라이언트 IP: " + clientIP);
                    e.printStackTrace();
                    break;
                }
            }
        } catch (IOException e) {
            // 소켓 초기화 과정에서의 예외 처리
            System.err.println("IOException: ObjectInputStream 생성 중 문제 발생");
            System.err.println(" - 원인: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 자원 정리
            try {
                if (objectInputStream != null) objectInputStream.close();
            } catch (IOException e) {
                System.err.println("ObjectInputStream 닫기 중 IOException 발생: " + e.getMessage());
                e.printStackTrace();
            }
            try {
                if (socket != null && !socket.isClosed()) socket.close();
            } catch (IOException e) {
                System.err.println("소켓 닫기 중 IOException 발생: " + e.getMessage());
                e.printStackTrace();
            }
            System.out.println("TCP socket is closed");
        }
    }
	*/
    
    private StringBuilder printByteArrayAsBinary(byte[] byteArray) {
    	StringBuilder binaryStringBuilder = new StringBuilder();
        for (byte b : byteArray) {
            // 각 바이트를 0과 1로 변환
        	// 16진수 1111 1111과 &연산
            String binaryString = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            //System.out.println(binaryString); // 변환된 이진수 출력
            binaryStringBuilder.append(binaryString).append(" ");
            
        }
        return binaryStringBuilder;
    }
    public void stopTCPCheck() {
    	startCheck.stopChecking();
    }

}

package client_Source;
import java.util.Arrays;
public class UDPCheckThread implements Runnable {
    //private final UDPReceive receiver_udp;
    private final TcpSocketConnection tcpConnection;
    private final TCPSend tcpsend;
    public UDPCheckThread( TcpSocketConnection tcpConnection,TCPSend tcpsend) {
        //this.receiver_udp = receiver_udp;
        this.tcpConnection = tcpConnection;
        this.tcpsend = tcpsend;
        
    }

    @Override
    public void run() {
        while (true) {
            
        	try {

                if(!Arrays.equals(UDPReceive.checkNewMessage, UDPReceive.lastMessage)) { // checkNewMessage �迭�� ��ȭ�� ������ ���� ack ����
                	
                	
                	//byte�迭�� Ack�޽����� �����Եȴ�. sendAckMessage�� �����ǵǾ�����
                	//tcpConnection.sendAckMessage(receiver_udp.checkNewMessage);
                	             
                	//printByteArrayAsBinary(receiver_udp.checkNewMessage); // ���� ack���� ���           	

                	// �迭�� ��� ��Ʈ�� 1���� Ȯ��
                	boolean allBitsOne = true;
                	for (byte b : UDPReceive.checkNewMessage) {
                	    if (b != (byte) 0xFF) { // ���� �� ����Ʈ�� 0xFF�� �ƴ϶��
                	        allBitsOne = false;
                	        break;
                	    }
                	}

                	if (allBitsOne) {
                	    System.out.println("all packet received");
                	    /*��� 1�̸� boolean�� Ack�޽����� �������� ����*/
                	    //true or false �� ����
                	    tcpsend.sendMessage_tcp_alltrue(allBitsOne);
                	    
                	    //��ü Ÿ������ ����
                	    //tcpsend.sendAckObject(UDPReceive.checkNewMessage);
                	    
                	    
                	    Arrays.fill(UDPReceive.checkNewMessage, (byte) 0); // checkNewMessage �迭�� 0���� �ʱ�ȭ
                	    //byte �迭 �ʱ�ȭ(�����ؾ��� ��Ʈ���� ��� 1��)
                        for(int i=UDPReceive.array_index*8 - UDPReceive.ignored_bits+1 ; i<= UDPReceive.array_index*8; i++){
                        	UDPReceive.SetNewMsgBit(i);     	
                        	} 
                        
                       
                	    UDPReceive.receivedMessageNum++; // ���� �޽����� ���� �غ�
                	    System.out.println("receivedMessageNum: " + UDPReceive.receivedMessageNum);
                	}
                	
                	// �迭�� ������ �����Ͽ� lastMessage�� ����
                	UDPReceive.lastMessage = Arrays.copyOf(UDPReceive.checkNewMessage, UDPReceive.checkNewMessage.length);
                
                
                	
                
                }
                
                // ������ �ð� ���� ���
                long interval = 50;
                
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                // �����尡 �ߴܵǾ��� �� ���� ó��
                System.out.println("Thread was interrupted");
                break;
            }
        }
    }
    public static void printByteArrayAsBinary(byte[] byteArray) {
        for (byte b : byteArray) {
            // �� ����Ʈ�� 0�� 1�� ��ȯ
            String binaryString = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            System.out.println(binaryString); // ��ȯ�� ������ ���
        }
    }
}
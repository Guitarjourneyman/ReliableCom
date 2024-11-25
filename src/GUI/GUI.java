package GUI;

import main.*;
import server_Source.BroadcastAddressFinder;
import server_Source.UDPBeaconbroadcast;
import server_Source.UDPBroadcastSend;

import javax.swing.*;

import client_Source.UDPReceive;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;


public class GUI extends JFrame {

    public static JTextArea receivedMessagesArea;
    public static JTextArea sendMessageArea;
    public static JTextArea consoleArea;
    private JButton connection_Button;
    private JButton sendButton_UDP;
    private JButton sendStopButton_UDP;
    private JButton connectionSetup_Button;
    private JButton receiveButton_UDP;
    private JButton clearReceiveButton;
    private JButton clearSendButton;
    private JButton stopSetup_Button;
    private JButton resetProgram;
    
    
    
    public static JTextField inputIp;
    public static JTextField inputIp_udpBroad;
    
   
    
    

    
    public GUI() {

    	
        // GUI 기본 설정
        setTitle("Reliable Com");
        setSize(1300, 600); // 크기를 조금 더 늘려줌
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 수신 메시지 영역 (맨 위에 위치)
        receivedMessagesArea = new JTextArea();
        receivedMessagesArea.setEditable(false);
        receivedMessagesArea.setLineWrap(true);
        receivedMessagesArea.setWrapStyleWord(true);
        JScrollPane receivedScrollPane = new JScrollPane(receivedMessagesArea);
        receivedScrollPane.setBorder(BorderFactory.createTitledBorder("Received Messages"));

        // Clear 버튼 추가 (수신 메시지)
        clearReceiveButton = new JButton("Clear Received Messages");
        clearReceiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                receivedMessagesArea.setText(""); // 수신 메시지 창의 텍스트 초기화
                //edit
                UDPReceive.reset_message_num(); // 수신 메시지 카운터 초기화
                
            }
        });

        // 전송 메시지 입력 영역 (중간에 위치)
        sendMessageArea = new JTextArea();
        sendMessageArea.setLineWrap(true);
        sendMessageArea.setWrapStyleWord(true);
        JScrollPane sendScrollPane = new JScrollPane(sendMessageArea);
        sendScrollPane.setBorder(BorderFactory.createTitledBorder("Send Message"));

        // Clear 버튼 추가 (전송 메시지)
        clearSendButton = new JButton("Clear Send Messages");
        clearSendButton.addActionListener(new ActionListener() {
            @Override
            
            //edit
            public void actionPerformed(ActionEvent e) {
                sendMessageArea.setText(""); // 전송 메시지 창의 텍스트 초기화
                //edit
                UDPBroadcastSend.resetCount();
            }
        });

        // 콘솔 영역 (맨 아래에 위치)
        consoleArea = new JTextArea();
        consoleArea.setEditable(false);
        consoleArea.setLineWrap(true);
        consoleArea.setWrapStyleWord(true);
        JScrollPane consoleScrollPane = new JScrollPane(consoleArea);
        consoleScrollPane.setBorder(BorderFactory.createTitledBorder("Console"));

        // 버튼 생성
        connection_Button = new JButton("Connection");
        sendButton_UDP = new JButton("Send UDP Message");
        connectionSetup_Button = new JButton("Connection Setup");
        receiveButton_UDP = new JButton("Wait for UDP");
        sendStopButton_UDP = new JButton("Stop UDP Msg");
        stopSetup_Button = new JButton("Stop Connection Setup");
        resetProgram = new JButton("Reset Program");
        
        // TCP 소켓의 IP 입력 필드
        inputIp = new JTextField("192.168.0.228", 15);
        //자동으로 WifiBroadAddress를 찾는 클래스 실행
        //edit
      		InetAddress BroadcastAddress = BroadcastAddressFinder.getWiFiBroadcastAddress();
      		if (BroadcastAddress != null) {
      			String BroadIP = BroadcastAddress.getHostAddress();
      			System.out.println("Found IP: "+ BroadIP);
      			inputIp_udpBroad = new JTextField(BroadIP,15);//프로그램이 실행되자말자 IP를 읽어들여 Broadcast 주소를 입력해둔다.
      			
      		}
       
        
        
        // 버튼과 텍스트 필드를 담을 패널
        JPanel buttonPanel_main = new JPanel(new FlowLayout());
        JPanel buttonPanel_1 = new JPanel(new FlowLayout());
        JPanel buttonPanel_2 = new JPanel(new FlowLayout());
        JPanel buttonPanel_3 = new JPanel(new FlowLayout());
        JPanel buttonPanel_4 = new JPanel(new BorderLayout());
        JPanel buttonPanel_5 = new JPanel(new BorderLayout());
        JPanel buttonPanel_6 = new JPanel(new BorderLayout());
        //새로운 버튼을 위한 패널 설정
        JPanel buttonSmallPanel = new JPanel(new BorderLayout());
        
        buttonPanel_1.add(new JLabel("Client1 IP:"));
        buttonPanel_1.add(inputIp);
        buttonSmallPanel.add(buttonPanel_1,BorderLayout.NORTH);       
        buttonPanel_main.add(buttonSmallPanel);
        
        buttonPanel_2.add(new JLabel("Broad IP:"));
        buttonPanel_2.add(inputIp_udpBroad);
        buttonPanel_main.add(buttonPanel_2);
          
        buttonPanel_3.add(connection_Button);        
        //buttonPanel_main.add(buttonPanel_3);
        
        buttonPanel_4.add(connectionSetup_Button,BorderLayout.NORTH);
        buttonPanel_4.add(stopSetup_Button,BorderLayout.SOUTH);
        buttonPanel_main.add(buttonPanel_4);
        buttonPanel_5.add(sendButton_UDP,BorderLayout.NORTH);
        buttonPanel_5.add(sendStopButton_UDP,BorderLayout.SOUTH);
        //buttonPanel_5.add(receiveButton_UDP,BorderLayout.SOUTH);
        buttonPanel_main.add(buttonPanel_5);
               
        
        buttonPanel_6.add(resetProgram);	
        buttonPanel_main.add(buttonPanel_6);
        

        // 메인 레이아웃 설정
        setLayout(new BorderLayout());

        // 수신 메시지 + Clear 버튼을 위한 패널
        JPanel receivedPanel = new JPanel(new BorderLayout());
        receivedPanel.add(receivedScrollPane, BorderLayout.CENTER);
        receivedPanel.add(clearReceiveButton, BorderLayout.SOUTH);

        // 전송 메시지 + Clear 버튼을 위한 패널
        JPanel sendPanel = new JPanel(new BorderLayout());
        sendPanel.add(sendScrollPane, BorderLayout.CENTER);
        sendPanel.add(clearSendButton, BorderLayout.SOUTH);

        // Center Panel: 모든 창을 동일한 크기로 설정하기 위한 GridLayout
        JPanel centerPanel = new JPanel(new GridLayout(3, 1));  // 3 rows, 1 column
        centerPanel.add(receivedPanel);  // 수신 메시지 창 + Clear 버튼
        centerPanel.add(sendPanel);      // 전송 메시지 창 + Clear 버튼
        centerPanel.add(consoleScrollPane);   // 콘솔 창

        add(centerPanel, BorderLayout.CENTER);      // 중앙에 3개의 창을 같은 크기로 배치
        add(buttonPanel_main, BorderLayout.SOUTH);        // 하단에 버튼 패널 배치
        
        
        
        // 연결 버튼 이벤트 처리
        connection_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               Main.ComSetupResponse();
            }
        });
        //해당 버튼을 눌러야 연결버튼을 통한 소켓연결이 가능함 
        
     // TCP 소켓연결 버튼 이벤트 처리
        connectionSetup_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	Main.ComSetup();
            	}
        });
        //SETUP 중지 버튼
        stopSetup_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	Main.StopSetup();
            }
        });

        sendButton_UDP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	Main.ComBroadcastSend();
  
            }
        });
        // UDP 전송 중지 버튼
        sendStopButton_UDP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	Main.StopBroadcastSend();
            }
        });
        receiveButton_UDP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	Main.ComReceive();
            }
        });
        
        
        resetProgram.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	Main.ComReset();
            }
        });
        
    }
    
   
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }
}


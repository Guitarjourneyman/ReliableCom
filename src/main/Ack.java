package main;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Ack implements Serializable {
	private static final long serialVersionUID = 1L; // 직렬화 버전 관리 ID

    public boolean isCompleted; // 모든 배열 값이 1인지 여부
    private int sizeOfMessage;   // 바이트 배열 크기
    public byte[] byteMessage;      // 바이트 배열
    private String time;         // 타임스탬프

    // 생성자
    public Ack(byte[] byteArray) {
        if (byteArray == null) {
            throw new IllegalArgumentException("Byte array cannot be null");
        }
        this.byteMessage = byteArray;
        this.sizeOfMessage = byteArray.length;
        this.isCompleted = true; // 모든 값이 1인지 확인
        // 현재 시간을 hh:mm:ss.SSS 형식으로 가져오기
        String timeStamp = new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
        this.time = " From Window " + "[" + timeStamp + "]";              // 타임스탬프 생성
    }


    // Getter 메서드
    public boolean isCompleted() {
        return isCompleted;
    }

    public int getSizeOfMessage() {
        return sizeOfMessage;
    }

    public byte[] getMessage() {
        return byteMessage;
    }

    public String getTime() {
        return time;
    }

    // toString 메서드 오버라이드 (디버깅 및 출력용)
    @Override
    public String toString() {
        return "Ack{" +
                "isCompleted=" + isCompleted +
                ", sizeOfMessage=" + sizeOfMessage +
                ", message=" + (byteMessage != null ? new String(byteMessage) : "null") +
                ", time='" + time + '\'' +
                '}';
    }
}


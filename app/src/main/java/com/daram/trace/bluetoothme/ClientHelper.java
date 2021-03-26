package com.daram.trace.bluetoothme;

public class ClientHelper {
    //이젠 사용하지 않는 클래스


    public byte[] appendByte = new byte[3000];//지금까지 받았던 byte들 다 수집
    int idx= 0;//2byte의 숫자를 취급하기 위해

    int data=0;//2번에 걸쳐서 모으기
    private String TAG = "CLIENT_HELPER";
    public byte[] parseMode(String message){
        byte m[] = new byte[1];//mode를 나타내는 바이트 하나
        switch (message){
            case "RUN":
                m[0] = (byte) 0x01;
                break;
            case "Stop":
                m[0]= (byte)0x02;
                break;
            case "VERSION":
                m[0] = (byte)0x03;
                break;
            case "MESURE":
                m[0] = (byte) 0x04;
        }
        return m;
    }


    //그냥 순차적으로 왼발 따로 오른발 따로 받는다 치기


    //1월에 만들어뒀던거 버전이 아마 byte[] 1차원에 양 발 데이터가 들어있는듯

    public int parseDataSlicedByteVersion(byte[] barr){//홀수일 때만 의미있는 값이겠지만 일단은 리턴
        int b = barr[0];
        if(idx %2 ==0){//윗부분, refresh
            data= b * 0X100;
        }
        else{
            data+=b;
        }
        appendByte[idx] = barr[0];//일단 저장해놓자
        return data;
    }
    //딴 거 하나도 안 오고 그냥 족압 데이터 숫자 하나 받는 경우(8개 자리 고려 ㄴㄴ)
    public int parseDataSlicedByteVersion(byte b){//홀수일 때만 의미있는 값이겠지만 일단은 리턴
        if(idx %2 ==0){//윗부분, refresh
            data= b * 0X100;
        }
        else{
            data+=b;
        }
        appendByte[idx] = b;//일단 저장해놓자
        return data;
    }
    int sensoridx = 0;
    int[] sensordata = new int[8];
    //s-m-d(2)-e 순으로 들어오는데 하나의 string으로 합쳐져서 받는 경우
    public int parseDataSlicedStringVersion1(String s){
        byte start = (byte)s.charAt(0);
        byte mode = (byte)s.charAt(1);
        //임의로 2 개 byte를 data라고 가정
        int data= (int)s.charAt(2)*0x100+(int)s.charAt(3);
        sensordata[sensoridx]= data;
        //data에서 어떻게 숫자를 뽑아내야하는지 몰라서 대충 채워넣음
        ByteUtil.logStringAskii(s);
        sensoridx++;
        if(sensoridx==8){
            sensoridx = 0;
        }
        return data;
    }
    //s-m-d(16)-e 순으로 들어오는데 한개의 string으로 합쳐져서 받는 경우
    public int[] parseDataSlicedStringVersion2(String s){
        byte start = (byte)s.charAt(0);
        byte mode = (byte)s.charAt(1);
        int data[] = new int[8];
        for(int i = 0;i<8;i++){
            data[i] = s.charAt(2*i)*0x100+s.charAt(2*i+1);
        }
        ByteUtil.logStringAskii(s);
        return data;
    }
    //s-m-d(16)-e 순으로 들어오는데 한 개의 byte로 받는 경우

}

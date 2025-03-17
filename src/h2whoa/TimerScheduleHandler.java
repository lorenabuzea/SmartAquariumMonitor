package h2whoa;
import java.util.TimerTask;
import com.fazecast.jSerialComm.SerialPort;
import  com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortDataListener;

import javax.swing.*;

public class TimerScheduleHandler extends  TimerTask implements SerialPortDataListener{

    private final  long timestart;
    //constructor
    public TimerScheduleHandler(long timeStart) {
        this.timestart = timeStart;
    }

    //Override the time method in TimerTask
    @Override
    public void run(){
        System.out.println("Time elapsed: " + (System.currentTimeMillis() - this.timestart) + " miliseconds");
    }

    @Override
    public int getListeningEvents(){
        return  SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    @Override
    public  void serialEvent( SerialPortEvent serialPortEvent){
        if( serialPortEvent.getEventType() == SerialPort.LISTENING_EVENT_DATA_RECEIVED);
        System.out.println("Yes! The arduino is alive");
    }

}

/*
@Author Mohamad Mohyeddine

the job of this thread is only receiving connections with serversocket and associate each socket to another thread
named SensorManagement
 */


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class SensorReception extends Thread
{
    private ServerSocket Receiver;
    private Socket socket;
    private SupervisionParms supervisionParms;
    private boolean running=true;
    private final int portNb = 1111;
    private DescriptorReceiver descriptorReceiver;

    //constructor
    public SensorReception(SupervisionParms supervisionParms) {
        this.supervisionParms = supervisionParms;
    }

    public synchronized boolean isRunning(Boolean state) {
        if(state!=null)
            running=state;
        return state;
    }

    //Start receiving connections
    private void StartReceiving()
    {
        while(isRunning(null)) {
            try {
                socket = Receiver.accept();
                Thread manager = new SensorManagement(socket, supervisionParms);
                manager.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        descriptorReceiver.isRunning(false);
    }

    //start a descriptor Receiver
    private void StartDescriptorReceiving()
    {
        try {
            descriptorReceiver = new DescriptorReceiver(supervisionParms);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        descriptorReceiver.start();
    }
    public void run()
    {
        //initialize ServerSocket;
        try {
            Receiver=new ServerSocket(portNb);
            StartDescriptorReceiving();
            StartReceiving();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StartReceiving();

    }
}

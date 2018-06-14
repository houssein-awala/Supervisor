/*
@Author Mohamad Mohyeddine

the job of this thread is only receiving connections with serversocket and associate each socket to another thread
named SensorManagement
 */


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SensorReception extends Thread
{
    private ServerSocket Receiver;
    private Socket socket;
    private SupervisionParms supervisionParms;
    private final int portNb = 1111;

    //constructor
    public SensorReception(SupervisionParms supervisionParms) {
        this.supervisionParms = supervisionParms;
    }

    //Start receiving connections
    private void StartReceiving()
    {
        while(true) {
            try {
                socket = Receiver.accept();
                Thread manager = new SensorManagement(socket, supervisionParms);
                manager.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void run()
    {
        //initialize ServerSocket;
        try {
            Receiver=new ServerSocket(portNb);
        } catch (IOException e) {
            e.printStackTrace();
        }

        StartReceiving();

    }
}

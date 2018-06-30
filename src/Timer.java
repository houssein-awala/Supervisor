/*
@Author Mohamad Mohyeddine

this thread is a timer that reset every time a datagram packet is received

and will make sure that when the time is out ,inform the sensor manager to deal with this sensor
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Timer extends Thread
{
    //tics represents the chances that the sensor have before been considered down
    private int tics;

    //rate is the time gap between receiving two packets
    private int rate;

    //Supervision Parms to access Descriptor
    private SupervisionParms parms;

    //id of the sensor that this timer is associated with
    private String id;

    //Object to ensure mutual execution on tics
    private final Object lock = new Object();


    public Timer(String id,int tics, int rate, SupervisionParms parms) {
        this.tics = tics;
        this.rate = rate;
        this.parms = parms;
        this.id=id;
    }

    //reset the tics, called when a datagram packet received
    public void ResetTics(int newTics)
    {
        synchronized (lock) {
            this.tics = newTics;
        }
    }

    @Override
    public void run()
    {
        while (true)
        {
            synchronized (lock)
            {
                if(--tics == 0)
                    break;
            }
            try {
                sleep(rate*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        parms.getDescriptor(id).setState(Descriptor.ERROR);
        try {

            Socket socket = new Socket("localhost", 9999);
            PrintWriter writer=new PrintWriter(socket.getOutputStream());
            writer.print(id);
            socket.close();
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        parms.deleteTimer(id);
    }
}


/*
@Author Mohamad Mohyeddine
the job of this thread is only receiving packets with datagram socket and associate each packet  to another thread
named DescriptorsUpdating
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class DescriptorReceiver extends Thread
{
    SupervisionParms parms;
    DatagramSocket receiver;
    DatagramPacket packet;
    byte[] data;
    boolean running;

    //Constructor
    public DescriptorReceiver(SupervisionParms parms) throws SocketException {
        this.parms = parms;
        receiver=new DatagramSocket(1111);
        data=new byte[1024*10];
    }

    public synchronized boolean isRunning(Boolean state) {
        if(state!=null)
            running=state;
        return state;
    }

    //Start Receiving packets
    private void StartReception() throws IOException {
        while (isRunning(null))
        {
            packet=new DatagramPacket(data,data.length);
            receiver.receive(packet);
            DescriptorsUpdating descriptorsUpdating =new DescriptorsUpdating(packet.getData(),parms);
            descriptorsUpdating.start();
        }
    }
    @Override
    public void run()
    {
        try {
            StartReception();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

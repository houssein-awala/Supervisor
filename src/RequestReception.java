import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/*
@Author Ghina Saad/Mohamad Mohyeddine

This Thread receives connections from the sink and start a RequestHandling thread to deal with them
*/
public class RequestReception extends Thread
{
    private ServerSocket connectsink;
    private Socket socket;
    private SupervisionParms supervisionParms;
    private final int portNb = 8888;
    private boolean running;

    public RequestReception(SupervisionParms supervisionParms) {
        this.supervisionParms = supervisionParms;
    }

    public synchronized boolean isRunning(Boolean state) //By Mohamad Mohyeddine
     {
        if(state!=null)
            running=state;
        return state;
    }

    public void run()
    {
        try {
            connectsink=new ServerSocket(portNb);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(isRunning(null)) {
            try {
          socket = connectsink.accept();
                Thread t=new RequestHandling(supervisionParms,socket);
            t.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

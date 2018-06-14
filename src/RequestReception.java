import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/*
@Author Ghina Saad
*/
public class RequestReception extends Thread
{
    private ServerSocket connectsink;
    private Socket socket;
    private SupervisionParms supervisionParms;
    private final int portNb = 8888;

    public RequestReception(SupervisionParms supervisionParms) {
        this.supervisionParms = supervisionParms;
    }

    public void run()
    {
        try {
            connectsink=new ServerSocket(portNb);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(true) {
            try {
          socket = connectsink.accept();
                Thread t=new RequestHandling(socket,supervisionParms);
            t.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

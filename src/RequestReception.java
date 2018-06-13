import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RequestReception extends Thread
{
    ServerSocket connectsink;
    Socket socket;
    SupervisionParms supervisionParms;
    public RequestReception()
    { }
    public void run()
    {
        while(true) {
            try {
          socket = connectsink.accept();
                Thread t=new RequestHandling(socket);
            t.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

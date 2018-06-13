import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RequestHandling extends Thread
{
    ServerSocket connectsink;
    Socket socket;
    public RequestHandling()
    { }
    public void run()
    {
        while(true) {
            try {
          socket = connectsink.accept();
            Thread t=new RequestReception(connectsink,socket);
            t.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

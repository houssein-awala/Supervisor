import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
//Editor :Mohamad Mohyeddine and Ghina Saad
/*
the role of this class consist of receiving requests and hand them to a handler */
public class RequestReception extends Thread
{
    ServerSocket connectsink;
    Socket socket;
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

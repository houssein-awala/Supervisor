import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Map;

/*
@Author Mohamad Mohyeddine/Ghina Saad

this Thread handle a request coming from the sink and return to it the id of a sensor that can execute this

request according to different parameters and criteria while ensuring minimum load balancing and then distance

optimization
 */
public class RequestHandling extends Thread //By Mohamad Mohyeddine
{
    private BufferedReader reader;

    private BufferedWriter writer;

    private SupervisionParms parms;

    private Point position;

    private int type;

    private int requestId;

    private Socket request;

    public RequestHandling(SupervisionParms parms, Socket request) { //By Mohamad Mohyeddine

        this.parms = parms;
        this.request = request;
        position=new Point();
    }

    @Override
    public void run() //By Mohamad Mohyeddine

    {
        try {
            iniRequest();
            sendResponse(AssignSensor());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //methode to read the request string and extract the necessary parameters
    private void iniRequest() throws Exception //By Mohamad Mohyeddine

    {
        reader=new BufferedReader(new InputStreamReader(request.getInputStream()));
        String statment =reader.readLine();
        String tab[]=statment.split("-");
        type=Integer.parseInt(tab[0]);
        position.x=Integer.parseInt(tab[1]);
        position.y=Integer.parseInt(tab[2]);
        requestId=parms.newSinkRequest();
        reader.close();
    }

    //search the best sensor to execute the request
    private String AssignSensor() //By Mohamad Mohyeddine

    {
        String sensorId="";
        int minSinkRequest=Integer.MAX_VALUE;
        double minDistance=Double.MAX_VALUE;

        for (Map.Entry<String,Descriptor> entry :parms.getDescriptors().entrySet())
        {
            if(entry.getValue().getType()==type&&entry.getValue().getCapacity()>entry.getValue().getNbRequest())
            {
                double distanceWithRequest =distance(position,entry.getValue().getPosition());
                if(distanceWithRequest<=entry.getValue().getRange())
                {
                    int lastReaquest=entry.getValue().getLastRequestServed();
                    double distanceWithSink =distance(new Point(0,0),entry.getValue().getPosition());
                    if((lastReaquest<minSinkRequest)||(lastReaquest==minSinkRequest&&distanceWithSink<minDistance))
                    {
                        minSinkRequest=lastReaquest;
                        minDistance=distanceWithSink;
                        sensorId=entry.getKey();
                    }
                }
            }
        }
        return "RequestID:"+requestId+"/Sensor:"+sensorId;
    }

    //send the sink the id of the selected sensor
    private void sendResponse(String id) throws IOException { //By Mohamad Mohyeddine

        writer=new BufferedWriter(new OutputStreamWriter(request.getOutputStream()));
        writer.write(id);
        writer.flush();
        writer.close();
    }

    private double distance(Point a, Point b) //By Ghina Saad

    {
        return Math.sqrt(Math.pow((a.getX() - b.getX()), 2) + Math.pow((a.getY() - b.getY()), 2));
    }
}
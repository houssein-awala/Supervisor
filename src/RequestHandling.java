import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
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
        double max_proba=0;
        double maxDistance=0;
        HashMap<String,Double> distances=new HashMap<>();
        HashMap<String,Double> finaleValue=new HashMap<>();

        //loop to calculate distances and find the max one
        for (Map.Entry<String,Descriptor> entry :parms.getDescriptors().entrySet())
        {
            if(entry.getValue().getType()==type&&entry.getValue().getCapacity()>entry.getValue().getNbRequest())
            {
                double distanceWithRequest =distance(position,entry.getValue().getPosition());
                if(distanceWithRequest<=entry.getValue().getRange())
                {
                    distances.put(entry.getKey(),distanceWithRequest);
                    if(distanceWithRequest>maxDistance)
                        maxDistance=distanceWithRequest;
                    double temp=(1-(entry.getValue().getNbRequest()/entry.getValue().getCapacity()))*0.2+(1-(entry.getValue().getLastRequestServed()/(requestId-1)))*0.4;
                    finaleValue.put(entry.getKey(),temp);
                }
            }
        }
        //decide the suitable sensor
        for(Map.Entry<String,Double> entry:distances.entrySet())
        {
            double temp=((1-(entry.getValue()/maxDistance))*0.4)+finaleValue.get(entry.getKey());
            if(temp>max_proba)
            {
                max_proba=temp;
                sensorId=entry.getKey();
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
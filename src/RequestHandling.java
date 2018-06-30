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
    private ObjectInputStream objectInputStream;

    private ObjectOutputStream objectOutputStream;

    private SupervisionParms parms;

    private int requestId;

    private Socket socket;

    private SinkRequest request;

    public RequestHandling(SupervisionParms parms, Socket socket) { //By Mohamad Mohyeddine

        this.parms = parms;
        this.socket = socket;
    }

    @Override
    public void run() //By Mohamad Mohyeddine

    {
        try {
            iniRequest();
            AssignSensor();
            sendResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //method to read the request string and extract the necessary parameters
    private void iniRequest() throws Exception //By Mohamad Mohyeddine

    {
        objectInputStream=new ObjectInputStream(socket.getInputStream());
        request=(SinkRequest)objectInputStream.readObject();
        requestId=parms.newSinkRequest();
        request.setRequestID(requestId);
        objectInputStream.close();
    }

    //search the best sensor to execute the request
    private void AssignSensor() //By Mohamad Mohyeddine

    {
        String sensorId="";
        double max_proba=0;
        double maxDistance=0;
        HashMap<String,Double> distances=new HashMap<>();
        HashMap<String,Double> finaleValue=new HashMap<>();
        if(request.getState()==SinkRequest.ERROR)
            request.addCurrentToSensors();
        //loop to calculate distances and find the max one
        for (Map.Entry<String,Descriptor> entry :parms.getDescriptors().entrySet())
        {
            if(!request.isUnReached(entry.getKey())&&entry.getValue().getType()==request.getTypeOfService()&&entry.getValue().getCapacity()>entry.getValue().getNbRequest())
            {
                double distanceWithRequest =distance(request.getPosition(),entry.getValue().getPosition());
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
        request.setSelectedSensor(sensorId);
    }

    //send the sink the id of the selected sensor
    private void sendResponse() throws IOException { //By Mohamad Mohyeddine

        objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(request);
        objectOutputStream.close();
    }

    private double distance(Point a, Point b) //By Ghina Saad

    {
        return Math.sqrt(Math.pow((a.getX() - b.getX()), 2) + Math.pow((a.getY() - b.getY()), 2));
    }
}
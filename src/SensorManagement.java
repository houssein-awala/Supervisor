/*

@Author Mohamad Mohyeddine

This Thread is responsible of the setting up a sensor
i.e register the descriptor and change the state of the sensor to ready
after given it an ID
 */

import sun.security.krb5.internal.crypto.Des;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.*;


public class SensorManagement extends Thread
{
    private SupervisionParms parms;
    private Socket sensorSocket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private ISensor sensor;
    Descriptor desc;
    boolean ready;

    //Constructor
    public SensorManagement(Socket sensorSocket, SupervisionParms parms ) throws IOException {
        this.parms = parms;
        this.sensorSocket = sensorSocket;
        objectInputStream=new ObjectInputStream(sensorSocket.getInputStream());
        objectOutputStream=new ObjectOutputStream(sensorSocket.getOutputStream());
    }

    /*generate id for the sensor,set it in the descriptor ,add descriptor and sensor
        to SupervisionParms and update the descriptor in the sensor with the new parameters

     */
    private void addDescriptor()
    {
        String uniqueID = UUID.randomUUID().toString();
        parms.addSensor(uniqueID,sensor);
        desc=sensor.getDescriptor();
        desc.setId(uniqueID);
        desc.setState(Descriptor.REGISTRED);
        parms.addDescriptor(desc);
        sensor.configure(desc);
        SendACK();
    }

    //get Sensor Reference from TCP connection
    private void getSensorReference()
    {
        try {
            sensor=(ISensor)objectInputStream.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //wait for the sensor to finish setting up with the data server
    private void waitForSensorSignal()
    {
        try {
            ready=objectInputStream.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Change the sensor state to ready
    private void UpdateSensorState()
    {
        if(ready) {
            desc.setState(Descriptor.READY);
            parms.addDescriptor(desc);
            sensor.configure(desc);
            sensor.setRoutingTable(getNeighbors());
            SendACK();
        }
    }

    private HashMap<String,Descriptor> getNeighbors()
    {
        SinkDistance [] distances=new SinkDistance[parms.getDescriptors().size()];
        int i=0;
        for(Map.Entry<String,Descriptor> entry:parms.getDescriptors().entrySet())
        {
            double dist=distance(desc.getPosition(),entry.getValue().getPosition());
            if(desc.getRange()*2>dist)
            {
                distances[i] = new SinkDistance(entry.getKey(),dist);
                i++;
            }
        }
        Arrays.sort(distances);
        HashMap<String,Descriptor> neighbors=new HashMap<>();
        for(i=0;i<5;i++)
            neighbors.put(distances[0].ID,parms.getDescriptors().get(distances[0].ID));
        return neighbors;
    }


    private double distance(Point a, Point b)
    {
        return Math.sqrt(Math.pow((a.getX() - b.getX()), 2) + Math.pow((a.getY() - b.getY()), 2));
    }


    //after changing state to ready inform the sensor that it's ready to work
    private void SendACK()
    {
        try {

            objectOutputStream.writeBoolean(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run()
    {
        getSensorReference();

        addDescriptor();

        waitForSensorSignal();

        UpdateSensorState();
    }

    private class SinkDistance implements Comparable<Double>
    {
        String ID;
        Double distance;

        public SinkDistance(String ID, double distance) {
            this.ID = ID;
            this.distance = distance;
        }

        @Override
        public int compareTo(Double aDouble) {
            return distance.compareTo(aDouble);
        }
    }
}
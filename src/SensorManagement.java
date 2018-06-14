/*

@Author Mohamad Mohyeddine

This Thread is responsible of the setting up a sensor
i.e register the descriptor and change the state of the sensor to ready
after given it an ID
 */

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
            inFormSensor();
        }
    }

    //after changing state to ready inform the sensor that it's ready to work
    private void inFormSensor()
    {
        try {
            objectOutputStream=new ObjectOutputStream(sensorSocket.getOutputStream());
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
}
import java.util.ArrayList;
import java.util.HashMap;
/*
@Author Mohamad Mohyeddine
    This class contains the parameters that to be used by all supervisor's tasks

    ISensor is an Interface extends Remote
*/
public class SupervisionParms
{
    // hashmap contains reference to all sensors to use with the simulator and first initialize of a sensor
    protected HashMap<String,ISensor> Sensors;

    //hashmap contains descriptors of all sensors to use regularly
    protected HashMap<String,Descriptor> Descriptors;

    //hashmap contains a timer for each sensor registered
    protected HashMap<String,Timer> Timers;

    //Number of Request taken from Sink,may be considered the id of a request
    protected int nbSinkRequests;

    //How many chances given to a sensor to update its descriptor
    protected final int nbChances=3;

    //Constructor
    public SupervisionParms()
    {
        Sensors=new HashMap<>();
        Descriptors=new HashMap<>();
        Timers=new HashMap<>();
        nbSinkRequests=0;
    }

    //Increase the number of requests by 1
    public void IncNbRequest()
    {
        nbSinkRequests++;
    }

    //get the total number of request received from sink
    public int getNbSinkRequests() {
        return nbSinkRequests;
    }

    // Getters and setters
    public HashMap<String, Descriptor> getDescriptors() {
        return Descriptors;
    }


    public HashMap<String, ISensor> getSensors() {
        return Sensors;
    }


    //get sensor reference by ID
    public ISensor getSensor(String id)
    {
        return Sensors.get(id);
    }

    //add a sensor reference
    public void addSensor(String id,ISensor sensor)
    {
        Sensors.put(id,sensor);
    }

    //get descriptor  by ID
    public Descriptor getDescriptor(String id)
    {
        return Descriptors.get(id);
    }

    //add a descriptor
    public void addDescriptor(Descriptor descriptor)
    {
        Descriptors.put(descriptor.getId(),descriptor);
    }


    //add a timer and start it
    public void addTimer(String id,Timer timer)
    {
        Timers.put(id,timer);
        Timers.get(id).start();
    }

    //get a timer if it exists,else return null
    public Timer getTimer(String id)
    {
        return Timers.get(id);
    }

    //delete a timer if it exists
    public void deleteTimer(String id)
    {
        if(Timers.containsKey(id))
            Timers.remove(id);
    }

    //Check if a timer exist
    public boolean isTimerExist(String id)
    {
        return Timers.containsKey(id);
    }

    //get number of chances
    public int getNbChances() {
        return nbChances;
    }
}
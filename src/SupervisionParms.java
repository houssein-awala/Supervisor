import java.util.ArrayList;
import java.util.HashMap;
/*
@Author Mohamad Mohyeddine
    This class contains the parameters that to be used by all supervisor's tasks

    ISensor is an Interface extends Remote
*/
public class SupervisionParms
{
   // hashmap contains reference to all sensors to use with the simulator and first initialize of a sensor and its lock
    protected HashMap<String,ISensor> Sensors;
    private Object senlock;
   //hashmap contains descriptors of all sensors to use regularly and its lock
    protected HashMap<String,Descriptor> Descriptors;
    private Object deslock;
   //hashmap contains a timer for each sensor registered and its lock
    protected HashMap<String,Timer> Timers;
    private Object timlock;
   //Number of Request taken from Sink,may be considered the id of a request and its lock
    protected int nbSinkRequests;
    private Object reqlock;
    //How many chances given to a sensor to update its descriptor
    protected final int nbChances=3;

    //Constructor
    public SupervisionParms()
    {
        Sensors=new HashMap<>();
        Descriptors=new HashMap<>();
        Timers=new HashMap<>();
        nbSinkRequests=0;
        senlock=new Object();
        deslock=new Object();
        timlock=new Object();
        reqlock=new Object();
    }

    //Increase the number of requests by 1
    public void IncNbRequest()
    {
        synchronized (reqlock)
        { nbSinkRequests++;}
    }

    //get the total number of request received from sink
    public int getNbSinkRequests() {
       synchronized (reqlock){return nbSinkRequests;}
    }

    //this method combine IncNbRequest and getNbRequest
    public int newSinkRequest(){

            synchronized (reqlock){return ++nbSinkRequests;}
    }

    // Getters and setters
    public HashMap<String, Descriptor> getDescriptors() {
        synchronized (deslock){return Descriptors;}
    }


    public HashMap<String, ISensor> getSensors() {
       synchronized (senlock){return Sensors;}
    }


    //get sensor reference by ID
    public ISensor getSensor(String id)
    {
       synchronized (senlock){return Sensors.get(id);}
    }

    //add a sensor reference
    public void addSensor(String id,ISensor sensor)
    {
       synchronized (senlock){Sensors.put(id,sensor);}
    }

    //get descriptor  by ID
    public Descriptor getDescriptor(String id)
    {
       synchronized (deslock){return Descriptors.get(id);}
    }

    //add a descriptor
    public void addDescriptor(Descriptor descriptor)
    {
       synchronized (deslock){Descriptors.put(descriptor.getId(),descriptor);}
    }


    //add a timer and start it
    public void addTimer(String id,Timer timer)
    {
        synchronized (timlock) {
            Timers.put(id, timer);
            Timers.get(id).start();
        }
    }

    //get a timer if it exists,else return null
    public Timer getTimer(String id)
    {
       synchronized (timlock){return Timers.get(id);}
    }

    //delete a timer if it exists
    public void deleteTimer(String id)
    {
        synchronized (timlock) {
            if (Timers.containsKey(id))
                Timers.remove(id);
        }
    }

    //Check if a timer exist
    public boolean isTimerExist(String id)
    {
       synchronized (timlock){return Timers.containsKey(id);}
    }

    //get number of chances
    public int getNbChances() {
        return nbChances;
    }
}
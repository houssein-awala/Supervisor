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

    //Number of Request taken from Sink,may be considered the id of a request
    protected int nbSinkRequests;

    //How many chances given to a sensor to update its descriptor
    protected int nbChances;

    public SupervisionParms(int nbChances)
    {
        Sensors=new HashMap<>();
        Descriptors=new HashMap<>();
        nbSinkRequests=0;
        this.nbChances=nbChances;
    }


    public void IncNbRequest()
    {
        nbSinkRequests++;
    }

    public void DecNbRequest()
    {
        nbSinkRequests--;
    }

    // Getters and setters
    public HashMap<String, Descriptor> getDescriptors() {
        return Descriptors;
    }

    public void setDescriptors(HashMap<String, Descriptor> descriptors) {
        Descriptors = descriptors;
    }

    public HashMap<String, ISensor> getSensors() {
        return Sensors;
    }

    public void setSensors(HashMap<String, ISensor> sensors) {
        Sensors = sensors;
    }

    //get sensor reference by ID
    public ISensor getSensor(String id)
    {
        if(Sensors.containsKey(id))
            return Sensors.get(id);
        return null;
    }

    //add a sensor reference
    public void addSensor(ISensor sensor)
    {
        if(Sensors.containsKey(sensor.getDescriptor().getId()))
            return;
        Sensors.put(sensor.getDescriptor().getId(),sensor);
    }

    //get descriptor  by ID
    public Descriptor getDescriptor(String id)
    {
        if(Descriptors.containsKey(id))
            return Descriptors.get(id);
        return null;
    }

    //add a descriptor
    public void addDescriptor(Descriptor descriptor)
    {
        if(Descriptors.containsKey(descriptor.getId()))
            return;
        Descriptors.put(descriptor.getId(),descriptor);
    }

    //get number of chances
    public int getNbChances() {
        return nbChances;
    }

    //set number of chances (used in simulation)
    public void setNbChances(int nbChances) {
        this.nbChances = nbChances;
    }
}
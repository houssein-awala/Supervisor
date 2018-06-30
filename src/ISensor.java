import java.awt.*;
import java.rmi.Remote;
/*
 * this interface is the remote interface shared with supervisor to setup the sensor remotely
 */
public interface ISensor extends Remote {
    public Descriptor getDescriptor();
    public void configure(Descriptor descriptor);
    public void editSensor(String id,int type,Point position);
    public  void deleteSensor(String id);
}
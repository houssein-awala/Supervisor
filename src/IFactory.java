



import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author mostafa slim
 */
public interface IFactory extends Remote{

    public void createSensor(int type, Point position, int service, int capacity, double range) throws RemoteException;
}
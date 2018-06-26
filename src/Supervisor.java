/*
@Author:Mohamad Mohyeddine
 */
public class Supervisor extends Thread
{
    public static final Object lock=Supervisor.class;

    private SupervisionParms parms;

    public static void lock()
    {
        try {
            lock.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public SupervisionParms getParms()
    {
        return parms;
    }
    //unlock
    public static void stopSupervisor()
    {
        lock.notify();
    }

    //After Starting the thread , the main thread locked on the lock object
    // when unlocked it will stop all Receivers
    // the unlock method , called stopSupervisor , will be called from the simulator on Closing
    //Note that this is totally unnecessary since all threads will be stopped when stopping the program
    // but it is good to have this level of control
    @Override
    public void run()
    {
        parms=new SupervisionParms();
        SensorReception sensorReception=new SensorReception(parms);
        RequestReception requestReception=new RequestReception(parms);
        sensorReception.start();
        requestReception.start();
        Supervisor.lock();
        sensorReception.isRunning(false);
        requestReception.isRunning(false);
    }
}

/*
@Author Mohamad Mohyeddine

This Thread Handel the data received from a packet in the DescriptorReceiver Thread

its job consists of updating the descriptor of the sensor and resetting the timer of this

sensor id it's already exists, and create it if not
 */

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class DescriptorsUpdating extends Thread
{
    private byte[] data;

    private Descriptor desc;

    private SupervisionParms parms;

    private String id;

    //Constructor
    public DescriptorsUpdating(byte[] data,SupervisionParms parms)
    {
        this.data=data;
        this.parms=parms;
    }

    //Convert The array of bytes to Descriptor Object
    private void ReadyDescriptor() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        try {
            desc= (Descriptor) is.readObject();
            id=desc.getId();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //Update descriptor
    private void Update()
    {
        setTimer();
        parms.addDescriptor(desc);
    }

    //Set or reset the timer
    private void setTimer()
    {
        if(!parms.isTimerExist(id))
            parms.addTimer(id,new Timer(id,parms.getNbChances(),Descriptor.DescriptorUpdateFrequency,parms));
        else
        {
            parms.getTimer(id).ResetTics(parms.getNbChances());
        }
    }

    @Override
    public void run()
    {
        try {
            ReadyDescriptor();
            Update();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
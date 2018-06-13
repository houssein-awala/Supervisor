import com.sun.xml.internal.ws.util.QNameMap;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestReception extends Thread {

    ServerSocket connectsink;
    DataInputStream din ;
    DataOutputStream dout;
    int type;
    String positionx;
    String positiony;
    Point position;
    //coordonnee of point position
    int x,y;
    String idmin;
    SupervisionParms supervisionParms;
    ArrayList<Descriptor> capteurs ;
    Socket s;
    static int idRequest=0;

    public RequestReception(ServerSocket connectsink,Socket s,SupervisionParms supervisionParms) {
        this.connectsink=connectsink;
        this.s=s;
       supervisionParms=new SupervisionParms();
    }
    // this method return id of closest sensor
    public String SearchIdSensor() throws IOException {
        DataInputStream din = new DataInputStream(s.getInputStream()); // TODO Auto-generated catch block
        String request="";
        while((request=din.readUTF())!=null)
        {
            String tab[];
            tab = request.split("-");

            type=Integer.parseInt(tab[0]);
            positionx=tab[1];
            positiony=tab[2];
            x=Integer.parseInt(positionx);
            y=Integer.parseInt(positiony);
            position.x=x;
            position.y=y;
            double distance_min=0;
            // id for the closest sensor
            idmin=null;
            int i=0;
            HashMap<String, Descriptor> Descriptors=supervisionParms.getDescriptors();

            for (Map.Entry<String,Descriptor> entry :Descriptors.entrySet())
            {
                if(entry.getValue().getType()==type)
                {
                    if(entry.getValue().getCapacity()<entry.getValue().getNbRequest())
                    {
                        String id=entry.getValue().getId();
                        double distance=distance(position,entry.getValue().getPosition());
                        //if the position in the sensor range
                        if(distance<=entry.getValue().getRange()) {

                            if(i==0)
                            {
                                distance_min=distance+entry.getValue().getLastRequestServed();
                                idmin=id;
                            }
                            else{
                                if((distance+entry.getValue().getLastRequestServed())< distance_min)
                                {
                                    distance_min=distance+entry.getValue().getLastRequestServed();
                                    idmin=id;
                                }
                            }

                        }
                    }
                }
            }
            supervisionParms.getDescriptor(idmin).setLastRequestServed(idRequest);
        }
        return idmin;
    }
    //this method for send reponse
    public void jfhjh(String idSensormin1) throws IOException {
        DataOutputStream dout=new DataOutputStream(s.getOutputStream());
        String reponse;

        // if no sensor capable of serving
        if(idSensormin1==null)
            reponse="no sensor capable of serving you.";
        else{
            reponse="sensor="+idSensormin1;}
        dout.writeUTF(reponse);
        dout.flush();
    }
    public void run(){
        while(true){
            try {
                 idmin=SearchIdSensor();
                 idRequest++;
                jfhjh(idmin);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    //method calcul la distance
    public double distance(Point a,Point b)
    {
        return Math.sqrt(Math.pow((a.getX() - b.getX()), 2) + Math.pow((a.getY() - b.getY()), 2));
    }



}

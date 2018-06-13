import com.sun.xml.internal.ws.util.QNameMap;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestHandling extends Thread {


    DataInputStream din ;
    DataOutputStream dout;
    int type;
    int positionx,positiony;  //coordonnee of point position
    Point position;
    Double calcul;
    String idSensor;
    SupervisionParms supervisionParms;
    Socket socket;
    static int idRequest=0;

    public RequestHandling(Socket socket) {
        this.socket=socket;
    }
    // this method return id of closest sensor
    public String getIdSensor() throws IOException {
        din = new DataInputStream(socket.getInputStream()); // TODO Auto-generated catch block
        String request="";
        while((request=din.readUTF())!=null)
        {
            //read request and split with separator
            readLine(request);
            supervisionParms=new SupervisionParms();
            idSensor=null;// id for the closest sensor

            //method search sensor suitable for request
            SearchSensor();
            //gives the request an id
           givesIdRequest(idSensor,idRequest);
        }
        return idSensor;
    }
    //this method for read line
    public void readLine(String line)
    {
        String tab[];
        tab = line.split("-");
        type=Integer.parseInt(tab[0]);
        positionx=Integer.parseInt(tab[1]);
        positiony=Integer.parseInt(tab[2]);
        position.x=positionx;
        position.y=positiony;
    }
    //this method search sensor suitable for request
    private void SearchSensor() {
        int i=0;
        double distance_min=0;
        HashMap<String, Descriptor> Descriptors=supervisionParms.getDescriptors();
        for (Map.Entry<String,Descriptor> entry :Descriptors.entrySet())
        {
          //  if he is the same type
            if(entry.getValue().getType()==type)
            {
                //if the capacity is not full
                if(entry.getValue().getCapacity()<entry.getValue().getNbRequest())
                {
                    String id=entry.getValue().getId();
                    double distance=distance(position,entry.getValue().getPosition());
                    //if the position in the sensor range
                    if(distance<=entry.getValue().getRange()) {
                        calcul=distance+entry.getValue().getLastRequestServed();
                        if(i==0)
                        {
                            distance_min=calcul;
                            idSensor=id;
                        }
                        else{
                            if(calcul< distance_min)
                            {
                                distance_min=calcul;
                                idSensor=id;
                            }
                        }
                        i++;
                    }
                }
            }
        }
    }

    // this method gives for each request id
    public void givesIdRequest(String idSensor,int idRequest)
    {
        supervisionParms.getDescriptor(idSensor).setLastRequestServed(idRequest);
    }

    //this method for send reponse
    public void SendReponse(String idSensor) throws IOException {
        DataOutputStream dout=new DataOutputStream(socket.getOutputStream());
        String reponse;

        // if no sensor capable of serving
        if(idSensor==null)
            reponse="no sensor capable of serving you.";
        else{
            reponse="sensor="+idSensor;
        }
        dout.writeUTF(reponse);
        dout.flush();
        dout.close();
    }
    //end method SendReponse

    public void run(){
        while(true){
            try {
                 idSensor=getIdSensor();
                 idRequest++;
                SendReponse(idSensor);
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

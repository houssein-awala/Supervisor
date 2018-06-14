import com.sun.xml.internal.ws.util.QNameMap;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/*
@Author Ghina Saad
 */
public class RequestHandling extends Thread {


    private BufferedReader din ;
    DataOutputStream dout;
    private int type;
    private Point position;
    private Double calcul;
    private String idSensor;
    private SupervisionParms supervisionParms;
    private Socket socket;
    private static int idRequest=0;

    public RequestHandling(Socket socket,SupervisionParms supervisionParms) {

        this.socket=socket;
        this.supervisionParms=supervisionParms;
    }


    // this method return id of closest sensor
    public String getIdSensor() throws IOException {

            //read String request
            readLine();
            idSensor=null;// id for the closest sensor

            //method search sensor suitable for request
            SearchSensor();
            //gives the request an id
           givesIdRequest(idSensor,idRequest);
        return idSensor;
    }

    //this method for read line
    public void readLine() throws IOException {
        din = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String request="";
        request=din.readLine();
        String tab[];
        tab = request.split("-");
        type=Integer.parseInt(tab[0]);
        position.x=Integer.parseInt(tab[1]);
        position.y=Integer.parseInt(tab[2]);
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
    private void givesIdRequest(String idSensor, int idRequest)
    {
        supervisionParms.getDescriptor(idSensor).setLastRequestServed(idRequest);
    }

    //this method for send reponse
    private void SendReponse(String idSensor) throws IOException {
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
            try {
                 idSensor=getIdSensor();
                 idRequest++;
                SendReponse(idSensor);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    //method calcul la distance
    private double distance(Point a, Point b)
    {
        return Math.sqrt(Math.pow((a.getX() - b.getX()), 2) + Math.pow((a.getY() - b.getY()), 2));
    }



}

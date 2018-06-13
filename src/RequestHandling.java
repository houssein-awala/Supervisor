import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class RequestHandling extends Thread {

    DataInputStream din ;
    DataOutputStream dout;
    int type;
    String positionx;
    String positiony;
    Point position;
    //coordonnee of point position
    int x,y;
    String idmin;
    ArrayList<Descriptor> capteurs ;
    Socket s;

    public RequestHandling(Socket s) {
        this.s=s;
    }
    // this method return id of closest sensor
    public String SearchIdSensor() throws IOException {
        DataInputStream din = new DataInputStream(s.getInputStream());
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
            for ( Descriptor capteur :capteurs)
            {
                if(capteur.getType()==type)
                {
                    if(capteur.getCapacity()<capteur.getNbRequest())
                    {
                        String id=capteur.getId();
                        double distance=distance(position,capteur.getPosition());
                        //if the position in the sensor range
                        if(distance<=capteur.getRange()) {

                            if(i==0)
                            {
                                distance_min=distance;
                                idmin=id;
                            }
                            else{
                                if(distance< distance_min)
                                {
                                    distance_min=distance;
                                    idmin=id;
                                }
                            }

                        }
                    }
                }
            }
        }
        return idmin;
    }
    //this method for send reponse
    public void jfhjh(String idmin1) throws IOException {
        DataOutputStream dout=new DataOutputStream(s.getOutputStream());
        String reponse;

        // if no sensor capable of serving
        if(idmin1==null)
            reponse="no sensor capable of serving you.";
        else{
            reponse="sensor="+idmin1;}
        dout.writeUTF(reponse);
        dout.flush();
    }
    public void run(){
        while(true){
            try {
                idmin=SearchIdSensor();
                jfhjh(idmin);
            } catch (IOException e) {
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

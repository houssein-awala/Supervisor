import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class connectSink extends Thread {

    ServerSocket connectDonner;
    DataInputStream din ;
    DataOutputStream dout;
    int type;
    String positionx;
    String positiony;
    Point position;
    int x,y;
    String idmin;
    ArrayList<Descriptor> capteurs ;

    public connectSink(ServerSocket connectDonner) {
        this.connectDonner=connectDonner;
    }
    public void run(){
        while(true){
            try {
                Socket s=connectDonner.accept();
                din = new DataInputStream(s.getInputStream()); // TODO Auto-generated catch block
                dout=new DataOutputStream(s.getOutputStream());
                String msgin="";
                while((msgin=din.readUTF())!=null)
                {
                    String tab[];
                    tab = msgin.split("-");

                    type=Integer.parseInt(tab[1]);
                    positionx=tab[2];
                    positiony=tab[3];
                    x=Integer.parseInt(positionx);
                    y=Integer.parseInt(positiony);
                    position.x=x;
                    position.y=y;
                    double min=0;
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
                                        min=distance;
                                        idmin=id;
                                    }
                                    else{
                                        if(distance<min)
                                        {
                                            min=distance;
                                            idmin=id;
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
                String reponse;
                if(idmin==null)
                 reponse="no sensor capable of serving you.";
                else{
                reponse="sensor="+idmin;}
                dout.writeUTF(reponse);
                dout.flush();
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

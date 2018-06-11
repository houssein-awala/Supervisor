import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class connectSink extends Thread {

    ServerSocket connectDonner;
    ObjectInputStream ois ;
    DataInputStream din ;
    DataOutputStream dout;
    BufferedReader br;
    int type;
    String positionx;
    String positiony;
    Point position;
    int x,y;
    ArrayList<Descriptor> capteurs ;
    ArrayList<Distance> capteurvoisins;
    // int[][] distance;
    public connectSink(ServerSocket connectDonner) {
        this.connectDonner=connectDonner;
    }
    public void run(){
        while(true){
            try {
                Socket s=connectDonner.accept();
                ois=new ObjectInputStream(s.getInputStream());
                din = new DataInputStream(s.getInputStream()); // TODO Auto-generated catch block
                dout=new DataOutputStream(s.getOutputStream());
                br=new BufferedReader(new InputStreamReader(System.in));
                String msgin="",msgout="";
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
                    for ( Descriptor capteur :capteurs)
                    {
                        if(capteur.getType()==type)
                        {
                            if(capteur.getCapacity()<capteur.getNbRequest())
                            {
                                String id=capteur.getId();
                                double distance=distance(position,capteur.getPosition());
                                Distance e=new Distance(id,distance);
                                capteurvoisins.add(e);
                            }
                        }
                    }
                }
                double min=0;
                String idmin=null;
                int i=0;
                for(Distance dist:capteurvoisins)
                {
                    if(i==0)
                    {
                        idmin=dist.getId();
                        min=dist.getDistance();
                    }
                    else{
                        if(dist.getDistance()<min)
                        {
                            min=dist.getDistance();
                            idmin=dist.getId();
                        }
                    }
                }
                String reponse="sensor="+idmin;
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
        return Math.sqrt((a.getX()-b.getX()) + (a.getY()-b.getY()));
    }

    private static class Distance {

        double distance;
        String id;
        public Distance( String id,double distance) {
            this.distance=distance;
            this.id=id;
        }
        public void setDistance(double distance)
        {
            this.distance=distance;
        }
        public double getDistance()
        {
            return distance;
        }
        public void setId(String Id)
        {
            this.id=Id;
        }
        public String getId()
        {
            return id;
        }
    }

}

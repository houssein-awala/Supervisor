import com.sun.org.apache.xml.internal.utils.SystemIDResolver;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.*;
import static javax.script.ScriptEngine.FILENAME;
import javax.swing.table.DefaultTableModel;
/*
@Author Ghina Saad
*/
public class Interface extends DefaultTableModel {



    // HashMap<String,Descriptor> Descriptors=supervisionParms.getDescriptors();
    private ISensor sensor;
    private SupervisionParms parms;
    private JTable jt;
    private DefaultTableModel model;
    private String data[][];
    private String [] column={"ID", "TYPE","CAPACITY","STATE","NBREQUEST","REQUEST","RANGE","SERVICE","POSITIONx","POSITIONy"};
    JFrame f;
    JButton jbt1 = new JButton("AddSensor");
    JButton jbt2 = new JButton("Edit");
    JButton jbt3 = new JButton("Delette");
    JButton jbt4 = new JButton("RequestSink");
    JButton jbt5 = new JButton("Execute File");
    static int row;
    String id;
    int typee,positionX,positionY,state,capacity,range,nbrequest,service;
    Point position;
    Interface() {


        f = new JFrame("simulation");

     //   String data[][] = {{"12","6567","667","566","657","","","","332","434"},{"13","","","","","","","","",""}};
        data=remplir_table();
        JPanel panel=new JPanel();
        JPanel p1=new JPanel();


       // column = {"ID", "TYPE","CAPACITY","STATE","NBREQUEST","REQUEST","RANGE","SERVICE","POSITIONx","POSITIONy"};
        model = new DefaultTableModel(data, column);
        jt = new JTable( model );

        //Action nb3t lal sink
        ActionListener ActionRequestSink=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //nb3t lal sink
            }
        };
        //Action on the request sink
        ActionListener requestSink =new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame=new JFrame();
                JDialog d=new JDialog(frame,"Request",true);
                d.setSize(400,400);
                d.setLayout(null);

                String TypeData[]={" TEMPERATURE","HUMIDITY","IMAGE","SPEED","DIRECTION"};
                JComboBox comboBox =new JComboBox(TypeData);
                comboBox.setBounds(80,0,200,20);
                d.add(comboBox);
                JTextField positiont=new JTextField();
                positiont.setBounds(80,50,200,20);
                d.add(positiont);
                JLabel typeOfData =new JLabel(" typeSensor:");
                typeOfData.setBounds(0,0,80,10);
                d.add(typeOfData);
                JLabel position =new JLabel(" position:");
                position.setBounds(0,50,80,10);
                d.add(position);
                JButton buttonadd =new JButton("send");
                buttonadd.setBounds(0,100,80,23);
                d.add(buttonadd);
                JButton b1=new JButton("cancel");
                b1.setBounds(70,100,80,23);
                d.add(b1);
                //action on the button add
                buttonadd.addActionListener(ActionRequestSink);
                d.setVisible(true);
            }
        };

        //action for delete sensor
        ActionListener delete=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jt.setCellSelectionEnabled(true);
                DefaultTableModel model=   (DefaultTableModel) jt.getModel() ;
                String Data = null;
                int[] row = jt.getSelectedRows();
                int[] columns = jt.getSelectedColumns();
                for (int i = 0; i < row.length; i++) {
                    Data = (String) jt.getValueAt(row[i],0);
                    if(Data==id)
                    {
                        System.out.println("Table element selected is: " + Data);
                        jt.remove(i);
                        model.removeRow(i);
//                        jt.getSelectionModel().clearSelection();
                    }
                }
                sensor.deleteSensor(id);
            }
        };
        ActionListener ActionEditensor=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sensor.editSensor(id,typee,position);
            }
        };
        // action pour edit sensor
        ActionListener editSensor=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f1=new JFrame();

                JDialog d=new JDialog(f1,"edit sensor",true);
                d.setSize(400,400);
                JLabel type=new JLabel("type:");
                type.setBounds(0,0,80,20);
                d.setLayout(null);
                d.add(type);
                JLabel positionx=new JLabel("positionx:");
                positionx.setBounds(0,20,80,20);
                JLabel positiony=new JLabel("positiony:");
                positiony.setBounds(0,40,80,20);
                d.add(positionx);
                d.add(positiony);
                DefaultTableModel model = (DefaultTableModel)jt.getModel();
                // get the selected row index
                int selectedRowIndex = jt.getSelectedRow();
                row=selectedRowIndex;
                String TypeSensor[]={"Base","Router"};
                id= model.getValueAt(selectedRowIndex, 0).toString();
               typee= Integer.parseInt(model.getValueAt(selectedRowIndex, 1).toString());
                positionX= Integer.parseInt(model.getValueAt(selectedRowIndex, 8).toString());
                positionY= Integer.parseInt(model.getValueAt(selectedRowIndex, 9).toString());
                position.x=positionX;
                position.y=positionY;
                JTextField typeet=new JTextField(typee);
                typeet.setBounds(80,0,200,20);
                d.add(typeet);
                JTextField positiontx=new JTextField(positionX);
                positiontx.setBounds(80,20,200,20);
                d.add(positiontx);
                JTextField positionty=new JTextField(positionY);
                positionty.setBounds(80,40,200,20);
                d.add(positionty);
                JButton buttonedit =new JButton("edit");
                buttonedit.setBounds(0,200,60,23);
                d.add(buttonedit);
                JButton b1=new JButton("cancel");
                b1.setBounds(70,200,80,23);
                d.add(b1);
                //action on the button edit
                buttonedit.addActionListener(ActionEditensor);

                d.setVisible(true);
            }
        };
        // action pour ouvrir un fenetre supprimer
        ActionListener printAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(row != -1) {
                    System.out.println("hellooooo"+row);
                    DefaultTableModel model = (DefaultTableModel)jt.getModel();
                    model.removeRow(row);
                }

            }
        };
        // action pour ajouter un sensor
        ActionListener ActionAddSensor=new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //    AddSensor();
            }
        };
        //action quand on clique sur add sensor
        ActionListener printAction1 = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame f1=new JFrame();

                JDialog d=new JDialog(f1,"Add sensor",true);
                d.setSize(400,400);
                JLabel type=new JLabel("type:");
                type.setBounds(0,0,80,20);
                d.setLayout(null);
                d.add(type);
                JLabel service=new JLabel("service:");
                service.setBounds(0,20,80,20);
                JLabel capacity=new JLabel("capacity:");
                capacity.setBounds(0,40,80,20);

                JLabel range=new JLabel("range:");
                range.setBounds(0,60,80,20);
                JLabel position=new JLabel("position:");
                position.setBounds(0,80,80,20);


                JLabel typesensor=new JLabel("typeSensor:");
                typesensor.setBounds(0,100,80,20);
                d.add(service);
                d.add(capacity);
                d.add(range);
                d.add(position);
                d.add(typesensor);

                String TypeSensor[]={"Base","Router"};
                JComboBox comboBox =new JComboBox(TypeSensor);
                comboBox.setBounds(80,100,200,20);
                d.add(comboBox);
                JTextField typet=new JTextField();
                typet.setBounds(80,0,200,20);
                d.add(typet);
                JTextField servicet=new JTextField();
                servicet.setBounds(80,20,200,20);
                d.add(servicet);
                JTextField capacityt=new JTextField();
                capacityt.setBounds(80,40,200,20);
                d.add(capacityt);

                JTextField ranget=new JTextField();
                ranget.setBounds(80,60,200,20);
                d.add(ranget);
                JTextField positiont=new JTextField();
                positiont.setBounds(80,80,200,20);
                d.add(positiont);
                JButton buttonadd =new JButton("add");
                buttonadd.setBounds(0,200,60,23);
                d.add(buttonadd);
                JButton b1=new JButton("cancel");
                b1.setBounds(70,200,80,23);
                d.add(b1);
                //action on the button add
                buttonadd.addActionListener(ActionAddSensor);

                d.setVisible(true);
            }
        };
        //action quand on select une ligne du table

        /*jt.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                // do some actions here, for example
                // print first column value from selected row
                row = jt.getSelectedRow();
                System.out.println("row="+row);

             //   jbt3.setEnabled(true);
                id = Integer.parseInt(jt.getValueAt(jt.getSelectedRow(), 0).toString());
                capacity=Integer.parseInt(jt.getValueAt(jt.getSelectedRow(), 2).toString());
                type=Integer.parseInt(jt.getValueAt(jt.getSelectedRow(), 1).toString());
                service= Integer.parseInt(jt.getValueAt(jt.getSelectedRow(), 7).toString());
                 positionX=Integer.parseInt(jt.getValueAt(jt.getSelectedRow(), 8).toString());
                 positionY=Integer.parseInt(jt.getValueAt(jt.getSelectedRow(), 9).toString());
                  state=Integer.parseInt(jt.getValueAt(jt.getSelectedRow(), 3).toString());
                  nbrequest=Integer.parseInt(jt.getValueAt(jt.getSelectedRow(), 4).toString());
                  request=  Integer.parseInt(jt.getValueAt(jt.getSelectedRow(), 5).toString());
                  range=Integer.parseInt(jt.getValueAt(jt.getSelectedRow(), 6).toString());
                System.out.println(jt.getValueAt(jt.getSelectedRow(), 0).toString());
            }
        });*/
        // action quand on clique sur executefile
        ActionListener executefile =new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ExecuteFile();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        };
        JScrollPane sp = new JScrollPane(jt);

        panel.add(sp,BorderLayout.EAST);

        panel.setBounds(1,1,500,800);

        panel.setBackground(Color.CYAN);

        p1.setBackground(Color.blue);
        p1.setSize(300,500);

        jbt1.setPreferredSize(new Dimension(100,50));
        jbt1.addActionListener(printAction1);

        jbt2.addActionListener(editSensor);
        jbt3.setPreferredSize(new Dimension(100,50));
       // jbt3.setEnabled(false);
        jbt3.addActionListener( printAction);
        jbt4.addActionListener(requestSink);
        jbt5.addActionListener(executefile);
        Box box = Box.createVerticalBox();
        box.add(jbt1);
        box.add(Box.createVerticalStrut(10));
        box.add(jbt2);
        box.add(Box.createVerticalStrut(10));
        box.add(jbt3);
        box.add(Box.createVerticalStrut(10));
        box.add(jbt4);
        box.add(Box.createVerticalStrut(10));
        box.add(jbt5);
        p1.add(box);

        f.add(panel,BorderLayout.LINE_START);

        f.add(p1,BorderLayout.CENTER);
        f.setSize(800, 500);
        f.setVisible(true);
    }

    public void AddSensor(int type,int service,Point position,int capacity,String typesensor,int range )
    {

    }


    public void ExecuteFile() throws FileNotFoundException {
        //BufferedReader br = null;
        //  FileReader fr = null;
        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\LENOVO\\Desktop\\simulator.txt"));
        try {

            //br = new BufferedReader(new FileReader(FILENAME));
            // FileReader  fr = new FileReader("simulator.txt");
            //     br = new BufferedReader(fr);

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                String [] tab=sCurrentLine.split(":");
                System.out.println("hiiiii");
                if(tab[0]=="create sensor")
                {int type=0,service=0,positionx,positiony,range=0,capacity=0;
                    String typesensor="";
                    Point position = null;
                    positionx=position.x;
                    positiony=position.y;
                    String [] tab1=tab[1].split(",");
                    for(int i=0;i<tab1.length;i++)
                    {
                        String[] tab2=tab[i].split("=");
                        if(tab2[0]=="positionx") {
                            positionx=Integer.parseInt(tab2[1]);
                        }
                        if(tab2[0]=="positiony")
                        {
                            positiony=Integer.parseInt(tab2[1]);
                        }
                        if(tab2[0]=="range")
                        {
                            range=Integer.parseInt(tab2[1]);
                        }
                        if(tab2[0]=="service")
                        {
                            service=Integer.parseInt(tab2[1]);
                        }
                        if(tab2[0]=="capacity")
                        {
                            capacity=Integer.parseInt(tab2[1]);
                        }
                        if(tab2[0]=="typesensor")
                            typesensor=tab2[1];
                        if(tab2[0]=="type")
                            type=Integer.parseInt(tab2[1]);
                    }

                    AddSensor(type,service,position,capacity,typesensor,range);
                }
                else
                if(tab[0]=="edit sensor")
                {
                  //  Edit();
                }
                else
                if(tab[0]=="delete sensor")
                {
                    String [] tab1=tab[1].split(",");
                    for(int i=0;i<tab1.length;i++) {
                        String[] tab2 = tab[i].split("=");
                        if(tab2[0]=="id") {
                            id = tab2[1];
                            break;
                        }
                    }
                    sensor.deleteSensor(id);
                }
                else{}
            }

        } catch (IOException e) {

            e.printStackTrace();

        }
        finally {

            try {

                if (br != null)
                    br.close();

             /*   if (fr != null)
                    fr.close();*/

            } catch (IOException ex) {

                ex.printStackTrace();

            }
        }

    }
    //get data descriptor for remplir jtable
    public String[][] remplir_table()
    {
        int j=0;
        String[][] data = new String[0][];
        for (Map.Entry<String,Descriptor> entry :parms.getDescriptors().entrySet()) {

                data[j][0]=entry.getValue().getId();
            data[j][1]= String.valueOf(entry.getValue().getType());
            data[j][2]= String.valueOf(entry.getValue().getCapacity());
            data[j][3]= String.valueOf(entry.getValue().getState());
            data[j][4]= String.valueOf(entry.getValue().getNbRequest());
            data[j][5]= String.valueOf(entry.getValue().getRequest());
            data[j][6]= String.valueOf(entry.getValue().getRange());
            data[j][7]= String.valueOf(entry.getValue().getService());
            Point p=entry.getValue().getPosition();
            data[j][8]= String.valueOf(p.getX());
            data[j][9]= String.valueOf(p.getY());
            j++;
        }
        return data;
    }
    //update jtable
    public void updateTable(String[][] data,JTable jt,String [] colomn,  DefaultTableModel model)
    {
         model = new DefaultTableModel(data, column);
        jt = new JTable( model );
    }
    public static void main(String[] args) {

        new Interface();
    }
}


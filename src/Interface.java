import com.sun.org.apache.xml.internal.utils.SystemIDResolver;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.*;
import static javax.script.ScriptEngine.FILENAME;
import javax.swing.table.DefaultTableModel;
/*
@Author Ghina Saad
*/
public class Interface extends DefaultTableModel {


    private ISensor sensor;
    private IFactory factory;
    private SupervisionParms parms=new SupervisionParms() ;
    private JTable jt;
    private DefaultTableModel model;
    private String[][] data;
    private String [] column={"ID", "TYPE","CAPACITY","STATE","NBREQUEST","REQUEST","RANGE","SERVICE","POSITIONx","POSITIONy"};
    JFrame f;
    JButton jbt1 = new JButton("AddSensor");
    JButton jbt2 = new JButton("Edit");
    JButton jbt3 = new JButton("Delette");
    JButton jbt4 = new JButton("RequestSink");
    JButton jbt5 = new JButton("Execute File");
    JTextField typet;
    JTextField positiontx;
    JTextField positionty;
    JTextField statet;
    static int row;
    String id=null,Typesensor;
    int typee,positionX,positionY,State,Capacity,Nbrequest,Service;
    Double Range;
    Point position;
    Interface() {


        f = new JFrame("simulation");

         data = new String[][]{{"12", "6567", "667", "566", "657", "56", "33", "22", "332", "434"}, {"13", "", "", "", "", "", "", "", "", ""}};
        JPanel panel=new JPanel();
        JPanel p1=new JPanel();



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
                JLabel state=new JLabel("state:");
                state.setBounds(0,60,80,20);
                d.add(state);
                DefaultTableModel model = (DefaultTableModel)jt.getModel();
                // get the selected row index
                int selectedRowIndex = jt.getSelectedRow();
                row=selectedRowIndex;
                String t = null;
                String x=null;
                String y=null;
                String s=null;
                if(row!=-1) {
                    System.out.println("hiiiiiiii");
                    String TypeSensor[] = {"Base", "Router"};
                    id = model.getValueAt(selectedRowIndex, 0).toString();
                     t= (model.getValueAt(selectedRowIndex, 1).toString());
                     x = (model.getValueAt(selectedRowIndex, 8).toString());
                     y = (model.getValueAt(selectedRowIndex, 9).toString());
                     s=(model.getValueAt(selectedRowIndex,3).toString());
                }
               JTextField typett=new JTextField(t);
                typett.setBounds(80,0,200,20);
                d.add(typett);
               positiontx=new JTextField(x);
                positiontx.setBounds(80,20,200,20);
                d.add(positiontx);
                positionty=new JTextField(y);
                positionty.setBounds(80,40,200,20);
                d.add(positionty);
                statet=new JTextField(s);
                statet.setBounds(80,60,200,20);
                d.add(statet);
                JButton buttonedit =new JButton("edit");
                buttonedit.setBounds(0,200,60,23);
                d.add(buttonedit);
                JButton b1=new JButton("cancel");
                b1.setBounds(70,200,80,23);
                d.add(b1);
                buttonedit.addActionListener( new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                typee=Integer.parseInt(typett.getText().toString());
                positionX=Integer.parseInt(positiontx.getText().toString());
                positionY=Integer.parseInt(positionty.getText().toString());
                State=Integer.parseInt(statet.getText().toString());
                position=new Point(positionX,positionY);
                        System.out.println(typee  );
                        System.out.println(positionX);
                        f1.setVisible(false);
                        sensor.editSensor(id,typee,position,State);

                       d.setVisible(false);

                    }
                });
                d.setVisible(true);


            }
        };
        // action for delete sensor
        ActionListener printAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(row != -1) {
                    id= (String) jt.getValueAt(row,0);
                    System.out.println("hellooooo"+row);
                    DefaultTableModel model = (DefaultTableModel)jt.getModel();
                    model.removeRow(row);

                }
                sensor.deleteSensor(id);
                data=fill_table();
                 updateTable(data,jt,column,model);
            }
        };

        //action quand on clique sur add sensor
        ActionListener printAction1 = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame f1=new JFrame();

                JDialog d=new JDialog(f1,"Add sensor",true);
                d.setSize(400,400);

                d.setLayout(null);
                JLabel service=new JLabel("service:");
                service.setBounds(0,20,80,20);
                JLabel capacity = new JLabel("capacity:");
                capacity.setBounds(0,40,80,20);

                JLabel range = new JLabel("range:");
                range.setBounds(0,60,80,20);
                JLabel positionx=new JLabel("positionx:");
                positionx.setBounds(0,80,80,20);
                JLabel positiony=new JLabel("positiony:");
                positiony.setBounds(0,120,80,20);

                JLabel typesensor=new JLabel("typeSensor:");
                typesensor.setBounds(0,100,80,20);
                d.add(service);
                d.add(capacity);
                d.add(range);
                d.add(positionx);
                d.add(positiony);
                d.add(typesensor);

                String TypeSensor[]={"Base","Router"};
                JComboBox comboBox =new JComboBox(TypeSensor);
                comboBox.setBounds(80,100,200,20);
                d.add(comboBox);
                JTextField servicet=new JTextField();
                servicet.setBounds(80,20,200,20);
                d.add(servicet);
                JTextField capacityt=new JTextField();
                capacityt.setBounds(80,40,200,20);
                d.add(capacityt);
                JTextField ranget=new JTextField();
                ranget.setBounds(80,60,200,20);
                d.add(ranget);
                JTextField positiontx=new JTextField();
                positiontx.setBounds(80,80,200,20);
                d.add(positiontx);
                JTextField positionty=new JTextField();
                positionty.setBounds(80,120,200,20);
                d.add(positionty);
                JButton buttonadd =new JButton("add");
                buttonadd.setBounds(0,200,60,23);
                d.add(buttonadd);
                JButton b1=new JButton("cancel");
                b1.setBounds(70,200,80,23);
                d.add(b1);
                //action on the button add
                buttonadd.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        Range= Double.valueOf((ranget.getText().toString()));
                        Capacity=Integer.parseInt(capacityt.getText().toString());
                        Service=Integer.parseInt(servicet.getText().toString());
                        positionX=Integer.parseInt(positiontx.getText().toString());
                        positionY=Integer.parseInt(positionty.getText().toString());
                        position=new Point(positionX,positionY);
                        Typesensor=comboBox.getSelectedItem().toString();
                        int type_sensor;
                        if(Typesensor=="Base")
                        {
                             type_sensor=1;
                        }
                        else{
                             type_sensor=2;
                        }
                        System.out.println(typee);
                        try {
                            factory=(IFactory) Naming.lookup("rmi://127.0.0.1:1199/Cal");
                        } catch (NotBoundException e1) {
                            e1.printStackTrace();
                        } catch (MalformedURLException e1) {
                            e1.printStackTrace();
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            factory.createSensor(type_sensor,position,Service,Capacity,Range);
                        } catch (RemoteException e1) {
                            e1.printStackTrace();
                        }
                        data=fill_table();
                        updateTable(data,jt,column,model);

                    }
                });

                d.setVisible(true);
            }
        };
        //action quand on select une ligne du table
        jt.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                                                            public void valueChanged(ListSelectionEvent event) {
                                                                jbt2.setEnabled(true);
                                                                jbt3.setEnabled(true);
                                                            }});


        // action quand on clique sur executefile
        ActionListener executefile =new ActionListener() {
            @Override
            //Handle open button action.
            public void actionPerformed(ActionEvent e) {

                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

                int returnValue = jfc.showOpenDialog(null);
                // int returnValue = jfc.showSaveDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jfc.getSelectedFile();
                    System.out.println(selectedFile.getAbsolutePath());
                    String nom_file=selectedFile.getAbsolutePath();
                    try {
                        ExecuteFile(nom_file);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }

            }};
        JScrollPane sp = new JScrollPane(jt);
        sp.setPreferredSize(new Dimension(700,900));
        panel.add(sp);

        panel.setBounds(1,1,700,800);

        panel.setBackground(Color.CYAN);

        p1.setBackground(Color.blue);
        p1.setSize(300,500);

        jbt1.setPreferredSize(new Dimension(100,50));
        jbt1.addActionListener(printAction1);

        jbt2.addActionListener(editSensor);
        jbt2.setEnabled(false);
        jbt3.setPreferredSize(new Dimension(100,50));
        jbt3.setEnabled(false);
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
       // f.setSize(800, 500);
        f.setSize(1000,1000);
        panel.setBounds(0,0,700,1000);
        p1.setBounds(700,0,300,1000);
        f.setVisible(true);
    }


    public void ExecuteFile(String nom_file) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(nom_file));
        try {

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                System.out.println(sCurrentLine);
                String [] tab=sCurrentLine.split(":");
                System.out.println("hiiiii");
                if(tab[0]=="create sensor")
                {int type=0,service=0,positionx=0,positiony=0,range=0,capacity=0;
                        String typesensor=null;
                    Point position = null;
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
                        position=new Point(positionx,positiony);
                    }

                    factory=(IFactory) Naming.lookup("rmi://127.0.0.1:1199/Cal");
                    factory.createSensor(typee,position,Service,Capacity,Range);
                   data=fill_table();
                    updateTable(data,jt,column,model);
                }
                else
                if(tab[0]=="edit sensor")
                {
                    String [] tab1=tab[1].split(",");
                    String id=null;
                    int type=0,positionx=0,positiony=0,state=0;
                    for(int i=0;i<tab1.length;i++) {
                        String[] tab2 = tab[i].split("=");
                        if(tab2[0]=="positionx")
                            positionx=Integer.parseInt(tab2[1]);
                        if(tab2[0]=="type")
                            type=Integer.parseInt(tab2[1]);
                        if(tab2[0]=="positiony")
                            positiony=Integer.parseInt(tab2[1]);
                        if(tab2[0]=="state")
                            state=Integer.parseInt(tab2[1]);
                        if(tab2[0]=="id")
                            id=tab2[1];
                    }
                    Point positionn =new Point(positionx,positiony);
                    sensor.editSensor(id,typee,positionn,State);
                       data=fill_table();
                     updateTable(data,jt,column,model);

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
                    data=fill_table();
                    updateTable(data,jt,column,model);
                }
                else{
                    System.out.println("gffd");
                }
            }

        } catch (IOException e) {

            e.printStackTrace();

        } catch (NotBoundException e) {
            e.printStackTrace();
        } finally {

            try {

                if (br != null)
                    br.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }
        }

    }
    //get data descriptor for remplir jtable
    public String[][] fill_table()
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


import javax.swing.*;

public class Panel extends JPanel {
    JPanel p1;
    Panel(){
        p1=new JPanel();
        JTextArea ta=new JTextArea(20,20);
        JButton button =new JButton();
        p1.add(ta);
        p1.setVisible(true);
    }
}

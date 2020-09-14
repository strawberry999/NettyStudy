package NettyStudy.v10;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientFrame extends Frame {

    public static final ClientFrame INSTANCE = new ClientFrame();

    TextArea ta = new TextArea();
    TextField tf = new TextField();

    Client client = null;


    public ClientFrame(){
        this.setSize(600,460);
        this.setLocation(100,200);
        this.add(ta,BorderLayout.CENTER);
        this.add(tf,BorderLayout.SOUTH);
        tf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.send(tf.getText());
                //把字符串发送到服务器
//                ta.setText(ta.getText()+tf.getText());
                tf.setText("");
            }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                client.closeWindow();
                System.exit(0);
            }
        });

//        this.setVisible(true);
//        connectToServer();
    }

    private void connectToServer(){
        client = new Client();
        client.connect();
    }

    public static void main(String[] args) {
        ClientFrame clientFrame = ClientFrame.INSTANCE;
        clientFrame.setVisible(true);
        clientFrame.connectToServer();
    }

    public void updateFrame(String serverReturnMsg) {
        this.ta.setText(ta.getText()+System.getProperty("line.separator")+serverReturnMsg);
    }
}

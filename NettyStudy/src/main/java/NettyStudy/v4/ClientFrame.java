package NettyStudy.v4;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientFrame extends Frame {

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
                //���ַ������͵�������
                ta.setText(ta.getText()+tf.getText());
                tf.setText("");
            }
        });
        this.setVisible(true);

        connectToServer();
    }

    private void connectToServer(){
        client = new Client();
        client.connect();
    }

    public static void main(String[] args) {
        new ClientFrame();
    }
}

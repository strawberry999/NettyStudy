package NettyStudy.v10;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ServerFrame extends Frame {

    public static final ServerFrame INSTANCE = new ServerFrame();
    Server server = new Server();
    TextArea taLeft = new TextArea();
    TextArea taRight = new TextArea();

    private ServerFrame(){
        this.setSize(800,500);
        this.setLocation(500,100);
        Panel panel = new Panel(new GridLayout(1,2));
        panel.add(taLeft);
        panel.add(taRight);
        this.add(panel);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

    }

    public static void main(String[] args) {
        ServerFrame.INSTANCE.setVisible(true);
        ServerFrame.INSTANCE.server.startServer();
    }

    public void updateServerMsg(String msg) {
        this.taLeft.setText(taLeft.getText()+System.getProperty("line.separator")+msg);
    }

    public void updateClientMsg(String accepted) {
        this.taRight.setText(taRight.getText()+System.getProperty("line.separator")+accepted);
    }
}

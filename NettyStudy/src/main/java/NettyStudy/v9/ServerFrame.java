package NettyStudy.v9;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ServerFrame extends Frame {

    public static final ServerFrame INSTANCE = new ServerFrame();
    Server server = new Server();
    Button button = new Button("start");
    TextArea taLeft = new TextArea();
    TextArea taRight = new TextArea();

    private ServerFrame(){
        this.setSize(800,500);
        this.setLocation(500,100);
        this.add(button,BorderLayout.NORTH);
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

        this.button.addActionListener((e)->{
            server.startServer();
        });
    }

    public static void main(String[] args) {
        ServerFrame.INSTANCE.setVisible(true);
    }

    public void updateServerMsg(String msg) {
        this.taLeft.setText(taLeft.getText()+System.getProperty("line.separator")+msg);
    }
}

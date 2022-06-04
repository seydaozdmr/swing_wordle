package A_Giris.service;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Client extends JFrame {
    public static Socket socket;

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Client client=new Client();
                client.setVisible(true);
            }
        });
        try {
            socket=new Socket("localhost",1234);
            System.out.println("connected server");
        } catch (IOException e) {
            System.out.println("bağlantı kurulamadı");
            e.printStackTrace();
        }
    }

    public Client(){
        setTitle("Client");
        setSize(800 ,800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(null);
        getData();
    }

    private static void getData() {
        JLabel[] result=new JLabel [25];
        DataInputStream dIn = null;
        try {
            dIn = new DataInputStream(socket.getInputStream());
            int length = dIn.readInt();                    // read length of incoming message
            if(length>0) {
                byte[] message = new byte[length];
                dIn.readFully(message, 0, message.length);
                System.out.println(message);// read the message
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



}

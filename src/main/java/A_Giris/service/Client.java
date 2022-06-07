package A_Giris.service;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Client extends JFrame {
    public static Socket socket;
    private static JLabel [] myArray =new JLabel[25];

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
            getData();
        } catch (IOException e) {
            System.out.println("bağlantı kurulamadı");
            e.printStackTrace();
        }
    }

    public Client(){
        ControlService controlService=new ControlService();
        setTitle("Client");
        setSize(800 ,800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Point labelsStartPoint=new Point(200,10);
        myArray = controlService.createLabelsForKeyBoard(this,labelsStartPoint);
        setLayout(null);

    }

    private static void getData() {
        Thread t1=new Thread(new Runnable() {
            @Override
            public void run() {
                DataInputStream dIn=null;
                {
                    try {
                        dIn = new DataInputStream(socket.getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                while (socket.isConnected()) {
                    try {
                        byte[] message;
                        int length = dIn.readInt();
                        System.out.println("mesaj uzunluğu: " + length);
                        message = new byte[length];
                        dIn.readFully(message);
                        for (int i = 0; i < message.length; i++) {
                            if(myArray[1]!=null && message[i]!=32){
                                myArray[i].setText(String.valueOf((char)message[i]));
                            }
                        }
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        t1.start();
    }



}

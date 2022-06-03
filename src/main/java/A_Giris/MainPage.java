package A_Giris;

import A_Giris.game.*;
import A_Giris.model.GameControls;
import A_Giris.model.User;
import A_Giris.service.GameService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPage extends JFrame{


    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainPage mainPage=new MainPage();
                mainPage.setVisible(true);
            }
        });


    }

    public MainPage(){
        setTitle("Wordle Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500,500,700,500);



        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(5,5,5,5));

        setContentPane(panel);
        panel.setLayout(null);

        JTextField userName= new JFormattedTextField();
        userName.setBounds(150,220,250,30);
        panel.add(userName);

        JTextField userName2= new JFormattedTextField();
        userName2.setBounds(150,290,250,30);
        userName2.setEditable(false);
        panel.add(userName2);


        //TODO fare klavye
        JRadioButton rb1=new JRadioButton("Fare",true);
        rb1.setActionCommand("f");
        rb1.setBounds(150,100,250,20);
        JRadioButton rb2=new JRadioButton("Klavye");
        rb2.setActionCommand("k");
        rb2.setBounds(150,120,250,20);
        //TODO single - multiplayer
        JRadioButton rbSingle= new JRadioButton("Single Player",true);
        rbSingle.setActionCommand("s");
        rbSingle.setBounds(150,140,250,20);
        rbSingle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userName2.setEditable(false);
            }
        });
        JRadioButton rbMultiPlayer= new JRadioButton("MultiPlayer");
        rbMultiPlayer.setActionCommand("m");
        rbMultiPlayer.setBounds(150,160,250,20);
        rbMultiPlayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userName2.setEditable(true);
            }
        });
        ButtonGroup bg2= new ButtonGroup();
        bg2.add(rbSingle);
        bg2.add(rbMultiPlayer);


        JLabel o1=new JLabel("1- Oyuncu İsmi: ");
        o1.setBounds(150,190,250,20);
        JLabel o2=new JLabel("2- Oyuncu İsmi: ");
        o2.setBounds(150,260,250,20);

        panel.add(o1);
        panel.add(o2);



        ButtonGroup bg= new ButtonGroup();
        bg.add(rb1);
        bg.add(rb2);

        JButton button=new JButton("Başla");
        button.setBounds(150,340,100,30);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User gameUser=new User();
                User gameUser2=new User();
                if(userName.getText().length()==0){
                    GameService.showWarning("kullanıcı adı boş olamaz");
                }else{
                    if( bg2.getSelection().getActionCommand().charAt(0)=='m' && userName2.getText().length()==0){
                        GameService.showWarning("kullanıcı adı boş olamaz");
                    }else {
                        gameUser.setUserName(userName.getText());
                        gameUser.setScore(500);
                        gameUser2.setUserName(userName2.getText());
                        gameUser.setScore(500);

                        if(bg.getSelection().getActionCommand().charAt(0)=='f'){
                            gameUser.setGameControls(GameControls.MOUSE);
                        }else{
                            gameUser.setGameControls(GameControls.KEYBOARD);
                        }
                        //TODO Oynama şekli klavye fare belirlenecek?
                        WordlGameFactory factory=new WordleGameFactoryWithKeyboard();
                        WordlGameFactory mauseFactory=new WordleGameFactoryWithMouse();
                        if(gameUser.getGameControls()==GameControls.KEYBOARD && bg2.getSelection().getActionCommand().charAt(0)=='s'){
                            WordleGameWithKeyboard wordleGameWithKeyboard = (WordleGameWithKeyboard) factory.createSingleWordleGame(gameUser);
                            wordleGameWithKeyboard.setVisible(true);
                        }else if(gameUser.getGameControls()==GameControls.MOUSE && bg2.getSelection().getActionCommand().charAt(0)=='s'){
                            WordleGameWithMouse wordleGameWithMouse=(WordleGameWithMouse) mauseFactory.createSingleWordleGame(gameUser);
                            wordleGameWithMouse.setVisible(true);
                        }else if(gameUser.getGameControls()==GameControls.KEYBOARD && bg2.getSelection().getActionCommand().charAt(0)=='m'){
                            MultiplayerWordleGameWithKeyboard multiplayerWordleGameWithKeyboard=(MultiplayerWordleGameWithKeyboard) factory.createMultiplayerWordleGame(gameUser,gameUser2);
                            multiplayerWordleGameWithKeyboard.setVisible(true);
                        }
                        //new WordleGame(gameUser);

                        dispose();
                    }

                }


            }
        });
        JLabel photo = new JLabel(new ImageIcon("images.png"));
        photo.setBounds(0,0,500,100);
        panel.add(photo);

        panel.add(button);

        panel.add(rb1);
        panel.add(rb2);
        panel.add(rbSingle);
        panel.add(rbMultiPlayer);
//        f.setSize(400,500);
//        f.setLayout(null);
//        f.setVisible(true);
    }
}

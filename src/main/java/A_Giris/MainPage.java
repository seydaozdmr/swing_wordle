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

//TODO burası ana ekran, JFrame
public class MainPage extends JFrame{

    public static void main(String[] args) {
        //TODO kuyruğa bu runnable'ı atıyor
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainPage mainPage = new MainPage();
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

        //Jframe'e paneli veriyorum
        setContentPane(panel);
        panel.setLayout(null);

        //TODO iki tane kullanıcı adı alıyorum (single'da bir tane)
        JTextField userName= new JFormattedTextField();
        userName.setBounds(150,230,250,30);
        panel.add(userName);

        JTextField userName2= new JFormattedTextField();
        userName2.setBounds(150,300,250,30);
        userName2.setEditable(false);
        panel.add(userName2);

        //TODO fare ya da klavye ile nasıl oynanacaksa o seçiliyor
        JRadioButton rb1=new JRadioButton("Fare",true);
        rb1.setActionCommand("f");
        rb1.setBounds(150,100,250,20);
        JRadioButton rb2=new JRadioButton("Klavye");
        rb2.setActionCommand("k");
        rb2.setBounds(150,120,250,20);

        ButtonGroup bg= new ButtonGroup();
        bg.add(rb1);
        bg.add(rb2);

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

        //TODO socket ile ağdan izlemek üzere kontrol yapıyor
        JCheckBox checkBox1 = new JCheckBox("Ziyaretçi");
        checkBox1.setBounds(150,180, 150,20);
        panel.add(checkBox1);

        //TODO burası single player ve multiplayer buttonlarının bağlı olduğu grup
        ButtonGroup bg2= new ButtonGroup();
        bg2.add(rbSingle);
        bg2.add(rbMultiPlayer);

        JLabel o1=new JLabel("1- Oyuncu İsmi: ");
        o1.setBounds(150,200,250,20);
        JLabel o2=new JLabel("2- Oyuncu İsmi: ");
        o2.setBounds(150,270,250,20);

        panel.add(o1);
        panel.add(o2);



        JButton button=new JButton("Başla");
        button.setBounds(150,340,100,30);

        //TODO başla butonuna basıldığında olacak olan olaylar
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
                        gameUser2.setScore(500);

                        if(bg.getSelection().getActionCommand().charAt(0)=='f'){
                            gameUser.setGameControls(GameControls.MOUSE);
                        }else{
                            gameUser.setGameControls(GameControls.KEYBOARD);
                        }
                        //TODO Oynama şekli klavye fare belirlenecek?
                        //TODO burada belirlenen özelliklere göre (fare-klavye, single-multi, visitor?) oyun yaratılıyor
                        //TODO Abstract Factory tasarım kalıbı
                        WordlGameFactory keyboardFactory = new WordleGameFactoryWithKeyboard();
                        WordlGameFactory mouseFactory = new WordleGameFactoryWithMouse();
                        if(gameUser.getGameControls()==GameControls.KEYBOARD && bg2.getSelection().getActionCommand().charAt(0)=='s'){
                            //TODO bir oyuncu oynayacağı için burada onu aktifleştiriyorum
                            gameUser.getIsActive().set(true);
                            WordleGameWithKeyboard wordleGameWithKeyboard = (WordleGameWithKeyboard) keyboardFactory.createSingleWordleGame(gameUser,checkBox1.isSelected());
                            wordleGameWithKeyboard.setVisible(true);
                        }else if(gameUser.getGameControls()==GameControls.MOUSE && bg2.getSelection().getActionCommand().charAt(0)=='s'){
                            WordleGameWithMouse wordleGameWithMouse=(WordleGameWithMouse) mouseFactory.createSingleWordleGame(gameUser,checkBox1.isSelected());
                            wordleGameWithMouse.setVisible(true);
                        }else if(gameUser.getGameControls()==GameControls.KEYBOARD && bg2.getSelection().getActionCommand().charAt(0)=='m'){
                            MultiplayerWordleGameWithKeyboard multiplayerWordleGameWithKeyboard=(MultiplayerWordleGameWithKeyboard) keyboardFactory.createMultiplayerWordleGame(gameUser,gameUser2,checkBox1.isSelected());
                            multiplayerWordleGameWithKeyboard.setVisible(true);
                        }else if(gameUser.getGameControls()==GameControls.MOUSE && bg2.getSelection().getActionCommand().charAt(0)=='m'){
                            MultiPlayerWordleGameWithMouse multiPlayerWordleGameWithMouse=(MultiPlayerWordleGameWithMouse) mouseFactory.createMultiplayerWordleGame(gameUser,gameUser2,checkBox1.isSelected());
                            multiPlayerWordleGameWithMouse.setVisible(true);
                        }
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
    }
}

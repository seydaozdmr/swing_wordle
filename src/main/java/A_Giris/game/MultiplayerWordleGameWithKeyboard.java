package A_Giris.game;

import A_Giris.model.User;
import A_Giris.model.WordlPuzzle;
import A_Giris.service.ControlService;

import javax.swing.*;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MultiplayerWordleGameWithKeyboard extends JFrame implements MultiPlayerGame {
    private static User user1;
    private static User user2;
    static boolean flag = true;

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MultiplayerWordleGameWithKeyboard multiplayerWordleGameWithKeyboard =new MultiplayerWordleGameWithKeyboard(user1,user2);
                multiplayerWordleGameWithKeyboard.setVisible(true);
            }
        });
    }

    public MultiplayerWordleGameWithKeyboard(User user1, User user2) {
        this.user1=user1;
        this.user2=user2;
        try{
            WordlPuzzle puzzle=new WordlPuzzle(user1,user2);
            createAndShowGui(puzzle,this);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void createAndShowGui(WordlPuzzle puzzle, JFrame jFrame) throws IOException{
        ControlService controlService=new ControlService();
        JLabel [] labels=ControlService.createLabelsForKeyBoard(jFrame);

        jFrame.setTitle("Wordle Game");
        jFrame.setSize(800 ,800);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int chooseActiveUser= new Random().nextInt(2);
        System.out.println(chooseActiveUser);
        if(chooseActiveUser==0){
            user1.setActive(true);
        }else{
            user2.setActive(true);
        }


        JButton [] buttons=new JButton[31];
        buttons=ControlService.createButtonsForKeyBoard(labels,buttons,jFrame,controlService,puzzle,user1,user2);
        //TODO kullanıcılardan birini aktif olarak seçiyor ve 30 saniye süre veriyor süre bitince diğerine sıra geçiyor


        jFrame.setLayout(null);
    }


}

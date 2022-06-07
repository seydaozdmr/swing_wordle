package A_Giris.game;

import A_Giris.model.User;
import A_Giris.model.WordlPuzzle;
import A_Giris.service.ControlService;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import java.util.Random;


public class MultiplayerWordleGameWithKeyboard extends JFrame implements MultiPlayerGame {
    private static User user1;
    private static User user2;
    private static boolean hasVisitor;
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MultiplayerWordleGameWithKeyboard multiplayerWordleGameWithKeyboard =new MultiplayerWordleGameWithKeyboard(user1,user2,hasVisitor);
                multiplayerWordleGameWithKeyboard.setVisible(true);
            }
        });
    }

    public MultiplayerWordleGameWithKeyboard(User user1, User user2,boolean hasVisitor) {
        this.user1=user1;
        this.user2=user2;
        this.hasVisitor=hasVisitor;
        try{
            WordlPuzzle puzzle=new WordlPuzzle(user1,user2);
            createAndShowGui(puzzle,this);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public void createAndShowGui(WordlPuzzle puzzle, JFrame jFrame) throws IOException{
        ControlService controlService=new ControlService();
        Point labelsStartPoint=new Point(200,10);
        JLabel [] labels=ControlService.createLabelsForKeyBoard(jFrame,labelsStartPoint);

        jFrame.setTitle("MultiPlayer Wordle Game");
        jFrame.setSize(800 ,800);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int chooseActiveUser= new Random().nextInt(2);

        if(chooseActiveUser==0){
            user1.getIsActive().set(true);
            user2.getIsActive().set(false);
        }else{
            user2.getIsActive().set(true);
            user1.getIsActive().set(false);
        }

        Point buttonsStartPoint=new Point(80,350);
        JButton [] buttons=new JButton[31];
        buttons=ControlService.createButtonsForKeyBoard(labels,buttonsStartPoint,buttons,jFrame,controlService,puzzle,user1,user2);
        //TODO kullanıcılardan birini aktif olarak seçiyor ve 30 saniye süre veriyor süre bitince diğerine sıra geçiyor
        ControlService.createUserInformations(jFrame,controlService,user1,user2);
        if(hasVisitor)
            controlService.createSocketServer(labels);
        jFrame.setLayout(null);
    }


}

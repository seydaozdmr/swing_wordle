package A_Giris.game;

import A_Giris.model.User;
import A_Giris.model.WordlPuzzle;
import A_Giris.service.ControlService;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class MultiPlayerWordleGameWithMouse extends JFrame implements MultiPlayerGame {
    private static User user1;
    private static User user2;
    private static boolean hasVisitor;

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MultiPlayerWordleGameWithMouse multiPlayerWordleGameWithMouse = new MultiPlayerWordleGameWithMouse(user1,user2,hasVisitor);
                multiPlayerWordleGameWithMouse.setVisible(true);
            }
        });
    }

    public MultiPlayerWordleGameWithMouse (User user1,User user2,boolean hasVisitor){
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
    public void createAndShowGui(WordlPuzzle puzzle, JFrame jFrame) throws IOException {
        ControlService controlService=new ControlService();
        Point labelsStartPoint=new Point(200,10);
        JLabel [] labels=ControlService.createLabelsForMouse(jFrame,controlService,labelsStartPoint);

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
        buttons=ControlService.createButtonsForMouse(labels,buttonsStartPoint,buttons,jFrame,controlService,puzzle,user1,user2);
        ControlService.createUserInformations(jFrame,controlService,user1,user2);
        if(hasVisitor)
            controlService.createSocketServer(labels);
        jFrame.setLayout(null);

    }
}

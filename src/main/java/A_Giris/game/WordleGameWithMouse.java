package A_Giris.game;

import A_Giris.model.User;
import A_Giris.model.WordlPuzzle;
import A_Giris.service.ControlService;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class WordleGameWithMouse extends JFrame implements SingleWordleGame {
    private static User user;
    private static boolean hasVisitor;

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                WordleGameWithMouse wordleGameWithMouse=new WordleGameWithMouse(user,hasVisitor);
                wordleGameWithMouse.setVisible(true);
            }
        });
    }

    public WordleGameWithMouse(User user,boolean hasVisitor){
        this.user=user;
        this.setHasVisitor(hasVisitor);
        try {
            WordlPuzzle puzzle=new WordlPuzzle(user);
            createAndShowGui(puzzle,this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createAndShowGui(WordlPuzzle puzzle, JFrame jFrame) throws IOException {
        ControlService controlService=new ControlService();
        //label'ların olduğu dizi
        Point labelsStartPoint=new Point(200,10);
        JLabel [] labels =controlService.createLabelsForMouse(jFrame,controlService,labelsStartPoint);

        jFrame.setTitle("Wordle Game");
        jFrame.setSize(800,800);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Point buttonsStartPoint=new Point(80,350);
        JButton[] buttons=new JButton[31];
        buttons=ControlService.createButtonsForMouse(labels,buttonsStartPoint,buttons,jFrame,controlService,puzzle,user,null);
        user.getIsActive().set(true);
        ControlService.createUserInformations(jFrame,controlService,user,null);
        //TODO Eğer ziyaretçi varsa onun bağlanmasını bekliyor
        if(hasVisitor)
            controlService.createSocketServer(labels);
        jFrame.setLayout(null);
    }

    public boolean isHasVisitor() {
        return hasVisitor;
    }

    public void setHasVisitor(boolean hasVisitor) {
        this.hasVisitor = hasVisitor;
    }
}

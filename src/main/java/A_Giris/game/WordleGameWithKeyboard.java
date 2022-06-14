package A_Giris.game;

import A_Giris.model.User;
import A_Giris.model.WordlPuzzle;
import A_Giris.service.ControlService;
import A_Giris.service.FileService;
import A_Giris.service.GameService;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.event.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WordleGameWithKeyboard extends JFrame implements SingleWordleGame{

    //TODO static user
    private static User user;
    private static boolean hasVisitor;

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                WordleGameWithKeyboard wordleGameWithKeyboard =new WordleGameWithKeyboard(user,hasVisitor);
                wordleGameWithKeyboard.setVisible(true);
            }
        });
    }
    //TODO Constructor
    public WordleGameWithKeyboard(User user , boolean hasVisitor){
        this.user=user;
        this.hasVisitor= hasVisitor;
        try {
            WordlPuzzle puzzle=new WordlPuzzle(user);
            //TODO oyunun oynanacağı frame'e gerekli olan labelları ve buttonları eklemesi için
            //TODO bu methoda kendisini geçiyorum
            createAndShowGui(puzzle,this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createAndShowGui(WordlPuzzle puzzle,JFrame jFrame) throws IOException {
        //TODO bu sınıfın sahip olduğu bütün kontrolleri tutan getiren sınıf
        ControlService controlService=new ControlService();
        //TODO label'ların olduğu dizi controle service'de yarattırıp alıyorum
        Point labelsStartPoint=new Point(200,10);
        JLabel [] labels = controlService.createLabelsForKeyBoard(jFrame,labelsStartPoint);

        //TODO jFrame ayarları
        jFrame.setTitle("Wordle Game");
        jFrame.setSize(800 ,800);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //TODO ekrandaki harfleri yazdırmak için buttonları yarattırıyorum
        JButton [] buttons=new JButton[31];
        Point buttonsStartPoint=new Point(80,350);
        //TODO single player olduğu için ikinci kullanıcı null
        buttons= ControlService.createButtonsForKeyBoard(labels,buttonsStartPoint,buttons,jFrame,controlService,puzzle,user,null);
        //user.getIsActive().set(true);

        //TODO kullanıcının bilgilerinin ekranda yazılması için yarattırıyorum
        ControlService.createUserInformations(jFrame,controlService,user,null);
        //TODO visitor varsa thread'i başlat
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




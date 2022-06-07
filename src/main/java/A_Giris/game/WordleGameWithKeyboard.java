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
    private static int [] keyboard=  {69,82,84,89,85,73,79,80,286,220,65,83,68,70,71,72,74,75,76,350,304,10,90,67,86,66,78,77,214,199,8};
    private static int [] lowerKeyboard= {101,114,116,121,117,305,111,112,287,252,97,115,100,102,103,104,106,107,108,351,105,10,122,120,99,118,98,110,109,246,231,8};
    //TODO ya fareden basarak ya da klavyeden basarak çalışmalı
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
            createAndShowGui(puzzle,this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createAndShowGui(WordlPuzzle puzzle,JFrame jFrame) throws IOException {
        //bu sınıfın sahip olduğu bütün kontrolleri tutan getiren sınıf
        ControlService controlService=new ControlService();
        //label'ların olduğu dizi controle service'de yarattırıp alıyorum
        Point labelsStartPoint=new Point(200,10);
        JLabel [] myArray = controlService.createLabelsForKeyBoard(jFrame,labelsStartPoint);

        //jFrame ayarları
        jFrame.setTitle("Wordle Game");
        jFrame.setSize(800 ,800);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton [] buttons=new JButton[31];
        Point buttonsStartPoint=new Point(80,350);
        buttons= ControlService.createButtonsForKeyBoard(myArray,buttonsStartPoint,buttons,jFrame,controlService,puzzle,user,null);
        user.getIsActive().set(true);

        ControlService.createUserInformations(jFrame,controlService,user,null);
        //visitor varsa thread'i başlat
        if(hasVisitor)
            controlService.createSocketServer(myArray);

        jFrame.setLayout(null);
    }

    public boolean isHasVisitor() {
        return hasVisitor;
    }

    public void setHasVisitor(boolean hasVisitor) {
        this.hasVisitor = hasVisitor;
    }
}




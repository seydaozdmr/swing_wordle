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
import java.io.File;
import java.io.IOException;
import java.util.*;

public class WordleGameWithKeyboard extends JFrame implements SingleWordleGame{
    private static int [] keyboard=  {69,82,84,89,85,73,79,80,286,220,65,83,68,70,71,72,74,75,76,350,304,10,90,67,86,66,78,77,214,199,8};
    private static int [] lowerKeyboard= {101,114,116,121,117,305,111,112,287,252,97,115,100,102,103,104,106,107,108,351,105,10,122,120,99,118,98,110,109,246,231,8};
    //TODO ya fareden basarak ya da klavyeden basarak çalışmalı
    private static User user;

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                WordleGameWithKeyboard wordleGameWithKeyboard =new WordleGameWithKeyboard(user);
                wordleGameWithKeyboard.setVisible(true);
            }
        });
    }
    public WordleGameWithKeyboard(User user){
        this.user=user;
        try {
            WordlPuzzle puzzle=new WordlPuzzle(user);
            createAndShowGui(puzzle,this);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void createAndShowGui(WordlPuzzle puzzle,JFrame jFrame) throws IOException {
        //bu sınıfın sahip olduğu bütün kontrolleri tutan getiren sınıf
        ControlService controlService=new ControlService();
        //label'ların olduğu dizi controle service'de yarattırıp alıyorum
        JLabel [] myArray = controlService.createLabelsForKeyBoard(jFrame);
        //jFrame ayarları
        jFrame.setTitle("Wordle Game");
        jFrame.setSize(800 ,800);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton [] buttons=new JButton[31];
        //buttons = createButtons(myArray,buttons,jFrame,controlService,puzzle);
        buttons= ControlService.createButtonsForKeyBoard(myArray,buttons,jFrame,controlService,puzzle,user,null);
        jFrame.setLayout(null);
        //jFrame.setVisible(true);
    }


    private static JButton [] createButtons(JLabel[] myArray, JButton[] buttons, JFrame jFrame, ControlService controlService,WordlPuzzle puzzle) {
        //TODO Buraya point jpanel falan ekle
        int x=10;
        int y=10;


            for(int i=0;i<31;i++){
                if(i<=9){
                    buttons[i] = new JButton(String.valueOf((char) keyboard[i]));
                    buttons[i].setBounds(x + i*50,y,50,30);
                    buttons[i].setLocale(Locale.getDefault());
                    JButton temp=buttons[i];
                    buttons[i].setTransferHandler(new ValueExportTransferHandler(temp.getText()));
                    buttons[i].addMouseMotionListener(new MouseAdapter() {
                        @Override
                        public void mouseDragged(MouseEvent e) {
                            JButton temp= (JButton) e.getSource();
                            TransferHandler handle = temp.getTransferHandler();
                            handle.exportAsDrag(temp,e,TransferHandler.COPY);
                        }
                    });
                //TODO Bu işlemleri transferHandle kendi bünyesinde yapmak zorunda ve silme-enter tuşlarına basıldığında
                    // TODO bu işlevler dikkate alınarak işlem yapılmalı
                    //TODO Not bu handle'ı labelların olduğu panele ekleyip panele gelen bilgiyi sıradaki label'a atmasını deneyebiliriz
                    //TODO Burada tüm kontrollere dikkat etmek gerekmektedir.

//                buttons[i].addActionListener(new ActionListener() {
//                    @Override
//                    public void actionPerformed(ActionEvent e) {
//                        JLabel tempLabel = Arrays.stream(myArray).filter(a->a.getText()==" ").findFirst().get();
//                        tempLabel.setText(temp.getText());
//                        controlService.getBuilder().append(tempLabel.getText());
//                        controlService.getWrittenLabels().add(tempLabel);
//                        controlService.addJLabel(tempLabel);
//                        controlService.setTempButton(temp);
//                    }
//                });
                    buttons[i].addKeyListener(new KeyListener() {
                        @Override
                        public void keyTyped(KeyEvent e) {

                        }
                        @Override
                        public void keyPressed(KeyEvent e) {
                            //TODO oyun bittiğinde yeni bir event alma game is over...
                            if(!controlService.getFinishedRound().get()){
                                if(e.getKeyCode()==8 && controlService.getLabelCounter()!=0){
                                    JLabel lastModified= controlService.getLastJLabel();
                                    lastModified.setText(" ");
                                    controlService.getCount().decrementAndGet();
                                    controlService.getBuilder().deleteCharAt(controlService.getBuilder().length()-1);
                                    controlService.getWrittenLabels().remove(controlService.getWrittenLabels().size()-1);
                                }

                                if(!controlService.getFinishedRound().get() && controlService.getCount().get()<5 && e.getKeyCode()!=8 && e.getKeyCode()!=10) {
                                    controlService.getCount().incrementAndGet();
                                    try {
                                        JLabel tempLabel = Arrays.stream(myArray).filter(a -> a.getText() == " ").findFirst().get();
                                        tempLabel.setText(String.valueOf((char) Arrays.stream(lowerKeyboard)
                                                .filter(a -> e.getKeyChar() == a).findFirst().getAsInt()).toUpperCase(Locale.getDefault()));
                                        controlService.addJLabel(tempLabel);
                                        controlService.setTempButton(temp);
                                        //TODO son eklenenlerin de silinmesi lazım
                                        controlService.getBuilder().append(tempLabel.getText());
                                        controlService.getWrittenLabels().add(tempLabel);
                                    } catch (NoSuchElementException a) {
                                        System.out.println(e.getKeyCode() + " oyun için geçersiz");
                                    }
                                }
                                if(e.getKeyCode()==10 && controlService.getCount().get()==5 && !controlService.getFinishedRound().get()){
                                    //TODO Girilen her kelime sonrası kelimenin kontrol edilmesi
                                    controlService.getRoundCounter().incrementAndGet();
                                    controlService.getCount().set(0);
                                    puzzle.addWord(controlService.getBuilder().toString());
                                    controlService.removeTempLabels();
                                    if(puzzle.checkWord()){
                                        puzzle.calculateScore(puzzle.getMatches(),puzzle.getNotMatchesWords());
                                        controlService.getFinishedRound().set(true);

                                        for(JLabel elem: controlService.getWrittenLabels()){
                                            elem.setBorder(BorderFactory.createLineBorder(Color.GREEN));
                                        }
                                        congratulations(user);
                                        FileService.writeScore(user);
                                    }else{
                                        for(int i=0;i<puzzle.getMatches().length;i++){
                                            if(puzzle.getMatches()[i]==true){
                                                controlService.getWrittenLabels().get(i).setBorder(BorderFactory.createLineBorder(Color.GREEN));
                                            }else{
                                                if(puzzle.getNotMatchesWords()[i]==true){
                                                    controlService.getWrittenLabels().get(i).setBorder(BorderFactory.createLineBorder(Color.YELLOW));
                                                }
                                            }
                                        }
                                        puzzle.calculateScore(puzzle.getMatches(),puzzle.getNotMatchesWords());
                                        System.out.println("skor : "+user.getScore());
                                    }
                                    controlService.getBuilder().delete(0,5);
                                    controlService.getWrittenLabels().clear();
                                    if (controlService.getRoundCounter().get() == 5) {
                                        puzzle.calculateScore(puzzle.getMatches(),puzzle.getNotMatchesWords());
                                        looseMessage(user);
                                        controlService.getFinishedRound().set(true);
                                    }
                                }
                            }

                        }
                        @Override
                        public void keyReleased(KeyEvent e) {

                        }
                    });
                    jFrame.add(buttons[i]);

                }else{
                    y=50;
                    if(i<=20){
                        buttons[i] =new JButton(String.valueOf((char) keyboard[i]));
                        buttons[i].setBounds(x + (i-10)*50 ,y,50,30);
                        JButton temp=buttons[i];
//                    buttons[i].addActionListener(new ActionListener() {
//                        @Override
//                        public void actionPerformed(ActionEvent e) {
//                            JLabel tempLabel = Arrays.stream(myArray).filter(a->a.getText()==" ").findFirst().get();
//                            tempLabel.setText(temp.getText());
//                            controlService.getBuilder().append(tempLabel.getText());
//                            controlService.getWrittenLabels().add(tempLabel);
//                            controlService.addJLabel(tempLabel);
//                            controlService.setTempButton(temp);
//                        }
//                    });
                        jFrame.add( buttons[i]);

                    }else{
                        y=100;
                        if(!(keyboard[i]==10) && !(keyboard[i]==8)){
                            buttons[i] =new JButton(String.valueOf((char) keyboard[i]));
                            buttons[i].setBounds(x+ (i-21)*50,y,50,30);
                            JButton temp=buttons[i];
//                        buttons[i].addActionListener(new ActionListener() {
//                            @Override
//                            public void actionPerformed(ActionEvent e) {
//                                JLabel tempLabel = Arrays.stream(myArray).filter(a->a.getText()==" ").findFirst().get();
//                                tempLabel.setText(temp.getText());
//                                controlService.getBuilder().append(tempLabel.getText());
//                                controlService.getWrittenLabels().add(tempLabel);
//                                controlService.addJLabel(tempLabel);
//                                controlService.setTempButton(temp);
//                            }
//                        });
                            jFrame.add( buttons[i]);
                        }else if(keyboard[i]==10){
                            //TODO ENTER Tuşu
                            buttons[i] = new JButton("EN");
                            Image buttonIcon = null;
                            try {
                                buttonIcon = ImageIO.read(new File("enter.png"));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            buttons[i] = new JButton(new ImageIcon(buttonIcon));
                            buttons[i].setBorderPainted(true);
                            buttons[i].setFocusPainted(true);
                            buttons[i].setContentAreaFilled(false);
                            buttons[i].setBounds(x,y,50,30);
//                        buttons[i].addActionListener(new ActionListener() {
//                            @Override
//                            public void actionPerformed(ActionEvent e) {
//                                System.out.println("ENTER");
//                                //TODO eğer 5 tanesi dolmuşsa enter olur
////                                JLabel tempLabel = Arrays.stream(myArray).filter(a->a.getText()==" ").findFirst().get();
////                                tempLabel.setText(temp.getText());
//
//                            }
//                        });
                            jFrame.add(buttons[i]);
                            //x+=50;
                        }else if(keyboard[i]==8){
                            //TODO BACKSPACE tuşu
                            buttons[i] =new JButton("<-");
                            buttons[i].setBounds(x + (i-21)*50,y,50,30);
                            JButton temp=buttons[i];
//                        buttons[i].addActionListener(new ActionListener() {
//                            @Override
//                            public void actionPerformed(ActionEvent e) {
//                                //TODO sonuncuyu bulup onu silmek gerekiyor
////                                System.out.println(e.getActionCommand());
////                                JLabel tempLabel = Arrays.stream(myArray).filter(a->a.getText()==" ").findFirst().get();
////                                tempLabel.setText(temp.getText());
//                                try{
//                                    JLabel lastModified;
//                                    if(controlService.getLabelCounter()>0 && (lastModified = controlService.getLastJLabel())!=null && controlService.getWrittenLabels().size()>0)
//                                    {
//                                        //JLabel lastModified= controlService.getLastJLabel();
//                                        lastModified.setText(" ");
//                                        controlService.getCount().decrementAndGet();
//                                        if(controlService.getBuilder().length()>0){
//                                            controlService.getBuilder().deleteCharAt(controlService.getBuilder().length()-1);
//                                        }
//                                        //controlService.getBuilder().deleteCharAt(controlService.getBuilder().toString().length()-1);
//                                        controlService.getWrittenLabels().remove( controlService.getWrittenLabels().size()-1);
//                                    }
//                                }catch (NullPointerException ex){
//                                }
//                            }
//                        });
                            jFrame.add(buttons[i]);
                        }
                    }
                }
            }
            return buttons;

    }

    private static void looseMessage(User user) {
        StringBuilder builder=new StringBuilder();
        builder.append("Üzgünüz \n");
        builder.append("Kullanıcı: "+user.getUserName()+" \n");
        builder.append("Skor: "+user.getScore()+" \n");
        builder.append("Sonuç: Oyunu Kaybettiniz \n");
        GameService.showLooseMessage(builder.toString());
    }

    private static void congratulations(User user) {
        StringBuilder builder=new StringBuilder();
        builder.append("Tebrikler \n");
        builder.append("Kullanıcı: "+user.getUserName()+" \n");
        builder.append("Skor: "+user.getScore()+" \n");
        builder.append("Sonuç: oyunu kazandınız \n");
        GameService.showCongratulations(builder.toString());
    }


    static  JLabel[] createLabels(JFrame jFrame){
        //TODO Ekrandaki label'lar
        JLabel [] myArray = new JLabel[25];
        int count =0;
        for(int i=0;i<5;i++) {
            for (int j = 0; j < 5; j++) {
                myArray[count] = new JLabel(" ");
                JLabel temp = myArray[count++];
                Border blackline = BorderFactory.createLineBorder(Color.black);
                temp.setBorder(blackline);
                temp.setBounds(100 + j * 60, 200 + i * 60, 50, 50);
                temp.setTransferHandler(new ValueImportTransferHandler(myArray));
                jFrame.add(temp);
            }
        }
        return myArray;
    }


    public static class ValueExportTransferHandler extends TransferHandler {

        public static final DataFlavor SUPPORTED_DATE_FLAVOR = DataFlavor.stringFlavor;
        private String value;

        public ValueExportTransferHandler(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public int getSourceActions(JComponent c) {
            return DnDConstants.ACTION_COPY_OR_MOVE;
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            Transferable t = new StringSelection(getValue());
            return t;
        }

        @Override
        protected void exportDone(JComponent source, Transferable data, int action) {
            super.exportDone(source, data, action);
            // Decide what to do after the drop has been accepted
        }

    }
    //TODO data gelince onu control nesnelerine eklemek gerekiyor
    //TODO bu işlevi ayırmak gerekiyor fare - klavye
    public static class ValueImportTransferHandler extends TransferHandler {

        public static final DataFlavor SUPPORTED_DATE_FLAVOR = DataFlavor.stringFlavor;
        private JLabel[] jLabels;
        public ValueImportTransferHandler(JLabel[] myArray) {
            jLabels=myArray;
        }

        @Override
        public boolean canImport(TransferHandler.TransferSupport support) {
            return support.isDataFlavorSupported(SUPPORTED_DATE_FLAVOR);
        }

        @Override
        public boolean importData(TransferHandler.TransferSupport support) {
            boolean accept = false;
            if (canImport(support)) {
                try {
                    Transferable t = support.getTransferable();
                    Object value = t.getTransferData(SUPPORTED_DATE_FLAVOR);
                    if (value instanceof String) {
                        Component component = support.getComponent();
                        if (component instanceof JLabel) {
                            JLabel temp= Arrays.stream(jLabels).filter(e->e.getText().equals(" ")).findFirst().get();
                            if(((JLabel)component).equals(temp)){
                                //TODO her birini controledeki array'e ekle ve say sadece her bir raundda 5 tane ekleyebil enter'a basınca
                                //Baştan ekleme yappp
                                ((JLabel) component).setText(value.toString());
                            }else{
                                System.out.println("buraya ekleyemezsin");
                            }

                            accept = true;
                        }
                    }
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }
            return accept;
        }
    }
}




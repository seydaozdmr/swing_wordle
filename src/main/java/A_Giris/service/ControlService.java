package A_Giris.service;

import A_Giris.model.Timer;
import A_Giris.model.User;
import A_Giris.model.WordlPuzzle;

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
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ControlService {
    private JButton tempButton;
    private JLabel [] tempJLabel = new JLabel[5];
    private int labelCounter=0;
    private StringBuilder builder=new StringBuilder();
    private List<JLabel> writtenLabels= new ArrayList<>();
    private AtomicInteger count = new AtomicInteger(0);
    private AtomicBoolean finishedRound = new AtomicBoolean(false);
    private AtomicInteger roundCounter=new AtomicInteger(0);
    public AtomicInteger getRoundCounter() {
        return roundCounter;
    }
    private static int [] keyboard=  {69,82,84,89,85,73,79,80,286,220,65,83,68,70,71,72,74,75,76,350,304,10,90,67,86,66,78,77,214,199,8};
    private static int [] lowerKeyboard= {101,114,116,121,117,305,111,112,287,252,97,115,100,102,103,104,106,107,108,351,105,10,122,120,99,118,98,110,109,246,231,8};
    private static boolean flag=true;

    public void setRoundCounter(AtomicInteger roundCounter) {
        this.roundCounter = roundCounter;
    }

    public JButton getTempButton() {
        return tempButton;
    }

    public void setTempButton(JButton tempButton) {
        this.tempButton = tempButton;
    }

    public void addJLabel(JLabel temp){
        this.tempJLabel[labelCounter++]=temp;
    }

    public JLabel getLastJLabel(){
        return this.tempJLabel[(labelCounter--)-1];
    }

    public StringBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(StringBuilder builder) {
        this.builder = builder;
    }

    public List<JLabel> getWrittenLabels() {
        return writtenLabels;
    }

    public void setWrittenLabels(List<JLabel> writtenLabels) {
        this.writtenLabels = writtenLabels;
    }

    public JLabel[] getTempJLabel() {
        return tempJLabel;
    }

    public void setTempJLabel(JLabel[] tempJLabel) {
        this.tempJLabel = tempJLabel;
    }

    public int getLabelCounter() {
        return labelCounter;
    }

    public void setLabelCounter(int labelCounter) {
        this.labelCounter = labelCounter;
    }

    public AtomicInteger getCount() {
        return count;
    }

    public void setCount(AtomicInteger count) {
        this.count = count;
    }

    public void removeTempLabels(){
        this.tempJLabel=null;
        this.tempJLabel=new JLabel[5];
        this.labelCounter=0;
    }

    public AtomicBoolean getFinishedRound() {
        return finishedRound;
    }

    public void setFinishedRound(AtomicBoolean finishedRound) {
        this.finishedRound = finishedRound;
    }



    public static JButton[] createButtonsForMouse(JLabel[] myArray,JButton[] buttons, JFrame jFrame, ControlService controlService, WordlPuzzle puzzle,User user){
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
                jFrame.add(buttons[i]);
            }else{
                y=50;
                if(i<=20){
                    buttons[i] =new JButton(String.valueOf((char) keyboard[i]));
                    buttons[i].setBounds(x + (i-10)*50 ,y,50,30);
                    JButton temp=buttons[i];
                    //TODO buraya button sürükleme gelecek
                    buttons[i].setTransferHandler(new ValueExportTransferHandler(temp.getText()));
                    buttons[i].addMouseMotionListener(new MouseAdapter() {
                        @Override
                        public void mouseDragged(MouseEvent e) {
                            JButton temp= (JButton) e.getSource();
                            TransferHandler handle = temp.getTransferHandler();
                            handle.exportAsDrag(temp,e,TransferHandler.COPY);
                        }
                    });
                    jFrame.add( buttons[i]);

                }else{
                    y=100;
                    if(!(keyboard[i]==10) && !(keyboard[i]==8)){
                        buttons[i] =new JButton(String.valueOf((char) keyboard[i]));
                        buttons[i].setBounds(x+ (i-21)*50,y,50,30);
                        JButton temp=buttons[i];
                        //TODO buraya button sürükleme gelecek
                        buttons[i].setTransferHandler(new ValueExportTransferHandler(temp.getText()));
                        buttons[i].addMouseMotionListener(new MouseAdapter() {
                            @Override
                            public void mouseDragged(MouseEvent e) {
                                JButton temp= (JButton) e.getSource();
                                TransferHandler handle = temp.getTransferHandler();
                                handle.exportAsDrag(temp,e,TransferHandler.COPY);
                            }
                        });
                        jFrame.add( buttons[i]);
                    }else if(keyboard[i]==10){
                        //TODO ENTER Tuşu
                        buttons[i] = new JButton();
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
                        //TODO enter çalıştırılacak
                        buttons[i].addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if(controlService.getCount().get()==5 && !controlService.getFinishedRound().get()){
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
                        });
                        jFrame.add(buttons[i]);
                    }else if(keyboard[i]==8){
                        //TODO BACKSPACE tuşu
                        buttons[i] =new JButton("<-");
                        buttons[i].setBounds(x + (i-21)*50,y,50,30);

                        buttons[i].addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try{
                                    JLabel lastModified;
                                    if(controlService.getLabelCounter()>0 && (lastModified = controlService.getLastJLabel())!=null && controlService.getWrittenLabels().size()>0)
                                    {
                                        lastModified.setText(" ");
                                        controlService.getCount().decrementAndGet();
                                        if(controlService.getBuilder().length()>0){
                                            controlService.getBuilder().deleteCharAt(controlService.getBuilder().length()-1);
                                        }
                                        controlService.getWrittenLabels().remove( controlService.getWrittenLabels().size()-1);
                                    }
                                }catch (NullPointerException ex){
                                }
                            }
                        });
                        jFrame.add(buttons[i]);
                    }
                }
            }
        }
        return buttons;
    }

    public static JButton[] createButtonsForKeyBoard(JLabel[] myArray, JButton[] buttons, JFrame jFrame, ControlService controlService, WordlPuzzle puzzle,User user1,User user2){
        int x=10;
        int y=10;
        //TODO BURASI saniyeleri sayan ve aktif kullanıcının değiştiği bölüm
        //TODO Bunun yanında aktif olan kullanıcıyı check yapan ve yeşil yanmasını sağlayan thread yaratılmalı
        Timer timer=new Timer();
        AtomicBoolean isSwitch=new AtomicBoolean(false);
        Thread t1=new Thread(new Runnable() {
            void switchActiveUser(){
                if(user1.isActive()){
                    user2.setActive(true);
                    user1.setActive(false);
                }else if(user2.isActive()){
                    user2.setActive(false);
                    user1.setActive(true);
                }

            }
            @Override
            public void run() {
                while(!controlService.getFinishedRound().get()){
                    for(int i=0;i<30;i++){
                        if(!isSwitch.get()){
                            System.out.println(timer.incrementTimer());
                            if(user2!=null){
                                switchActiveUser();
                            }

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else{
                            try {
                                Thread.sleep(1000);
                                timer.reset();
                                isSwitch.set(false);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    try {
                        timer.reset();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });


        //two threads run respectively
//        Timer timer=new Timer();
//        ReentrantLock lock = new ReentrantLock();
//        Condition condition = lock.newCondition();
//        Thread t1=new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                for(int i=0;i<3;i++) {
//                    lock.lock();
//                    while (flag == false) {
//                        try {
//                            for(int j=0;j<30;j++){
//                                System.out.println(Thread.currentThread().getName() + " : " + timer.incrementTimer());
//                                Thread.sleep(1000);
//                            }
//                            condition.await();  // wait until thread2 set true
//                        } catch (InterruptedException e) {
//                        }
//                    }
//                    flag = false;
//                    condition.signalAll();
//                    timer.reset();
//                    lock.unlock();
//                }
//
//            }
//        });
//
//        Thread t2=new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                for(int i=0;i<3;i++){
//                    lock.lock();
//                    while (flag == true) {
//                        try {
//                            for(int j=0;j<30;j++){
//                                System.out.println(Thread.currentThread().getName() + " : " + timer.incrementTimer());
//                                Thread.sleep(1000);
//                            }
//                            condition.await();  // wait until thread2 set true
//                        } catch (InterruptedException e) {
//                        }
//                    }
//                    flag = true;
//                    condition.signalAll();
//                    timer.reset();
//                    lock.unlock();
//                }
//            }
//        });
//        t1.start();
//        t2.start();
        //Burada switch gelecek
        t1.start();
        for(int i=0;i<31;i++){
            if(i<=9){
                buttons[i] = new JButton(String.valueOf((char) keyboard[i]));
                buttons[i].setBounds(x + i*50,y,50,30);
                buttons[i].setLocale(Locale.getDefault());
                JButton temp=buttons[i];

                //Klavye tuşlarını dinleyen KeyListener
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
                                    //Eğer aktif olan User1 ise User2 değilse user2
                                    congratulations(user1);
                                    FileService.writeScore(user1);
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
                                    //sıra diğer kullanıcıya geçer.
                                    if(user2!=null)
                                        isSwitch.set(true);
                                    puzzle.calculateScore(puzzle.getMatches(),puzzle.getNotMatchesWords());
                                    System.out.println("skor : "+user1.getScore());
                                }
                                controlService.getBuilder().delete(0,5);
                                controlService.getWrittenLabels().clear();
                                if (controlService.getRoundCounter().get() == 5) {
                                    puzzle.calculateScore(puzzle.getMatches(),puzzle.getNotMatchesWords());
                                    looseMessage(user1);
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
                    jFrame.add( buttons[i]);

                }else{
                    y=100;
                    if(!(keyboard[i]==10) && !(keyboard[i]==8)){
                        buttons[i] =new JButton(String.valueOf((char) keyboard[i]));
                        buttons[i].setBounds(x+ (i-21)*50,y,50,30);
                        jFrame.add( buttons[i]);
                    }else if(keyboard[i]==10){
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
                        jFrame.add(buttons[i]);
                    }else if(keyboard[i]==8){
                        //TODO BACKSPACE tuşu
                        buttons[i] =new JButton("<-");
                        buttons[i].setBounds(x + (i-21)*50,y,50,30);
                        jFrame.add(buttons[i]);
                    }
                }
            }
        }
        return buttons;

    }

    public static JLabel[] createLabelsForKeyBoard (JFrame jFrame){
        JLabel [] myArray = new JLabel[25];
        int count =0;
        for(int i=0;i<5;i++) {
            for (int j = 0; j < 5; j++) {
                myArray[count] = new JLabel(" ");
                JLabel temp = myArray[count++];
                Border blackline = BorderFactory.createLineBorder(Color.black);
                temp.setBorder(blackline);
                temp.setBounds(100 + j * 60, 200 + i * 60, 50, 50);
                jFrame.add(temp);
            }
        }
        return myArray;
    }

    public static JLabel[] createLabelsForMouse(JFrame jFrame,ControlService service){
        JLabel [] myArray = new JLabel[25];
        int count =0;
        for(int i=0;i<5;i++) {
            for (int j = 0; j < 5; j++) {
                myArray[count] = new JLabel(" ");
                JLabel temp = myArray[count++];
                Border blackline = BorderFactory.createLineBorder(Color.black);
                temp.setBorder(blackline);
                temp.setBounds(100 + j * 60, 200 + i * 60, 50, 50);
                temp.setTransferHandler(new ValueImportTransferHandler(myArray,service));
                jFrame.add(temp);
            }
        }
        return myArray;
    }

    public static class ValueImportTransferHandler extends TransferHandler {
        public static final DataFlavor SUPPORTED_DATE_FLAVOR = DataFlavor.stringFlavor;
        //Sıradaki JLabel'a ekleme yapabilmek için bu sınıf label ve control
        private JLabel[] jLabels;
        private ControlService controlService;
        public ValueImportTransferHandler(JLabel[] myArray,ControlService service) {
            jLabels=myArray;
            this.controlService=service;
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
                            if(!controlService.getFinishedRound().get() && controlService.getCount().get()<5){
                                JLabel tempLabel= Arrays.stream(jLabels).filter(e->e.getText().equals(" ")).findFirst().get();
                                if(((JLabel)component).equals(tempLabel)){
                                    ((JLabel) component).setText(value.toString());
                                    controlService.addJLabel(tempLabel);//3
                                    controlService.getBuilder().append(tempLabel.getText());//5
                                    controlService.getWrittenLabels().add(tempLabel);
                                    controlService.getCount().incrementAndGet();
                                }else{
                                    System.out.println("buraya ekleyemezsin");
                                }
                            }else{
                                System.out.println("şu anda ekleme yapamazsınız");
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
}

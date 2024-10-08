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
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collector;

public class ControlService {
    private JButton tempButton;
    private JLabel [] tempJLabel = new JLabel[5];
    private int labelCounter=0;
    private StringBuilder builder=new StringBuilder();
    private List<JLabel> writtenLabels= new ArrayList<>();

    private AtomicInteger count = new AtomicInteger(0);
    private AtomicBoolean finishedRound = new AtomicBoolean(false);
    private AtomicInteger roundCounter=new AtomicInteger(0);

    private static int [] keyboard=  {69,82,84,89,85,73,79,80,286,220,65,83,68,70,71,72,74,75,76,350,304,10,90,67,86,66,78,77,214,199,8};
    private static int [] lowerKeyboard= {101,114,116,121,117,305,111,112,287,252,97,115,100,102,103,104,106,107,108,351,105,10,122,120,99,118,98,110,109,246,231,8};
    private Timer timer=new Timer();
    private static AtomicBoolean isSwitch = new AtomicBoolean(false);

    private static ServerSocket serverSocket;
    private static Socket socket;





    public AtomicInteger getRoundCounter() {
        return roundCounter;
    }
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

    public void createSocketServer(JLabel [] myArray){
        try {
            serverSocket = new ServerSocket(1234);
            socket = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //TODO bu thread sürekli visitor'a label'ların bilgisini gönderir...
        Thread t1=new Thread(new Runnable() {
            @Override
            public void run() {
                DataOutputStream dOut =null;
                try {
                     dOut = new DataOutputStream(socket.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sendData(myArray,dOut);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
    }


    //TODO USER INFORMATION VE THREAD
    public static void createUserInformations(JFrame jFrame, ControlService controlService,User user1,User user2){
        //TODO bunların x'i farklı olacak
        JLabel user1Label=new JLabel("Oyuncu-1 : ");
        user1Label.setBounds(10,510,100,20);
        JLabel user1Details=new JLabel(user1.getUserName());
        user1Details.setBounds(100,510,50,20);
        JLabel user1Time=new JLabel("");
        user1Time.setBounds(10,530,50,20);
        JLabel user1Active = new JLabel("");
        user1Active.setBounds(100,530,50,50);
        user1Active.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        //TODO ikinci oyuncunun gelmediği durum ayarlanacak
            JLabel user2Label=new JLabel("Oyuncu-2 : ");
            user2Label.setBounds(200,510,100,20);
            JLabel user2Details=new JLabel(" ");
            user2Details.setBounds(300,510,250,20);
            JLabel user2Time=new JLabel("");
            user2Time.setBounds(200,530,100,20);
            JLabel user2Active = new JLabel("");
            user2Active.setBounds(300,530,50,50);
            user2Active.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        Thread t1=new Thread(new Runnable() {
            @Override
            public void run() {
                while(!controlService.getFinishedRound().get()){
                    if(user1.getIsActive().get()){
                        user1Time.setText(String.valueOf(controlService.timer.getTime()));
                        user1Active.setBorder(BorderFactory.createLineBorder(Color.GREEN));
                        user1Active.setBackground(Color.GREEN);
                        user1Active.setForeground(Color.GREEN);
                        user1Active.setOpaque(true);
                        user2Active.setOpaque(true);
                        user2Active.setForeground(Color.DARK_GRAY);
                        user2Active.setBackground(Color.DARK_GRAY);
                        user2Active.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                    }else if(user2!=null && user2.getIsActive().get()){
                        user2Time.setText(String.valueOf(controlService.timer.getTime()));
                        user2Active.setBorder(BorderFactory.createLineBorder(Color.GREEN));
                        user2Active.setBackground(Color.GREEN);
                        user2Active.setForeground(Color.GREEN);
                        user2Active.setOpaque(true);
                        user1Active.setOpaque(true);
                        user1Active.setForeground(Color.DARK_GRAY);
                        user1Active.setBackground(Color.DARK_GRAY);
                        user1Active.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                    }
                }
            }
        });
        t1.start();

        jFrame.add(user1Label);
        jFrame.add(user1Details);
        jFrame.add(user1Time);
        jFrame.add(user1Active);
        if(user2!=null){
            user2Details.setText(user2.getUserName());
            jFrame.add(user2Label);
            jFrame.add(user2Details);
            jFrame.add(user2Time);
            jFrame.add(user2Active);
        }
    }

    //TODO TIMER THREAD
    public static void createTimerThread(User user1,User user2,ControlService controlService){
        //TODO RoundCount ve eğer hiç birşey girilmediyse bütün labelları kırmızı yap
        Thread t1=new Thread(new Runnable() {
            void switchActiveUser(){
                if(user1.getIsActive().get()){
                    user2.getIsActive().set(true);
                    user1.getIsActive().set(false);
                }else if(user2.getIsActive().get()){
                    user2.getIsActive().set(false);
                    user1.getIsActive().set(true);
                }
            }
            @Override
            public void run() {
                //TODO saymaya devam ediyor
                while(!controlService.getFinishedRound().get()){
                    for(int i=0;i<30;i++){
                        if(!isSwitch.get()){
                            //switch değişmediği sürece timer arttır
                            System.out.println(controlService.timer.incrementTimer());
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else{
                            //eğer switch true olursa
                            if(user2!=null){
                                switchActiveUser();
                            }//önce aktif kullanıcıyı değiştir
                            try {
                                Thread.sleep(1000);
                                controlService.timer.reset();
                                isSwitch.set(false);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        if(user2!=null){
                            switchActiveUser();
                        }
                        isSwitch.set(false);
                        controlService.timer.reset();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t1.start();
    }

    public static JButton[] createButtonsForMouse(JLabel[] myArray,Point buttonsStartPoint,JButton[] buttons, JFrame jFrame, ControlService controlService, WordlPuzzle puzzle,User user1,User user2){
        int x=buttonsStartPoint.x;
        int y=buttonsStartPoint.y;
        //TODO burada altta bulunan timer thread'i çalıştırıyor
        createTimerThread(user1,user2,controlService);
        for(int i=0;i<31;i++){
            //TODO ilk sıra
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
                //TODO ikinci sıra
                if(i<=20){
                    buttons[i] =new JButton(String.valueOf((char) keyboard[i]));
                    buttons[i].setBounds(x + (i-10)*50 ,y+50,50,30);
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
                    //TODO 10 enter - > 8 backspace silme
                    if(!(keyboard[i]==10) && !(keyboard[i]==8)){
                        buttons[i] =new JButton(String.valueOf((char) keyboard[i]));
                        buttons[i].setBounds(x+ (i-21)*50,y+100,50,30);
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
                        buttons[i].setBounds(x,y+100,50,30);
                        //TODO enter çalıştırılacak
                        buttons[i].addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                //TODO eğer 5 tane harf girildeyse ve oyun bitmediyse
                                if(controlService.getCount().get()==5 && !controlService.getFinishedRound().get()){
                                    //TODO Girilen her kelime sonrası kelimenin kontrol edilmesi
                                    controlService.getRoundCounter().incrementAndGet();
                                    //TODO enter'a bastıktan sonra counter'ı sıfırlamam lazım
                                    controlService.getCount().set(0);
                                    //TODO puzzle'a girilen harfleri gönderiyorum
                                    puzzle.addWord(controlService.getBuilder().toString());
                                    //TODO puzzle'a gidince tempteki labeller temizler
                                    controlService.removeTempLabels();
                                    //TODO Uygulamanın seçtiği kelime ile benim girdiğim kelime karşılaştırılıyor.
                                    if(puzzle.checkWord()){
                                        puzzle.calculateScore(puzzle.getMatches(),puzzle.getNotMatchesWords());
                                        controlService.getFinishedRound().set(true);
                                        for(JLabel elem: controlService.getWrittenLabels()){
                                            elem.setBorder(BorderFactory.createLineBorder(Color.GREEN));
                                        }
                                        //Eğer doğru bilinmiş ise tebrik ediyor ve skoru dosyaya yazıyor
                                        if(user2==null){
                                            congratulations(user1);
                                            //TODO bu skoru eğer en büyük skorsa dosyaya yazan method
                                            FileService.writeScore(user1);
                                        }else{
                                            if(user1.getIsActive().get()){
                                                congratulations(user1);
                                                FileService.writeScore(user1);
                                            }else{
                                                congratulations(user2);
                                                FileService.writeScore(user2);
                                            }
                                        }
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
                                        if(user2==null){
                                            looseMessage(user1);
                                            controlService.getFinishedRound().set(true);
                                        }else{
                                            if(user1.getIsActive().get()){
                                                looseMessage(user1);
                                                controlService.getFinishedRound().set(true);
                                            }else{
                                                //TODO 2. oyuncunun skorunu düzelt
                                                looseMessage(user2);
                                                controlService.getFinishedRound().set(true);
                                            }
                                        }
                                    }
                                }
                            }
                        });
                        jFrame.add(buttons[i]);
                        //TODO son olarak backspace buttonun yaratıyorum
                    }else if(keyboard[i]==8){
                        //TODO BACKSPACE tuşu
                        buttons[i] =new JButton("<-");
                        buttons[i].setBounds(x + (i-21)*50,y+100,50,30);
                        buttons[i].addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                try{
                                    JLabel lastModified;
                                    if(controlService.getLabelCounter()>0 && (lastModified = controlService.getLastJLabel())!=null && controlService.getWrittenLabels().size()>0)
                                    {
                                        lastModified.setText(" ");
                                        //TODO 4 tane harf girildi bunu sildikten sonra girilmiş harf sayısını 1 azaltıyorum
                                        controlService.getCount().decrementAndGet();
                                        if(controlService.getBuilder().length()>0){
                                            //TODO silinen hafri builder'dan da siliyorum
                                            controlService.getBuilder().deleteCharAt(controlService.getBuilder().length()-1);
                                        }
                                        //TODO yazılmış labellar içinden siliyorum
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

    public static JButton[] createButtonsForKeyBoard(JLabel[] myArray,Point buttonsStartPoint, JButton[] buttons, JFrame jFrame, ControlService controlService, WordlPuzzle puzzle,User user1,User user2){
        //TODO burası klavye ile oynamak için gerekli buttonların ve zamanlayıcının çalıştırıldığı method
        int x=buttonsStartPoint.x;
        int y=buttonsStartPoint.y;
        //TODO TIMER THREAD'IN YARATILMASI
        createTimerThread(user1,user2,controlService);

        for(int i=0;i<31;i++){
            if(i<=9){
                buttons[i] = new JButton(String.valueOf((char) keyboard[i]));
                buttons[i].setBounds(x + i*50,y,50,30);
                buttons[i].setLocale(Locale.getDefault());
                JButton temp=buttons[i];
                //TODO Klavye tuşlarını dinleyen KeyListener
                buttons[i].addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                    }
                    //TODO klavyenin tuşuna basıldığında çalışıyor
                    @Override
                    public void keyPressed(KeyEvent e) {
                        //TODO oyun bittiğinde yeni bir event alma game is over...
                        if(!controlService.getFinishedRound().get()){
                            //TODO silme tuşu
                            if(e.getKeyCode()==8 && controlService.getLabelCounter()!=0){
                                JLabel lastModified= controlService.getLastJLabel();
                                lastModified.setText(" ");
                                controlService.getCount().decrementAndGet();
                                controlService.getBuilder().deleteCharAt(controlService.getBuilder().length()-1);
                                controlService.getWrittenLabels().remove(controlService.getWrittenLabels().size()-1);
                            }
                            //TODO harf yazma işlemleri
                            if(!controlService.getFinishedRound().get() && controlService.getCount().get()<5 && e.getKeyCode()!=8 && e.getKeyCode()!=10) {
                                controlService.getCount().incrementAndGet();
                                try {
                                    JLabel tempLabel = Arrays.stream(myArray).filter(a -> a.getText() == " ").findFirst().get();
                                    tempLabel.setText(String.valueOf((char) Arrays.stream(lowerKeyboard)
                                            .filter(a -> e.getKeyChar() == a).findFirst().getAsInt()).toUpperCase(Locale.getDefault()));
                                    controlService.addJLabel(tempLabel);
                                    //sending data to client
                                    controlService.setTempButton(temp);
                                    //TODO son eklenenlerin de silinmesi lazım
                                    controlService.getBuilder().append(tempLabel.getText());
                                    controlService.getWrittenLabels().add(tempLabel);
                                } catch (NoSuchElementException a) {
                                    System.out.println(e.getKeyCode() + " oyun için geçersiz");
                                }
                            }
                            //TODO enter tuşuna basıldığında
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
                                    //TODO Eğer aktif olan User1 ise User2 değilse user2
                                    if(user2==null){
                                        user1.setWin(true);
                                        FileService.writeStatistic(user1);
                                        congratulations(user1);
                                        FileService.writeScore(user1);
                                    }else{
                                        if(user1.getIsActive().get()){
                                            user1.setWin(true);
                                            FileService.writeStatistic(user1);
                                            congratulations(user1);
                                            FileService.writeScore(user1);

                                        }else{
                                            user2.setWin(true);
                                            FileService.writeStatistic(user2);
                                            congratulations(user2);
                                            FileService.writeScore(user2);
                                            FileService.writeStatistic(user1);
                                        }
                                    }
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
                                    //TODO sıra diğer kullanıcıya geçer.
                                    isSwitch.set(true);
                                    puzzle.calculateScore(puzzle.getMatches(),puzzle.getNotMatchesWords());
                                    System.out.println("skor : "+user1.getScore());
                                }
                                //TODO kontrol edilen kelimeyi builder'dan siliyorum
                                controlService.getBuilder().delete(0,5);
                                //TODO yazılmış labelları temizliyorum
                                controlService.getWrittenLabels().clear();
                                //TODO 5 kere kelime girdim 5'inde de bilemedim o zaman oyun yine bitiyor
                                if (controlService.getRoundCounter().get() == 5) {
                                    puzzle.calculateScore(puzzle.getMatches(),puzzle.getNotMatchesWords());
                                    //TODO kullanıcı 2 null ise bu single player
                                    if(user2==null){
                                        user1.setWin(false);
                                        FileService.writeStatistic(user1);
                                        looseMessage(user1);
                                        controlService.getFinishedRound().set(true);
                                    }else{
                                        //TODO burası multiplayer ise çalışacak
                                        if(user1.getIsActive().get()){
                                            user1.setWin(false);
                                            FileService.writeStatistic(user1);
                                            looseMessage(user1);
                                            controlService.getFinishedRound().set(true);
                                        }else{
                                            //TODO 2. oyuncunun skorunu düzelt
                                            user2.setWin(false);
                                            FileService.writeStatistic(user2);
                                            looseMessage(user2);
                                            controlService.getFinishedRound().set(true);
                                        }
                                    }
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
                if(i<=20){
                    buttons[i] =new JButton(String.valueOf((char) keyboard[i]));
                    buttons[i].setBounds(x + (i-10)*50 ,y+50,50,30);
                    jFrame.add( buttons[i]);

                }else{
                    if(!(keyboard[i]==10) && !(keyboard[i]==8)){
                        buttons[i] =new JButton(String.valueOf((char) keyboard[i]));
                        buttons[i].setBounds(x+ (i-21)*50,y+100,50,30);
                        jFrame.add( buttons[i]);
                    }else if(keyboard[i]==10){
                        buttons[i] = new JButton("");
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
                        buttons[i].setBounds(x,y+100,50,30);
                        jFrame.add(buttons[i]);
                    }else if(keyboard[i]==8){
                        //TODO BACKSPACE tuşu
                        buttons[i] =new JButton("<-");
                        buttons[i].setBounds(x + (i-21)*50,y+100,50,30);
                        jFrame.add(buttons[i]);
                    }
                }
            }
        }
        return buttons;
    }

    private void sendData(JLabel [] myArray,DataOutputStream dOut) {
            while(true){
                //TODO burada bug var
                byte [] message= Arrays.stream(myArray).map(e->e.getText().getBytes(StandardCharsets.UTF_8)[0]).collect(toByteArray());
                //String [] messageString= Arrays.stream(myArray).map(e->e.getText()).toArray(String[]::new);
                try {
                    dOut.writeInt(message.length);
//                    for(int i=0;i<messageString.length;i++){
//                        dOut.writeUTF(messageString[i]);
//                    }
                    //dOut.writeUTF(messageString);
                    dOut.write(message); //burası önemli
//                    for(int i=0;i<message.length;i++){
//                        System.out.println(String.valueOf(message[i]));
//                    }
//                    System.out.println("mesajın uzunluğu : "+message.length);
                    dOut.flush();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


    }

    public static Collector<Byte, ?, byte[]> toByteArray() {
        return Collector.of(ByteArrayOutputStream::new, ByteArrayOutputStream::write, (baos1, baos2) -> {
            try {
                baos2.writeTo(baos1);
                return baos1;
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }, ByteArrayOutputStream::toByteArray);
    }

    public static JLabel[] createLabelsForKeyBoard (JFrame jFrame,Point startPoint){
        JLabel [] myArray = new JLabel[25];
        int count =0;
        for(int i=0;i<5;i++) {
            for (int j = 0; j < 5; j++) {
                myArray[count] = new JLabel(" ");
                JLabel temp = myArray[count++];
                Border blackline = BorderFactory.createLineBorder(Color.black);
                temp.setBorder(blackline);
                temp.setBounds((int)startPoint.getX() + j * 60, (int)startPoint.getY() + i * 60, 50, 50);
                jFrame.add(temp);
            }
        }
        return myArray;
    }

    public static JLabel[] createLabelsForMouse(JFrame jFrame,ControlService service,Point point){
        JLabel [] myArray = new JLabel[25];
        int count =0;
        for(int i=0;i<5;i++) {
            for (int j = 0; j < 5; j++) {
                myArray[count] = new JLabel(" ");
                JLabel temp = myArray[count++];
                Border blackline = BorderFactory.createLineBorder(Color.black);
                temp.setBorder(blackline);
                temp.setBounds((int)point.getX() + j * 60, (int)point.getY() + i * 60, 50, 50);
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
        //BURALAR DOSYADAN OKUNACAK...
        StringBuilder builder=new StringBuilder();
        Optional<User> temp= FileService.findUserFromStatisticFile(user);
        if(temp.isPresent()){
            User tempUser=temp.get();
            user.setPlayedCount(tempUser.getPlayedCount());
            user.setWined(tempUser.getWined());
            user.setLoosed(tempUser.getLoosed());
        }else{
            user.setPlayedCount(1);
            if(user.isWin())
                user.setWined(1);
            else
                user.setLoosed(1);
        }
        builder.append("Üzgünüz \n");
        builder.append("Kullanıcı: "+user.getUserName()+" \n");
        builder.append("Skor: "+user.getScore()+" \n");
        builder.append("Oynanan Oyun Sayısı: "+user.getPlayedCount()+" \n");
        builder.append("Kazanılan: "+user.getWined()+" \n");
        builder.append("Kaybedilen :"+ user.getLoosed()+" \n");
        builder.append("Sonuç: Oyunu Kaybettiniz \n");
        GameService.showLooseMessage(builder.toString());
    }

    private static void congratulations(User user) {
        StringBuilder builder=new StringBuilder();
        Optional<User> temp= FileService.findUserFromStatisticFile(user);
        if(temp.isPresent()){
            User tempUser=temp.get();
            user.setPlayedCount(tempUser.getPlayedCount());
            user.setWined(tempUser.getWined());
            user.setLoosed(tempUser.getLoosed());
        }else{
            user.setPlayedCount(1);
            if(user.isWin())
                user.setWined(1);
            else
                user.setLoosed(1);
        }
        builder.append("Tebrikler \n");
        builder.append("Kullanıcı: "+user.getUserName()+" \n");
        builder.append("Skor: "+user.getScore()+" \n");
        builder.append("Oynanan Oyun Sayısı: "+user.getPlayedCount()+" \n");
        builder.append("Kazanılan: "+user.getWined()+" \n");
        builder.append("Kaybedilen :"+ user.getLoosed()+" \n");
        builder.append("Sonuç: oyunu kazandınız \n");
        GameService.showCongratulations(builder.toString());
    }
}

package A_Giris.service;

import javax.swing.*;

public class GameService {
    //private

    public static void showWarning(String message){
        JOptionPane.showMessageDialog(null,message,"Uyarı",JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showCongratulations(String messsage){
        JOptionPane.showMessageDialog(null,messsage,"Tebrikler",JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showLooseMessage(String message){
        JOptionPane.showMessageDialog(null,message,"Üzgünüm",JOptionPane.INFORMATION_MESSAGE);
    }
}

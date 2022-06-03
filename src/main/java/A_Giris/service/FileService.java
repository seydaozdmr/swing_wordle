package A_Giris.service;

import A_Giris.model.User;

import java.io.*;

public class FileService {

    public static void main(String[] args) {
        FileService service=new FileService();
        service.readScore();
        writeScore(new User("hasan",12));
    }


    public static void writeScore(User user){
        String userDetails=user.getUserName()+","+user.getScore();
        char[] userDetailsChar= userDetails.toCharArray();
        int getScore=readScore();
        if(user.getScore()>getScore){
            try(FileOutputStream out= new FileOutputStream(new File("score.txt"))){
                for(int i=0;i<userDetailsChar.length;i++){
                    out.write(userDetailsChar[i]);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static int readScore(){
        Integer result=0;
        try(FileInputStream in = new FileInputStream(new File("score.txt"))) {
            int c;
            String input="";
            while((c = in.read()) != -1){
                input+=((char) c);
            }
            if(!input.equals("")){
                result=  Integer.parseInt(input.split(",")[1]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("file nor exist");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(result);
        return result;
    }
}

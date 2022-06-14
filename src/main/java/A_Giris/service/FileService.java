package A_Giris.service;

import A_Giris.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileService {

//    public static void main(String[] args) {
//        FileService service=new FileService();
//        service.readScore();
//        writeScore(new User("hasan",12));
//    }


    //TODO write score ve read score high scoru yazıyor ve okuyor
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

    public static List<User> readStatistic(){
        List<User> users=new ArrayList<>();

        try(BufferedReader fileReader = new BufferedReader(new FileReader("statistic.txt"))) {
            String l;
            while((l=fileReader.readLine())!=null){
                String [] fetchedData = l.split(",");
                User user= new User(fetchedData[0],Integer.valueOf(fetchedData[1]),Integer.valueOf(fetchedData[2]),Integer.valueOf(fetchedData[3]),Integer.valueOf(fetchedData[4]));
                users.add(user);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static Optional<User> findUserFromStatisticFile(User user){
        List<User> getUsersFromFile= readStatistic();
        if(!getUsersFromFile.isEmpty())
            return  getUsersFromFile.stream().filter(e->e.getUserName().equals(user.getUserName())).findFirst();
        else
            return Optional.empty();
    }


    public static void writeStatistic(User user1) {
        List<User> getUsersFromFile= readStatistic();
        if(!getUsersFromFile.isEmpty()){
            Optional<User> getUser= getUsersFromFile.stream().filter(e->e.getUserName().equals(user1.getUserName())).findFirst();
            if(getUser.isPresent()){
                int count=0;
                for(User u:getUsersFromFile){
                    if(u.getUserName().equals(user1.getUserName()))
                        break;
                    count++;
                }
                User temp = getUser.get();
                temp.setScore(user1.getScore());
                temp.setPlayedCount(temp.getPlayedCount()+1);
                if(user1.isWin())
                    temp.setWined(temp.getWined()+1);
                else
                    temp.setLoosed(temp.getLoosed()+1);
                getUsersFromFile.set(count,temp);
            }
            else{
                user1.setPlayedCount(1);
                if(user1.isWin())
                    user1.setWined(1);
                else
                    user1.setLoosed(1);
                getUsersFromFile.add(user1);
            }
        }else{
            user1.setPlayedCount(1);
            if(user1.isWin())
                user1.setWined(1);
            else
                user1.setLoosed(1);
            getUsersFromFile.add(user1);
        }
        writeScoresToStatisticFile(getUsersFromFile);
    }

    private static void writeScoresToStatisticFile(List<User> getUsersFromFile) {
        try(BufferedWriter fileWriter = new BufferedWriter(new FileWriter("statistic.txt"))) {
            StringBuilder builder=new StringBuilder();
            for(int i=0;i<getUsersFromFile.size();i++){
                builder = new StringBuilder();
                User temp= getUsersFromFile.get(i);
                builder.append(temp.getUserName());
                builder.append(",");
                builder.append(temp.getScore());
                builder.append(",");
                builder.append(temp.getPlayedCount());
                builder.append(",");
                builder.append(temp.getWined());
                builder.append(",");
                builder.append(temp.getLoosed());
                builder.append("\n");
                fileWriter.write(builder.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

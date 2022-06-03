package A_Giris.model;

import java.util.List;

public class WordList {
    public static List<String> words;


    public WordList() {


    }

    public static void createWords(){
        words=  List.of("kalem","kağıt","durak","deniz","takip");
    }


}

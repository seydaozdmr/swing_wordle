package A_Giris.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WordlPuzzle {
    private char[] word;
    private boolean[] matches;
    private boolean[] notMatchesWords;
    private String chosenWord;
    private Map<Character, Integer> notFoundChars;
    private User user1;
    private User user2;

    public WordlPuzzle(User user1) {
        this.user1 =user1;
        this.word = new char[5];
        matches = new boolean[5];
        Random r = new Random();
        WordList.createWords();
        chosenWord = WordList.words.get(r.nextInt(WordList.words.size())).toUpperCase();
        System.out.println("seçilen kelime: " + chosenWord);
        notMatchesWords = new boolean[5];
        notFoundChars = new HashMap<>();
    }
    public WordlPuzzle(User user1,User user2){
        this(user1);
        this.user2=user2;
    }

    public void addWord(String s) {
        word = s.toCharArray();

    }

    public boolean checkWord() {
        matches = new boolean[5];
        notMatchesWords = new boolean[5];
        notFoundChars.clear();

        boolean result = true;
        char[] chosen = chosenWord.toCharArray();
        for (int i = 0; i < 5; i++) {
            if (word[i] == chosen[i]) {
                matches[i] = true;
            } else {

                notFoundChars.merge(chosen[i], 1, Integer::sum);
                result = false;
            }
        }

        for (int i = 0; i < 5; i++) {
            if (!matches[i]) {
                for (int j = 0; j < 5; j++) {

                    if (chosen[i] == word[j] && notFoundChars.get(word[j]) >= 0) {
                        //Eğer chosen kelime içinde iki tane harf var ve tahminde de iki tane harf var fakat
                        //farklı bir yerdeyse ve ve notMatches(sarı) arıyorsak ve ilkini bulduktan sonra diğerini bulmak için döngüyü
                        //continue ile devam ettiriyoruz.
                        if(notMatchesWords[j]==true)
                            continue;
                        notMatchesWords[j] = true;
                        notFoundChars.merge(word[j], -1, Integer::sum);
                        break;
                    }
                }
            }
        }

        return result;
    }

    public void calculateScore(boolean[] matches, boolean[] notMatchesWords){
        for(int i=0;i<5;i++){
            if(matches[i]){
                //Bilinen kelimelere puan verilebilir
                user1.setScore(user1.getScore());
                continue;
            }
            if(notMatchesWords[i]){
                user1.setScore(user1.getScore()-20);
                continue;
            }
            if(!matches[i] && !notMatchesWords[i]){
                user1.setScore(user1.getScore()-100);
                continue;
            }
        }
    }

    public boolean[] getMatches() {
        return matches;
    }


    public boolean[] getNotMatchesWords() {
        return notMatchesWords;
    }

}






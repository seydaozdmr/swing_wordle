package A_Giris.game;

import A_Giris.model.WordlPuzzle;

import javax.swing.*;
import java.io.IOException;

public interface MultiPlayerGame {
    void createAndShowGui(WordlPuzzle puzzle, JFrame jFrame) throws IOException;
}

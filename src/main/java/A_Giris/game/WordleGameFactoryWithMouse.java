package A_Giris.game;

import A_Giris.model.User;

public class WordleGameFactoryWithMouse implements WordlGameFactory {

    @Override
    public SingleWordleGame createSingleWordleGame(User user) {

        return new WordleGameWithMouse(user);
    }

    @Override
    public MultiPlayerGame createMultiplayerWordleGame(User user1,User user2) {
        return null;
    }
}

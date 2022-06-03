package A_Giris.game;

import A_Giris.model.User;

public class WordleGameFactoryWithKeyboard implements WordlGameFactory{


    @Override
    public SingleWordleGame createSingleWordleGame(User user) {
        return new WordleGameWithKeyboard(user);

    }

    @Override
    public MultiPlayerGame createMultiplayerWordleGame(User user1, User user2) {
        return new MultiplayerWordleGameWithKeyboard(user1,user2);
    }
}

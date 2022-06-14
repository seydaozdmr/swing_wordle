package A_Giris.game;

import A_Giris.model.User;

public class WordleGameFactoryWithKeyboard implements WordlGameFactory{
    //TODO klavye ile oynanacak oyunları yaratıyor

    @Override
    public SingleWordleGame createSingleWordleGame(User user,boolean hasVisitor) {
        return new WordleGameWithKeyboard(user,hasVisitor);

    }

    @Override
    public MultiPlayerGame createMultiplayerWordleGame(User user1, User user2 , boolean hasVisitor) {
        return new MultiplayerWordleGameWithKeyboard(user1,user2,hasVisitor);
    }
}

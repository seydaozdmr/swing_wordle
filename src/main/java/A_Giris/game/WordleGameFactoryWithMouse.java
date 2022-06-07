package A_Giris.game;

import A_Giris.model.User;

public class WordleGameFactoryWithMouse implements WordlGameFactory {

    @Override
    public SingleWordleGame createSingleWordleGame(User user,boolean hasVisitor) {

        return new WordleGameWithMouse(user,hasVisitor);
    }

    @Override
    public MultiPlayerGame createMultiplayerWordleGame(User user1,User user2,boolean hasVisitor) {
        return new MultiPlayerWordleGameWithMouse(user1,user2,hasVisitor);
    }
}

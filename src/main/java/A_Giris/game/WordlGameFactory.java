package A_Giris.game;

import A_Giris.model.User;

public interface WordlGameFactory {
    //TODO Bu interface single ve multiplayer oyun seçeneklerinin arayüzü
    SingleWordleGame createSingleWordleGame(User user,boolean hasVisitor);

    MultiPlayerGame createMultiplayerWordleGame(User user1,User user2 , boolean hasVisitor);

}

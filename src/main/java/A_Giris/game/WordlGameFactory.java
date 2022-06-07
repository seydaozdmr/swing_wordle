package A_Giris.game;

import A_Giris.model.User;

public interface WordlGameFactory {

    SingleWordleGame createSingleWordleGame(User user,boolean hasVisitor);

    MultiPlayerGame createMultiplayerWordleGame(User user1,User user2 , boolean hasVisitor);



}

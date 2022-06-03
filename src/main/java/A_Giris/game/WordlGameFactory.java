package A_Giris.game;

import A_Giris.model.User;

public interface WordlGameFactory {

    SingleWordleGame createSingleWordleGame(User user);

    MultiPlayerGame createMultiplayerWordleGame(User user1,User user2);



}

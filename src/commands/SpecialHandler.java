package commands;

import Battleship.Battleship;
import Battleship.PointValues;
import Exceptions.InvalidPositionException;
import Exceptions.InvalidSyntaxException;
import Exceptions.NotEnoughPointsException;
import Exceptions.PlayerNotFoundException;
import MessagesAndPrinter.Messages;

import java.io.IOException;

public class SpecialHandler implements CommandHandler {

    @Override
    public void execute(Battleship.PlayerHandler playerHandler, Battleship game) {
        try {
            checkPlayerPoints(playerHandler);
            playerHandler.getCharacter().special(playerHandler, game);
            playerHandler.setPlayerPoints(playerHandler.getPlayerPoints() - PointValues.SPECIAL.getPoints());
        } catch (PlayerNotFoundException e) {
            //TODO mensagem desconecção
            game.closeGame();
            throw new RuntimeException(e);
        } catch (InvalidSyntaxException | InvalidPositionException | NotEnoughPointsException e) {
            playerHandler.sendMessage(e.getMessage());
            try {
                playerHandler.takeTurn();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    private void checkPlayerPoints(Battleship.PlayerHandler playerHandler) throws NotEnoughPointsException {
        if (playerHandler.getPlayerPoints() < PointValues.SPECIAL.getPoints()) {
            throw new NotEnoughPointsException(Messages.NOT_ENOUGH_POINTS);
        }
    }
}

package commands;

import Battleship.Battleship;
import Battleship.PointValues;
import Battleship.ships.Ship;
import Exceptions.InvalidSyntaxException;
import Exceptions.NotEnoughPointsException;
import Exceptions.PlayerNotFoundException;
import MessagesAndPrinter.Messages;

import java.util.List;
import java.util.Optional;

public class CommandHelper {
    public static boolean isNotNumber(String number) {
        for (char digit : number.toCharArray()) {
            if (!Character.isDigit(digit)) {
                return true;
            }
        }
        return false;
    }

    public static void checkValidInput(String[] separated) throws InvalidSyntaxException {
        if (separated.length != 3) {
            throw new InvalidSyntaxException(Messages.INVALID_PLACEMENT_SYNTAX);
        }
        if (isNotNumber(separated[1])) {
            throw new InvalidSyntaxException(Messages.INVALID_PLACEMENT_SYNTAX);
        }
        if (separated[2].charAt(0) < 65 || separated[2].charAt(0) > 90) {
            throw new InvalidSyntaxException(Messages.INVALID_PLACEMENT_SYNTAX);
        }
    }

    public static Battleship.PlayerHandler getOpponent(Battleship game, Battleship.PlayerHandler playerHandler) throws PlayerNotFoundException {
        Optional<Battleship.PlayerHandler> opponent = game.getOtherPlayer(playerHandler);
        if (opponent.isEmpty()) {
            throw new PlayerNotFoundException(Messages.PLAYER_DISCONNECTED);
        }
        return opponent.get();
    }

    public static void checkPlayerPoints(Battleship.PlayerHandler playerHandler, PointValues type) throws NotEnoughPointsException {
        if (playerHandler.getPlayerPoints() < type.getPoints()) {
            throw new NotEnoughPointsException(Messages.NOT_ENOUGH_POINTS);
        }
    }

    public static boolean checkInvalidPosition(int row, int col, List<List<String>> opponentMap) {
        if (row < 1 || row >= opponentMap.size() - 2 || col < 1 || col >= opponentMap.get(1).size() - 2) {
            return true;
        }
        String position = opponentMap.get(row).get(col);
        if (position.length() > 1) {
            return true;
        }
        char positionChar = position.charAt(0);
        return positionChar == ' ' || positionChar == '*' || positionChar == 'R';
    }

    public static boolean checkForMine(Battleship.PlayerHandler otherPlayer, int row, int col) {
        return otherPlayer.getMyMap().get(row).get(col).charAt(0) == 'O';
    }

    public static void mineExplosion(Battleship.PlayerHandler player, Battleship.PlayerHandler opponent, int row, int col) {
        opponent.getMyMap().get(row).set(col, "\u001B[34mR\u001B[0m");
        player.getOppMap().get(row).set(col, "\u001B[34mR\u001B[0m");

        int randRow = (int) (Math.random() * (player.getMyMap().size() - 4 + 1) + 1);
        int randCol = (int) (Math.random() * (player.getMyMap().get(0).size() - 4 + 1) + 1);
        Ship ship = opponent.checkIfHit(row, col);
        if (ship != null) {
            player.getMyMap().get(randRow).set(randCol, "\u001B[31mX\u001B[0m");
            return;
        }
        player.getMyMap().get(randRow).set(randCol, "\u001B[34mX\u001B[0m");
    }
}

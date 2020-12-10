package tictactoe;

import java.util.Random;

public class AI {

    public enum Level{
        EASY,
        MEDIUM,
        HARD;
    }

    Level level;

    public void setLevel(Level level) {
        this.level = level;
    }

    public int[] move(String[][] gameField, String symbol) {

        System.out.println("Making move level \""+ level.name().toLowerCase() +"\"");
        Random random = new Random();
        int computerFirstCoordinate;
        int computerSecondCoordinate;

        String opponentSymbol;
        if(symbol.equals("X")) {
            opponentSymbol = "O";
        } else {
            opponentSymbol = "X";
        }

        if (this.level == Level.EASY) {
            while (true) {
                computerFirstCoordinate = random.nextInt(3) + 1;
                computerSecondCoordinate = random.nextInt(3) + 1;
                if (!gameField[Main.processInputCoordinates(computerSecondCoordinate)][computerFirstCoordinate * 2].equals(" ")) {
                    continue;
                }
                break;
            }
        } else /*if (this.level == Level.MEDIUM)*/ {
            while (true) {
                int[] computerMoves = chooseNextMove(gameField, symbol);
                int[] opponentMoves = chooseNextMove(gameField, opponentSymbol);
                if (computerMoves[0] == 0 && opponentMoves[0] == 0) {
                    computerFirstCoordinate = random.nextInt(3) + 1;
                    computerSecondCoordinate = random.nextInt(3) + 1;
                    if (!gameField[Main.processInputCoordinates(computerSecondCoordinate)][computerFirstCoordinate * 2].equals(" ")) {
                        continue;
                    }
                    break;
                } else if (computerMoves[0] != 0 && opponentMoves[0] == 0) {
                    computerFirstCoordinate = computerMoves[0];
                    computerSecondCoordinate = computerMoves[1];
                    if (!gameField[Main.processInputCoordinates(computerSecondCoordinate)][computerFirstCoordinate * 2].equals(" ")) {
                        computerFirstCoordinate = random.nextInt(3) + 1;
                        computerSecondCoordinate = random.nextInt(3) + 1;
                        if (!gameField[Main.processInputCoordinates(computerSecondCoordinate)][computerFirstCoordinate * 2].equals(" ")) {
                            continue;
                        }
                        break;
                    }
                    break;
                } else  {
                    computerFirstCoordinate = opponentMoves[0];
                    computerSecondCoordinate = opponentMoves[1];
                    if (!gameField[Main.processInputCoordinates(computerSecondCoordinate)][computerFirstCoordinate * 2].equals(" ")) {
                        computerFirstCoordinate = random.nextInt(3) + 1;
                        computerSecondCoordinate = random.nextInt(3) + 1;
                        if (!gameField[Main.processInputCoordinates(computerSecondCoordinate)][computerFirstCoordinate * 2].equals(" ")) {
                            continue;
                        }
                        break;
                    }
                    break;
                }
            }
        }
        return new int[]{computerFirstCoordinate, computerSecondCoordinate};
    }

    public int[] chooseNextMove(String[][] gameField, String symbol) {
        int inRowCounter = 0;

//        if (symbol.equals("X")) {
//            inRowCounter = xInRowCounter;
//        } else {
//            inRowCounter = oInRowCounter;
//        }

        /* In row check */
        for (int i = 1; i < 4; i++) {
            for (int j = 1; j < 7; j++) {
                if (j % 2 != 0) {
                    continue;
                }
                if (gameField[i][j].equals(symbol)) {
                    inRowCounter++;
                }
                if (inRowCounter == 2 && j == 4) {
                    return new int[]{3, i};
                }
                if (inRowCounter == 2 && j == 6 && gameField[i][4].equals(symbol)) {
                    return new int[]{1, i};
                }
                if (inRowCounter == 2 && j == 6) {
                    return new int[]{2, i};
                }
            }
            inRowCounter = 0;
        }

        inRowCounter = 0;
        /* In column check */
        for (int j = 1; j < 7; j++) {
            for (int i = 1; i < 4; i++) {
                if (j % 2 != 0) {
                    continue;
                }
                if (gameField[i][j].equals(symbol)) {
                    inRowCounter++;
                }
                if (inRowCounter == 2 && i == 2) {
                    return new int[]{j/2, 1};
                }
                if (inRowCounter == 2 && i == 3 && gameField[2][j].equals(symbol)) {
                    return new int[]{j/2, 3};
                }
                if (inRowCounter == 2 && i == 3) {
                    return new int[]{j/2, 2};
                }
            }
            inRowCounter = 0;
        }

        inRowCounter = 0;
        /* Cross check 1st diagonal */
        for (int i = 1; i < 4; i++) {
            for (int j = 1; j < 7; j++) {
                if (j % 2 != 0) {
                    continue;
                }
                if (j == i * 2 && gameField[i][j].equals(symbol)) {
                    inRowCounter++;
                }
                if (inRowCounter == 2 && i == 2 && j == 4) {
                    return new int[]{3, 1};
                }
                if (inRowCounter == 2 && i == 3 && j == 6 && gameField[2][4].equals(symbol)) {
                    return new int[]{1, 3};
                }
                if (inRowCounter == 2 && i == 3 && j == 6) {
                    return new int[]{2, 2};
                }
            }
        }

        inRowCounter = 0;
        /* Cross check 2nd diagonal */
        for (int i = 1; i < 4; i++) {
            for (int j = 1; j < 7; j++) {
                if (j % 2 != 0) {
                    continue;
                }
                if (j == 9 - 1 - 2 * i && gameField[i][j].equals(symbol)) {
                    inRowCounter++;
                }
                if (inRowCounter == 2 && i == 2 && j == 4) {
                    return new int[]{1, 1};
                }
                if (inRowCounter == 2 && i == 3 && j == 2 && gameField[2][4].equals(symbol)) {
                    return new int[]{3, 3};
                }
                if (inRowCounter == 2 && i == 3 && j == 2) {
                    return new int[]{2, 2};
                }
            }
        }

        return new int[]{0, 0};
    }

}

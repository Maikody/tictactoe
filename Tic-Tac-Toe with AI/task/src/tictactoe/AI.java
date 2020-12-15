package tictactoe;

import java.util.*;

public class AI {

    public enum Level{
        EASY,
        MEDIUM,
        HARD
    }

    Level level;

    public void setLevel(Level level) {
        this.level = level;
    }

    public int[] move(String[][] gameField, String symbol) {

        System.out.println("Move by level \""+ level.name().toLowerCase() +"\":");
        int computerFirstCoordinate;
        int computerSecondCoordinate;

        String opponentSymbol;
        if(symbol.equals("X")) {
            opponentSymbol = "O";
        } else {
            opponentSymbol = "X";
        }

        Random random = new Random();

        if (this.level == Level.EASY) {
            while (true) {
                computerFirstCoordinate = random.nextInt(3) + 1;
                computerSecondCoordinate = random.nextInt(3) + 1;
                if (!gameField[computerFirstCoordinate][computerSecondCoordinate * 2].equals(" ")) {
                    continue;
                }
                break;
            }
        } else if (this.level == Level.MEDIUM) {
            while (true) {
                int[] computerMoves = chooseNextMoveLevelMedium(gameField, symbol);
                int[] opponentMoves = chooseNextMoveLevelMedium(gameField, opponentSymbol);
                if (computerMoves[0] == 0 && opponentMoves[0] == 0) {
                    computerFirstCoordinate = random.nextInt(3) + 1;
                    computerSecondCoordinate = random.nextInt(3) + 1;
                    if (!gameField[computerFirstCoordinate][computerSecondCoordinate * 2].equals(" ")) {
                        continue;
                    }
                    break;
                } else if (computerMoves[0] != 0 && opponentMoves[0] == 0) {
                    computerFirstCoordinate = computerMoves[0];
                    computerSecondCoordinate = computerMoves[1];
                    if (!gameField[computerFirstCoordinate][computerSecondCoordinate * 2].equals(" ")) {
                        computerFirstCoordinate = random.nextInt(3) + 1;
                        computerSecondCoordinate = random.nextInt(3) + 1;
                        if (!gameField[computerFirstCoordinate][computerSecondCoordinate * 2].equals(" ")) {
                            continue;
                        }
                        break;
                    }
                    break;
                } else  {
                    computerFirstCoordinate = opponentMoves[0];
                    computerSecondCoordinate = opponentMoves[1];
                    if (!gameField[computerFirstCoordinate][computerSecondCoordinate * 2].equals(" ")) {
                        computerFirstCoordinate = random.nextInt(3) + 1;
                        computerSecondCoordinate = random.nextInt(3) + 1;
                        if (!gameField[computerFirstCoordinate][computerSecondCoordinate * 2].equals(" ")) {
                            continue;
                        }
                        break;
                    }
                    break;
                }
            }
        } else {
            int[] computerMoves = bestMoveLevelHard(gameField, symbol);
            computerFirstCoordinate = computerMoves[0];
            computerSecondCoordinate = computerMoves[1] / 2;
        }
        return new int[]{computerFirstCoordinate, computerSecondCoordinate};
    }

    public int[] chooseNextMoveLevelMedium(String[][] gameField, String symbol) {
        int inRowCounter = 0;

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
                    return new int[]{i, 3};
                }
                if (inRowCounter == 2 && j == 6 && gameField[i][4].equals(symbol)) {
                    return new int[]{i, 1};
                }
                if (inRowCounter == 2 && j == 6 && gameField[i][4].equals(" ")) {
                    return new int[]{i, 2};
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
                    return new int[]{3, j/2};
                }
                if (inRowCounter == 2 && i == 3 && gameField[2][j].equals(symbol)) {
                    return new int[]{1, j/2};
                }
                if (inRowCounter == 2 && i == 3 && gameField[2][j].equals(" ")) {
                    return new int[]{2, j/2};
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
                    return new int[]{3, 3};
                }
                if (inRowCounter == 2 && i == 3 && j == 6 && gameField[2][4].equals(symbol)) {
                    return new int[]{1, 1};
                }
                if (inRowCounter == 2 && i == 3 && j == 6 && gameField[2][4].equals(" ")) {
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
                    return new int[]{3, 1};
                }
                if (inRowCounter == 2 && i == 3 && j == 4 && gameField[2][4].equals(symbol)) {
                    return new int[]{1, 3};
                }
                if (inRowCounter == 2 && i == 3 && j == 4 && gameField[2][4].equals(" ")) {
                    return new int[]{2, 2};
                }
            }
        }

        return new int[]{0, 0};
    }

    public int[] bestMoveLevelHard(String[][] gameField, String symbol){
        int[] move = new int[2];

        int bestScore = Integer.MIN_VALUE;
        for (int i = 1; i < 4; i++) {
            for (int j = 1; j < 7; j++) {
                if (j % 2 != 0) {
                    continue;
                }
                if (gameField[i][j].equals(" ")) {
                    gameField[i][j] = symbol;
                    int score;
                    if(symbol.equals("X")) {
                        score = minimaxX(gameField, false);
                    } else {
                        score = minimaxO(gameField, false);
                    }
                    gameField[i][j] = " ";
                    if (score > bestScore) {
                        bestScore = score;
                        move[0] = i;
                        move[1] = j;
                    }
                }
            }
        }

        return move;
    }

    public int minimaxX(String[][] gameField, boolean isMaximizing){

        List<int[]> availableSpots = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            for (int j = 1; j < 7; j++) {
                if (j % 2 != 0) {
                    continue;
                }
                if (gameField[i][j].equals(" ")) {
                    availableSpots.add(new int[]{i, j});
                }
            }
        }

        if (checkIfWinning(gameField, "X")) {
            return 10;
        }
        else if (checkIfWinning(gameField, "O")) {
            return -10;
        }
        else if (availableSpots.size() == 0) {
            return 0;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 1; i < 4; i++) {
                for (int j = 1; j < 7; j++) {
                    if (j % 2 != 0) {
                        continue;
                    }
                    if (gameField[i][j].equals(" ")) {
                        gameField[i][j] = "X";
                        int score = minimaxX(gameField, false);
                        gameField[i][j] = " ";
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 1; i < 4; i++) {
                for (int j = 1; j < 7; j++) {
                    if (j % 2 != 0) {
                        continue;
                    }
                    if (gameField[i][j].equals(" ")) {
                        gameField[i][j] = "O";
                        int score = minimaxX(gameField, true);
                        gameField[i][j] = " ";
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }


    public int minimaxO(String[][] gameField, boolean isMaximizing){

        List<int[]> availableSpots = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            for (int j = 1; j < 7; j++) {
                if (j % 2 != 0) {
                    continue;
                }
                if (gameField[i][j].equals(" ")) {
                    availableSpots.add(new int[]{i, j});
                }
            }
        }

        if (checkIfWinning(gameField,"O")) {
            return 10;
        }
        else if (checkIfWinning(gameField,"X")) {
            return -10;
        }
        else if (availableSpots.size() == 0) {
            return 0;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 1; i < 4; i++) {
                for (int j = 1; j < 7; j++) {
                    if (j % 2 != 0) {
                        continue;
                    }
                    if (gameField[i][j].equals(" ")) {
                        gameField[i][j] = "O";
                        int score = minimaxO(gameField, false);
                        gameField[i][j] = " ";
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 1; i < 4; i++) {
                for (int j = 1; j < 7; j++) {
                    if (j % 2 != 0) {
                        continue;
                    }
                    if (gameField[i][j].equals(" ")) {
                        gameField[i][j] = "X";
                        int score = minimaxO(gameField, true);
                        gameField[i][j] = " ";
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }

    public boolean checkIfWinning(String[][] gameField, String symbol) {
        int inRowCounter = 0;

        /* In row check */
        for (int i = 1; i < 4; i++) {
            for (int j = 1; j < 7; j++) {
                if (gameField[i][j].equals(symbol)) {
                    inRowCounter++;
                }
            }
            if (inRowCounter == 3) {
                return true;
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
            }
            if (inRowCounter == 3) {
                return true;
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
            }
            if (inRowCounter == 3) {
                return true;
            }
        }

        inRowCounter = 0;
        /* Cross check 2nd diagonal */
        for (int i = 1; i < 4; i++) {
            for (int j = 1; j < 7; j++) {
                if (j % 2 != 0) {
                    continue;
                }
                if (j == 6 * i && gameField[i][j].equals("X")) {
                    inRowCounter++;
                }
            }
            if (inRowCounter == 3) {
                return true;
            }

        }
        return false;
    }

}

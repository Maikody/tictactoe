package tictactoe;

import java.util.*;

public class AI {

    public enum Level{
        EASY,
        MEDIUM,
        HARD
    }

    private String aiSymbol;
    private final Level level;

    public AI(Level level) {
        this.level = level;
    }

    public void setAiSymbol(String aiSymbol) {
        this.aiSymbol = aiSymbol;
    }

    public int[] move(String[][] gameField) {

        System.out.println("Move by level \"" + level.name().toLowerCase() + "\":");
        int computerFirstCoordinate = 0;
        int computerSecondCoordinate = 0;

        String opponentSymbol = aiSymbol.toUpperCase().equals("X") ? "O": "X";

        Random random = new Random();
        List<int[]> freeSpots = getAvailableSpots(gameField);

        switch (this.level) {
            case EASY:
                int[] easyMove = freeSpots.get(random.nextInt(freeSpots.size()));
                computerFirstCoordinate = easyMove[0];
                computerSecondCoordinate = easyMove[1] / 2;
                break;
            case MEDIUM:
                int[] computerMediumMoves = chooseNextMoveLevelMedium(gameField, aiSymbol);
                int[] opponentMediumMoves = chooseNextMoveLevelMedium(gameField, opponentSymbol);
                if (computerMediumMoves[0] == 0 && opponentMediumMoves[0] == 0) {
                    int[] mediumMove = freeSpots.get(random.nextInt(freeSpots.size()));
                    computerFirstCoordinate = mediumMove[0];
                    computerSecondCoordinate = mediumMove[1] / 2;
                } else if (computerMediumMoves[0] != 0 && opponentMediumMoves[0] == 0) {
                    computerFirstCoordinate = computerMediumMoves[0];
                    computerSecondCoordinate = computerMediumMoves[1];
                } else {
                    computerFirstCoordinate = opponentMediumMoves[0];
                    computerSecondCoordinate = opponentMediumMoves[1];
                }
                break;
            case HARD:
                if (freeSpots.size() == 9) {
                    int[] hardFirstMove = freeSpots.get(random.nextInt(freeSpots.size()));
                    computerFirstCoordinate = hardFirstMove[0];
                    computerSecondCoordinate = hardFirstMove[1] / 2;
                } else {
                    int[] computerHardMoves = bestMoveHard(gameField);
                    computerFirstCoordinate = computerHardMoves[0];
                    computerSecondCoordinate = computerHardMoves[1] / 2;
                }
                break;
        }

        return new int[]{computerFirstCoordinate, computerSecondCoordinate};
    }


    public int[] chooseNextMoveLevelMedium(String[][] gameField, String symbol) {
        int inRowCounter = 0;

        /* In row check */
        for (int i = 1; i < 4; i++) {
            for (int j = 2; j < 7; j++) {
                if (j % 2 != 0) {
                    continue;
                }
                if (gameField[i][j].equals(symbol)) {
                    inRowCounter++;
                }
                if (inRowCounter == 2 && j == 4 && gameField[i][6].equals(" ")) {
                    return new int[]{i, 3};
                }
                if (inRowCounter == 2 && j == 6 && gameField[i][4].equals(symbol) && gameField[i][2].equals(" ")) {
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
        for (int j = 2; j < 7; j++) {
            for (int i = 1; i < 4; i++) {
                if (j % 2 != 0) {
                    continue;
                }
                if (gameField[i][j].equals(symbol)) {
                    inRowCounter++;
                }
                if (inRowCounter == 2 && i == 2 && gameField[3][j].equals(" ")) {
                    return new int[]{3, j/2};
                }
                if (inRowCounter == 2 && i == 3 && gameField[2][j].equals(symbol) && gameField[1][j].equals(" ")) {
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
            for (int j = 2; j < 7; j++) {
                if (j % 2 != 0) {
                    continue;
                }
                if (j == i * 2 && gameField[i][j].equals(symbol)) {
                    inRowCounter++;
                }
                if (inRowCounter == 2 && i == 2 && j == 4 && gameField[3][6].equals(" ")) {
                    return new int[]{3, 3};
                }
                if (inRowCounter == 2 && i == 3 && j == 6 && gameField[2][4].equals(symbol) && gameField[1][2].equals(" ")) {
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
            for (int j = 2; j < 7; j++) {
                if (j % 2 != 0) {
                    continue;
                }
                if (j == - 2 * i + 8 && gameField[i][j].equals(symbol)) {
                    inRowCounter++;
                }
                if (inRowCounter == 2 && i == 2 && j == 4 && gameField[3][2].equals(" ")) {
                    return new int[]{3, 1};
                }
                if (inRowCounter == 2 && i == 3 && j == 2 && gameField[2][4].equals(symbol) && gameField[3][6].equals(" ")) {
                    return new int[]{1, 3};
                }
                if (inRowCounter == 2 && i == 3 && j == 2 && gameField[2][4].equals(" ")) {
                    return new int[]{2, 2};
                }
            }
        }

        return new int[]{0, 0};
    }


    public List<int[]> getAvailableSpots(String[][] gameField) {
        List<int[]> availableSpots = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            for (int j = 2; j < 7; j++) {
                if (j % 2 != 0) {
                    continue;
                }
                if (gameField[i][j].equals(" ")) {
                    availableSpots.add(new int[]{i, j});
                }
            }
        }
        return availableSpots;
    }


    public int[] bestMoveHard(String[][] gameField) {
        int[] bestMove = new int[2];

        int bestScore = Integer.MIN_VALUE;
        for (int i = 1; i < 4; i++) {
            for (int j = 2; j < 7; j++) {
                if (j % 2 != 0) {
                    continue;
                }
                if (gameField[i][j].equals(" ")) {
                    gameField[i][j] = aiSymbol;
                    int score = miniMax(gameField, false);
                    gameField[i][j] = " ";
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }

        return bestMove;
    }


    public int miniMax(String[][] gameField, boolean isMaximizing){

        String opponentSymbol = aiSymbol.toUpperCase().equals("X") ? "O": "X";

        if (checkIfWinning(gameField, aiSymbol)) {
            return 10;
        }
        else if (checkIfWinning(gameField, opponentSymbol)) {
            return -10;
        }
        else if (getAvailableSpots(gameField).size() == 0) {
            return 0;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 1; i < 4; i++) {
                for (int j = 2; j < 7; j++) {
                    if (j % 2 != 0) {
                        continue;
                    }
                    if (gameField[i][j].equals(" ")) {
                        gameField[i][j] = aiSymbol;
                        int score = miniMax(gameField, false);
                        gameField[i][j] = " ";
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 1; i < 4; i++) {
                for (int j = 2; j < 7; j++) {
                    if (j % 2 != 0) {
                        continue;
                    }
                    if (gameField[i][j].equals(" ")) {
                        gameField[i][j] = opponentSymbol;
                        int score = miniMax(gameField, true);
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
            for (int j = 2; j < 7; j++) {
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
        for (int j = 2; j < 7; j++) {
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
            for (int j = 2; j < 7; j++) {
                if (j % 2 != 0) {
                    continue;
                }
                if (j == i * 2 && gameField[i][j].equals(symbol)) {
                    inRowCounter++;
                }
            }
        }
        if (inRowCounter == 3) {
            return true;
        }

        inRowCounter = 0;
        /* Cross check 2nd diagonal */
        for (int i = 1; i < 4; i++) {
            for (int j = 2; j < 7; j++) {
                if (j % 2 != 0) {
                    continue;
                }
                if (j == - 2 * i + 8 && gameField[i][j].equals(symbol)) {
                    inRowCounter++;
                }
            }
        }
        if (inRowCounter == 3) {
            return true;
        }

        return false;
    }

}

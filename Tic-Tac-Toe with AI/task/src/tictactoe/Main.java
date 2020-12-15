package tictactoe;

import java.util.*;

public class Main {

    static String[][] gameField = new String[5][9];
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while(true) {
            generateGameField();

            System.out.println("Input commands: [start][user/easy/medium/hard][user/easy/medium/hard] or [exit]");
            String startOrEnd, whoStarts, opponent;
            while (true) {
                try {
                    String gameInputs = scanner.nextLine();
                    String[] gameInputsArray = gameInputs.trim().split("\\s+");
                    startOrEnd = gameInputsArray[0].toUpperCase();
                    if (startOrEnd.equals("EXIT")) {
                        return;
                    }
                    whoStarts = gameInputsArray[1].toUpperCase();
                    opponent = gameInputsArray[2].toUpperCase();
                    break;
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Bad parameters!");
                }
            }

            Object[] players = choosePlayersOfGame(whoStarts, opponent);

            gameLoop(players);
        }
    }


    public static void gameLoop(Object[] players) {
        if (players[0] instanceof AI && players[1] instanceof AI) {
            AI player1 = (AI) players[0];
            AI player2 = (AI) players[1];
            while (!checkStateOfTheGame()) {
                int[] computer1Moves = player1.move(gameField, "X");
                markSymbol(computer1Moves[0], computer1Moves[1]);
                printGameField();
                if (checkStateOfTheGame()) {
                    return;
                }
                int[] computer2Moves = player2.move(gameField, "O");
                markSymbol(computer2Moves[0], computer2Moves[1]);
                printGameField();
                if (checkStateOfTheGame()) {
                    return;
                }
            }
        } else if (players[0] instanceof User && players[1] instanceof AI) {
            printGameField();
            User player1 = (User) players[0];
            AI player2 = (AI) players[1];
            while (!checkStateOfTheGame()) {
                System.out.println("Enter the coordinates:");
                int[] userMoves = player1.move(gameField, scanner);
                markSymbol(userMoves[0], userMoves[1]);
                printGameField();
                if (checkStateOfTheGame()) {
                    return;
                }
                int[] computerMoves = player2.move(gameField, "O");
                markSymbol(computerMoves[0], computerMoves[1]);
                printGameField();
                if (checkStateOfTheGame()) {
                    return;
                }
            }
        } else if (players[0] instanceof AI && players[1] instanceof User) {
            AI player1 = (AI) players[0];
            User player2 = (User) players[1];
            while (!checkStateOfTheGame()) {
                int[] computerMoves = player1.move(gameField, "X");
                markSymbol(computerMoves[0], computerMoves[1]);
                printGameField();
                if (checkStateOfTheGame()) {
                    return;
                }
                System.out.println("Enter the coordinates:");
                int[] userMoves = player2.move(gameField, scanner);
                markSymbol(userMoves[0], userMoves[1]);
                printGameField();
                if (checkStateOfTheGame()) {
                    return;
                }
            }
        } else if (players[0] instanceof User && players[1] instanceof User){
            printGameField();
            User player1 = (User) players[0];
            User player2 = (User) players[1];
            while (!checkStateOfTheGame()) {
                System.out.println("Enter the coordinates:");
                int[] user1Moves = player1.move(gameField, scanner);
                markSymbol(user1Moves[0], user1Moves[1]);
                printGameField();
                if (checkStateOfTheGame()) {
                    return;
                }
                System.out.println("Enter the coordinates:");
                int[] user2Moves = player2.move(gameField, scanner);
                markSymbol(user2Moves[0], user2Moves[1]);
                printGameField();
                if (checkStateOfTheGame()) {
                    return;
                }
            }
        }
    }


    public static Object[] choosePlayersOfGame(String player1, String player2){
        if(player1.equals("USER") && player2.equals("USER")) {
            return new Object[]{new User("User 1"), new User("User 2")};
        }
        if (player1.equals("USER") && player2.equals("EASY")) {
            AI opponentAI = new AI();
            opponentAI.setLevel(AI.Level.EASY);
            return new Object[]{new User("User 1"), opponentAI};
        }
        if (player1.equals("USER") && player2.equals("MEDIUM")) {
            AI opponentAI = new AI();
            opponentAI.setLevel(AI.Level.MEDIUM);
            return new Object[]{new User("User 1"), opponentAI};
        }
        if (player1.equals("USER") && player2.equals("HARD")) {
            AI opponentAI = new AI();
            opponentAI.setLevel(AI.Level.HARD);
            return new Object[]{new User("User 1"), opponentAI};
        }
        if (player1.equals("EASY") && player2.equals("USER")) {
            AI playerAI = new AI();
            playerAI.setLevel(AI.Level.EASY);
            return new Object[]{playerAI, new User("User 2")};
        }
        if (player1.equals("MEDIUM") && player2.equals("USER")) {
            AI playerAI = new AI();
            playerAI.setLevel(AI.Level.MEDIUM);
            return new Object[]{playerAI, new User("User 2")};
        }
        if (player1.equals("HARD") && player2.equals("USER")) {
            AI playerAI = new AI();
            playerAI.setLevel(AI.Level.HARD);
            return new Object[]{playerAI, new User("User 2")};
        }
        if (player1.equals("EASY") && player2.equals("EASY")) {
            AI playerAI = new AI();
            playerAI.setLevel(AI.Level.EASY);
            AI opponentAI = new AI();
            opponentAI.setLevel(AI.Level.EASY);
            return new Object[]{playerAI, opponentAI};
        }
        if (player1.equals("MEDIUM") && player2.equals("MEDIUM")) {
            AI playerAI = new AI();
            playerAI.setLevel(AI.Level.MEDIUM);
            AI opponentAI = new AI();
            opponentAI.setLevel(AI.Level.MEDIUM);
            return new Object[]{playerAI, opponentAI};
        }
        if (player1.equals("HARD") && player2.equals("HARD")) {
            AI playerAI = new AI();
            playerAI.setLevel(AI.Level.HARD);
            AI opponentAI = new AI();
            opponentAI.setLevel(AI.Level.HARD);
            return new Object[]{playerAI, opponentAI};
        }
        if (player1.equals("EASY") && player2.equals("MEDIUM")) {
            AI playerAI = new AI();
            playerAI.setLevel(AI.Level.EASY);
            AI opponentAI = new AI();
            opponentAI.setLevel(AI.Level.MEDIUM);
            return new Object[]{playerAI, opponentAI};
        }
        if (player1.equals("MEDIUM") && player2.equals("EASY")) {
            AI playerAI = new AI();
            playerAI.setLevel(AI.Level.MEDIUM);
            AI opponentAI = new AI();
            opponentAI.setLevel(AI.Level.EASY);
            return new Object[]{playerAI, opponentAI};
        }
        if (player1.equals("EASY") && player2.equals("HARD")) {
            AI playerAI = new AI();
            playerAI.setLevel(AI.Level.EASY);
            AI opponentAI = new AI();
            opponentAI.setLevel(AI.Level.HARD);
            return new Object[]{playerAI, opponentAI};
        }
        if (player1.equals("HARD") && player2.equals("EASY")) {
            AI playerAI = new AI();
            playerAI.setLevel(AI.Level.HARD);
            AI opponentAI = new AI();
            opponentAI.setLevel(AI.Level.EASY);
            return new Object[]{playerAI, opponentAI};
        }
        if (player1.equals("MEDIUM") && player2.equals("HARD")) {
            AI playerAI = new AI();
            playerAI.setLevel(AI.Level.MEDIUM);
            AI opponentAI = new AI();
            opponentAI.setLevel(AI.Level.HARD);
            return new Object[]{playerAI, opponentAI};
        }

        AI playerAI = new AI();
        playerAI.setLevel(AI.Level.HARD);
        AI opponentAI = new AI();
        opponentAI.setLevel(AI.Level.MEDIUM);
        return new Object[]{playerAI, opponentAI};
    }


    public static void printGameField() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(gameField[i][j]);
            }
            System.out.println();
        }
    }


    public static void generateGameField() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 9; j++) {
                if (i == 0 || i == 4) {
                    gameField[i][j] = "-";
                } else if (j == 0 || j == 8) {
                    gameField[i][j] = "|";
                } else {
                    gameField[i][j] = " ";
                }
            }
        }
    }


    public static void markSymbol(int firstCoordinate, int secondCoordinate) {
        int xCounter = 0;
        int yCounter = 0;

        for (int i = 1; i < 4; i++) {
            for (int j = 1; j < 7; j++) {
                if (gameField[i][j].equals("X")) {
                    xCounter++;
                } else if (gameField[i][j].equals("O")) {
                    yCounter++;
                }
            }
        }

        String symbol;
        if (xCounter == yCounter) {
            symbol = "X";
        } else {
            symbol = "O";
        }

        gameField[firstCoordinate][secondCoordinate * 2] = symbol;
    }


    public static boolean checkStateOfTheGame() {
        int xInRowCounter = 0;
        int oInRowCounter = 0;

        /* In row check */
        for (int i = 1; i < 4; i++) {
            for (int j = 1; j < 7; j++) {
                if (gameField[i][j].equals("X")) {
                    xInRowCounter++;
                } else if (gameField[i][j].equals("O")) {
                    oInRowCounter++;
                }
            }
            if (xInRowCounter == 3) {
                System.out.println("X wins\n");
                return true;
            }
            if (oInRowCounter == 3) {
                System.out.println("O wins\n");
                return true;
            }
            xInRowCounter = 0;
            oInRowCounter = 0;
        }

        xInRowCounter = 0;
        oInRowCounter = 0;
        /* In column check */
        for (int j = 1; j < 7; j++) {
            for (int i = 1; i < 4; i++) {
                if (j % 2 != 0) {
                    continue;
                }
                if (gameField[i][j].equals("X")) {
                    xInRowCounter++;
                } else if (gameField[i][j].equals("O")) {
                    oInRowCounter++;
                }
            }
            if (xInRowCounter == 3) {
                System.out.println("X wins\n");
                return true;
            }
            if (oInRowCounter == 3) {
                System.out.println("O wins\n");
                return true;
            }
            xInRowCounter = 0;
            oInRowCounter = 0;
        }

        xInRowCounter = 0;
        oInRowCounter = 0;
        /* Cross check 1st diagonal */
        for (int i = 1; i < 4; i++) {
            for (int j = 1; j < 7; j++) {
                if (j % 2 != 0) {
                    continue;
                }
                if (j == i * 2 && gameField[i][j].equals("X")) {
                    xInRowCounter++;
                } else if (j == 2 * i && gameField[i][j].equals("O")) {
                    oInRowCounter++;
                }
            }
        }
        if (xInRowCounter == 3) {
            System.out.println("X wins\n");
            return true;
        }
        if (oInRowCounter == 3) {
            System.out.println("O wins\n");
            return true;
        }

        xInRowCounter = 0;
        oInRowCounter = 0;
        /* Cross check 2nd diagonal */
        for (int i = 1; i < 4; i++) {
            for (int j = 1; j < 7; j++) {
                if (j % 2 != 0) {
                    continue;
                }
                if (j == - 2 * i + 8 && gameField[i][j].equals("X")) {
                    xInRowCounter++;
                } else if (j == - 2 * i + 8 && gameField[i][j].equals("O")) {
                    oInRowCounter++;
                }
            }
        }
        if (xInRowCounter == 3) {
            System.out.println("X wins\n");
            return true;
        }
        if (oInRowCounter == 3) {
            System.out.println("O wins\n");
            return true;
        }

        for (int i = 1; i < 4; i++) {
            for (int j = 1; j < 7; j++) {
                if (j % 2 != 0) {
                    continue;
                }
                if (gameField[i][j].equals(" ")) {
//                    System.out.println("Game not finished");
                    return false;
                }
            }
        }

        System.out.println("Draw\n");
        return true;
    }
}
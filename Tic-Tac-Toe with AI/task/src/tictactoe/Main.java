package tictactoe;

import java.util.*;

public class Main {

    static String[][] gameField = new String[5][9];
    static Scanner scanner = new Scanner(System.in);
    private static String turn;

    public static void swapTurn(){
        turn = turn.equals("X") ? "O" : "X";
    }

    public static void main(String[] args) {
        while(true) {
            generateGameField();

            System.out.println("Input command: [start/exit] [user/easy/medium/hard] [user/easy/medium/hard]");
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
            Object player1 = choosePlayer(whoStarts);
            Object player2 = choosePlayer(opponent);
            Object[] players = {player1, player2};

            gameLoop(players);
        }
    }

    public static void loop(){}

    public static void gameLoop(Object[] players) {
        turn = "X";
        if (players[0] instanceof AI && players[1] instanceof AI) {
            AI player1 = (AI) players[0];
            AI player2 = (AI) players[1];
            while (!checkStateOfTheGame()) {
                player1.setAiSymbol(turn);
                int[] computer1Moves = player1.move(gameField);
                markSymbol(computer1Moves[0], computer1Moves[1]);
                printGameField();
                if (checkStateOfTheGame()) {
                    return;
                }
                swapTurn();
                player2.setAiSymbol(turn);
                int[] computer2Moves = player2.move(gameField);
                markSymbol(computer2Moves[0], computer2Moves[1]);
                printGameField();
                swapTurn();
            }
        } else if (players[0] instanceof User && players[1] instanceof AI) {
            printGameField();
            User player1 = (User) players[0];
            AI player2 = (AI) players[1];

            while (true) {
                System.out.println("Enter the coordinates:");
                int[] userMoves = player1.move(gameField, scanner);
                markSymbol(userMoves[0], userMoves[1]);
                printGameField();
                if (checkStateOfTheGame()) {
                    return;
                }
                swapTurn();
                player2.setAiSymbol(turn);
                int[] computerMoves = player2.move(gameField);
                markSymbol(computerMoves[0], computerMoves[1]);
                printGameField();

                swapTurn();
            }
        } else if (players[0] instanceof AI && players[1] instanceof User) {
            AI player1 = (AI) players[0];
            player1.setAiSymbol(turn);
            User player2 = (User) players[1];
            while (true) {
                int[] computerMoves = player1.move(gameField);
                markSymbol(computerMoves[0], computerMoves[1]);
                printGameField();
                if (checkStateOfTheGame()) {
                    return;
                }
                swapTurn();
                System.out.println("Enter the coordinates:");
                int[] userMoves = player2.move(gameField, scanner);
                markSymbol(userMoves[0], userMoves[1]);
                printGameField();
                if (checkStateOfTheGame()) {
                    return;
                }
                swapTurn();
            }
        } else if (players[0] instanceof User && players[1] instanceof User) {
            printGameField();
            User player = (User) players[0];
            while (!checkStateOfTheGame()) {
                System.out.println("Enter the coordinates:");
                int[] userMoves = player.move(gameField, scanner);
                markSymbol(userMoves[0], userMoves[1]);
                printGameField();

                swapTurn();
            }
        }
    }


    public static Object choosePlayer(String player){
        User user = null;
        AI ai = null;
        switch (player.toUpperCase()) {
            case ("USER"):
                user = new User("User");
                break;
            case ("EASY"):
                ai = new AI(AI.Level.EASY);
                break;
            case ("MEDIUM"):
                ai= new AI(AI.Level.MEDIUM);
                break;
            case ("HARD"):
                ai = new AI(AI.Level.HARD);
                break;
        }
        Object playerObject;
        if (ai != null) {
            playerObject = ai;
        } else {
            playerObject = user;
        }

        return playerObject;
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
        gameField[firstCoordinate][secondCoordinate * 2] = turn;
    }


    public static boolean checkStateOfTheGame() {
        int xInRowCounter = 0;
        int oInRowCounter = 0;

        /* In row check */
        for (int i = 1; i < 4; i++) {
            for (int j = 2; j < 7; j++) {
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
        for (int j = 2; j < 7; j++) {
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
            for (int j = 2; j < 7; j++) {
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
            for (int j = 2; j < 7; j++) {
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
            for (int j = 2; j < 7; j++) {
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
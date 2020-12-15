package tictactoe;

import java.util.InputMismatchException;
import java.util.Scanner;

public class User {
    final String name;

    public User(String name) {
        this.name = name;
    }

    public int[] move(String[][] gameField, Scanner scanner) {
        int firstCoordinate;
        int secondCoordinate;
        while (true) {
            try {
                String[] coordinatesArrayProcessed = scanner.nextLine().trim().split("\\s+");
                firstCoordinate = Integer.parseInt(coordinatesArrayProcessed[0]);
                secondCoordinate = Integer.parseInt(coordinatesArrayProcessed[1]);
                if (firstCoordinate > 3 || firstCoordinate < 1
                        || secondCoordinate > 3 || secondCoordinate < 1) {
                    System.out.println("Coordinates should be from 1 to 3!");
                    continue;
                }
                if (!gameField[firstCoordinate][secondCoordinate * 2].equals(" ")) {
                    System.out.println("This cell is occupied! Choose another one!");
                    continue;
                }
                break;
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("You should enter numbers!");
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("You should enter two coordinates!\n");
            }
        }
        return new int[]{firstCoordinate, secondCoordinate};
    }

}


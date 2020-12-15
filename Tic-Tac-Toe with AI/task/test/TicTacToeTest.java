import org.assertj.core.util.Lists;
import org.hyperskill.hstest.dynamic.input.DynamicTestingMethod;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;
import tictactoe.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

enum FieldState {
    X, O, FREE;

    static FieldState get(char symbol) {
        switch (symbol) {
            case 'X':
                return X;
            case 'O':
                return O;
            case ' ':
            case '_':
                return FREE;
            default:
                return null;
        }
    }
}

class TicTacToeField {

    final FieldState[][] field;

    TicTacToeField(FieldState[][] field) {
        this.field = new FieldState[3][3];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                this.field[row][col] = field[row][col];
            }
        }
    }

    TicTacToeField(String str) {
        field = new FieldState[3][3];

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                field[row][col] =
                    FieldState.get(str.charAt((row * 3 + col)));
            }
        }
    }

    boolean equalTo(TicTacToeField other) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (field[i][j] != other.field[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    boolean hasNextAs(TicTacToeField other) {
        boolean improved = false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (field[i][j] != other.field[i][j]) {
                    if (field[i][j] == FieldState.FREE && !improved) {
                        improved = true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return improved;
    }

    boolean isCloseTo(TicTacToeField other) {
        return equalTo(other)
            || hasNextAs(other)
            || other.hasNextAs(this);
    }

    static TicTacToeField parse(String fieldStr) {

        try {
            List<String> lines = fieldStr
                .lines()
                .map(String::strip)
                .filter(e ->
                    e.startsWith("|")
                        && e.endsWith("|"))
                .collect(Collectors.toList());

            for (String line : lines) {
                if (line.length() != 9) {
                    throw new WrongAnswer("Line of Tic-Tac-Toe " +
                        "field should be 9 characters long\n" +
                        "found " + line.length() + " characters in \"" + line + "\"");
                }
                for (char c : line.toCharArray()) {
                    if (c != 'X'
                        && c != 'O'
                        && c != '|'
                        && c != ' '
                        && c != '_') {
                        return null;
                    }
                }
            }

            FieldState[][] field = new FieldState[3][3];

            int y = 0;
            for (String line : lines) {
                char[] cols = new char[]{
                    line.charAt(2),
                    line.charAt(4),
                    line.charAt(6)
                };

                int x = 0;
                for (char c : cols) {
                    FieldState state = FieldState.get(c);
                    if (state == null) {
                        return null;
                    }
                    field[y][x] = state;
                    x++;
                }
                y++;
            }

            return new TicTacToeField(field);
        } catch (Exception ex) {
            return null;
        }
    }

    static List<TicTacToeField> parseAll(String output) {
        List<TicTacToeField> fields = new ArrayList<>();

        List<String> lines = output
            .lines()
            .map(String::strip)
            .filter(e -> e.length() > 0)
            .collect(Collectors.toList());

        String candidateField = "";
        boolean insideField = false;
        for (String line : lines) {
            if (line.contains("----") && !insideField) {
                insideField = true;
                candidateField = "";
            } else if (line.contains("----") && insideField) {
                TicTacToeField field = TicTacToeField.parse(candidateField);
                if (field != null) {
                    fields.add(field);
                }
                insideField = false;
            }

            if (insideField && line.startsWith("|")) {
                candidateField += line + "\n";
            }
        }

        return fields;
    }

}


class Clue {
    int x, y;

    Clue(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

public class TicTacToeTest extends StageTest<Clue> {

    static String[] inputs = new String[]{
        "1 1", "1 2", "1 3",
        "2 1", "2 2", "2 3",
        "3 1", "3 2", "3 3"
    };

    String iterateCells(String initial) {
        int index = -1;
        for (int i = 0; i < inputs.length; i++) {
            if (initial.equals(inputs[i])) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            return "";
        }
        String fullInput = "";
        for (int i = index; i < index + 9; i++) {
            fullInput += inputs[i % inputs.length] + "\n";
        }
        return fullInput;
    }

    @DynamicTestingMethod
    CheckResult autoTest() {
        TestedProgram main = new TestedProgram(Main.class);
        main.start();
        String output = main.execute("start easy easy");
        if (!(output.toLowerCase().contains("wins") || output.toLowerCase().contains("draw"))) {
            return CheckResult.wrong("The game was not finished (your program did not print the result of the game).");
        }

        List<TicTacToeField> fields = TicTacToeField.parseAll(output);

        if (fields.size() == 0) {
            return new CheckResult(false, "No fields found");
        }

        for (int i = 1; i < fields.size(); i++) {
            TicTacToeField curr = fields.get(i - 1);
            TicTacToeField next = fields.get(i);

            if (!(curr.equalTo(next) || curr.hasNextAs(next))) {
                return new CheckResult(false,
                    "For two fields following each " +
                        "other one is not a continuation " +
                        "of the other (they differ more than in two places).");
            }
        }

        return CheckResult.correct();
    }

    // Simple test
    @DynamicTestingMethod
    CheckResult manualTest1() {
        TestedProgram main = new TestedProgram(Main.class);
        main.start();
        main.execute("start user user");

        CheckResult temp;
        if ((temp = assertPosition(main, 0, 0, "x")) != null) return temp;
        if ((temp = assertPosition(main, 1, 2, "o")) != null) return temp;
        if ((temp = assertPosition(main, 1, 0, "x")) != null) return temp;
        if ((temp = assertPosition(main, 2, 2, "o")) != null) return temp;
        String output = main.execute("3 1");
        if (!output.contains("wins") || !output.toLowerCase().contains("x")) {
            return CheckResult.wrong("A win message was expected, but the game didn't stop.");
        }

        return CheckResult.correct();
    }

    // checking overlapping
    @DynamicTestingMethod
    CheckResult manualTest2() {
        TestedProgram main = new TestedProgram(Main.class);
        main.start();
        main.execute("start user user");
        CheckResult temp;
        String output;

        if ((temp = assertPosition(main, 0, 0, "x")) != null) return temp;
        output = main.execute("1 1");
        if (output.split("\n").length > 3) {
            return CheckResult.wrong("We placed a symbol to an occupied cell, but your program didn't warned about it.");
        }
        output = main.execute("1 2");
        TicTacToeField ticTacToeField = TicTacToeField.parse(output);
        if (ticTacToeField == null) {
            return CheckResult.wrong("The game field is incorrect");
        }
        if (!ticTacToeField.field[0][0].toString().toLowerCase().equals("x")) {
            return CheckResult.wrong("The \"O\" symbol overlapped the \"X\" one.");
        }

        return CheckResult.correct();
    }

    //exit check
    @DynamicTestingMethod
    CheckResult exitCheck() {
        TestedProgram main = new TestedProgram(Main.class);
        main.start();

        main.execute("exit");
        if (!main.isFinished()) {
            return CheckResult.wrong("Your program did't finish after \"exit\" command");
        }

        return CheckResult.correct();
    }

    CheckResult assertPosition(TestedProgram program, int a, int b, String mode) {
        String output = program.execute((a + 1) + " " + (b + 1));
        TicTacToeField field = TicTacToeField.parse(output);

        if (field == null) {
            return CheckResult.wrong("The game field is incorrect");
        }
        try {
            System.out.println(field.field[a][b]);
            if (!field.field[a][b].toString().toLowerCase().equals(mode)) {
                return CheckResult.wrong("The " + mode.toUpperCase() + " was placed to a wrong position." +
                    "The " + mode.toUpperCase() + " symbol was not found (" + field.field[a][b] + " instead of it).");
            }
        } catch (NullPointerException nullPointerException) {
            return CheckResult.wrong("The error in the game field caused an exception in the testing system.");
        }

        return null;
    }

    @DynamicTestingMethod
    CheckResult testOfEasyDifficulty() {
        int win = 0, draw = 0, lose = 0;
        int result;
        for (int i = 0; i < 50; i++) {
            result = testGameSession("easy easy");
            if (result == -1) {
                return CheckResult.wrong("An error in process of the game was found");
            } else if (result == 0) {
                draw++;
            } else if (result == 1) {
                win++;
            } else if (result == 2) {
                lose++;
            }
        }
        if (win > 13) {
            return CheckResult.correct();
        } else {
            return CheckResult.wrong("The difficulty of your easy AI is too high." +
                "Make it easier.\n" +
                "If you are sure the AI difficulty is fine, try to rerun the test.");
        }
    }

    @DynamicTestingMethod
    CheckResult testOfMediumDifficulty() {
        int win = 0, draw = 0, lose = 0;
        int result;
        for (int i = 0; i < 50; i++) {
            result = testGameSession("medium medium");
            if (result == -1) {
                return CheckResult.wrong("An error in process of the game was found");
            } else if (result == 0) {
                draw++;
            } else if (result == 1) {
                win++;
            } else if (result == 2) {
                lose++;
            }
        }
        if (win > 10) {
            return CheckResult.correct();
        } else {
            return CheckResult.wrong("The difficulty of your medium AI is too high." +
                "Make it easier.\n" +
                "If you are sure the AI difficulty is fine, try to rerun the test.");
        }
    }

    @DynamicTestingMethod
    CheckResult testOfHardDifficulty1() {
        int win = 0, draw = 0, lose = 0;
        int result;
        for (int i = 0; i < 50; i++) {
            result = testGameSession("hard hard");
            if (result == -1) {
                return CheckResult.wrong("An error in process of the game was found");
            } else if (result == 0) {
                draw++;
            } else if (result == 1) {
                win++;
            } else if (result == 2) {
                lose++;
            }
        }
        if (draw > 40) {
            return CheckResult.correct();
        } else {
            return CheckResult.wrong("The difficulty of your hard AI is too high. " +
                "Make it easier.\n" +
                "If you are sure the AI difficulty is fine, try to rerun the test.");
        }
    }

    @DynamicTestingMethod
    CheckResult testOfHardDifficulty2() {
        int win = 0, draw = 0, lose = 0;
        int result;
        for (int i = 0; i < 50; i++) {
            result = testGameSession("hard medium");
            if (result == -1) {
                return CheckResult.wrong("An error in process of the game was found");
            } else if (result == 0) {
                draw++;
            } else if (result == 1) {
                win++;
            } else if (result == 2) {
                lose++;
            }
        }
        if (win > 35) {
            return CheckResult.correct();
        } else {
            return CheckResult.wrong("The difficulty of your hard AI is too low.");
        }
    }

    int testGameSession(String mode) {
        List<String> inputs = Lists.newArrayList(
            "1 1", "1 2", "1 3",
            "2 1", "2 2", "2 3",
            "3 1", "3 2", "3 3");

        TestedProgram main = new TestedProgram(Main.class);
        main.start();
        String output = main.execute("start " + mode);

        while (!main.isFinished()) {
            if (output.contains("command:")){
                break;
            }
            int randomIndex = new Random().nextInt(inputs.size());
            output = main.execute(inputs.get(randomIndex));
            inputs.remove(randomIndex);
            if (inputs.isEmpty()){
                return -2;
            }
        }

        if (!(output.toLowerCase().contains("wins") || output.toLowerCase().contains("draw"))) {
            return -1;
        }

        if (output.toLowerCase().contains("x wins")) {
            return 1;
        } else if (output.toLowerCase().contains("o wins")) {
            return 2;
        } else if (output.toLowerCase().contains("draw")) {
            return 0;
        }

        main.stop();
        return -1;
    }
}

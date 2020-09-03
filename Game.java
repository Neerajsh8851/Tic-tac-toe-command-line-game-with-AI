package tictactoe;

import java.util.ArrayList;
import java.util.Random;

import static tictactoe.Main.scan;

public class Game {



    private final char[] BOARD = new char[] {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};

    private boolean running = false;
    private char mark_to_draw;
    private char markDrawn;
    private String modeP1;
    private String modeP2;
    private final String p1;
    private final String p2;
    private boolean isFirst = true;
    private boolean isBestToWin = true;


    // Constructor
    public Game(String inputCommand) {
        drawTheField();
        mark_to_draw = 'X';
        String[] input =  inputCommand.split(" +");

        if(input[1].equals("user")) {
            p1 = "user";
        } else {
            p1 = "bot";
            modeP1 = input[1];
        }

        if(input[2].equals("user")) {
            p2 = "user";
        } else {
            p2 = "bot";
            modeP2 = input[2];
        }
    }

    public void run() {
        running = true;

        while (running) {
            nextTurn();
            drawTheField();
            check();
        }
    }

    public void drawTheField() {
        System.out.println(
                   "----------" + "\n" +
                "| " + BOARD[6] + " " + BOARD[7] + " " + BOARD[8] + " |" + "\n" +
                "| " + BOARD[3] + " " + BOARD[4] + " " + BOARD[5] + " |" + "\n" +
                "| " + BOARD[0] + " " + BOARD[1] + " " + BOARD[2] + " |" + "\n"
                + "----------"
        );
    }

    // calculate the nextTurn
    private void nextTurn() {
        if (mark_to_draw == 'X') {
            if (p1.equals("user")) {
                userTurn();
            } else {
                botTurn(modeP1);
            }
        } else {
            if (p2.equals("user")) {
                userTurn();
            } else {
                botTurn(modeP2);
            }
        }

        if (isFirst) {
            isFirst = false;
        }


    }

    // empty cell: _ , cell with mark x: 'X', cell with mark o: 'O'
    private void userTurn() {
            int x;

            while (true) {
                try {
                    System.out.print("Enter the empty spot position: ");
                    x = scan.nextInt();
                    if (x > 9 || x < 1) {
                        System.out.println("Spot position should be from 1 to 9!");
                    } else if (BOARD[x - 1] != ' ') {
                        System.out.println("This spot is occupied! Choose another one!");
                    } else break;
                }  catch (final Exception e) {
                        System.out.println("You should enter numbers!");
                        scan.next();
                    }
            }

            BOARD[x - 1] = mark_to_draw;
            updateMark();
            // read newline char
            scan.nextLine();
    }

    private void botTurn(String botPower) {


        System.out.println("Making move level \"" + botPower + "\"");
        switch (botPower) {
            case "easy":
                botMoveEasy(makeAvailableSpotsArray());
                break;
            case "medium":
                botMoveMedium(makeAvailableSpotsArray());
                break;
            case "hard" :
                botMoveHard();
        }

        updateMark();
    }

    // Return a collection of available spots
    private ArrayList<Integer> makeAvailableSpotsArray() {
        ArrayList<Integer> availableSpots = new ArrayList<>(9);
        for (int i = 0; i < 9; i++) {
            if (BOARD[i] == ' ')
                availableSpots.add(i);
        }
        return availableSpots;
    }

    // pick randomly available spot
    private void botMoveEasy(ArrayList<Integer> availableSpots) {
        Random rand = new Random();
        int randSpot = rand.nextInt(availableSpots.size());
        int spot = availableSpots.get(randSpot);

        BOARD[spot] = mark_to_draw;
    }

    // check for every available  spots if there is a win
    // then that the best to pick
    // if the no winning found then try to block opponent's best spot to pick
    // lastly if nothing works, choose random spot
    private  void botMoveMedium (ArrayList<Integer> emptyPlaces) {
        boolean win = false;
        for (int i : emptyPlaces) {
            BOARD[i] = mark_to_draw;
            win = isWin();
            if (!win) {
                BOARD[i] = ' ';
            } else  {
                break;
            }
        }

        if (!win) {
            mark_to_draw = (mark_to_draw == 'X') ? 'O' : 'X';
            for (int i : emptyPlaces) {
                BOARD[i] = mark_to_draw;
                win = isWin();
                if (win) {
                    mark_to_draw = (mark_to_draw == 'X') ? 'O' : 'X';
                    BOARD[i] = mark_to_draw;
                    break;
                } else {
                    BOARD[i] = ' ';
                }
            }
        }

        if (!win) {
            mark_to_draw = (mark_to_draw == 'X') ? 'O' : 'X';
            botMoveEasy(emptyPlaces);
        }
    }


    // using minimax: choosing the best move where the win is possible
    private void botMoveHard () {
        if (isFirst) {
            botMoveEasy(makeAvailableSpotsArray());
        } else
        {
            int score;
            int bestScore = -1;
            int move = 0;

            // loop through all the available spots
            for (int i = 0; i < BOARD.length; i++) {
                if (BOARD[i] == ' ') {
                    BOARD[i] = mark_to_draw;
                    score = minimax(false, 0);
                    BOARD[i] = ' ';

                    // must go in the branch where the win is possible
                    if (score > bestScore && isBestToWin) {
                        bestScore  = score;
                        move = i;
                        isBestToWin = false;
                    }
                }
            }
            isBestToWin = true;
            BOARD[move] = mark_to_draw;
        }
        }

    private int minimax (boolean isMaximizing, int depth) {
        if (isWin()) {
            if (isMaximizing) {
                return -1;
            } else {
                isBestToWin = true;
                return 1;
            }
        }

        if (isDraw()) {
            return 0;
        }

        if (isMaximizing) {
            int score;
            int bestScore = -1;
            for (int i = 0; i < BOARD.length; i++) {
                if (BOARD[i] == ' ') {
                    BOARD[i] = mark_to_draw;
                    score = minimax(false, depth + 1);

                    BOARD[i] = ' ';

                    if (score > bestScore) {
                        bestScore = score;
                    }
                }
            }
            return bestScore;
        } else  {
            int score;
            int bestScore = 1;
            for (int i = 0; i < BOARD.length; i++) {
                if (BOARD[i] == ' ') {
                    BOARD[i] = (mark_to_draw == 'X') ? 'O' : 'X';
                    score = minimax(true, depth + 1);
                    BOARD[i] = ' ';
                    if (score  <   bestScore) {
                        bestScore = score;
                    }
                }
            }
            return bestScore;
        }
    }

    // update mark_to_draw and markDrawn
    private void updateMark() {
        markDrawn = mark_to_draw;

        if (mark_to_draw == 'X') {
            mark_to_draw = 'O';
        } else {
            mark_to_draw = 'X';
        }
    }

    // check for a win or a draw
    private void check() {
        if (isWin()) {
            System.out.println(markDrawn + " wins");
            running = false;
        } else if (isDraw()) {
            System.out.println("Draw");
            running = false;
        }
    }

    private boolean isWin() {
        // total 8 conditions one of them could be true if there is a win
            return (BOARD[0] == BOARD[1] && BOARD[1] == BOARD[2] && BOARD[1] != ' ' ||
                    BOARD[3] == BOARD[4] && BOARD[4] == BOARD[5] && BOARD[4] != ' ' ||
                    BOARD[6] == BOARD[7] && BOARD[7] == BOARD[8] && BOARD[7] != ' ' ||
                    BOARD[6] == BOARD[3] && BOARD[3] == BOARD[0] && BOARD[3] != ' ' ||
                    BOARD[7] == BOARD[4] && BOARD[4] == BOARD[1] && BOARD[4] != ' ' ||
                    BOARD[8] == BOARD[5] && BOARD[5] == BOARD[2] && BOARD[5] != ' ' ||
                    BOARD[6] == BOARD[4] && BOARD[4] == BOARD[2] && BOARD[4] != ' ' ||
                    BOARD[0] == BOARD[4] && BOARD[4] == BOARD[8] && BOARD[4] != ' '
            ) ;
    }

    private boolean isDraw() {
        for (char symbols : BOARD) {
            if (symbols == ' ') {
                return false;
            }
        }
        return  true;
    }
}

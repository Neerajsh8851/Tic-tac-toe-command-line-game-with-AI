package tictactoe;

import java.util.Scanner;

public class Main {
    public static  Scanner scan = new Scanner(System.in);
    private static final String  EXIT = "exit";
    private static final String  START = "start" + " (easy|user|medium|hard) +(easy|user|medium|hard)";
    private static final String INPUT_COMND = "Input command: ";

    public static void main(String[] args) {
        System.out.print(INPUT_COMND);
        String inputCommand = scan.nextLine().strip();

        while (!inputCommand.equals(EXIT)) {
            if (inputCommand.matches(START)) {
                Game game = new Game(inputCommand);
                game.run();
            } else {
                System.out.println("Bad parameters!");
            }

            System.out.print(INPUT_COMND);
            inputCommand = scan.nextLine().strip();
        }

        scan.close();
    }

}

import java.util.Scanner;

public class SudokuTesting1 {
    public static void main(String[] args) {
        //----------------------------------------------------------------------
        //code ONLY for https://www.codingame.com/ide/puzzle/mini-sudoku-solver:
        //code for IntelliJ -> see SudokuTesting1.java
        //----------------------------------------------------------------------
        //code ONLY for IntelliJ:

        String[] sudokuStr = {"2043", "0020", "4300", "0034"};

        Sudoku sudoku = new Sudoku(4, sudokuStr);
        sudoku.solve();

        sudoku.printEasy();




        System.out.println("\nsudoku.printIndexesOfCells() -> ");
        sudoku.printIndexesOfCells();


    }
}


class SudokuLiveInput {
    public static void main(String[] args) {
        //this can be used to live test ANY sudoku by entering the sudoku in the terminal:

        Scanner in = new Scanner(System.in);

        //get the size of the sudoku:
        int size = 0;
        System.out.println("Please enter your sudoku.\n" +
                "Currently, only sudokus with 4x4 and 9x9 will calculate also their respective subgroups.");

        System.out.print("\nWhat is the size of your sudoku (max-size: 9): ");
        do {
            String input = in.nextLine();
            try {
                size = Integer.parseInt(input);
                if (size < 1 || size > 9) {
                    throw new IllegalArgumentException();
                }
            } catch (Exception e) {
                System.out.print("\nThis was not a valid entry, try again (it must be a number in the range of 1 to 9): ");
            }
        } while (size < 1 || size > 9);

        String[] sudokuStr = new String[size];
        System.out.println("\nEnter your " + size + " lines of the sudoku, each separated by ENTER.");
        System.out.println("a not yet solved cell should be entered with 0 (also possible: with a space or any other character than 1-9):");
        for (int i = 0; i < size; i++) {
            System.out.print("line " + (i + 1) + ": ");
            sudokuStr[i] = in.nextLine();
        }



        /*
        Example of a 9x9 sudoku:
002000009
030000710
800006300
000087003
703502104
900460000
001900006
095000070
600000800
         */

        Sudoku sudoku = new Sudoku(size, sudokuStr);

        //close the scanner:
        in.close();

        sudoku.printFull();
        sudoku.solve();
        //use the following line for https://www.codingame.com/ide/puzzle/mini-sudoku-solver:
        sudoku.printFull();



    }


}


class SudokuTestingExtended {
    public static void main(String[] args) {


        /*
        //initial array with funny values (to test the error-catching:
        String[] sudokuErrorsStr = new String[4];
        sudokuErrorsStr[0] = "2043";
        sudokuErrorsStr[1] = "0a205";
        sudokuErrorsStr[2] = "4394";
        sudokuErrorsStr[3] = "003";
        Sudoku sudokuErrors = new Sudoku(4, sudokuErrorsStr);
        sudokuErrors.printFull();
*/

        //testing an empty 9x9 sudoku:
        Sudoku sudo3 = new Sudoku(9);
        System.out.println();
        sudo3.printFull();

        //testing a real 9x9 sudoku (difficult):
        String[] sudo4Str = {
                "002000009",
                "030000710",
                "800006300",
                "000087003",
                "703502104",
                "900460000",
                "001900006",
                "095000070",
                "600000800"
        };
        Sudoku sudo4 = new Sudoku(9, sudo4Str);
        sudo4.printFull();
        sudo4.solve();
//        sudo4.printEasy();

        System.out.println("\nsudoku.printIndexesOfCells() -> ");
        sudo4.printIndexesOfCells();


    }
}

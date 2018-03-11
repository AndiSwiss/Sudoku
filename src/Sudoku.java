import java.util.Scanner;

class Solution {
    //----------------------------------------------------------------------
    //code ONLY for https://www.codingame.com/ide/puzzle/mini-sudoku-solver:
    //code for IntelliJ -> see SudokuTesting1.java
    //----------------------------------------------------------------------
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        String[] sudokuStr = new String[4];
        for (int i = 0; i < 4; i++) {
            sudokuStr[i] = in.next();
        }
        //close the scanner:
        in.close();

        Sudoku sudoku = new Sudoku(4, sudokuStr);
        sudoku.printFull();
        sudoku.solve();
        //use the following line for https://www.codingame.com/ide/puzzle/mini-sudoku-solver:
        sudoku.printMiniSudokuSolver();

    }
}


public class Sudoku {

    //---------------------------------------------------------------------------------
    //VARIABLES: (for detailed explanation -> see the initializeVariables(size)-method!
    //---------------------------------------------------------------------------------
    int s;              //size
    int[][][] sudoku;   //three-dimensional sudoku
    private String[] sudokuStr; //maybe it's good to keep the string, before it gets parsed
    int maxSolutions;
    boolean[][] hSolved;
    boolean[][] vSolved;
    boolean[][] subSolved;

    private int subDivision;

    //for the sudoku solver:
    int[][][] hLines;  //actual copy of the original sudoku (points to the same values as the original sudoku)
    int[][][] vLines;  //swapped vertical with horizontal lines -> for the solver to check one vertical line
    int[][][] subGroups;  //contains the subGroups -> for the solver to check one subGroup


    //-----------------------------------------------------
    //CONSTRUCTORS:
    //-----------------------------------------------------
    //constructor: simple:
    Sudoku(int size) {
        initializeVariables(size);
        //remember: all values are initialized to 0. Used later in the constructor with the parsing. Check this:
//        System.out.println(sudoku[2][1][3]);
        initializePossibilities();
    }

    //constructor, which accepts an two-dimensional int-array (converting it to a three-dimensional int-array:
    Sudoku(int size, int[][] sudoku) {
        initializeVariables(size);
        //conversion from two-dimensional to three-dimensional int-array:
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.sudoku[i][j][0] = sudoku[i][j];
            }
        }
        initializePossibilities();
    }

    //constructor: with parsing (and error-handling)
    Sudoku(int size, String[] sudokuStr) {
        initializeVariables(size);
        this.sudokuStr = sudokuStr; //not really necessary, but maybe nice for something later down the road...
        parseStringArrayToIntArray(sudokuStr);
        initializePossibilities();
    }


    //-----------------------------------------------------
    //FUNCTIONS FOR CONSTRUCTOR:
    //-----------------------------------------------------

    //function to convert a String[] in to int[][][]:
    private void parseStringArrayToIntArray(String[] sudokuStr) {

        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s; j++) {
                try {
                    //Integer.parseInt parses a String to an Int. So in order to convert the char to an int,
                    //the easy trick is to add   ""+   -> this makes a string out the char:
                    sudoku[i][j][0] = Integer.parseInt("" + sudokuStr[i].charAt(j));
                    if (sudoku[i][j][0] < 0 || sudoku[i][j][0] > s) {
                        throw new IllegalArgumentException();
                    }
                } catch (NumberFormatException e) {
                    //parsing error:
                    System.out.printf("ERROR with parsing: the provided char at line %d, position %d ('%s') is not of type int! It will be automatically set to '0'.\n", i + 1, j + 1, sudokuStr[i].charAt(j));
                    //since all elements in sudoku are automatically initialized to 0, I don't have do this manually!
                } catch (IllegalArgumentException e) {
                    //this happens, when parsing was successfull, but if the number was below 0 or greater than 's'
                    //('s' is the size of the sudoku)
                    System.out.printf("ERROR: the provided number at line %d, position %d ('%d') is not valid (size of sudoku is %d)! It will be automatically set to '0'.\n", i + 1, j + 1, s, sudoku[i][j][0]);
                    sudoku[i][j][0] = 0;
                } catch (StringIndexOutOfBoundsException e) {
                    //this happens, when not enough numbers were provided per line:
                    System.out.printf("ERROR: no number (or 0) provided at line %d, position %d! It will be automatically set to '0'.\n", i + 1, j + 1);
                    //since all elements in sudoku are automatically initialized to 0, I don't have do this manually!
                } catch (Exception e) {
                    //if there was another error:
                    e.printStackTrace();
                }
            }
            //for nice output:
/*
            System.out.printf("Parsed line %d: ", i + 1);
            for (int j = 0; j < s; j++) {
                System.out.print(sudoku[i][j][0]);
            }
            System.out.println();
*/
        }
    }

    //initialize most of the variables:
    private void initializeVariables(int size) {
        //-----------------------------------------------------
        //INITIALIZE SOME VARIABLES (with explanations)
        //-----------------------------------------------------

        //set the size of the sudoku: -> that means, that will be used a lot!
        //maximum size: 9 (otherwise, code for parsing has to be rewritten; the rest should work)
        s = size;

        //-----------------------------------------------------
        //THREE DIMENSIONAL IDEA:
        //-----------------------------------------------------
        //1st dimension (size s): line (vertical)
        //2nd dimension (size s): position in the line (horizontal)
        //3rd dimension (size s + 1!!!):
        //Initialization would be like this: sudoku = new int[s][s][s + 1]
        //    Conventions to 3rd dimension:
        //    - sudoku[i][j][0] is used for the following stuff:
        //      -> if it is positive, then this is the definitive solution
        //      -> it it is negative, it shows the still possible solutions. -9 means 9 possible solutions, -2 means 2 possible solutions,...
        //      -> so the given 0 at the input are converted to  '-s' (the size of the sudoku, so for a 9x9, this would result in '-9')
        //      -> So, if sudoku[i][j][0] == 0, then there is NO possible solution and the sudoku would be unsolvable!
        //         If that would happen, then print an error-message immediately and abort the program!
        //
        //    - If sudoku[i][j][1] = 1 -> 1 is a possible solution, if sudoku[i][j][1] = 0, then 1 is not a possible solution.
        //      Like this, I can track, if 1-s (s = size of sudoku) are possible solutions in this field.
        //    - If the solution is found (meaning sudoku[i][j][0] > 0), then all other places should be 0.
        sudoku = new int[s][s][s + 1];


        //set variable to the current maximum of possibilities in all the positions (adjust later)
        //is not in a constructor -> the constructor initializes it with s:
        maxSolutions = s;

        //track the already solved numbers in a line:
        //first index is the line
        //second index: 0: true, if ALL numbers of the line were found
        //second index: 1-9: true; if the respective numbers were found
        //
        //example:
        //if hSolved[4][0] == false, then there are still unsolved numbers in line 4!
        //if hSolved[4][0] == true, then ALL numbers have been found in line 4!
        //for all the others (e.g. hSolved[4][5] == false means, that in line 4, the number 5 has not been found yet
        //                                       == true means, that in line 4, the number 5 has been found

        //horizontally:
        hSolved = new boolean[s][s + 1];
        //vertically:
        vSolved = new boolean[s][s + 1];

        //sub-groups:
        subSolved = new boolean[s][s + 1];


        //init subDivision:
        if (s == 4) {
            subDivision = 2;
        } else if (s == 9) {
            subDivision = 3;
        } else {
            //initialize with 0:
            subDivision = 0;
        }


        //initialize the arrays hLines, vLines and subGroups
        //(which are essentially just reordered copies of the original, actually hLines is identical to the original, but is nice for readability)
        initializeHLinesVLinesAndSubGroups();


    }

    private void initializeHLinesVLinesAndSubGroups() {
        //--------------------------------------------
        //init hLines[][][]  (horizontal lines):
        //--------------------------------------------
        //e.g. sudoku[0] is already the first horizontal line:
        //remember: to access a certain value of the variable sudoku inside Sudoku, you have to call it like: objectName.sudoku[0][1]
        hLines = new int[s][s][s + 1];
        //copy the array:
        //manually:
//        for (int i = 0; i < s; i++) {
//            hLines[i] = sudoku[i];
//        }
        //replaced more intelligent code by IntelliJ for a array copy:
        System.arraycopy(sudoku, 0, hLines, 0, s);

        //REMEMBER: hLines is a simple copy of the original array, BUT STILL POINTING TO THE EXACT SAME VALUE (the values are NOT copied)
        //-> hLines is not really necessary, but nice for code-readability  (vLines and subGroups are way more relevant!)

        //--------------------------------------------
        //init vLines[][][]  (vertical lines):
        //--------------------------------------------
        //think of this as just a reordered sudoku: the vertical line is now represented in a horizontal line
        vLines = new int[s][s][s + 1];
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s; j++) {
                //just switch the positions of i and j:
                vLines[i][j] = sudoku[j][i];
            }
        }

        //--------------------------------------------
        //init subGroups[][][]:
        //--------------------------------------------
        //initialize subGroups, but if not valid, then set subGroups to 'null'
        if (s != 4 && s != 9) {
            //hint: read the code above out loud: "if s is not 4 AND s is not 9, then..."  -> like this, it is very easy to understand :-)
            System.out.println("subGroups won't be created because s (" + s + ") is not a valid size for creating subGroups.");
        } else {

            subGroups = new int[s][s][s + 1];

            //for debugging:
//            System.out.print("printing i-values");

            //construct the subGroups:
            for (int g = 0; g < s; g++) {
                //for each element in the group:

                //for debugging:
//                System.out.print("\ngroup no " + g + ": ");

                for (int e = 0; e < s; e++) {
                    int i = 0; //horizontal index of original sudoku
                    int j = 0; //vertical index of original sudoku

                    //i:
                    //e.g. in 9x9:
                    //compare with the values generated with method printIndexesOfCells():
                    //group 0:          0 0 0 1 1 1 2 2 2
                    //group 1:          0 0 0 1 1 1 2 2 2
                    //group 2:          also the same
                    //groups 3 & 4 & 5: 3 3 3 4 4 4 5 5 5
                    //groups 6 & 7 & 8: 6 6 6 7 7 7 8 8 8

                    //so first handle the difference inside one line:
                    i += e / subDivision;
                    //now add the difference between the 3 different pairs of groups:
                    i += (g / subDivision) * subDivision;

                    //for debugging i:
//                    System.out.print(i + " ");

                    //j:
                    //e.g. in 9x9:
                    //group 0:          0 1 2 0 1 2 0 1 2
                    //group 1:          3 4 5 3 4 5 3 4 5
                    //group 2:          6 7 8 6 7 8 6 7 8
                    //groups 3 & 6:     same as group 0
                    //groups 4 & 7:     same as group 1
                    //groups 5 & 8:     same as group 2

                    //so first handle the difference inside one line:
                    j += e % subDivision;
                    //now add the difference between the 3 different pairs of groups:
                    j += (g % subDivision) * subDivision;
                    //compared to the calculations for 'i', this is the same, just use modulo instead of a division!

                    //for debugging j:
//                    System.out.print(j + " ");

                    subGroups[g][e] = sudoku[i][j];
                }
            }
        }
    }

    //initialize sudoku with all possible solutions for the not solved cells, and more:
    private void initializePossibilities() {
        //convert all the unsolved positions, that means '0', to '-s', that means to the maximal possible solutions for this field:
        //and also set all possible solutions (necessary, because I will build on that later on):
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s; j++) {
                if (sudoku[i][j][0] == 0) {
                    sudoku[i][j][0] = -s;
                    //write all possible solutions:
                    for (int n = 1; n <= s; n++) {
                        //n has values of 1 to s!!
                        sudoku[i][j][n] = n;
                    }
                }

            }
        }
    }


    //-----------------------------------------------------
    //SUDOKU-SOLVING:
    //-----------------------------------------------------
    int solveDummy4by4() {
        //dummy-code for first testing:
        int[][] solution1 = {
                {2, 1, 4, 3},
                {3, 4, 2, 1},
                {4, 3, 1, 2},
                {1, 2, 3, 4}};
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s; j++) {
                sudoku[i][j][0] = solution1[i][j];
            }
        }
        //1 = 1 solution (should be the normal case!), more than 1 -> multiple solutions, 0 -> not solvable
        return 1;
    }


    //actual sudoku-solver-code:
    int solve() {
        //idea: this method should solve until no more new things were found (e.g. deleted a possibility, solved a cell....)
        //then it should return 1, if found exactly 1 definitive solution

        //CONCEPT:
        //this solver should call a method called something like solveOneLine(), to which should be passed one hLine, vLine or subGroup.
        //it should iterate over all the hLines, vLines and subGroups, but ideally sort them by maxSolutions per line -> tracked in hSolved, vSolved subSolved


        //remember: initially, all values in hSolved, vSolved and subSolved are FALSE:
        //remember: hSolved, vSolved and subSolved are two-dimensional arrays:
        //first index is the line
        //second index: 0: true, if ALL numbers of the line were found
        //second index: 1-9: true; if the respective numbers were found

        //initial printing:
        printFull();
        printReport();

        int completeSolvingRuns = 0;
        int[] achievedInThisRun;
        int totalAchievedInThisRun;
        int unsolvedCells;
        int amountOfSolutions;

        do {
            completeSolvingRuns++;

            int[] achievedHLines = new int[3];
            int[] achievedVLines = new int[3];
            int[] achievedSubGroups = new int[3];

            //run once for all unsolved hLines:
            System.out.println("Solving all hLines:");
            for (int i = 0; i < s; i++) {
                //only run, if line is not yet solved:
                if (!hSolved[i][0]) {
                    int[] newlyAchieved = solveOneLine("hLine", i);
                    for (int k = 0; k < newlyAchieved.length; k++) {
                        achievedHLines[k] += newlyAchieved[k];
                    }
                }
            }
            //for extensive debugging:
            if (achievedHLines[1] > 0 || achievedHLines[2] > 0) {
                System.out.printf("Found %d new definitive solutions and removed %d possibilities when solving all hLines once\n", achievedHLines[1], achievedHLines[2]);
            }

            //only do this printOut in the first run (the happens a lot!):
            if (completeSolvingRuns == 1) {
                printFull();
                printReport();
            }



            System.out.println("Solving all vLines:");
            for (int i = 0; i < s; i++) {
                //only run, if line is not yet solved:
                if (!vSolved[i][0]) {
                    int[] newlyAchieved = solveOneLine("vLine", i);
                    for (int k = 0; k < newlyAchieved.length; k++) {
                        achievedVLines[k] += newlyAchieved[k];
                    }

                }
            }
            //for extensive debugging:
            if (achievedVLines[1] > 0 || achievedVLines[2] > 0) {
                System.out.printf("Found %d new definitive solutions and removed %d possibilities when solving all vLines once\n", achievedVLines[1], achievedVLines[2]);
            }

            //only do this printOut in the first run (the happens a lot!):
            if (completeSolvingRuns == 1) {
                printFull();
                printReport();
            }


            System.out.println("Solving all subGroups:");
            for (int i = 0; i < s; i++) {
                //only run, if line is not yet solved:
                if (!subSolved[i][0]) {
                    int[] newlyAchieved = solveOneLine("subGroup", i);
                    for (int k = 0; k < newlyAchieved.length; k++) {
                        achievedSubGroups[k] += newlyAchieved[k];
                    }

                }
            }
            //for extensive debugging:
            if (achievedSubGroups[1] > 0 || achievedSubGroups[2] > 0) {
                System.out.printf("Found %d new definitive solutions and removed %d possibilities when solving all subGroups once\n", achievedSubGroups[1], achievedSubGroups[2]);
            }



            printFull();
            System.out.println();

            achievedInThisRun = new int[3];
            totalAchievedInThisRun = 0;
            //add achievements
            for (int i = 0; i < achievedHLines.length; i++) {
                achievedInThisRun[i] += achievedHLines[i];
                achievedInThisRun[i] += achievedVLines[i];
                achievedInThisRun[i] += achievedSubGroups[i];

                totalAchievedInThisRun += achievedHLines[i];
                totalAchievedInThisRun += achievedVLines[i];
                totalAchievedInThisRun += achievedSubGroups[i];
            }

            System.out.println("=========================================================================================================");
            System.out.printf("== In this whole run, there were %d complete lines solved, %d cells solved and %d possibilities removed.\n",
                    achievedInThisRun[0], achievedInThisRun[1], achievedInThisRun[2]);
            System.out.println("=========================================================================================================");

            printReport();


            //check if unsolved Cells is 0 -> if yes, finish!
            unsolvedCells = 0;
            for (int i = 0; i < s; i++) {
                for (int j = 0; j < s; j++) {
                    if (sudoku[i][j][0] <= 0) {
                        unsolvedCells++;
                    }
                }
            }



            //todo: write code if a solution can only be found with guessing:
            //todo: check if still unsolvedCells, but totalAchievedInThisRun = 0
            //todo: -> in that case make an assumption (with a separate method and a variable which keeps track of that assumption)
            //todo: for example the sudo9hard in OneSudokuLineTesting can only be solved with guessing (I don't see another option at all)
            //todo: this could result in amountOfSolutions > 1!!


        } while (unsolvedCells != 0 && totalAchievedInThisRun != 0 && completeSolvingRuns < 100);


        System.out.println("======================================================================================");
        System.out.println("==  The whole solving-Code ran " + completeSolvingRuns + " times.");
        if (unsolvedCells == 0) {
            System.out.println("==  And the one and only solution was found. All cells have a definitive solution.");
            amountOfSolutions = 1;
        } else {
            System.out.println("==  But there was not found a definitive solution. There are still " + unsolvedCells + " unsolved cells.");

            //todo: rewrite the following line as soon as you did the guessing stuff!
            amountOfSolutions = unsolvedCells;
        }
        System.out.println("======================================================================================");


        //1 = 1 solution (should be the normal case!), more than 1 -> multiple solutions, 0 -> not solvable (but this would throw errors!)
        return amountOfSolutions;


        //todo: write some code for testing a large amount of sudokus (saved in a file, including the solutions to it)
        //todo: parse this, solve it and check it against the solution
        //todo: if it doesn't pass the test, show the differences of my solution and the solution in the file
        //todo: so I can see, what my sudoku can't solve
        //todo: also check, if I only found the solution by guessing -> look at the solution BEFORE the guessing with the left over unsolved cells!!
    }

    int[] solveOneLine(String type, int i) {


        //todo: for the return statement of this method:
        int[] achievedDefinitive;
        int[] achievedOneSolutionLeft = new int[3];
        int[] achievedOneSpotLeft = new int[3];

        //first create the object OneSudokuLine:
        int[][] line;
        boolean[] lSolved;

        switch (type) {
            case "hLine":
                line = hLines[i];
                lSolved = hSolved[i];
                break;
            case "vLine":
                line = vLines[i];
                lSolved = vSolved[i];
                break;
            case "subGroup":
                line = subGroups[i];
                lSolved = subSolved[i];
                break;
            default:
                throw new IllegalArgumentException("Switch-statement in solveOneLine() failed. Input was " + type);
        }
        //create the object:
        OneSudokuLine oneLine = new OneSudokuLine(line, lSolved);


        //run lookForDefinitiveSolutions() in OneSudokuLine:
        try {
            achievedDefinitive = oneLine.lookForDefinitiveSolutions();

            //for extensive debugging:
//            if (achievedDefinitive[1] > 0 || achievedDefinitive[2] > 0) {
//                System.out.printf("Found %d new definitive solutions and removed %d possibilities when calling lookForDefinitiveSolutions() for %s[%d]!\n", achievedDefinitive[1], achievedDefinitive[2], type, i);
//            }
            if (achievedDefinitive[0] > 0) {
                System.out.printf("YES: Solved the complete line when calling lookForDefinitiveSolutions() for %s[%d]!\n", type, i);
            }


        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.err.printf("\nThe error above happened, when calling lookForDefinitiveSolutions() for %s[%d]!\n", type, i);
            throw new IllegalArgumentException("Stopped code execution by throwing another error!");
        }


        //run lookForOneSolutionLeft() in OneSudokuLine, but only if line is not yet solved:
        if (!lSolved[0]) {
            try {
                achievedOneSolutionLeft = oneLine.lookForOneSolutionLeft();

                //for extensive debugging:
//                if (achievedOneSolutionLeft[1] > 0 || achievedOneSolutionLeft[2] > 0) {
//                    System.out.printf("Found %d new definitive solutions and removed %d possibilities when calling lookForOneSolutionLeft() for %s[%d]!\n", achievedOneSolutionLeft[1], achievedOneSolutionLeft[2], type, i);
//                }
                if (achievedOneSolutionLeft[0] > 0) {
                    System.out.printf("YES: Solved the complete line when calling lookForOneSolutionLeft() for %s[%d]!\n", type, i);
                }

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                System.err.printf("\nThe error above happened, when calling lookForOneSolutionLeft() for %s[%d]!\n", type, i);
                throw new IllegalArgumentException("Stopped code execution by throwing another error!");
            }
        }

        //run lookForOneSpotLeft() in OneSudokuLine, but only if line is not yet solved:
        if (!lSolved[0]) {
            try {
                achievedOneSpotLeft = oneLine.lookForOneSpotLeft();

                //for extensive debugging:
//                if (achievedOneSpotLeft[1] > 0 || achievedOneSpotLeft[2] > 0) {
//                    System.out.printf("Found %d new definitive solutions and removed %d possibilities when calling lookForOneSpotLeft() for %s[%d]!\n", achievedOneSpotLeft[1], achievedOneSpotLeft[2], type, i);
//                }
                if (achievedOneSpotLeft[0] > 0) {
                    System.out.printf("YES: Solved the complete line when calling lookForOneSpotLeft() for %s[%d]!\n", type, i);
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.err.printf("\nThe error above happened, when calling lookForOneSpotLeft() for %s[%d]!\n", type, i);
                throw new IllegalArgumentException("Stopped code execution by throwing another error!");
            }
        }


        //for the return statement:
        int[] achieved = new int[3];
        for (int k = 0; k < 3; k++) {
            achieved[k] += achievedDefinitive[k];
            achieved[k] += achievedOneSolutionLeft[k];
            achieved[k] += achievedOneSpotLeft[k];
        }

        return achieved;
    }


    //-----------------------------------------------------
    //PRINTING-FUNCTIONS:
    //-----------------------------------------------------

    //easy output, also suitable for https://www.codingame.com/ide/puzzle/mini-sudoku-solver:
    void printEasy() {
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s; j++) {
                if (sudoku[i][j][0] > 0) {
                    System.out.print(sudoku[i][j][0]);
                } else {
                    System.out.print('0');
                }
            }
            System.out.println();
        }
    }

    //easy output, also suitable for https://www.codingame.com/ide/puzzle/mini-sudoku-solver:
    void printMiniSudokuSolver() {
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s; j++) {
                System.out.print(sudoku[i][j][0]);
            }
            System.out.println();
        }
    }

    void printFull() {
        BoxDrawing boxDrawing = new BoxDrawing(s, subDivision, "normal");
        boxDrawing.printSudoku(sudoku);
    }

    void printReport() {
        System.out.println("\n---------------------------------------------");
        System.out.println("Report:");
        int unsolvedCells = 0;
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s; j++) {
                if (sudoku[i][j][0] <= 0) {
                    unsolvedCells++;
                }
            }
        }
        System.out.println("Still unsolved total Cells: " + unsolvedCells);

        int unsolvedHLines = 0;
        for (int i = 0; i < s; i++) {
            if (!hSolved[i][0]) {
                unsolvedHLines++;
            }
        }
        System.out.println("Still unsolved hLines: " + unsolvedHLines);

        int unsolvedVLines = 0;
        for (int i = 0; i < s; i++) {
            if (!vSolved[i][0]) {
                unsolvedVLines++;
            }
        }
        System.out.println("Still unsolved vLines: " + unsolvedVLines);

        int unsolvedsubGroups = 0;
        for (int i = 0; i < s; i++) {
            if (!subSolved[i][0]) {
                unsolvedsubGroups++;
            }
        }

        System.out.println("Still unsolved SubGroups: " + unsolvedsubGroups);
        System.out.println("---------------------------------------------\n");
    }

    void printOneLine(int[][] line) {

        BoxDrawing boxDrawing = new BoxDrawing(s, subDivision, "normal");

        boxDrawing.printOneSudokuLine(line);
    }

    void printIndexesOfCells() {
        //nice for debugging:

        //top line (just the tips of the vertical lines:
        StringBuilder topLine = new StringBuilder();
        topLine.append(' ');
        for (int i = 0; i < s; i++) {
            topLine.append("   ");
            if (subDivision != 0 && i != s - 1 && (i + 1) % subDivision == 0) {
                topLine.append("\u257B ");
            }
        }
        System.out.println(topLine);


        for (int i = 0; i < s; i++) {
            StringBuilder outputLine = new StringBuilder();
            //add a space for nice format:
            outputLine.append(' ');
            for (int j = 0; j < s; j++) {

//                outputLine.append(i + "" + j + " ");
                //I wrote the line above, but IntelliJ suggested to use chained append calls, otherwise it would not run as efficient:
                outputLine.append(i).append(j).append(" ");


                if (subDivision != 0 && j != s - 1 && (j + 1) % subDivision == 0) {
                    outputLine.append("\u2503 ");
                }
            }
            System.out.println(outputLine);

            //add a graphical line, if it is a subdivision:
            if (subDivision != 0 && i != s - 1 && (i + 1) % subDivision == 0) {
                StringBuilder graphicLine = new StringBuilder();
                graphicLine.append('\u2501');
                for (int j = 0; j < s; j++) {

                    //horizontal line:
                    graphicLine.append("\u2501\u2501\u2501");

                    //add a cross at intersections:
                    if (subDivision != 0 && j != s - 1 && (j + 1) % subDivision == 0) {
                        graphicLine.append("\u254B\u2501");

                    }
                }
                System.out.println(graphicLine);
            }

        }

        StringBuilder bottomLine = new StringBuilder();
        bottomLine.append(' ');
        for (int i = 0; i < s; i++) {
            bottomLine.append("   ");
            if (subDivision != 0 && i != s - 1 && (i + 1) % subDivision == 0) {
                bottomLine.append("\u2579 ");
            }
        }
        System.out.println(bottomLine);

    }


    //-----------------------------------
    //OLD CODE - KEEP FOR BACKUP PURPOSES
    //-----------------------------------
    //they are marked as comments inside, so they don't get compiled anymore:

    void printFullBackup() {
/*
        //--------------------------------------------------------------------------------
        //PRINTING-FUNCTIONS: WITH BOX-ART:
        //see also fantastic chart at: https://en.wikipedia.org/wiki/Box-drawing_character
        //--------------------------------------------------------------------------------

        //---------

        //top line:
        System.out.print("\u250F");
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s; j++) {
                System.out.print("\u2501");
            }
            //additional | in 4x4-sudoku after 2nd and 9x9-sudoku after 3rd and 6th:
            if ((s == 4 && i == 1) || (s == 9 && (i == 2 || i == 5))) {
                System.out.print("\u2533");
            } else if (i == s - 1) {
                System.out.print("\u2513");
            } else {
                System.out.print("\u252F");

            }

        }
        System.out.println();

        //-----
        //body:
        for (int i = 0; i < s; i++) {
            System.out.print("\u2503");
            for (int j = 0; j < s; j++) {

                //if a solution is present:
                if (sudoku[i][j][0] > 0) {
                    System.out.print(" " + sudoku[i][j][0]);
                    for (int k = 2; k < s; k++) {
                        System.out.print(" ");
                    }
                }

                //if a solution is not present, print possibilities:
                //print " " for not present possibilities
                else {
                    for (int k = 1; k <= s; k++) {
                        if (sudoku[i][j][k] > 0) {
                            printSubscript(sudoku[i][j][k]);
                        } else {
                            System.out.print(" ");
                        }
                    }
                }

                //additional | in 4x4-sudoku after 2nd and 9x9-sudoku after 3rd and 6th:
                if ((s == 4 && j == 1) || (s == 9 && (j == 2 || j == 5)) || j == s - 1) {
                    System.out.print("\u2503");
                } else {
                    System.out.print("\u250A");  //or \u2502 for standard vertical line or \u2506 for differently dotted
                }
            }
            System.out.println();

            //-------------
            //bottom lines:

            //first char (different branches for fat lines or thin lines or last line):
            if ((s == 4 && i == 1) || (s == 9 && (i == 2 || i == 5))) {
                //thick lines:
                System.out.print("\u2523");
            } else if (i == s - 1) {
                //last line:
                System.out.print("\u2517");
            } else {
                //standard line:
                System.out.print("\u2520");
            }


            for (int j = 0; j < s; j++) {

                //different horizontal lines in 4x4-sudoku after 2nd and 9x9-sudoku after 3rd and 6th AND for last line:
                if ((s == 4 && i == 1) || (s == 9 && (i == 2 || i == 5))) {
                    //thick horizontal lines:
                    for (int k = 0; k < s; k++) {
                        System.out.print("\u2501");
                    }

                    //crosses at intersections:
                    if ((s == 4 && j == 1) || (s == 9 && (j == 2 || j == 5))) {
                        System.out.print("\u254B");

                    } else if (j == s - 1) {
                        //Last line:
                        System.out.print("\u252B");
                    } else {
                        System.out.print("\u253F");
                    }


                } else if (i == s - 1) {
                    //last horizontal line:
                    for (int k = 0; k < s; k++) {
                        System.out.print("\u2501");
                    }

                    //crosses at intersections:
                    if ((s == 4 && j == 1) || (s == 9 && (j == 2 || j == 5))) {
                        System.out.print("\u253B");

                    } else if (j == s - 1) {
                        //Last line:
                        System.out.print("\u251B");
                    } else {
                        System.out.print("\u2537");
                    }

                } else {
                    for (int k = 0; k < s; k++) {
                        //standard horizontal lines:
                        System.out.print("\u2508");   //or  \u2500 for standard line or \u2504 for differently dotted!
                    }

                    //crosses at intersections:
                    if ((s == 4 && j == 1) || (s == 9 && (j == 2 || j == 5))) {
                        System.out.print("\u2542");

                    } else if (j == s - 1) {
                        //Last line:
                        System.out.print("\u2528");
                    } else {
                        System.out.print("\u253C");
                    }
                }


            }
            System.out.println();
        }
*/
    }

    void printFullBackupOld() {
/*
        //-----------------------------------------------------
        //BEFORE THE BOX-ART-STUFF!!!
        //-----------------------------------------------------

        //yet without possible solutions, just known elements:
        //works currently for 4x4 sudoku:

        //top line:
        System.out.print("\u2016");
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s; j++) {
                System.out.print("=");
            }
            //additional | in 4x4-sudoku after 2nd and 9x9-sudoku after 3rd and 6th:
            if ((s == 4 && i == 1) || (s == 9 && (i == 2 || i == 5)) || i == s - 1) {
                System.out.print("\u2016");
            } else {
                System.out.print("|");

            }

        }
        System.out.println();

        //body:
        for (int i = 0; i < s; i++) {
            System.out.print("\u2016");
            for (int j = 0; j < s; j++) {

                //if a solution is present:
                if (sudoku[i][j][0] > 0) {
                    System.out.print(" " + sudoku[i][j][0]);
                    for (int k = 2; k < s; k++) {
                        System.out.print(" ");
                    }
                }

                //if a solution is not present, print possibilities:
                //print " " for not present possibilities
                else {
                    for (int k = 1; k <= s; k++) {
                        if (sudoku[i][j][k] > 0) {
                            printSubscript(sudoku[i][j][k]);
                        } else {
                            System.out.print(" ");
                        }
                    }
                }

                //additional | in 4x4-sudoku after 2nd and 9x9-sudoku after 3rd and 6th:
                if ((s == 4 && j == 1) || (s == 9 && (j == 2 || j == 5)) || j == s - 1) {
                    System.out.print("\u2016");
                } else {
                    System.out.print("|");
                }
            }
            System.out.println();

            //bottom lines:
            System.out.print("\u2016");
            for (int j = 0; j < s; j++) {


                //different symbol lines in 4x4-sudoku after 2nd and 9x9-sudoku after 3rd and 6th AND for last line:
                if ((s == 4 && i == 1) || (s == 9 && (i == 2 || i == 5)) || i == s - 1) {
                    for (int k = 0; k < s; k++) {
                        System.out.print("=");
                    }
                } else {
                    for (int k = 0; k < s; k++) {
                        System.out.print("-");
                    }
                }

                //additional | in 4x4-sudoku after 2nd and 9x9-sudoku after 3rd and 6th:
                if ((s == 4 && j == 1) || (s == 9 && (j == 2 || j == 5)) || j == s - 1) {
                    System.out.print("\u2016");
                } else {
                    System.out.print("|");
                }

            }
            System.out.println();
        }
*/
    }

    //used by printFullBackup(), to print small numbers (unicode subscript)
    private void printSubscript(int number) {
/*
        //print small numbers:
        //with Unicode superscript symbols: https://en.wikipedia.org/wiki/Unicode_subscripts_and_superscripts
        //and https://stackoverflow.com/questions/5585919/creating-unicode-character-from-its-number
        switch (number) {
            case 1:
                System.out.print("\u2081");
                break;
            case 2:
                System.out.print("\u2082");
                break;
            case 3:
                System.out.print("\u2083");
                break;
            case 4:
                System.out.print("\u2084");
                break;
            case 5:
                System.out.print("\u2085");
                break;
            case 6:
                System.out.print("\u2086");
                break;
            case 7:
                System.out.print("\u2087");
                break;
            case 8:
                System.out.print("\u2088");
                break;
            case 9:
                System.out.print("\u2089");
                break;
        }
*/
    }


}
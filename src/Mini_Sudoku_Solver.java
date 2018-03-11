import java.util.Scanner;





//https://www.codingame.com/training/community/mini-sudoku-solver

//todo: write concept first!!!

/*


initialize objects, which contains elements of: (the elements MUST be Objects (like int[] (containing all the possible solutions of one cell),
or maybe even Integer[], NOT basic types (else, the referencing doesn't work!)
The lines should only point to the values, but not contain the values itself (the values are in sudoku itself
    -> one for every horizontal line  (s elements)
    -> one for every vertical line  (s elements)
    -> one for every sub-group  (s elements)

write a function, which:
    - takes s objects (size of sudoku) -> can be horizontal line, vertical line or a sub-group (like in a 4x4 or 9x9 or 16x16 sudoku)
    - this object should be of type int[] (maybe Integer[] ?!?), of which:
        - arr[0] >= 1   -> contains either definitive solution
        - arr[0] == 0   -> shouldn't be, otherwise there is no solution (unless, the sudoku has not been properly initialized by the constructor!
        - arr[0] < 0    -> -n possible solutions



check for duplicates in Index 0, but only for numbers higher than 0 -> return error!!



//todo: later on, write setters, where people can solve the puzzle themselves -> the setters check, whether the solution would be currently valid



 */

























public class Mini_Sudoku_Solver {


    public static void main(String args[]) {
/*
        Scanner in = new Scanner(System.in);
        String[] lines = new String[4];
        for (int i = 0; i < 4; i++) {
            lines[i] = in.next();
        }
*/


        //for making the code more universal:
        //set the size of the sudoku: -> that means, that will be used a lot!
        //maximum size: 9 (otherwise, code for parsing has to be rewritten; the rest should work)
        int s = 4;

        //todo: the code is not yet written for standard 9x9 sudoku, with 3x3 sub-patterns. just realize this as boolean, whether it uses sub-patterns or not (only for sizes, which have valid square-roots, like 4x4, 9x9, 16x16,...)



/*
        //initial array with funny values (to test the error-catching:
        String[] lines = new String[s];
        lines[0] = "2043";
        lines[1] = "0a205";
        lines[2] = "4394";
        lines[3] = "003";
*/

        //code for testing in IntelliJ:
        String[] lines = new String[s];
        lines[0] = "2043";
        lines[1] = "0020";
        lines[2] = "4300";
        lines[3] = "0034";


        //convert to int:

        //-----------------------------------
        //THREE DIMENSIONAL ATTEMPT:
        //-----------------------------------
        //1st dimension (size s): line
        //2nd dimension (size s): specific number
        //3rd dimension (size s + 1!!!):
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
        //

        int[][][] sudoku = new int[s][s][s + 1];

        //remember: all values are initialized to 0. Check this:
//        System.err.println(sudoku[2][1][3]);


        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s; j++) {
                try {
                    //Integer.parseInt parses a String to an Int. So in order to convert the char to an int,
                    //the easy trick is to add   ""+   -> this makes a string out the char:
                    sudoku[i][j][0] = Integer.parseInt("" + lines[i].charAt(j));
                    if (sudoku[i][j][0] < 0 || sudoku[i][j][0] > s) {
                        throw new IllegalArgumentException();
                    }
                } catch (NumberFormatException e) {
                    //parsing error:
                    System.err.printf("ERROR with parsing: the provided char at line %d, position %d ('%s') is not of type int! It will be automatically set to '0'.\n", i + 1, j + 1, lines[i].charAt(j));
                    //since all elements in sudoku are automatically initialized to 0, I don't have do this manually!
                } catch (IllegalArgumentException e) {
                    //this happens, when parsing was successful, but if the number was below 0 or greater than 's'
                    //('s' is the size of the sudoku)
                    System.err.printf("ERROR: the provided number at line %d, position %d ('%d') is not valid (size of sudoku is %d)! It will be automatically set to '0'.\n", i + 1, j + 1, s, sudoku[i][j][0]);
                    sudoku[i][j][0] = 0;
                } catch (StringIndexOutOfBoundsException e) {
                    //this happens, when not enough numbers were provided per line:
                    System.err.printf("ERROR: no number (or 0) provided at line %d, position %d! It will be automatically set to '0'.\n", i + 1, j + 1);
                    //since all elements in sudoku are automatically initialized to 0, I don't have do this manually!
                } catch (Exception e) {
                    //if there was another error:
                    e.printStackTrace();
                }
            }
            //for nice output:
            System.err.printf("Parsed line %d: ", i + 1);
            for (int j = 0; j < s; j++) {
                System.err.print(sudoku[i][j][0]);
            }
            System.err.println();
        }





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


        //for debugging: print the sudoku at the current state:
        //this would print out for a 4x4 sudoku the following:
        //if input is 3 -> 30000
        //if input is 0 -> -41234  (the first is '-4' meaning the possible solutions!
        System.err.println("\nSudoku at the current state:");
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s; j++) {
                for (int n = 0; n <= s; n++) {
                    System.err.print(sudoku[i][j][n]);
                }
                System.err.print(" ");
            }
            System.err.println();
        }





        //set variable to the current maximum of possibilities in all the positions (adjust later)
        int maxSolutions = s;


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
        boolean[][] hSolved = new boolean[s][s + 1];
        //vertically:
        boolean[][] vSolved = new boolean[s][s + 1];



        //number of loops done:
        //also use this for stuff, that should only be done in the first run, but never again afterwords...
        //or use it for stuff, that shouldn't run the first time...
        //or maybe even more complex sudoku-algorithms which should only be considered after the 10th run or so... (e.g. assumptions and the possibility of multiple solutions)
        int runs = 0;


        //todo: from here on: do while-loop. But introduce a variable BEFORE, which counts the runs! That makes it easier to count the loops, and to define things to run only in the first run, or only after the first run
        //todo: BUT WAIT WITH THE LOOP FOR BETTER DEBUGGING!
        //define the while-condition with:   while (maxSolutions > 1 && runs < 1000)    -> with this, I can limit the number of runs, so it terminates eventually,
        //even if the program doesn't find a solution



        //for debugging: print the sudoku at the current state:
        //this would print out for a 4x4 sudoku the following:
        //if input is 3 -> 30000
        //if input is 0 -> -41234  (the first is '-4' meaning the possible solutions!
        System.err.println("\nSudoku at the current state:");
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s; j++) {
                for (int n = 0; n <= s; n++) {
                    System.err.print(sudoku[i][j][n]);
                }
                System.err.print(" ");
            }
            System.err.println();
        }





        //--------------------------------------------------
        //Now follows the erasing of not possible solutions:
        //--------------------------------------------------

        //--------------------------------------------------
        //the first iteration is a bit simple -> if there is already a solved position, erase all other occurrences of this number:
        //--------------------------------------------------


        //check for already solved cells. If yes, set hSolved of the corresponding line and corresponding number to true:
        //check for double occurrences (if someone typed the same number twice in the input):
        //horizontally and vertically at the same iteration  (by swapping i and j!)
        //then remove other possibilities:





        for (int i = 0; i < s; i++) {
            for (int n = 1; n <= s; n++) {

                //check for already solved cells. If yes, set hSolved of the corresponding line and corresponding number to true:
                for (int j = 0; j < s; j++) {

                    //for horizontal checking:
                    if (sudoku[i][j][0] == n) {
                        if (hSolved[i][n]) {
                            System.err.printf("Double occurrence found!! The number '%d' was entered more than once in the same horizontal line %d. The second occurrence was automatically replaced by a 0.\n", n, i + 1);
                            //in order for the code to continue, this double (or triple...) occurrence will be set to '0':
                            sudoku[i][j][0] = 0;
                        } else {
                            hSolved[i][n] = true;
                        }
                    }


                    //todo: separate vertical from horizontal checking again. Otherwise you cannot profit of an already solved vertical or horizontal line!!!! This is getting very relevant in later runs!!!


                    //for vertical checking:
                    if (sudoku[j][i][0] == n) {
                        if (vSolved[i][n]) {
                            System.err.printf("Double occurrence found!! The number '%d' was entered more than once in the same vertical line %d. The second occurrence was automatically replaced by a 0.\n", n, i + 1);
                            //in order for the code to continue, this double (or triple...) occurrence will be set to '0':
                            sudoku[j][i][0] = 0;
                        } else {
                            vSolved[i][n] = true;
                        }
                    }

                    //todo: for sub-groups as in the 9x9 sudoku (subgroups of 3x3)

                }

                //remove possibilities in lines, if the given number is already solved:
                //horizontally:
                if (hSolved[i][n]) {
                    for (int j = 0; j < s; j++) {
                        //only for the yet unsolved cells and only if possibility is still there (otherwise the count of possible solutions goes wrong):
                        if (sudoku[i][j][0] < 1 && sudoku[i][j][n] != 0) {
                            sudoku[i][j][n] = 0;
                            //reduce the count of the possible solutions:
                            sudoku[i][j][0]++;  // use ++, since it is a negative value!
                        }
                    }
                }

                //vertically:
                if (vSolved[i][n]) {
                    for (int j = 0; j < s; j++) {
                        //only for the yet unsolved cells:
                        if (sudoku[j][i][0] < 1 && sudoku[j][i][n] != 0) {
                            sudoku[j][i][n] = 0;
                            //reduce the count of the possible solutions:
                            sudoku[j][i][0]++;
                        }
                    }
                }

                //todo: for sub-groups as in the 9x9 sudoku (subgroups of 3x3)
            }
        }


        //for debugging: print the sudoku at the current state:
        //this would print out for a 4x4 sudoku the following:
        //if input is 3 -> 30000
        //if input is 0 -> -41234  (the first is '-4' meaning the possible solutions!
        System.err.println("\nFor debugging: print the sudoku at the current state:");
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s; j++) {
                for (int n = 0; n <= s; n++) {
                    System.err.print(sudoku[i][j][n]);
                }
                System.err.print(" ");
            }
            System.err.println();
        }

        //todo: convert the cells that only have one solution left in to an actual solution!! and also adjust it's hSolved or vSolved!! AND REMOVE POSSIBLE SOLUTIONS IN CONNECTED CELLS:

        //todo: check, if in a line (hSolved, vSolved), which is not yet marked as completely solved, all numbers are currently solved -> then set it as completely solved!

        //todo: calculate current maxSolutions. Then abort the loop or repeat as necessary.  Increase the 'runs'-count.







    }

}


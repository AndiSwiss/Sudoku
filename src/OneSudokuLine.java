public class OneSudokuLine {

    //this class takes s objects (for a 9x9 sudoku, s is 9)
    //and the corresponding solved item
    //remember: because Arrays are objects, this local variables 'line' and 'lSolved' point to their respective original values!

    private int[][] line;
    private boolean[] lSolved;
    private int s;

    //constructor:
    OneSudokuLine(int[][] line, boolean[] lSolved) {
        this.line = line;
        this.lSolved = lSolved;
        this.s = line.length;
    }


    int[] lookForDefinitiveSolutions() throws IllegalArgumentException {

        //look for definitive solutions:

        //for the return statement, do this:
        int[] achieved = new int[3];
        //use achieved[0] for a completely solved line
        //use achieved[1] for a freshly solved cell (not, if there was already a solution present in this cell!)
        //use achieved[2] for a removed possibility


        //iterate over each number -> 'n' (int n = 1; n <= s; n++)
        for (int n = 1; n <= s; n++) {
            //only run code if lSolved[n] is false
            if (!lSolved[n]) {
                //iterate over all cells -> 'i'
                for (int i = 0; i < s; i++) {
                    if (line[i][0] == n) {
                        //check for duplicate (meaning, if already found one in a previous iteration:
                        if (lSolved[n]) {
                            throw new IllegalArgumentException("Duplicate found! The number " + n + " was found more than once in this line!!");
                        } else {

                            //with the achieved =... your passing on the results of the method foundDefinitiveSolution():
                            int[] newlyAchieved = foundDefinitiveSolution(i, n);

                            for (int k = 0; k < newlyAchieved.length; k++) {
                                //add the new achievements:
                                achieved[k] += newlyAchieved[k];
                            }
                        }
                    }
                }
            }
        }


        return achieved;
    }

    int[] lookForOneSolutionLeft() throws IllegalArgumentException {
        //look for cells with only one solution left

        //for the return statement, do this:
        int[] achieved = new int[3];
        //use achieved[0] for a completely solved line
        //use achieved[1] for a freshly solved cell (not, if there was already a solution present in this cell!)
        //use achieved[2] for a removed possibility



        //repeat searching, because this will have impact on the other cells! Use variable 'foundNew' for this:
        boolean foundNew;
        boolean foundNewInCell;
        //and keep track of the repeats:
//        int runs = 0;

        do {
            foundNew = false;
//            runs++;

            //iterate over all cells:
            for (int i = 0; i < s; i++) {

                //check whether only one possibility is left:
                if (line[i][0] == -1) {

                    //used for error-checking:
                    foundNewInCell = false;

                    //iterate over the numbers 'n' to find which number it is (check for duplicate!)
                    for (int n = 1; n <= s; n++) {
                        if (line[i][n] == n) {
                            //found the number, but check if not already found another one (if yes -> error!!):
                            if (foundNewInCell) {
                                throw new IllegalArgumentException("ERROR: this should not have happened, because this means that there ARE still two ore more possibilities in this cell!!\n" +
                                        "that means that the counter in (line[" + i + "][0] == -1) was wrong!!");
                            } else {
                                foundNewInCell = true;
                                foundNew = true;
                                int[] newlyAchieved = foundDefinitiveSolution(i, n);
                                for (int k = 0; k < newlyAchieved.length; k++) {
                                    //add the new achievements:
                                    achieved[k] += newlyAchieved[k];
                                }
                            }
                        }
                    }
                }
            }
        } while (foundNew && !lSolved[0]);


//        if (runs > 1) {
//            //only print, if this was running more than once
//            System.out.println("lookForOneSolutionLeft() more than once! (" + runs + " times)");
//        }

        return achieved;

    }

    int[] lookForOneSpotLeft() {
        //so check in unsolved numbers, whether n appears in only one cell -> if yes, convert to definitive solution

        //for the return statement, do this:
        int[] achieved = new int[3];
        //use achieved[0] for a completely solved line
        //use achieved[1] for a freshly solved cell (not, if there was already a solution present in this cell!)
        //use achieved[2] for a removed possibility

        //repeat searching, because this will have impact on the other cells! Use variable 'foundNewSolution' for this:
        boolean foundNewSolution;
        int foundInCell;
        //and keep track of the repeats:
//        int runs = 0;

        do {
            foundNewSolution = false;
//            runs++;

            //iterate over all numbers:
            for (int n = 1; n <= s; n++) {

                //only if not yet found a definitive solution for this number:
                if (!lSolved[n]) {

                    //keep track where this number occurred:
                    //-1 = not yet found
                    foundInCell = -1;

                    //look in all the cells:
                    for (int i = 0; i < s; i++) {
                        if (line[i][n] == n) {

                            //found one. Check if not yet already found. If found, abort with break:
                            if (foundInCell == -1) {
                                foundInCell = i;
                            } else {
                                //already found -> abort:
                                foundInCell = -2;
                                break;
                            }
                        }
                    }

                    //
                    if (foundInCell >= 0) {
                        foundNewSolution = true;
                        int[] newlyAchieved = foundDefinitiveSolution(foundInCell, n);
                        for (int k = 0; k < newlyAchieved.length; k++) {
                            //add the new achievements:
                            achieved[k] += newlyAchieved[k];
                        }
                    }
                }
            }

        } while (foundNewSolution && !lSolved[0]);

//        if (runs > 1) {
//            //only print, if this was running more than once
//            System.out.println("lookForOneSolutionLeft() more than once! (" + runs + " times)");
//        }

        return achieved;

    }


    private int[] foundDefinitiveSolution(int atCell, int n) throws IllegalArgumentException {
        //n is the number, which was found as a definitive solution
        //atCell is the cell, in which this definitive solution should go to or already is in.


        //this method assumes, that the definitive solution has NOT been written in to this cell without having deleted all the other possibilities in this cell!!!
        //so either: line[atCell][0] == n   AND  all line[atCell][1..9] == 0
        //or         line[atCell][0] < 0    AND      line[atCell][n] == n    (this happens when a solution just has been found but not yet written as a definitive solution.
        //ELSE: throw errors!!! (like if there is another solution in that cell or it is 0 or whatever...)

        //for the return statement, do this:
        int[] achieved = new int[3];
        //use achieved[0] for a completely solved line
        //use achieved[1] for a freshly solved cell (not, if there was already a solution present in this cell!)
        //use achieved[2] for a removed possibility


        if (line[atCell][0] == n) {
            //this means, the definitiveSolution is already set
            //then just double check, whether the possibility fields are empty in this cell
            for (int j = 1; j <= s; j++) {
                if (line[atCell][j] != 0) {
                    throw new IllegalArgumentException(String.format("the definitive Solution was set: line[%d][0] == %d, but the possibilities in this field were not cleaned up (line[%d][%d] was NOT 0!)",
                            atCell, n, atCell, j));
                }
            }
            //if that is also fine, continue with code further down the line...

        } else if (line[atCell][0] < 0 && line[atCell][n] == n) {
            //this means, that a fresh Solution was found:
            line[atCell][0] = n;

            achieved[1]++;


            //delete all possibilities in this cell:
            for (int j = 1; j <= s; j++) {
                line[atCell][j] = 0;
            }
            //continue with code further down the line...

        } else {
            throw new IllegalArgumentException(String.format("Error in foundDefinitiveSolution(): line[%d][0] = %d  and  line[%d][%d] was %d (should be %d).",
                    atCell, line[atCell][0], atCell, n, line[atCell][n], n));
        }


        //then check for duplicate definitive Solution in all OTHER cells than line[atCell][0], throw an error if found
        //if it is valid, erase:
        //and in the same run, remove n as a possibility in this cell and reduce its possibility-count
        for (int i = 0; i < s; i++) {
            //only check in OTHER cells than 'atCell':
            if (i != atCell) {
                //check for duplicate:
                if (line[i][0] == n) {
                    throw new IllegalArgumentException(String.format("The new found solution %d in line[%d][0] turns out to already exist in line[%d][0] -> a duplicate!!",
                            n, atCell, i));
                }

                //if that cell is not yet already solved, then delete this possibility
                //for this, i could check (line[i][0] < 1), but this is checked later on anyhow -> throwing an error if (line[i][0] > -2):
                //so look, whether the possibility exists:

                if (line[i][n] != 0) {
                    //delete this number as a possibility in this cell, if it exists (this check is necessary for being able to adjust the possibilities-count:
                    line[i][n] = 0;

                    //and reduce the possibilities-count:
                    if (line[i][0] > -2) {
                        throw new IllegalArgumentException(String.format("In method foundDefinitiveSolution (n = %d),  line[%d][0] was %d. That means line[%d][0]++ would increment the definitive solution by 1!!", n, i, line[i][0], i));
                    } else {
                        line[i][0]++;  //reducing the possibilities-count, since it is negative, reducing is done with ++!

                        achieved[2]++;
                    }

                }


            }
        }


        lSolved[n] = true;

        //check if all numbers are solved (lSolved[1 to 9] == true  ->  lSolved[0) = true;
        int solvedYet = 0;
        for (int i = 1; i <= s; i++) {
            if (lSolved[i]) {
                solvedYet++;
            }
        }
        if (solvedYet == s) {
            lSolved[0] = true;

            achieved[0]++;
        }
//        else {
//            System.out.println("You have already " + n + " numbers solved. Still " + (s - n) + " to go.");
//        }

        return achieved;

    }


    //-----------------------------------------------------
    //PRINTING-FUNCTIONS:
    //-----------------------------------------------------
    void printLineBox() {

        //printing line:
        int s = line.length;
        int subDivision = 0;
        if (s == 4) {
            subDivision = 2;
        } else if (s == 9) {
            subDivision = 3;
        }
        BoxDrawing boxDrawing = new BoxDrawing(s, subDivision, "normal");
        boxDrawing.printOneSudokuLine(line);


        //printing lSolved, only if !null:

        if (lSolved != null) {
            StringBuilder out = new StringBuilder();
            out.append("Solved numbers: ");
            for (int i = 1; i < lSolved.length; i++) {
                if (lSolved[i]) {
                    out.append(i).append(',');
                }
            }
            //delete last comma:
            out.deleteCharAt(out.length() - 1);

            out.append("  --  Not yet solved: ");
            for (int i = 1; i < lSolved.length; i++) {
                if (!lSolved[i]) {
                    out.append(i).append(',');
                }
            }
            out.deleteCharAt(out.length() - 1);
            System.out.println(out);
            System.out.println();
        }
    }



}

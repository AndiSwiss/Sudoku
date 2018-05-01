/**
 * Testing class for OneSudokuLine
 */
public class OneSudokuLineTesting {
    public static void main(String[] args) {
        // for 9x9 sudokus:
        System.out.println("\n------------------------------------------------------------------");
        System.out.println("9x9 sudoku:");
        System.out.println("------------------------------------------------------------------");


        String[] sudo9hardStr = {
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

        String[] sudo9easyStr = {
                "008900001",
                "700103098",
                "003000060",
                "000048100",
                "017000640",
                "005210000",
                "020000800",
                "680501007",
                "500006300"
        };
        Sudoku sudo9easy = new Sudoku(9, sudo9easyStr);
        Sudoku sudo9hard = new Sudoku(9, sudo9hardStr);






        sudo9hard.solve();

//        sudo9easy.solve();





        // todo: look at double pairs:
        // if in a certain line (or subgroup) are two cells with both the identical possibilities left
        // this can be realized like this:
        //   - find a cell with exactly 2 possibilities. Get their values
        //   - maybe save them in a TreeMap or TreeSet
        //   - if you find another cell in that line with also 2 possibilities, then compare them with the already stored values in the TreeMap or TreeSet
        //           -> if found, then hurray, if not found, add to the TreeMap (or TreeSet), until the last cell is reached
        //   -> if such a pair is found twice, then delete the two possibilities out of all the other cells in this line.
        // e.g. in sudo9easy: subGroup[8]: two cells with numbers 4 and 9 -> then you can delete 4 and 9 from all other cells
        // or in hLine[4] (pair 5 and 9)
        //    -> here you can actually delete all other occurrences of 5 and 9 in hLine[4] AND in subGroup[4]
        // or also in vLine[7] (pair 3 and 7)
        // or in hLine[7] (pair 4 and 9)




        // todo: rewrite the constructor of OneSudokuLine slightly, so that it is aware, in which line it actually is:
        // original:       OneSudokuLine(int[][] line, boolean[] lSolved)
        // new:            OneSudokuLine(int[][][] wholeSudoku, String type, int lineNumber)   --   String type:  contains "hLine", "vLine" or "subGroup"
        //                   wholeSudoku is good, because out of this, I can get all the data I need to construct
        //                   todo: but that would be ridiculous; I would do everything again (calculate hLine, calculate vLine,,....)

        //       todo: BETTER: do some returning: ArrayList HashMap  -> e.g. new found definitive solutions (= this also equals to a new solved cell)
        //       and also amount of reduced possibilities
        //       with this I can print these values out AND do very intelligent calling of the individual solving parts:
        //       lookForDefinitiveSolutions(); would ONLY have to be run once!! Afterwords, If a new definitive solution would be found, just call the
        //       foundDefinitiveSolution(int n)   (for this, move the duplicate-checking from lookForDefinitiveSolutions() to foundDefinitiveSolution(int n)
        //       and after that run
        //



        // todo: check, if a certain number of a hLine or a vLine has to appear in a certain subGroup:
        // for example in the sudo9easy: in hLine[4], if you look at number 5, this number has to be in this horizontal line inside the middle subGroup
        //  -> so you can erase the 5 out of cell sudoku[4][8]
        // but I would have to completely rewrite the code, since it would have to look at a hLine AND at the corresponding subGroups at the same time

        // todo: very efficient would be to call always (iteratively) functions, if a definitive solution was found,
        // since this has massive impact and can lead to immediate full solution.
        // actually, todo: what you can instantly program, is that if a definitive solution is found in a cell, ALL the following can be set immediately:
        //                 for example in sudo9easy if 4 is found in sudoku[2,1] -> then hSolved[2][4] = true, vSolved[1][4] = true, and subSolved[0][4] = true
        //                 actually, this is only smart, if the corresponding functions foundDefinitiveSolution(...) would be executed
        //                 but for this, this function would have to live in the normal sudoku-class (and also it's callers! -> todo: i would have to rewrite a lot of code
        //                 maybe don't realize this!





        // todo: LAST POSSIBILITY: MAKE ASSUMPTIONS: easy solution to go on, BUT NOT ELEGANT!:
        // if no more solutions are found, take the first cell with the least amount of possibilities and just make an assumption:
        // -> first look if there is any cell, which only has two possibilities left -> then take it
        // here, it is already the first cell (index [0][0]):
//        sudo9easy.sudoku[0][0][0] = 2;
//        sudo9easy.sudoku[0][0][2] = 0;
//        sudo9easy.sudoku[0][0][4] = 0;
//        sudo9easy.solve();
//        // if it throws an error, then this number is NOT possible
//        // but if it doesn't throw an error, the solution is still not definitive, try the other number:
//        sudo9easy.sudoku[0][0][0] = 4;
//        sudo9easy.sudoku[0][0][2] = 0;
//        sudo9easy.sudoku[0][0][4] = 0;
//        sudo9easy.solve();
        // if that throws an error, then it was the first try (here: 2)
        // if this also doesn't throw an error, both 2 and 4 are still possible -> reset these runs (how?!?) and try with a different cell!
        // todo: make the methods throw the errors -> so it can be used better for error-tracking (with more enhanced error-messages (more info, WHEN exactly it happened)



        // todo: but you should write more intelligent code, with more advanced human-style sudoku-solving (without assuming a value!)

        // the following assumption works right away, the sudoku is solved!
//        sudo9easy.sudoku[8][8][0] = 9;
//        sudo9easy.sudoku[8][8][9] = 0;
//        sudo9easy.sudoku[8][8][4] = 0;
//        sudo9easy.solve();





    }
}

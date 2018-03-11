public class Testing_subGroups_and_h_and_vLines {
    public static void main(String[] args) {




        //for 4x4 sudokus:
        System.out.println("------------------------------------------------------------------");
        System.out.println("4x4 sudoku:");
        System.out.println("------------------------------------------------------------------");
        String[] sudo4Str = {"2043", "0020", "4300", "0034"};
        Sudoku sudo4 = new Sudoku(4, sudo4Str);
        //--------------------------------------------
        //horizontal Lines:
        //--------------------------------------------
        System.out.println("\nhLines-test:");
        Sudoku hLinesCopy4 = new Sudoku(4);
        hLinesCopy4.sudoku = sudo4.hLines;

        System.out.println("\nOriginal sudoku:");
        sudo4.printEasy();
        System.out.println("\nhLines:");
        hLinesCopy4.printEasy();
        //test successful.


        //--------------------------------------------
        //vertical lines:
        //--------------------------------------------
        System.out.println("\nvLines-test:");
        Sudoku vLinesCopy4 = new Sudoku(4);
        vLinesCopy4.sudoku = sudo4.vLines;

        System.out.println("\nOriginal sudoku:");
        sudo4.printEasy();
        System.out.println("\nvLines:");
        vLinesCopy4.printEasy();
        //test was successful!


        //--------------------------------------------
        //sub groups:
        //--------------------------------------------
        System.out.println("\nsubGroups-test:");
        Sudoku subGroupCopy4 = new Sudoku(4);
        subGroupCopy4.sudoku = sudo4.subGroups;

        System.out.println("\nOriginal sudoku:");
        sudo4.printEasy();
        System.out.println("\nsubGroups:");
        subGroupCopy4.printEasy();
        //test was successful!



        //for 9x9 sudokus:
        System.out.println("\n------------------------------------------------------------------");
        System.out.println("9x9 sudoku:");
        System.out.println("------------------------------------------------------------------");


        String[] sudo9Str = {
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
        Sudoku sudo9 = new Sudoku(9, sudo9Str);



        //test-code for hLines, vLines and subGroups:


        //--------------------------------------------
        //horizontal Lines:
        //--------------------------------------------
        System.out.println("\nhLines-test:");
        Sudoku hLinesCopy = new Sudoku(9);
        hLinesCopy.sudoku = sudo9.hLines;

        System.out.println("\nOriginal sudoku:");
        sudo9.printEasy();
        System.out.println("\nhLines:");
        hLinesCopy.printEasy();
        //test successful.


        System.out.println("\nTest-Code for proving, that the arrays point to the same values:");
        //-> hLines is a simple copy of the original array, BUT STILL POINTING TO THE EXACT SAME VALUE (the values are NOT copied)
        //-> hLines is not really necessary, but nice for code-readability  (vLines and subGroups are way more relevant!)

        //test-code:
        System.out.println("so they point to the same object:");
        System.out.println(sudo9.sudoku[0]);
        System.out.println(hLinesCopy.sudoku[0]);
        System.out.println(sudo9.sudoku[0][1]);
        System.out.println(hLinesCopy.sudoku[0][1]);
        System.out.println(sudo9.sudoku[0][2][0]);
        System.out.println(hLinesCopy.sudoku[0][2][0]);

        System.out.println("change hLines[0][2][0] = -3");
        hLinesCopy.sudoku[0][2][0] = -3;
        System.out.println(sudo9.sudoku[0][2][0]);
        System.out.println(hLinesCopy.sudoku[0][2][0]);
        //reset the changed value:
        hLinesCopy.sudoku[0][2][0] = 2;


        //test was successful -> both have changed, so they reference the SAME value!




        //--------------------------------------------
        //vertical lines:
        //--------------------------------------------
        System.out.println("\nvLines-test:");
        Sudoku vLinesCopy = new Sudoku(9);
        vLinesCopy.sudoku = sudo9.vLines;

        System.out.println("\nOriginal sudoku:");
        sudo9.printEasy();
        System.out.println("\nvLines:");
        vLinesCopy.printEasy();
        //test was successful!


        //--------------------------------------------
        //sub groups:
        //--------------------------------------------
        System.out.println("\nsubGroups-test:");
        Sudoku subGroupCopy = new Sudoku(9);
        subGroupCopy.sudoku = sudo9.subGroups;

        System.out.println("\nOriginal sudoku:");
        sudo9.printEasy();
        System.out.println("\nsubGroups:");
        subGroupCopy.printEasy();
        //test was successful!





    }
}

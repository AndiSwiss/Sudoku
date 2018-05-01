/**
 * simple test-class for BoxDrawing
 */
class BoxDrawingTesting {
    public static void main(String[] args) {
        // see also https://en.wikipedia.org/wiki/Box-drawing_character
        // there is a fantastic chart for it:

        BoxDrawing boxDraw1 = new BoxDrawing(9, 3, "normal and double");

        // top line:
        System.out.println(boxDraw1.topLine());


        // regular body line:
        String[] content = {
                "5", "0", "123456789",
                "12345678", "", "1234567",
                "2"
        };
        System.out.println(boxDraw1.regularBodyLine(content));
        System.out.println(boxDraw1.middleLineSingle());
        System.out.println(boxDraw1.regularBodyLine(content));
        System.out.println(boxDraw1.middleLineDouble());
        System.out.println(boxDraw1.regularBodyLine(content));
        System.out.println(boxDraw1.bottomLine());


    }
}

/**
 * draws nice looking boxes in the terminal
 */
class BoxDrawing {
    private int s;
    private int d;

    /**
     * style can be "normal and fat", "normal and double", "very thin and fat" and "very thin and double"
     * (very thin is with dotted lines) or without a division, it can also be "normal", "thin", "double" or "fat"
     */
    private String style; //

    // StylingVariables:
    // set them with the "setStyle"-Setter:

    // special chars for top line:
    private char cornerDoubleTopLeft;
    private char cornerDoubleTopRight;
    private char topDoubleDownSingle;
    private char topDoubleDownDouble;

    // special chars for left vertical line:
    private char leftDoubleRightSingle;
    private char leftDoubleRightDouble;

    // special chars for right vertical line:
    private char rightDoubleLeftSingle;
    private char rightDoubleLeftDouble;

    // only thick lines:
    private char horizontalDouble;
    private char verticalDouble;

    // full crosses:
    private char crossFullDouble;
    private char crossFullSingle;
    private char crossHorizontalDoubleVerticalSingle;
    private char crossHorizontalSingleVerticalDouble;

    // only thin lines:
    private char horizontalSingle;
    private char verticalSingle;

    // special chars for bottom line:
    private char cornerDoubleBottomLeft;
    private char cornerDoubleBottomRight;
    private char bottomDoubleUpSingle;
    private char bottomDoubleUpDouble;


    /**
     * constructor for BoxDrawing with sub divisions
     *
     * @param size        horizontal and vertical size (is always the same)
     * @param subDivision for visual sub divisions (use 0 for no sub divisions)
     * @param style       the graphical style of the boxes
     */
    BoxDrawing(int size, int subDivision, String style) {
        s = size;
        d = subDivision;
        setStyle(style);

        // if Division is greater than size:
        if (d >= s) {
            d = 0;
        }
    }

    /**
     * constructor for BoxDrawing without sub divisions
     *
     * @param size  horizontal and vertical size (is always the same)
     * @param style he graphical style of the boxes
     */
    BoxDrawing(int size, String style) {
        s = size;
        d = size;
        setStyle(style);
    }

    //-----------------------------------//
    // line drawing                      //
    //-----------------------------------//

    /**
     * top line
     *
     * @return the whole line as a string
     */
    String topLine() {
        StringBuilder result = new StringBuilder();
        result.append(cornerDoubleTopLeft);
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s; j++) {
                result.append(horizontalDouble);
            }
            // additional | in 4x4-sudoku after 2nd and 9x9-sudoku after 3rd and 6th:
            if ((s == 4 && i == 1) || (s == 9 && (i == 2 || i == 5))) {
                result.append(topDoubleDownDouble);
            } else if (i == s - 1) {
                result.append(cornerDoubleTopRight);
            } else {
                result.append(topDoubleDownSingle);
            }
        }
        return result.toString();
    }

    /**
     * regular body line with box drawing and content
     *
     * @param content to display inside the box
     * @return the whole line as a string
     */
    String regularBodyLine(String[] content) {
        StringBuilder result = new StringBuilder();
        result.append(verticalDouble);
        for (int i = 0; i < s; i++) {

            try {
                // content:
                // first element make a space if enough space to print the content anyway:
                if (content[i].length() < s) {
                    result.append(' ');
                }
                // append content itself (if it's larger, formatting will be bad, but anyhow:
                result.append(content[i]);

                // append extra spaces, if content is shorter:
                for (int j = content[i].length(); j < s - 1; j++) {
                    result.append(' ');
                }
            } catch (Exception e) {
                // if content[i] is not present, then append spaces in the size of s:
                for (int j = 0; j < s; j++) {
                    result.append(' ');
                }
            }

            // additional | in 4x4-sudoku after 2nd and 9x9-sudoku after 3rd and 6th:
            if ((s == 4 && i == 1) || (s == 9 && (i == 2 || i == 5)) || i == s - 1) {
                result.append(verticalDouble);
            } else {
                result.append(verticalSingle);
            }
        }
        return result.toString();
    }

    /**
     * middle line single (for normal box line)
     *
     * @return the whole line as a string
     */
    String middleLineSingle() {
        StringBuilder result = new StringBuilder();
        result.append(leftDoubleRightSingle);
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s; j++) {
                result.append(horizontalSingle);
            }
            // additional | in 4x4-sudoku after 2nd and 9x9-sudoku after 3rd and 6th:
            if ((s == 4 && i == 1) || (s == 9 && (i == 2 || i == 5))) {
                result.append(crossHorizontalSingleVerticalDouble);
            } else if (i == s - 1) {
                result.append(rightDoubleLeftSingle);
            } else {
                result.append(crossFullSingle);

            }
        }
        return result.toString();
    }

    /**
     * middle line double (for showing a division)
     *
     * @return the whole line as a string
     */
    String middleLineDouble() {
        StringBuilder result = new StringBuilder();
        result.append(leftDoubleRightDouble);
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s; j++) {
                result.append(horizontalDouble);
            }
            // additional | in 4x4-sudoku after 2nd and 9x9-sudoku after 3rd and 6th:
            if ((s == 4 && i == 1) || (s == 9 && (i == 2 || i == 5))) {
                result.append(crossFullDouble);
            } else if (i == s - 1) {
                result.append(rightDoubleLeftDouble);
            } else {
                result.append(crossHorizontalDoubleVerticalSingle);

            }
        }
        return result.toString();
    }

    /**
     * bottom line
     *
     * @return the whole line as a string
     */
    String bottomLine() {
        StringBuilder result = new StringBuilder();
        result.append(cornerDoubleBottomLeft);
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s; j++) {
                result.append(horizontalDouble);
            }
            // additional | in 4x4-sudoku after 2nd and 9x9-sudoku after 3rd and 6th:
            if ((s == 4 && i == 1) || (s == 9 && (i == 2 || i == 5))) {
                result.append(bottomDoubleUpDouble);
            } else if (i == s - 1) {
                result.append(cornerDoubleBottomRight);
            } else {
                result.append(bottomDoubleUpSingle);

            }
        }
        return result.toString();
    }

    /**
     * defines the style of the box drawing
     *
     * @param style as string: "normal and fat" or "normal and double" or "very thin and fat" or "very thin and double"
     */
    private void setStyle(String style) {
        switch (style) {
            case "normal and fat":
                // todo: define style "normal and fat"
                // break;                // uncomment, as soon as the style is defined
            case "normal and double":
                // todo: define style "normal and double"
                // break;                // uncomment, as soon as the style is defined
            case "very thin and fat":
                // todo: define style "very thin and fat"
                // break;                // uncomment, as soon as the style is defined
            case "very thin and double":
                //  break;   // no break, since this is equal to the default state:
            default:
                // standard is thin and double:
                // thin and double is only different for the purely vertical and purely horizontal normal lines:
                // special chars for top line:
                cornerDoubleTopLeft = '\u2554';
                cornerDoubleTopRight = '\u2557';
                topDoubleDownSingle = '\u2564';
                topDoubleDownDouble = '\u2566';

                // special chars for left vertical line:
                leftDoubleRightSingle = '\u255F';
                leftDoubleRightDouble = '\u2560';

                // special chars for right vertical line:
                rightDoubleLeftSingle = '\u2562';
                rightDoubleLeftDouble = '\u2563';

                // only thick lines:
                horizontalDouble = '\u2550';
                verticalDouble = '\u2551';

                // only thin lines:
                horizontalSingle = '\u2508';
                verticalSingle = '\u250A';

                // full crosses:
                crossFullDouble = '\u256C';
                crossFullSingle = '\u253C';
                crossHorizontalDoubleVerticalSingle = '\u256A';
                crossHorizontalSingleVerticalDouble = '\u256B';


                // special chars for bottom line:
                cornerDoubleBottomLeft = '\u255A';
                cornerDoubleBottomRight = '\u255D';
                bottomDoubleUpSingle = '\u2567';
                bottomDoubleUpDouble = '\u2569';
        }
    }

    /**
     * Prints the sudoku in the terminal with nice box drawing. <br>
     * <br>
     * This method assumes int[][][] of size int[s][s][s][s + 1],
     * but with one exception: there maybe a different amount of horizontal lines, and it still works
     * (i tweaked the code a bit).
     *
     * @param sudoku the sudoku to print
     * @param printPossibilities if true, the remaining possibilities are written with subscript
     */
    void printSudoku(int[][][] sudoku, boolean printPossibilities) {
        //--------------------------------------------------------------------------------
        // PRINTING-FUNCTIONS: WITH BOX-ART:
        // see also fantastic chart at: https://en.wikipedia.org/wiki/Box-drawing_character
        //--------------------------------------------------------------------------------

        //---------

        // top line:
        System.out.println(topLine());

        //-----
        // body:
        for (int i = 0; i < sudoku.length; i++) {
            // 'sudoku.length' returns the amount of horizontal lines -> nice tweak, so the
            // printOneSudokuLine()-method can work :-)

            // idea: print a line with content
            // then draw a line - every subdivision-line is a double line:

            // line with content: 1st: build the content array to pass to the regularBodyLine-function:
            String[] content = new String[s];
            for (int j = 0; j < s; j++) {
                // if a solution is present:
                if (sudoku[i][j][0] > 0) {
                    // use the "" +   for type-casting
                    content[j] = "" + sudoku[i][j][0];
                } else {

                    if (printPossibilities) {
                        // if a solution is not present, print possibilities in subscript:
                        // print " " for not present possibilities
                        StringBuilder temp = new StringBuilder();

                        for (int k = 1; k <= s; k++) {
                            if (sudoku[i][j][k] > 0) {
                                temp.append(returnSubscript(sudoku[i][j][k]));
                            } else {
                                temp.append(' ');
                            }
                        }
                        content[j] = temp.toString();

                    } else {
                        //don't print the possibilities
                        content[j] = " ";
                    }
                }
            }

            System.out.println(regularBodyLine(content));


            // thick line, or last line or normal line:
            if (i == sudoku.length - 1) {
                // 'sudoku.length' returns the amount of horizontal lines -> nice tweak, so the
                // printOneSudokuLine()-method can work :-)

                // last line:
                System.out.println(bottomLine());
            } else if ((s == 4 && i == 1) || (s == 9 && (i == 2 || i == 5))) {
                // thick line:
                System.out.println(middleLineDouble());
            } else {
                // normal line:
                System.out.println(middleLineSingle());
            }
        }
    }

    /**
     * prints one life of the sudoku in the terminal with nice box drawing
     *
     * @param oneSudokuLine the sudoku line to print
     */
    void printOneSudokuLine(int[][] oneSudokuLine) {
        // convert to 3 dimensional object and use the printSudoku()-method:
        // first dimension only holds one element:
        int[][][] oneLine = new int[1][s][s + 1];
        oneLine[0] = oneSudokuLine;
        printSudoku(oneLine, true);
    }


    /**
     * used by printSudoku(), to print small numbers (unicode subscript)
     *
     * @param number (int) number to print
     * @return (char) the unicode subscript of the given number
     */
    private char returnSubscript(int number) {
        // return superscript numbers:
        // with Unicode superscript symbols: https://en.wikipedia.org/wiki/Unicode_subscripts_and_superscripts
        // and https://stackoverflow.com/questions/5585919/creating-unicode-character-from-its-number
        switch (number) {
            case 1:
                return '\u2081';
            case 2:
                return '\u2082';
            case 3:
                return '\u2083';
            case 4:
                return '\u2084';
            case 5:
                return '\u2085';
            case 6:
                return '\u2086';
            case 7:
                return '\u2087';
            case 8:
                return '\u2088';
            case 9:
                return '\u2089';
            default:
                return '?';
        }
    }
}


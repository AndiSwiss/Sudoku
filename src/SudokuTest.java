import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/**
 * Tests for Sudoku <br>
 * <br>
 * Used help/tutorials: <br>
 * - https://blog.jetbrains.com/idea/2016/08/using-junit-5-in-intellij-idea/ <br>
 * - http://www.baeldung.com/junit-5-preview <br>
 * - http://www.baeldung.com/junit-5 <br>
 * - from IntelliJ IDEA - Youtube-Channel: "Unit Testing and Coverage in IntelliJ IDEA" https://www.youtube.com/watch?time_continue=392&v=QDFI19lj4OM
 */
class SudokuTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Nested
    @DisplayName("Easy sudokus:")
    class EasySudokus {
        @Test
        void template() {
            // from ...
            String[] sudokuStr = {
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    ""
            };

            String[] solutionStr = {
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    ""
            };

            Sudoku sudoku = new Sudoku(9, sudokuStr);
            Sudoku solution = new Sudoku(9, solutionStr);
            sudoku.solve();
            assertEquals(solution, sudoku);
        }

        @Test
        void easy1() {
            String[] sudokuStr = {"2043", "0020", "4300", "0034"};
            String[] solutionStr = {"2143", "3421", "4312", "1234"};

            Sudoku sudoku = new Sudoku(4, sudokuStr);
            Sudoku solution = new Sudoku(4, solutionStr);

            sudoku.solve();

            assertEquals(solution, sudoku);

        }

        /**
         * 60 easy sudokus (easy means 40 unknown cells)
         * from https://kjell.haxx.se/sudoku/
         * saved in /resources/https__kjell.haxx.se__sudoku/easy (40 known)/42341962-v3-40-L1.ss
         */
        @Test
        void sixtyEasy() {
            // todo: from the files in /resources/https__kjell.haxx.se__sudoku/easy (40 known)/42341962-v3-40-L1.ss

            // todo: run the test in assertAll -> see tutorial-example below "groupAssertions()"

            // todo: validate the input, if it has no duplicates
            // todo: generate solutions and validate, if they are possible

            String current = System.getProperty("user.dir");
            Path easySudokuPath = Paths.get(current, "resources/https__kjell.haxx.se__sudoku/easy (40 known)");
            System.out.printf("easySudokuPath: %s\n", easySudokuPath);
            List<Path> filePaths = new ArrayList<>();

            try (DirectoryStream<Path> files = Files.newDirectoryStream(easySudokuPath)) {
                for (Path found : files) {
                    if (Files.isRegularFile(found) && found.toString().toLowerCase().endsWith(".txt"))
                        filePaths.add(found);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            int counter = 0;

            for (Path file : filePaths) {
                List<String> oneFile = new ArrayList<>();
                try {
                    oneFile = Files.readAllLines(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // delete irrelevant lines, change characters and save it:
                List<String> sudokuStr = new ArrayList<>();
                oneFile.stream()
                        .filter(s -> s.charAt(0) != '-')
                        .map(s -> {
                            StringBuilder res = new StringBuilder();
                            for (int i = 0; i < s.length(); i++) {
                                if (s.charAt(i) == '.') res.append(' ');
                                else if (s.charAt(i) != '!') res.append(s.charAt(i));
                            }
                            return res.toString();
                        }).forEach(sudokuStr::add);


//                System.out.printf("file: %s\n", file);
//                System.out.printf("sudokuStr: %s\n", sudokuStr);
//                for (String s : sudokuStr) {
//                    System.out.println(s);
//                }

                Sudoku sudoku = new Sudoku(9, sudokuStr);

                sudoku.solve();
                sudoku.printFull();
                System.out.printf("sudoku.maxSolutions: %s\n", sudoku.maxSolutions);

                counter++;
                if (counter == 3) break;



            }
        }
    }

    @Nested
    @DisplayName("Hard sudokus:")
    class HardSudokus {
        @Test
        void hard1() {
            // from http://www.mathsphere.co.uk/downloads/sudoku/10203-hard.pdf
            // 1st example
            String[] sudokuStr = {
                    "000000002",
                    "000000940",
                    "003000005",
                    "092305074",
                    "840000000",
                    "067098000",
                    "000706000",
                    "000900020",
                    "408500360"
            };

            String[] solutionStr = {
                    "684159732",
                    "751832946",
                    "923674185",
                    "192365874",
                    "845217693",
                    "367498251",
                    "239746518",
                    "516983427",
                    "478521369"
            };

            Sudoku sudoku = new Sudoku(9, sudokuStr);
            Sudoku solution = new Sudoku(9, solutionStr);
            sudoku.solve();
            assertEquals(solution, sudoku);
        }
    }

    @Nested
    @DisplayName("Very hard sudokus:")
    class veryHardSudokus {
        @Test
        void veryHard1() {
            // From http://www.7sudoku.com/view-puzzle?date=20180427
            // For more, see http://www.7sudoku.com/very-difficult
            // This is not solvable with a simple algorithm.
            String[] sudokuStr = {
                    "002000040",
                    "000030000",
                    "705090000",
                    "000907000",
                    "000002008",
                    "009005603",
                    "400500310",
                    "090000000",
                    "080040206"
            };

            String[] solutionStr = {
                    "632851947",
                    "918734562",
                    "745296831",
                    "326987154",
                    "154362798",
                    "879415623",
                    "456528319",
                    "291673485",
                    "583149276"
            };

            Sudoku sudoku = new Sudoku(9, sudokuStr);
            Sudoku solution = new Sudoku(9, solutionStr);
            sudoku.solve();

            assertEquals(solution, sudoku);

        }
    }


    /**
     * from Tutorial http://www.baeldung.com/junit-5-preview
     */
    @Nested
    @DisplayName("zz Tutorial-Tests:")
    @SuppressWarnings("ConstantConditions")
    class Tutorial {
        //-----------------------------------------------------------------------//
        // Assertions:                                                           //
        //                                                                       //
        // Don't forget to import static org.junit.jupiter.api.Assertions.*;     //
        //-----------------------------------------------------------------------//


        @Test
        void lambdaExpression() {
            assertTrue(Stream.of(1, 2, 3).mapToInt(i -> i).sum() > 5, "sum should be greater than 5");
        }

        // With this, you can easily test for different things inside condition and it just will show the failed ones. <br>
        @Test
        void groupAssertions() {
            int[] numbers = {0, 1, 2, 3, 4};
            assertAll("numbers",
                    () -> assertEquals(1, numbers[0]),
                    () -> assertEquals(3, numbers[3]),
                    () -> assertEquals(1, numbers[4])
            );
        }

        //-----------------------------------------------------------------------//
        // Assumptions:                                                          //
        //                                                                       //
        // don't forget to import static org.junit.jupiter.api.Assumptions.*;    //
        //-----------------------------------------------------------------------//

        // The test runs only, if the assumeTrue(...) inside is true
        // else: the test will show "passed"
        @Test
        void trueAssumption() {
            assumeTrue(5 > 1);
            assertEquals(5 + 2, 7);
        }

        // The test runs only, if the assumeFalse(...) inside is false
        // else: the test will show "passed"
        @Test
        void falseAssumption() {
            assumeFalse(5 < 1);
            assertEquals(5 + 2, 7);
        }

        // This test runs only, if the first condition in assumingThat(...) is true
        // else: the test will show "passed"
        @Test
        void assumptionThat() {
            String someString = "Just a string";
            assumingThat(
                    someString.equals("Just a string"),
                    () -> assertEquals(2 + 2, 4)
            );
        }

        //-----------------------------------------------------------------------//
        // Exceptions:                                                           //
        //                                                                       //
        // don't forget to import static org.junit.jupiter.api.Assertions.*;     //
        //-----------------------------------------------------------------------//

        // -> Tutorial http://www.baeldung.com/junit-5
        // used to verify more detail of the thrown exception
        @Test
        void shouldThrowException() {
            Throwable exception = assertThrows(UnsupportedOperationException.class, () -> {
                throw new UnsupportedOperationException("Not supported");
            });
            assertEquals(exception.getMessage(), "Not supported");
        }

        // -> Tutorial http://www.baeldung.com/junit-5
        // this test just validates the type of exception
        @Test
        void assertThrowException() {
            String str = null;
            //noinspection ResultOfMethodCallIgnored
            assertThrows(IllegalArgumentException.class, () -> Integer.valueOf(str));
        }
    }
}

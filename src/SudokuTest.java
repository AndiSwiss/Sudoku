import org.junit.jupiter.api.*;

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
        @DisplayName("an easy sudoku")
        void easy1() {
            String[] sudokuStr = {"2043", "0020", "4300", "0034"};

            Sudoku sudoku = new Sudoku(4, sudokuStr);
            sudoku.solve();

            sudoku.printEasy();

            System.out.println("\nsudoku.printIndexesOfCells() -> ");
            sudoku.printIndexesOfCells();

        }
    }


    /**
     * from Tutorial http://www.baeldung.com/junit-5-preview
     */
    @Nested
    @DisplayName("Tutorial-Tests:")
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

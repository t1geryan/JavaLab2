

import org.example.ExpressionEvaluator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExpressionEvaluatorTest {

    @Test
    void evaluateSimpleExpressionReturnsCorrectResult() {
        String expression = "2 + 3 * 4";
        double expected = 14;
        double result = new ExpressionEvaluator(expression).evaluate();
        assertEquals(expected, result);
    }

    @Test
    void evaluateComplexExpressionReturnsCorrectResult() {
        String expression = "((3 + 4) * 5 - (2 + 1)) / (3 * 2)";
        double expected = 5.333333333333333;
        double result = new ExpressionEvaluator(expression).evaluate();
        assertEquals(expected, result);
    }

    @Test
    void evaluateExpressionWithDivisionByZeroThrowsArithmeticException() {
        String expression = "5 / 0";
        assertThrows(ArithmeticException.class, () -> new ExpressionEvaluator(expression).evaluate());
    }

    @Test
    void evaluateExpressionWithInvalidCharactersThrowsIllegalArgumentException() {
        String expression = "2 + a";
        assertThrows(IllegalArgumentException.class, () -> new ExpressionEvaluator(expression).evaluate());
    }

    @Test
    void evaluateExpressionWithInvalidSyntaxThrowsIllegalArgumentException() {
        String expression = "2 + ) * 3";
        assertThrows(IllegalArgumentException.class, () -> new ExpressionEvaluator(expression).evaluate());
    }
}

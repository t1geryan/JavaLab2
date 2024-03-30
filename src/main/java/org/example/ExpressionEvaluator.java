package org.example;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

/**
 * Class for calculating the value of a mathematical expression containing
 * round brackets, operators '+', '-', '/', '*' and numbers
 * <p>
 * Example: ExpressionEvaluator("((3 + 4) * 5 - (2 + 1)) / (3 * 2)").evaluate();
 */
public class ExpressionEvaluator {
    String expression;

    public ExpressionEvaluator(String expression) {
        this.expression = expression;
    }

    /**
     * Calculates the value of a mathematical expression.
     * Expression can contain round brackets, base operators '+', '-', '/', '*' and numbers.
     * Expression can contain any count of spaces.
     *
     * @return value of the mathematical expression
     * @throws IllegalArgumentException if illegal expression or unknown symbols are passed.
     * @throws ArithmeticException      when there is division by zero
     */
    public double evaluate() throws IllegalArgumentException, ArithmeticException {
        expression = expression.replaceAll("\\s", "");

        // list of tokens on postfix notation
        List<String> tokens = infixToPostfix(expression);

        Stack<Double> operands = new Stack<>();

        for (String token : tokens) {
            if (isNumeric(token)) {
                operands.push(Double.parseDouble(token));
            } else {
                double operand2 = operands.pop();
                double operand1 = operands.pop();
                operands.push(performOperation(token.charAt(0), operand1, operand2));
            }
        }

        return operands.pop();
    }

    private List<String> infixToPostfix(String infixExpression) {
        List<String> postfixList = new ArrayList<>();
        Stack<Character> stack = new Stack<>();
        boolean expectingOperand = true;

        try {
            for (int i = 0; i < infixExpression.length(); i++) {
                char ch = infixExpression.charAt(i);
                if (Character.isDigit(ch) || (ch == '-' && expectingOperand && (i == 0 || !Character.isDigit(infixExpression.charAt(i - 1))))) {
                    StringBuilder operand = new StringBuilder();
                    operand.append(ch);
                    while (i + 1 < infixExpression.length() && (Character.isDigit(infixExpression.charAt(i + 1)) || infixExpression.charAt(i + 1) == '.')) {
                        operand.append(infixExpression.charAt(++i));
                    }
                    postfixList.add(operand.toString());
                    expectingOperand = false;
                } else if (ch == '(') {
                    stack.push(ch);
                    expectingOperand = true;
                } else if (ch == ')') {
                    while (!stack.isEmpty() && stack.peek() != '(') {
                        postfixList.add(String.valueOf(stack.pop()));
                    }
                    stack.pop(); // Discard '('
                    expectingOperand = false;
                } else if (isOperator(ch)) {
                    while (!stack.isEmpty() && precedence(ch) <= precedence(stack.peek())) {
                        postfixList.add(String.valueOf(stack.pop()));
                    }
                    stack.push(ch);
                    expectingOperand = true;
                } else {
                    throw new IllegalArgumentException("Unknown symbol in expression");
                }
            }

            while (!stack.isEmpty()) {
                postfixList.add(String.valueOf(stack.pop()));
            }
        } catch (EmptyStackException e) {
            throw new IllegalArgumentException("Illegal expression");
        }

        return postfixList;
    }

    private int precedence(char operator) {
        return switch (operator) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            default -> -1;
        };
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isOperator(Character operator) {
        return operator == '+' || operator == '-' || operator == '*' || operator == '/';
    }

    private double performOperation(char operator, double a, double b) {
        return switch (operator) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> {
                if (b == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                yield a / b;
            }
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }
}

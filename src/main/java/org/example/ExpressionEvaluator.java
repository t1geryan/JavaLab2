package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ExpressionEvaluator {

    public static double evaluate(String expression) {
        expression = expression.replaceAll("\\s", "");

        List<String> tokens = infixToReversePolish(expression);

    }

    private static List<String> infixToReversePolish(String expression) {
        List<String> result = new ArrayList<>();
        Stack<Character> operators = new Stack<>();

        StringBuilder operand = new StringBuilder();
        for (char c : expression.toCharArray()) {
            if (Character.isDigit(c) || c == '.') {
                operand.append(c);
            } else {
                if (!operand.isEmpty()) {
                    result.add(operand.toString());
                    operand.setLength(0);
                }
                if (c == '(') {
                    operators.push(c);
                } else if (c == ')') {
                    while (!operators.isEmpty() && operators.peek() != '(') {
                        result.add(operators.pop().toString());
                    }
                    if (!operators.isEmpty() && operators.peek() == '(') {
                        operators.pop();
                    } else {
                        throw new IllegalArgumentException("Invalid expression");
                    }
                } else if (isOperator(c)) {
                    while (!operators.isEmpty() && hasHigherPrecedence(c, operators.peek())) {
                        result.add(operators.pop().toString());
                    }
                    operators.push(c);
                } else {
                    throw new IllegalArgumentException("Invalid character: " + c);
                }
            }
        }

        if (!operand.isEmpty()) {
            result.add(operand.toString());
        }

        while (!operators.isEmpty()) {
            if (operators.peek() == '(' || operators.peek() == ')') {
                throw new IllegalArgumentException("Invalid expression");
            }
            result.add(operators.pop().toString());
        }

        return result;
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private static boolean hasHigherPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        return (op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-');
    }
}

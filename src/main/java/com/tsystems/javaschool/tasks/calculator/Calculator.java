package com.tsystems.javaschool.tasks.calculator;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

public class Calculator {

    public static void main(String[] args) {
        System.out.println(new Calculator().evaluate("((1+2)-(6+7))*(7+8)"));
    }


    public Double operation(String operation, Double arg1, String arg2s) {
        Double arg2;
        arg2 = new Double(arg2s);
        switch (operation) {
            case "+":
                return arg1 + arg2;
            case "-":
                return arg1 - arg2;
            case "*":
                return arg1 * arg2;
            case "/": {
                double res;
                if (arg2 == 0) throw new RuntimeException();
                res = arg1 / arg2;
                return res;
            }
        }
        return null;
    }

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */
    public String evaluate(String statement) {
        try {
            if (statement.charAt(0) != '-') statement = "+" + statement;
            if (statement.charAt(0) == '*' | statement.charAt(0) == '/') return null;
        } catch (NullPointerException |
                StringIndexOutOfBoundsException e) {
            return null;
        }
        statement += "+";
        Stack<CalculationResult> calculationResultStack = new Stack<>();
        Deque<String> charsOfStatementQueue = new ArrayDeque<>();
        double result = 0.0;
        for (int i = 0; i < statement.length(); i = i + 1) {
            char arg = statement.charAt(i);
            switch (arg) {
                case '+':
                case '-': {
                    charsOfStatementQueue.add(Character.toString(arg));
                    if (charsOfStatementQueue.size() == 2) return null;
                    if (charsOfStatementQueue.size() == 1) break;
                    switch (charsOfStatementQueue.getFirst()) {
                        case "-":
                        case "*":
                        case "/":
                        case "+": {
                            String operation = charsOfStatementQueue.pop();
                            String argument = "";
                            int sizeQueue = charsOfStatementQueue.size();
                            for (int k = 0; k < sizeQueue - 1; k++) {
                                argument += charsOfStatementQueue.pop();
                            }
                            try {
                                result = operation(operation, result, argument);
                            } catch (Exception e) {
                                return null;
                            }
                            if (!calculationResultStack.empty()) {
                                CalculationResult calculationResult = calculationResultStack.pop();
                                try {
                                    result = operation(calculationResult.getSign(),
                                            calculationResult.getResult(),
                                            Double.toString(result));
                                } catch (Exception e) {
                                    return null;
                                }
                            }
                            break;
                        }
                    }
                    break;
                }
                case '*':
                case '/': {
                    charsOfStatementQueue.add(Character.toString(arg));
                    if (charsOfStatementQueue.size() == 2) return null;
                    switch (charsOfStatementQueue.getFirst()) {
                        case "+":
                        case "-": {
                            calculationResultStack.push(new CalculationResult(result, charsOfStatementQueue.pop()));
                            String argument = "";
                            for (int k = 0; k < charsOfStatementQueue.size(); k++) {
                                argument += charsOfStatementQueue.pop();
                            }
                            try {
                                result = operation("*", 1.0, argument);
                            } catch (Exception e) {
                                return null;
                            }
                            break;
                        }
                        case "*":
                        case "/": {
                            String argument = "";
                            String operation = charsOfStatementQueue.pop();
                            for (int k = 0; k < charsOfStatementQueue.size(); k++) {
                                argument += charsOfStatementQueue.pop();
                            }
                            try {
                                result = operation(operation, result, argument);
                            } catch (Exception e) {
                                return null;
                            }
                            break;
                        }
                        default:
                            break;
                    }
                    break;
                }
                case '(': {
                    int parentheses = 0;
                    for (int k = i + 1; k < statement.length(); k++) {
                        if (statement.charAt(k) == ')') parentheses++;
                        if (statement.charAt(k) == '(') parentheses--;
                        if (parentheses > 0) {
                            String str = evaluate(statement.substring(i + 1, k));
                            String strBegin = statement.substring(0, i);
                            String strEnd = statement.substring(k + 1);
                            try {
                                if (str.charAt(0) == '-' & (strBegin.charAt(i - 1) == '*' | strBegin.charAt(i - 1) == '/')) {
                                    result = result * (-1);
                                    str = str.substring(1);
                                }
                                if (str.charAt(0) == '-' & strBegin.charAt(i - 1) == '+') {
                                    strBegin = strBegin.substring(0, i - 1) + '-';
                                    charsOfStatementQueue.pop();
                                    charsOfStatementQueue.push("-");
                                    str = str.substring(1);
                                }
                                if (str.charAt(0) == '-' & strBegin.charAt(i - 1) == '-') {
                                    strBegin = strBegin.substring(0, i - 1) + '+';
                                    charsOfStatementQueue.pop();
                                    charsOfStatementQueue.push("+");
                                    str = str.substring(1);
                                }
                            } catch (NullPointerException e) {
                                return null;
                            }
                            statement = strBegin + str + strEnd;
                            i--;
                            break;
                        }
                    }
                    if (parentheses <= 0) return null;
                    break;
                }
                case ')':
                case ',':
                    return null;
                case ' ':
                    break;
                default:
                    charsOfStatementQueue.add(Character.toString(arg));
                    break;
            }
        }
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#.####", decimalFormatSymbols);
        return decimalFormat.format(result);
    }

}

class CalculationResult {
    private Double Result;
    private String sign;

    public CalculationResult(Double res, String sign) {
        this.Result = res;
        this.sign = sign;
    }

    public Double getResult() {
        return Result;
    }

    public String getSign() {
        return sign;
    }
}




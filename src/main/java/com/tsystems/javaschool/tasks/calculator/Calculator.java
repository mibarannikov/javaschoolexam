package com.tsystems.javaschool.tasks.calculator;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

public class Calculator {

    public static void main(String[] args) {
        System.out.println(new Calculator().evaluate("5*(1+1)*((5+5)*(5+5)(-1))"));
    }

    public Double operation(String operation, Double arg1, String arg2s) throws Exception {
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
                if (arg2 == 0) throw new Exception();
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
                                    result = operation(calculationResult.getSign(), calculationResult.getResult(), Double.toString(result));
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
                                result = operation("*", 1.0, argument);// может быть просто присвоение
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
                    int closeParentheses = 0;
                    int openParentheses = 0;
                    for (int k = i + 1; k < statement.length(); k++) {
                        char localArg = statement.charAt(k);
                        if (localArg == ')') closeParentheses++;
                        if (localArg == '(') openParentheses++;
                        if (closeParentheses > openParentheses) {
                            char[] dst = new char[k - i - 1];
                            statement.getChars(i + 1, k, dst, 0);
                            String statementR = new String(dst);
                            char[] begin = new char[i];
                            char[] end = new char[statement.length() - k - 1];
                            statement.getChars(0, i, begin, 0);
                            statement.getChars(k + 1, statement.length(), end, 0);
                            String str = evaluate(statementR);
                            if (str.charAt(0) == '-' & (begin[i - 1] == '*' | begin[i - 1] == '/')) {
                                result = result * (-1);
                                str = str.substring(1);
                            }
                            if (str.charAt(0) == '-' & begin[i - 1] == '+') {
                                begin[i - 1] = '-';
                                charsOfStatementQueue.pop();
                                charsOfStatementQueue.push("-");
                                str = str.substring(1);
                            }
                            if (str.charAt(0) == '-' & begin[i - 1] == '-') {
                                begin[i - 1] = '+';
                                charsOfStatementQueue.pop();
                                charsOfStatementQueue.push("+");
                                str = str.substring(1);
                            }
                            statement = new String(begin) + str + new String(end);
                            i--;
                            break;
                        }

                    }
                    if (closeParentheses <= openParentheses) return null;
                    break;
                }
                case ')': case',':
                    return null;
                case ' ':
                    break;
                //case 'g': return null;
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

    class CalculationResult {
        private final Double Result;
        private final String sign;

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
}






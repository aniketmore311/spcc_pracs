import java.io.*;
import java.util.*;

public class Code {
    public static void main(String[] args) throws IOException, FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("input.txt"));
        String input = br.readLine();
        String dest = input.split("=")[0].trim();
        String expression = input.split("=")[1].trim();
        System.out.println(input);
        String[] tokens = expression.split(" ");
        Stack<String> operands = new Stack<String>();
        Stack<String> operators = new Stack<String>();
        // HashMap<String, Integer> varValues = new HashMap<String, Integer>();
        int currTemp = 1;
        for (String token : tokens) {
            if (isOperator(token)) {
                while (!operators.isEmpty() && getPriority(operators.peek()) >= getPriority(token)) {
                    String operator = operators.pop();
                    String op2 = operands.pop();
                    String op1 = operands.pop();
                    // System.out.println(op1 + " " + operator + " " + op2);
                    String result = applyOperator(operator, op1, op2);
                    int tempVal = currTemp;
                    currTemp++;
                    String tempVarName = "t" + String.valueOf(tempVal);
                    // varValues.put(tempVarName,result);
                    System.out.println(tempVarName + "=" + result);
                    operands.push(tempVarName);
                }
                operators.add(token);
            } else {
                operands.push(token);
            }
        }
        while (!operators.isEmpty()) {
            String operator = operators.pop();
            String op2 = operands.pop();
            String op1 = operands.pop();
            // System.out.println(op1 + " " + operator + " " + op2);
            String result = applyOperator(operator, op1, op2);
            int tempVal = currTemp;
            currTemp++;
            String tempVarName = "t" + String.valueOf(tempVal);
            // varValues.put(tempVarName,result);
            System.out.println(tempVarName + "=" + result);
            operands.push(tempVarName);
        }
        String finalOp = operands.pop();
        System.out.println(dest + "=" + finalOp);
    }

    public static boolean isOperator(String op) {
        ArrayList<String> operators = new ArrayList<String>();
        operators.add("+");
        operators.add("-");
        operators.add("*");
        operators.add("/");
        if (operators.contains(op)) {
            return true;
        } else {
            return false;
        }
    }

    public static int getPriority(String op) {
        if (op.equals("+") || op.equals("-")) {
            return 1;
        } else if (op.equals("*") || op.equals("/")) {
            return 2;
        } else {
            throw new Error("invalid operator");
        }
    }

    public static String applyOperator(String operator, String op1, String op2) {
        switch (operator) {
            case "+":
                return op1 + "+" + op2;
            case "-":
                return op1 + "-" + op2;
            case "*":
                return op1 + "*" + op2;
            case "/":
                return op1 + "/" + op2;
            default:
                throw new Error("invalid operator");
        }
    }
}
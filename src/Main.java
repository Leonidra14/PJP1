import java.util.Scanner;
import java.util.Stack;

//rozdeleni a vymazani mezer
//rozdeleni na cisla a na znaky
//vyreseni zavorek
//pozor na poradi * a +

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int N = scanner.nextInt();
        scanner.nextLine(); // Přeskočení prázdného řádku

        for (int i = 0; i < N; i++) {
            String expression = scanner.nextLine();
            try {
                int result = evaluateExpression(expression);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println("ERROR");
            }
        }
    }

    public static int evaluateExpression(String expression) {
        char[] tokens = expression.toCharArray();

        //stack na cisla a na znaky
        Stack<Integer> values = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < tokens.length; i++) {
            //pokud mam mezeru jedu dal
            if (tokens[i] == ' ') {
                continue;
            }

            //vyber cisel
            if (Character.isDigit(tokens[i])) {
                StringBuilder sb = new StringBuilder();
                while (i < tokens.length && Character.isDigit(tokens[i])) {
                    sb.append(tokens[i]);
                    i++;
                }
                i--;

                //převod na cislo
                values.push(Integer.parseInt(sb.toString()));

            } else if (tokens[i] == '(') {
                operators.push(tokens[i]);
            } else if (tokens[i] == ')') {
                while (operators.peek() != '(') {
                    values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
                }
                operators.pop(); // Odstranění otevírací závorky ze zásobníku
            } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {
                while (!operators.isEmpty() && hasPrecedence(tokens[i], operators.peek())) {
                    values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
                }
                operators.push(tokens[i]);
            }
        }

        while (!operators.isEmpty()) {
            values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    private static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return false;
        }
        return true;
    }



    private static int applyOperator(char operator, int b, int a) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return a / b;
        }
        return 0;
    }
}
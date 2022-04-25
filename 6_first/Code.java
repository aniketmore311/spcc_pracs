import java.util.*;
import java.io.*;

/**
 * Code
 */
public class Code {
    public static Map<String, List<String>> productions;

    public static void main(String[] args) throws IOException {
        List<String> input = readLines(new BufferedReader(new InputStreamReader(System.in)));
        Map<String, List<String>> productions = parseInput(input);
        Solver sol = new Solver(productions);
        sol.findFirst();
        sol.printFirst();
    }

    public static Map<String, List<String>> parseInput(List<String> input) {
        Map<String, List<String>> productions = new HashMap<>();
        for (String line : input) {
            String left = line.split("->")[0].trim();
            String rulesStr = line.split("->")[1];
            String[] rulesArr = rulesStr.split("\\|");
            for (String rule : rulesArr) {
                if (!productions.containsKey(left)) {
                    productions.put(left, new ArrayList<String>());
                }
                productions.get(left).add(rule.trim());
            }
        }
        return productions;
    }

    public static List<String> readLines(BufferedReader br) throws IOException {
        List<String> input = new ArrayList<String>();
        String line = br.readLine();
        while (line != null) {
            input.add(line.trim());
            line = br.readLine();
        }
        return input;
    }

}

class Solver {
    public Map<String, List<String>> productions;
    public Map<String, List<String>> firsts = new HashMap<>();

    Solver(Map<String, List<String>> productions) {
        this.productions = productions;
    }

    public void findFirst() {
        for (String left : productions.keySet()) {
            List<String> ans = first(left);
            firsts.put(left, ans);
        }
    }

    public void printFirst() {
        for (String left : firsts.keySet()) {
            List<String> ans = firsts.get(left);
            System.out.println(left + " = " + ans);
        }
    }

    public List<String> first(String left) {
        List<String> ans = new ArrayList<>();
        List<String> rules = productions.get(left);
        for (String rule : rules) {
            for (char ch : rule.toCharArray()) {
                if (isTerminal(ch)) {
                    ans.add(String.valueOf(ch));
                    break;
                } else {
                    List<String> first = first(String.valueOf(ch));
                    for (String item : first) {
                        if (!item.equals("#")) {
                            ans.add(item);
                        }
                    }
                    if (!first.contains("#")) {
                        break;
                    }
                }
            }
        }
        return ans;
    }

    public boolean isTerminal(char ch) {
        if (ch <= 'Z' && ch >= 'A') {
            return false;
        } else {
            return true;
        }
    }

}

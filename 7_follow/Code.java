
/**
 * note: when input is changed make sure to change starting character in main method
 */
import java.util.*;
import java.io.*;

/**
 * Code
 */
public class Code {

    public static void main(String[] args) throws IOException {
        List<String> productionsInput = readLines(new BufferedReader(new FileReader("production.txt")));
        List<String> firstInput = readLines(new BufferedReader(new FileReader("first.txt")));
        Map<String, List<String>> productions = parseProductions(productionsInput);
        Map<String, List<String>> first = parseFirst(firstInput);
        System.out.println(productions);
        System.out.println(first);
        Solver sol = new Solver(productions, first, 'E');
        sol.findFollow();
        sol.printFollow();
    }

    public static Map<String, List<String>> parseProductions(List<String> input) {
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

    public static Map<String, List<String>> parseFirst(List<String> input) {
        Map<String, List<String>> firstMap = new HashMap<>();
        for (String line : input) {
            String left = line.split("=")[0].trim();
            String[] firstArr = line.split("=")[1].trim().split(",");
            List<String> firstList = new ArrayList<>();
            for (String item : firstArr) {
                firstList.add(item);
            }
            firstMap.put(left, firstList);
        }
        return firstMap;
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
    public Map<String, List<String>> first;
    public Map<String, List<String>> follow = new HashMap<>();
    public char startCharacter;

    Solver(Map<String, List<String>> productions, Map<String, List<String>> first, char startCharacter) {
        this.first = first;
        this.productions = productions;
        this.startCharacter = startCharacter;
    }

    public void findFollow() {
        for (String left : productions.keySet()) {
            List<String> followList = follow(left);
            follow.put(left, followList);
        }
    }

    public List<String> follow(String inputLeft) {
        List<String> followList = new ArrayList<>();
        if (String.valueOf(startCharacter).equals(inputLeft)) {
            followList.add("$");
        }
        for (String left : productions.keySet()) {
            List<String> rules = productions.get(left);
            for (String rule : rules) {
                if (rule.contains(inputLeft)) {
                    int index = rule.indexOf(inputLeft);
                    int i = 0;
                    for (i = index + 1; i < rule.length(); i++) {
                        List<String> first = getFirst(rule.charAt(i));
                        for (String item : first) {
                            if (!item.equals("#") && !followList.contains(item)) {
                                followList.add(item);
                            }
                        }
                        if (first.contains("#")) {
                            continue;
                        } else {
                            break;
                        }
                    }
                    if (i == rule.length()) {
                        String lhs = left;
                        if (!lhs.equals(inputLeft)) {
                            List<String> followAns = follow(lhs);
                            for (String item : followAns) {
                                if (!followList.contains(item)) {
                                    followList.add(item);
                                }
                            }
                        }
                    }
                }
            }
        }
        return followList;
    }

    public void printFollow() {
        for (String left : follow.keySet()) {
            System.out.println(left + " = " + follow.get(left));
        }
    }

    public List<String> getFirst(char ch) {
        if (isTerminal(ch)) {
            return first.get(String.valueOf(ch));
        } else {
            List<String> ans = new ArrayList<String>();
            ans.add(String.valueOf(ch));
            return ans;
        }
    }

    public String findLHS(String inputRule) {
        for (String left : productions.keySet()) {
            for (String rule : productions.get(left)) {
                if (rule.equals(inputRule)) {
                    return left;
                }
            }
        }
        return "";
    }

    public boolean isTerminal(char ch) {
        if (ch <= 'Z' && ch >= 'A') {
            return true;
        } else {
            return false;
        }
    }

}
import java.io.*;
import java.util.*;

public class Code{
	public static void main(String[] args) throws IOException {
		Analyser as = new Analyser();
		 List<String> input = readFile(new BufferedReader(new InputStreamReader(System.in)));
		 for(String line: input){
		 	as.analyse(line);
		 }
	}
	
	public static List<String> readFile(BufferedReader br) throws IOException {
		List<String> input = new ArrayList<>();
		String line = br.readLine();
		while(line != null){
			input.add(line);
			line = br.readLine();
		}
		return input;
	}
	
}

class Analyser{
	public HashSet<String> specSet = new HashSet<String>(Arrays.asList(";",":","\"","'","@","#","$","^","&","(",")","{","}","[","]","<",">",",",".","?","/"));
	public HashSet<String> keywordSet = new HashSet<String>(Arrays.asList("int","main","void","include","printf"));
	public HashSet<String> operatorSet = new HashSet<String>(Arrays.asList("+","-","/","*","="));
	
	Analyser(){
	
	}
	public void analyse(String line){
		List<String> tokens = tokenize(line);
		for(String token: tokens){
			token = token.trim();
			String category = getCategory(token);
			System.out.println(token + " : " + category);
		}
	}
	public List<String> tokenize(String line){
		List<String> tokens = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		for(char ch: line.toCharArray()){
			if(isBreaking(String.valueOf(ch))){
				if(sb.length() > 0){
					tokens.add(sb.toString());
				}
				if(specSet.contains(String.valueOf(ch)) || operatorSet.contains(String.valueOf(ch))){
					tokens.add(String.valueOf(ch));
				}
				sb = new StringBuilder();
			}else{
				sb.append(ch);
			}
		}
		return tokens;
	}
	public boolean isBreaking(String token){
		if(this.specSet.contains(token) || operatorSet.contains(token) || token.equals(" ")){
			return true;
		}else{
			return false;
		}
	}
	public String getCategory(String token){
		if(specSet.contains(token)){
			return "special character";
		}else if(operatorSet.contains(token)){
			return "operator";
		}else if(keywordSet.contains(token)){
			return "keyword";
		} else if(isNumber(token)){
			return "number";
		}
		else {
			return "identifier";
		}
	}
	public boolean isNumber(String token){
		for(char ch: token.toCharArray()){
			if(ch <= '9' && ch >= '0'){
				continue;
			}else{
				return false;
			}
		}
		return true;
	}
}

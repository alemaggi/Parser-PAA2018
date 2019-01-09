import java.io.IOException;
import java.util.Stack;

public class SyntaxControl {
	
	public void SyntaxCheck(String[] V) throws IOException {
	
		int pos = 0;
		
		TokenCodes tk = new TokenCodes();
		
		if (!V[0].equals("SET") && !V[0].equals("GET")) {
			System.out.println("Syntax error!\nTutte le istruzioni devono cominciare con SET o GET");
			System.exit(1);
		}
		
		//skippo gli spazi e le parentesi
		if (V[pos] != "(" || V[pos] != ")" || V[pos] != " ")
		{
			if (V[pos] == "SET") {
				if (tk.identifyToken(V[pos + 1]) != 0) {
					System.out.println("Errore di sintassi alla posizione: " + pos + 1 + "\n Nome della variabile richiesto");
				}
				//if (((V[pos + 1].charAt(0) - '0') >= 0) && ((V[pos + 1].charAt(0) - '0') <= 9)) {
				
				else if (Character.isDigit(V[pos + 1].charAt(0))) {
					System.out.println("Errore, i nomi delle variabili non possono cominciare con un numero");
				}
			}
		}
	}
	
	public boolean ParMatching(String str) {
	    Stack<Character> stack = new Stack<Character>();
	    char c;
	    for(int i=0; i < str.length(); i++) {
	        c = str.charAt(i);
	        if(c == '(')
	            stack.push(c);
	        else if(c == ')')
	            if(stack.empty())
	                return false;
	            else if(stack.peek() == '(')
	                stack.pop();
	            else
	            	return false;
	    }
	    return  stack.empty();
	    
	}
	
}

import java.util.ArrayList;
import java.util.Stack;

public class TreeVisitor {
	
	private ExprTree T;
	public ArrayList<Integer> Ops = new ArrayList<Integer>();
	private int position = 0;
	
	TreeVisitor(ExprTree T) {
		this.T = T;
		this.T.setCurrentNode(T.getRoot());
	}

	public void postorderIter() {
		if (T.getCurrentNode() == null)
			return;
		Stack<TokenNode> s = new Stack<TokenNode>();
		TokenNode current = T.getCurrentNode();
		
		while(true) {
			 
			if(current != null ) {
				if(current.getRC() != null) 
					s.push(current.getRC());
				s.push(current);
				current = current.getLC();
				continue;
			}
 
			if(s.isEmpty()) 
				return;
			current = s.pop();
 
			if( current.getRC() != null && ! s.isEmpty() && current.getRC() == s.peek()) {
				s.pop();
				s.push(current);
				current = current.getRC();
			} else {
				createOps(current.getValue());
				current = null;
				position++;
				for (Integer x : Ops)
					System.out.print(x+"--");
			}
		}
	}
	
	private void createOps(String val) {
		if (isANum(val)) {
			Ops.add(position, Integer.parseInt(val));
		}
		else if (val.equals("ADD")) {
			Ops.add(position, Ops.get(position - 2) + Ops.get(position - 1));
			Ops.remove(position - 1);
			Ops.remove(position - 2);
			position -= 2;
		}
		else if (val.equals("MUL")) {
			Ops.add(position, Ops.get(position - 2) * Ops.get(position - 1));
			Ops.remove(position - 1);
			Ops.remove(position - 2);
			position -= 2;
		}
		else if (val.equals("SUB")) {
			Ops.add(position, Ops.get(position - 2) - Ops.get(position - 1));
			Ops.remove(position - 1);
			Ops.remove(position - 2);
			position -= 2;
		}
		else if (val.equals("DIV")) {
			if (Ops.get(position - 1) != 0) {
				Ops.add(position, Ops.get(position - 2) / Ops.get(position - 1));
				Ops.remove(position - 1);
				Ops.remove(position - 2);
				position -= 2;
			}
			else {
				System.out.println("Impossibile eseguire una divisione per zero");
				System.exit(1);
			}
		}
		else if (val == "GET") {
			System.out.println("Risultato: " + Ops.get(position - 2));
			position = 0;
		}
		/*else if (val == "SET") {
			//aggiungere alla chiave il valore (pos - 1) nella hash map
		}*/
		/*else if (isAVar(val)) {
			//aggiungi alla hashmap
			System.out.println("OK");
		}*/
	}
	
	private boolean isAVar(String s) {
		return s.matches("^[\\$_a-zA-Z]+[\\$_\\w]*$");
	}
	
	private boolean isANum(String s) {
		return s.matches("^[0-9]*$");
	}
}

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

/**
 * Classe per visitare l' albero sintattico creato usato come metodo postorder e poter eseguire le operazioni che compongono il le espressioni andando
 * poi a stampare il risultato
 */

public class TreeVisitor {
	
	private ExprTree T;

	public ArrayList<Integer> Ops = new ArrayList<Integer>();
	private int position = 0;
    private Hashtable<String, Integer> VarTab;
	String tmpVar = "";
	String tmpVar2 = "";

	private ArrayList<String> vars;

	/**
	 * Costruttore con parametri
	 * @param T
	 * @param VarTab
	 */
	TreeVisitor(ExprTree T, Hashtable<String, Integer> VarTab, ArrayList<String> varlist) {
		this.T = T;
		this.T.setCurrentNode(T.getRoot());
		this.VarTab = VarTab;
		this.vars = varlist;
	}

	/**
	 * Metodo per prendere tutte le variabili delle istruzioni in modo da gestire le eccezioni sulle variabili non dichiarate
	 */
	public void collectVars() {
		if (T.getCurrentNode() == null)
			return ;
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
			} 
			else {
				if(current.getValue().matches("^[\\$_a-zA-Z]+[\\$_\\w]*$") && !vars.contains(current.getValue()) && !current.getValue().equals("SET")
						&& !current.getValue().equals("GET") && !current.getValue().equals("ADD") && !current.getValue().equals("SUB") && !current.getValue().equals("MUL")
						&& !current.getValue().equals("DIV")) {
				
				vars.add(current.getValue());
				return;
				}
				current = null;
			}
			
		}
	}

	/**
	 * Come algoritmo di visita dell' albero utilizzo postorder
	 * @return
	 */
	public int postorderIter() {
		if (T.getCurrentNode() == null)
			return 1;
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
				return 1;
			current = s.pop();
 
			if( current.getRC() != null && ! s.isEmpty() && current.getRC() == s.peek()) {
				s.pop();
				s.push(current);
				current = current.getRC();
			} 
			else {
				if(createOps(current.getValue()) == 1){
					return 0;
				}
				current = null;
				position++;
			}
		}
	}
	
	/**
	 * Metodo per analizzare ogni singolo token che viene passato in input ed eseguire l' azione che ne comporta
	 * Nel caso di operazioni non di SET e GET ho usato un metodo che sfrutta un array per inserire i vari valori numerici o i valori corrispondenti alle variabili
	 * per andare poi a sostuirne il valore una volta trovata l'operazione da eseguire
	 */
	private int createOps(String val) {

		if (isANumber(val)) {
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
				System.out.println("ERROR: Division by zero are not allowed.");
				return 1;
			}
		}
		else if (val == "GET") {
			System.out.println(Ops.get(position - 2));
			if(!vars.isEmpty()){
				vars.remove(0);
			}
			position = 0;
		}
		else if (val == "SET") {
			VarTab.replace(tmpVar, Ops.get(position-1));
			vars.remove(0);
			if(!tmpVar.isEmpty()) {
				System.out.println(tmpVar + " = "+ Ops.get(position - 1));
			}
			else {
				VarTab.replace(tmpVar2, Ops.get(position - 1));
				System.out.println(tmpVar2 + " = "+ Ops.get(position - 1));
			}
			position = 0;
		}
		else if (isAVar(val)) {

			if (VarTab.containsKey(val)){
				Ops.add(VarTab.get(val));
				position++;
				tmpVar2 = val;
			}
	
			else {
				if(!vars.get(0).equals(val)) {
					System.out.println("ERROR: " + val + " has not been declared.");
					return 1;
				}
				VarTab.put(val, 0);
				tmpVar = val;
			}
			position--;
		}
		return 0;
	}
	
	private boolean isAVar(String s) {
		return s.matches("^[\\$_a-zA-Z]+[\\$_\\w]*$");
	}

	private boolean isANumber(String s) {
        String temp;
		if(s.startsWith("-")){ //checks for negative values
			temp=s.substring(1);
			if(temp.matches("[+]?\\d*(\\.\\d+)?")){
				return true;
			}
		}
		if(s.matches("[+]?\\d*(\\.\\d+)?")) {
			return true;
		}
		return false;
	}
	
	
}
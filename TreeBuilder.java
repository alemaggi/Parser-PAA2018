import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe per la costruzioni degli alberi di istruzioni
 * Ha come attributi:
 * 	instructions
 * 	instructionTrees
 */
public class TreeBuilder {

	private ArrayList<String[]> instructions = new ArrayList<String[]>();
	private ArrayList<ExprTree> instructionTrees = new ArrayList<ExprTree>();
	
	public ArrayList<ExprTree> getInstructionTrees() {
		return instructionTrees;
	}

	public void setInstructionTrees(ArrayList<ExprTree> instructionTrees) {
		this.instructionTrees = instructionTrees;
	}

	/**
	 * Metodo per creare le istruzioni
	 * @param line
	 * @return
	 * @throws IOException
	 */
	private int createInstructions(String line) throws IOException {
		
		line = line.substring(1, line.length()-1);
		line = line.replace("(", " ( ");
		line = line.replace(")", " ) ");
		String[] expr = line.split(" ");

		int expectedTokN = 0;
		int realTokN = 0;
		for(int i = 0; i < expr.length; i++) {
			//Conto i token
			if(expr[i].equals("SET") || expr[i].equals("GET")
			||	expr[i].equals("ADD") || expr[i].equals("SUB") || expr[i].equals("MUL") || expr[i].equals("DIV")
			||  expr[i].matches("^[\\$_a-zA-Z]+[\\$_\\w]*$")
			||  expr[i].matches("-?\\d+"))
			{
				realTokN++;
			}
			//SET o GET richiede 2 token --> se stesso e un valore 
			if(expr[i].equals("SET")) {
				expectedTokN += 3;
			}
			//È un operazione --> richiede altri 2 token
			if(expr[i].equals("ADD") || expr[i].equals("SUB") || expr[i].equals("MUL") || expr[i].equals("DIV") || expr[i].equals("GET")) {
				expectedTokN += 2;
			}		
		}
		if(expectedTokN == realTokN) {
			instructions.add(expr);
			return 0;
		}
		else
			return 1;
	} 
	
	/**
	 * Metodo per leggere le istruzioni e rilevare eventuali errori come ad esempio un simbolo davanti al nome di una variabile --> -var (Non accettabile)
	 * @param text
	 * @return
	 * @throws IOException
	 */
	public int readInstructions(BufferedReader text) throws IOException {
		//SyntaxControl sc = new SyntaxControl();
		int i = 1;
		String line = text.readLine();
		while (line != null) {
			if(createInstructions(line) != 0) {
				System.out.println("ERROR: Invalid or missing token at line " + i);
				return 0;
			}
			line = text.readLine();
			i++;
		}
		return 1;
	}

	/**
	 * Metodo di creazione degli alberi
	 */
	public void createTrees() {
		int OpCode;
		TokenCodes TC = new TokenCodes();
		for (int i = 0; i < instructions.size(); i++) {
			ExprTree ET = new ExprTree();
			for (int j = 0; j < instructions.get(i).length; j++) {
				OpCode = TC.identifyToken(instructions.get(i)[j]);
				addNode(ET,OpCode,instructions.get(i)[j]);
			}

			instructionTrees.add(ET);
		}
	}

	/**
	 * Metodo per aggiungere un nodo all' albero
	 * @param ET
	 * @param opCode
	 * @param value
	 */
	private void addNode(ExprTree ET, int opCode, String value) {
		switch (opCode) {
		case 1 : 
			ET.getRoot().setValue("GET");
			break;
		case 2 :
			ET.getRoot().setValue("SET");
			break;
		case 3 :
			ET.getCurrentNode().setValue("ADD");
			break;
		case 4 : 
			ET.getCurrentNode().setValue("SUB");
			break;
		case 5 : 
			ET.getCurrentNode().setValue("MUL");
			break;
		case 6 :
			ET.getCurrentNode().setValue("DIV");
			break;
		case 7 : 
			if (ET.getCurrentNode().getLC().getValue() == " ") {
				ET.setCurrentNode(ET.getCurrentNode().getLC());
				ET.getCurrentNode().setRC(new TokenNode(" ",null,null,ET.getCurrentNode()));
				ET.getCurrentNode().setLC(new TokenNode(" ",null,null,ET.getCurrentNode()));
			}
			else {
				ET.setCurrentNode(ET.getCurrentNode().getRC());
				ET.getCurrentNode().setRC(new TokenNode(" ",null,null,ET.getCurrentNode()));
				ET.getCurrentNode().setLC(new TokenNode(" ",null,null,ET.getCurrentNode()));
			}
			break;
		case 8 :
			ET.setCurrentNode(ET.getCurrentNode().getFather());
			break;
		case 9 :
			break;
		default :
			if(ET.getCurrentNode().getLC().getValue() == " " )
				ET.getCurrentNode().getLC().setValue(value);
			else
				ET.getCurrentNode().getRC().setValue(value);
		}	
	}
}

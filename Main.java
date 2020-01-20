import java.io.IOException;
import java.util.Hashtable;
import java.util.*;

/**
 * Metodo Main del programma nel quale si crea l'albero di istruzioni e si chiama il metodo per iterare su di esso eseguendo le operazioni richieste
 */
public class Main {
    public static void main(String[] args) throws IOException {
        TreeBuilder T = new TreeBuilder();
        InputFileReader in = new InputFileReader();
		if (in.readFile(args[0]) != 0){
            return;
        }
		if(T.readInstructions(in.getText()) != 1)
			return;
		T.createTrees();
        ArrayList<String> varlist = new ArrayList<String>();
        Hashtable<String, Integer> vars = new Hashtable<String, Integer>();
		
		for (int i = 0; i < T.getInstructionTrees().size(); i++) {
			TreeVisitor EX = new TreeVisitor(T.getInstructionTrees().get(i), vars, varlist);
			EX.collectVars();
			if(EX.postorderIter()!=1) {
				break;
			}
        }
    }
}
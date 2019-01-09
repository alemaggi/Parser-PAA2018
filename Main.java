import java.io.IOException;


public class Main {

	public static void main(String[] args) throws IOException {
		InputFileReader fr  = new InputFileReader();

		fr.readFile(args[0]);
		TreeBuilder T = new TreeBuilder();
		T.readInstructions(fr.getText());
		T.createTrees();
		
		for (int i = 0; i < T.getInstructionTrees().size(); i++) {
			TreeVisitor TV = new TreeVisitor(T.getInstructionTrees().get(i));
			TV.postorderIter();
			System.out.println("\n");
		}

	}

}

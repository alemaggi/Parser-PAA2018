import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Classe creata per la gesione della lettura del file di input e usata per formattare il testo ed eseguire un primo controllo su alcuni elementi 
 * che creano problemi ed eccezioni durante l'esecuzione del programma.
 * Le istruzioni formattate nel modo corretto vengono scritte su un file temporaneo che viene poi cancellato
 */
class InputFileReader {
    private BufferedReader text = null;
    private String tmpFile = "tempFile.txt";

    //ArrayList che contiene come stringa ogni riga
    public ArrayList<String> instrucionList = new ArrayList<String>();

    //ArrayList che contiene una singola istruzione in maniera tempo prima che venga aggiunta ad instructionList 
    public ArrayList<String> tmpInstruction = new ArrayList<String>();

    //ArrayList di string che contiene tutte le istruzioni concatenate per poi andare a dividerle in modo da liberarci di qualunque formattazione sia stata fatta
    public ArrayList<String> concatenatedInstructions = new ArrayList<String>();


    /**
     * Funzione che legge il file di input, performa tutte le operazioni per
     * liberarsi della formattazione del testo e fa a scrivere la sequenza di
     * istruzioni su un nuovo file temporaneo
     * 
     * @param filePath
     * @throws IOException
     */
    public int readFile(String filePath) throws IOException {
		FileReader file = null;
		try {
			if(formatText(filePath) == 1){
                return 1;
            }
            else file = new FileReader(tmpFile);
            }
		catch (FileNotFoundException e) {
			System.out.println("Input file not found.");
			System.exit(1);
		}
		
		text = new BufferedReader(file);
		File tmp = new File("tempFile.txt");
        tmp.delete();
        return 0; //Se è andato tutto bene
    }
    
    //Getter per ottenere il testo una volta formattato
    public BufferedReader getText() {
		return text;
    }
    
    /**
     * 
     * @param filePath
     * @throws IOException
     */
    private int formatText(String filePath) throws IOException {
		Scanner file;
		PrintWriter writer;
		
		try {
			file = new Scanner(new File(filePath));
			writer = new PrintWriter(tmpFile);
			
			while(file.hasNext()) {
				String line = file.nextLine();
				line = line.trim();
				line = line.replace("(", " ( ");
				line = line.replace(")", " ) ");
				line = line.trim();
				String[] lineAsArray = line.split(" ");
				for (String c : lineAsArray) {
					concatenatedInstructions.add(c);
				}
			}

			//Controllo sulle parentesi prima che vengano divise
            SyntaxControl sc = new SyntaxControl();
            String stringaPerControlloParentesi = concatenatedInstructions.toString().substring(1, concatenatedInstructions.toString().length() - 1);

			if (!sc.ParMatching(stringaPerControlloParentesi)){
                System.out.println("ERROR: Parentesi non corrette e/o non bilanciate");
                file.close();
                writer.close();
				return 1;
            }

            //Controllo che tutte le istruzioni comincino con GET o SET
            if(!sc.controlloGetSet(stringaPerControlloParentesi)){
                System.out.println("ERROR: Missing declaration or evaluation statement");
                file.close();
                writer.close();
				return 1;
            }

			//Dato che ho gia controllato le parentesi posso fare questa divisione del testo
			for (int i = 0; i < concatenatedInstructions.size(); i++){
				
				if(i != 1) {
					if(!concatenatedInstructions.get(i).contains("GET") && !concatenatedInstructions.get(i).contains("SET")){
						tmpInstruction.add(concatenatedInstructions.get(i));
					}
					else {
						//salvo temporaneamente il simbolo prima del GET o SET
						String tmp = tmpInstruction.get(tmpInstruction.size() - 1);
						
						tmpInstruction.remove(tmpInstruction.size() - 1);
						
						instrucionList.add(tmpInstruction.toString());
						tmpInstruction.clear();
						tmpInstruction.add(tmp);
						tmpInstruction.add(concatenatedInstructions.get(i));
					}
					if(i == (concatenatedInstructions.size() - 1)){
						instrucionList.add(tmpInstruction.toString());
						tmpInstruction.clear();
					}
					
				}
				else {
					tmpInstruction.add(concatenatedInstructions.get(i));
				}
			}
                        
			for (String c : instrucionList) {

				c = c.substring(1, c.length() - 1);
				c = c.replace(",","");
				c = c.trim();
				c = c.substring(1, c.length() - 1);
				for(int i = 0; i < c.length(); i++){
					if (c.charAt(i) == ',' || c.charAt(i) == '[' || c.charAt(i) == ']') {
						//Non lo metto nel file temporaneo
					}
					else {
						writer.write(c.charAt(i));
					}
				}
				writer.write("\n");
            }
            //Arrivato a questo punto ho il testo con le operazioni correttemente formattato
            if (sc.syntaxCheck(instrucionList) != 0){
                file.close();
                writer.close();
                return 1;
            }

			file.close();
            writer.close();
		}
		catch (FileNotFoundException ex) {
            System.out.println("Input file not found.");
            return 1;
        }
        return 0; //Se è andato tutto a buon fine ritorno 1 cosi da poter continuare con l'esecuzione
	}
}
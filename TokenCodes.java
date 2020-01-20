
/*
Classe usata per definire i vari codici operativi dei token
*/
public class TokenCodes {
	
	private static final String[] OPCodes = new String[] { "GET" , "SET" , "ADD" , "SUB" , "MUL" , "DIV" , "(" , ")" , ""};
    
	/**
	 * 
	 *
	 * @param tk Parametro usato per passare alla funzione il token
	 * @return Ritorna un intero corrispondente alla posizione occupata dal relativo token nell' array OPCodes
	 */
	public int identifyToken(String tk) {
		int count = 1;
		for ( String s : OPCodes ) {  //controllo se il token appartiene alle op possibili e nel caso ritorno un numero per sapere qual'e'
			if (s.equals(tk)) 
				return count;
			else
				count++;
		}
		return 0;
	}
}
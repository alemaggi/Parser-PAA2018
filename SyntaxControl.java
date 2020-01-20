import java.io.IOException;
import java.util.Stack;
import java.util.ArrayList;

/**
 * Classe realizzata per controllare sintatticamente le espressioni date in input
 */

public class SyntaxControl {
    /**
     * Metodo per controllare che le parentesi siano bilanciate
     * @param str
     * @return
     */
    public boolean ParMatching(String str) {
	    Stack<Character> stack = new Stack<Character>();
        char c;
        if (str.contains("[") || str.contains("]")){
            return false;
        }
	    for(int i=0; i < str.length(); i++) {
            c = str.charAt(i);
	        if(c == '(')
	            stack.push(c);
	        else if(c == ')') {
	            if(stack.empty()) {
                    return false;
                }
	            else if(stack.peek() == '(') {
                    stack.pop();
                }
	            else {
                    return false;
                }
            }
	    }
	    return  stack.empty();
    }

    /**
     * Metodo per controllare che le istruzioni comincino tutte con GET o SET
     * @param str
     * @return
     */
    public boolean controlloGetSet(String str) {
        for (int i = 0; i < str.length() - 2; i++){
            if(i != 0) {
                if (str.charAt(i) == 'E' && str.charAt(i+1) == 'T'){
                    if(str.charAt(i - 1) != 'S' && str.charAt(i - 1) != 'G'){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Metodo per il controllo sintattico delle istruzioni
     * @param instructions
     * @return
     * @throws IOException
     */
    public int syntaxCheck(ArrayList<String> instructions) throws IOException {
        for (String instr : instructions) {
            String tmp = "";
            for (int i = 0; i < instr.length(); i++){
                if (instr.charAt(i) != ','){
                    tmp = tmp.concat(String.valueOf(instr.charAt(i)));
                }
            }
            tmp = tmp.replace("  ", " ");
            
            String[] arrayTmp = tmp.split(" ");

            //System.out.println(arrayTmp[1]);
            if (!arrayTmp[1].equals("GET") && !arrayTmp[1].equals("SET")){
                System.out.println("ERROR: tutte le istruzioni devono iniziare con SET o GET");
                return 1;
            }
            
            for (int i = 0; i < tmp.length(); i++){
                if (tmp.charAt(i) != ' ' && tmp.charAt(i) != ',' && tmp.charAt(i) != '[' && tmp.charAt(i) != '(' && tmp.charAt(i) != ']' && tmp.charAt(i) != ')'){
                    //Caso GET
                    if (tmp.charAt(i) == 'G') {
                        if (tmp.charAt(i + 1) == 'E') {
                            if (tmp.charAt(i + 2) == 'T') {
                                //Devo controllare che dopo ci sia uno spazio 
                                if(!Character.isWhitespace(tmp.charAt(i + 3))){
                                    System.out.println("ERROR: Manca uno spazio dopo il GET");
                                    return 1;
                                }
                                else {
                                    i += 3; //Cosi salto il get
                                }
                            }
                            else {
                                System.out.println("ERROR: Errore nel dichiarare un GET");
                                return 1;
                            }
                        }
                        else {
                            System.out.println("ERROR: Errore nel dichiarare un GET");
                            return 1;
                        }
                    }
                    //Caso SET
                    else if (tmp.charAt(i) == 'S') {
                        if (tmp.charAt(i + 1) == 'E') {
                            if (tmp.charAt(i + 2) == 'T') {
                                if (Character.isWhitespace(tmp.charAt(i + 3))) {
                                    /**
                                     * Controllo se dopo il Set di definizione di una variabile è presente uno
                                     * spazio bianco
                                     */
                                    if (!Character.isLetter(tmp.charAt(i + 4))){
                                        System.out.println("ERROR: dopo un SET bisogna dichiarare una variabile");
                                        return 1;
                                    }
                                    
                                } 
                                else {
                                    if (Character.isLetter(instr.charAt(i + 3))) {
                                        System.out.println("ERROR: Manca uno spazio dopo il SET");
                                        return 1;
                                    }
                                    else {
                                        i += 3; //Salto il SET
                                    }
                                }
                            }
                            else {
                                System.out.println("ERROR: nel dichiarare un SET");
                                return 1;
                            }
                            //Caso SUB
                        } else if (tmp.charAt(i + 1) == 'U') {
                            if (tmp.charAt(i + 2) == 'B') {
                                i += 3; //Salto il SUB
                            }
                            else {
                                System.out.println("ERROR: nel dichiarare un SUB");
                                return 1;
                            }
                        }
                        else {
                            System.out.println("ERROR: nel dichiarare un TOKEN");
                            return 1;
                        }
                    }
                    //Caso ADD
                    else if (tmp.charAt(i) == 'A') {
                        if (tmp.charAt(i + 1) == 'D') {
                            if (tmp.charAt(i + 2) == 'D') {
                                //Salto l'ADD
                                i += 3;
                            }
                            else {
                                System.out.println("ERROR: nel dichiarare un ADD");
                                return 1;
                            }
                        }
                        else {
                            System.out.println("ERROR: nel dichiarare un ADD");
                            return 1;
                        }
                    }
                    //Caso DIV
                    else if (tmp.charAt(i) == 'D') {
                        if (tmp.charAt(i + 1) == 'I') {
                            if (tmp.charAt(i + 2) == 'V') {
                                //Salto il DIV
                                i += 3;
                            }
                            else {
                                System.out.println("ERROR: nel dichiarare un DIV");
                                return 1;
                            }
                        }
                        else {
                            System.out.println("ERROR: nel dichiarare un DIV");
                            return 1;
                        }
                    }
                    //Caso MUL
                    else if (tmp.charAt(i) == 'M') {
                        if (tmp.charAt(i + 1) == 'U') {
                            if (tmp.charAt(i + 2) == 'L') {
                                //Salto il MUL
                                i += 3;
                            }
                            else {
                                System.out.println("ERROR: nel dichiarare un MUL");
                                return 1;
                            }
                        }
                        else {
                            System.out.println("ERROR: nel dichiarare un MUL");
                            return 1;
                        }
                    }
                }
            }
        }
        return 0;
    }
}
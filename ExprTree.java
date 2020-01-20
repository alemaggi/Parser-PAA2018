/**
 * Classe ExprTree usata per gestione degli alberi di espressioni
 * Ha come attributi:
 * 	private TokenNode root;
	private TokenNode CurrentNode;
 */
public class ExprTree {
	
	private TokenNode root;
	private TokenNode CurrentNode;
	
	public ExprTree() {
		TokenNode lChild = new TokenNode(" ",null,null,root);
		TokenNode rChild = new TokenNode(" ",null,null,root);
		root = new TokenNode(" ",lChild,rChild,null);
		CurrentNode = root;
	}

	/**
	 * Getters & Setters
	 * @return
	 */
	public TokenNode getRoot() {
		return root;
	}

	public void setRootValue(String rootValue) {
		root.setValue(rootValue);
	}

	public TokenNode getCurrentNode() {
		return CurrentNode;
	}

	public void setCurrentNode(TokenNode currentNode) {
		CurrentNode = currentNode;
	}
	
	/**
	 * Metodo usato durante lo sviluppo per stampare gli alberi in un formato comodo da leggere e controllarne la correttezza
	 */
	public void printBinaryTree(TokenNode root, int level){
	    if(root==null)
	         return;
	    printBinaryTree(root.getRC(), level+1);
	    if(level!=0){
	        for(int i=0;i<level-1;i++)
	            System.out.print("|\t");
	            System.out.println("|-------"+root.getValue());
	    }
	    else
	        System.out.println(root.getValue());
	    printBinaryTree(root.getLC(), level+1);
	}
}

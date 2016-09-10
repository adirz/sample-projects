package oop.ex5.data_structures;

public class Node {
	private static final int RELATED_NUM_OF_NODES = 3;
	public static final int FATHER = 0;
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	
	private Node[] family; // The connections of the node 
	private int key;
	private int height;
	private int depth;
	private boolean initialized;
	
	/**
	 * Constructor of uninitialized node. this node is connected to real nodes
	 * as a stamp to where other nodes should be.
	 */
	public Node(){
		initialized = false;
		family = new Node[FATHER+1];
		height = 0;
		depth = -1;
	}
	
	/**
	 * Creates a new node with the given value.
	 * @param value
	 */
	public Node(int value){
		family = new Node[RELATED_NUM_OF_NODES];
		for(int i = 0; i < RELATED_NUM_OF_NODES; i++){
			family[i] = new Node();
		}
		key = value;
		depth = 0;
		height = 1;
		initialized = true;
	}
	
	/**
	 * @return key- the node value.
	 */
	public int getKey(){
		return key;
	}
	
	/**
	 * Change the node's key to value
	 * @param value
	 */
	public void setKey(int value){
		key = value;
	}
	
	/**
	 * Gets a node connected to this one
	 * FATHER = 0
	 * Left = 1
	 * Right = 2
	 * @param member
	 * @return the proper connected node
	 */
	public Node getFamily(int member){
		return family[member];
	}
	
	/**
	 * @return is it a real node or a place filler
	 */
	public boolean hasInitialized() {
		return initialized;
	}

	/**
	 * Adds a new connection to the node
	 * @param node - a node to connect to this one
	 * @param member - where to connect it to.
	 */
	public void addToFamily(Node node, int member) {
		family[member] = node;
		if(member != FATHER){
			node.addToFamily(this, FATHER);
			updateHeight();
		}
	}

	/**
	 * As it sounds- updates the node's height. But also to his ancestors
	 */
	public void updateHeight(){
		int lHeight = 0, rHeight = 0;
		if(family[LEFT].hasInitialized())
			lHeight = family[LEFT].getHeight();
		if(family[RIGHT].hasInitialized())
			rHeight = family[RIGHT].getHeight();
		height = 1 + Math.max(lHeight, rHeight);
		if(family[FATHER].hasInitialized())
			family[FATHER].updateHeight();
	}
	
	/**
	 * @return the height of the node
	 */
	public int getHeight(){
		if(!initialized)
			height = 0;
		return height;
	}
	
	/**
	 * @return the depth of the node
	 */
	public int getDepth(){
		if(!initialized)
			depth = -1;
		return depth;
	}
	
	/**
	 * Sets the depth of the node to be "depth"
	 * @param depth
	 */
	public void setDepth(int depth){
		this.depth = depth;
	}
}

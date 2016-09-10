package oop.ex5.data_structures;

import java.util.Iterator;

public class AvlTree {
	private static final int UNALLOWED = 2;
	private Node root;
	private int numOfNode;
	private IteratorsHolder itHolder;
	
	/**
	 * A default constructor
	 */
	public AvlTree(){
		numOfNode = 0;
		root = new Node();
		itHolder = new IteratorsHolder();
	}
	
	/**
	 * A data constructor
	 * a constructor that builds the tree by adding the elements in the input
	 * array one-by-one. If the same value appears twice (or more) in the
	 * list, it is ignored.
	 * 
	 * @param data values to add to tree
	 */
	public AvlTree(int[] data){
		itHolder = new IteratorsHolder();
		numOfNode = 1;
		root = new Node(data[0]);
		for(int i = 1; i < data.length; i++){
			add(data[i]);
		}
	}
	
	/**
	 * A copy constructor
	 * a constructor that builds a tree that is a copy of an existing tree
	 * 
	 * @param tree an AvlTree
	 */
	public AvlTree(AvlTree tree){
		itHolder = new IteratorsHolder();
		numOfNode = tree.size();
		root = new Node(tree.getRoot().getKey());
		copy(tree.getRoot(), root);
	}
	
	/**
	 * Copies all sub-nodes from one AVL tree to another
	 * @param sourceNode - the node to be copied from
	 * @param copyNode - the node to be copied to
	 */
	private void copy(Node sourceNode, Node copyNode){
		for(int member = Node.LEFT; member <= Node.RIGHT; member ++){
			if(sourceNode.getFamily(member).hasInitialized()){
				copyNode.addToFamily(
						new Node(sourceNode.getFamily(member).getKey()),
						member);
				copy(sourceNode.getFamily(member),
						copyNode.getFamily(member));
			}
		}
	}
	
	/**
	 * This method calculates the minimum number of nodes in an AVL tree of
	 * height h
	 * 
	 * @param h a height of the tree (a non-negative integer)
	 * @return minimum number of nodes in the tree
	 */
	public static int findMinNodes(int h){
		int hig = h+3;
		double fSqr = (1 + Math.sqrt(5))/2;
		double min = ((Math.pow(fSqr, hig) -
				Math.pow(1-fSqr, hig))/Math.sqrt(5));
		// rounding to the closest integer
		return (int)(min + 0.5)-1;
	}
	
	/**
	 * Adds a new node with a key newValue into the tree
	 * 
	 * @param newValue new value to add to tree
	 * @return false if new value already exist in tree
	 */
	public boolean add(int newValue){
		if(!root.hasInitialized()){
			root = new Node(newValue);
			numOfNode ++;
			return true;
		}
		Node node = findNode(root, newValue, 0);
		if(node.hasInitialized()){
			if(node.getKey() == newValue)
				return false;
		}
		numOfNode ++;
		if(node.getKey() > newValue){
			node.addToFamily(new Node(newValue), Node.LEFT);
		}else{
			node.addToFamily(new Node(newValue), Node.RIGHT);
		}
		checkBalance(node);
		return true;
	}
	
	/**
	 * Remove a node from the tree if it exist in it
	 * 
	 * @param toDelete value to delete
	 * @return true if toDelete is found and deleted
	 */
	public boolean delete(int toDelete){
		Node byeByeNode = findNode(root, toDelete, 0);
		if(byeByeNode.getKey() != toDelete)
			return false;
		if(byeByeNode.getFamily(Node.LEFT).hasInitialized()){
			/*
			  replacer is the bottom node of the tree that can be switched
			  with the deleted one in place, than we can delete a leaf or one
			  with only one child
			*/
			Node replacer = byeByeNode.getFamily(Node.LEFT);
			while(replacer.getFamily(Node.RIGHT).hasInitialized()){
				replacer = replacer.getFamily(Node.RIGHT);
			}
			byeByeNode.setKey(replacer.getKey());
			//updates all iterators so none would be left outside of the tree
			itHolder.update(replacer, byeByeNode);
			byeByeNode = replacer;
		}
		itHolder.update(byeByeNode, byeByeNode);
		if(!byeByeNode.getFamily(Node.FATHER).hasInitialized()){
			if(byeByeNode.getFamily(Node.LEFT).hasInitialized()){
				root = byeByeNode.getFamily(Node.LEFT);
				root.addToFamily(new Node(), Node.FATHER);
			}else
				root = new Node();
		}else{
			Node papa = byeByeNode.getFamily(Node.FATHER);
			if(papa.getKey() < byeByeNode.getKey())
				papa.addToFamily(byeByeNode.getFamily(Node.LEFT),Node.RIGHT);
			else
				papa.addToFamily(byeByeNode.getFamily(Node.RIGHT),Node.LEFT);
		}
		checkBalance(byeByeNode.getFamily(Node.FATHER));
		numOfNode --;
		return true;
	}
	
	/**
	 * Because an AVL tree need to be kept balanced regarding the heights, you
	 * need to check it is still balanced after every change.
	 * it is done here by going up from the node where the change happened
	 * @param node 	The node to check if it is balanced on (with it kids'
	 * 				height difference.
	 */
	private void checkBalance(Node node) {
		if(node.hasInitialized()){
			int rotFactor;
			Node left = node.getFamily(Node.LEFT);
			Node right = node.getFamily(Node.RIGHT);
			int lHeight, rHeight;
			lHeight = left.getHeight();
			rHeight = right.getHeight();
			rotFactor = lHeight - rHeight;
			
			if(Math.abs(rotFactor) < UNALLOWED){//everything good in this node
				if(node.getDepth() != 0)
					checkBalance(node.getFamily(Node.FATHER));
			}else{//unbalanced!
				if(rotFactor == UNALLOWED){
					Node leftLeft = left.getFamily(Node.LEFT);
					Node leftRight = left.getFamily(Node.RIGHT);
					if(leftLeft.getHeight() < leftRight.getHeight()){
						//conditions to LR rotation
						rotateRight(leftRight);
						rotateLeft(leftRight);
					}else{
						//conditions to LL rotation
						rotateLeft(left);
					}
				}else{
					Node rightLeft = right.getFamily(Node.LEFT);
					Node rightRight = right.getFamily(Node.RIGHT);
					if(rightLeft.getHeight() < rightRight.getHeight()){
						//conditions to RR rotation
						rotateRight(right);
					}else{
						//conditions to RL rotation
						rotateLeft(rightLeft);
						rotateRight(rightLeft);
					}
				}
			}
		}
	}
	
	/**
	 * As learned in DAST the way to fix unbalanced tree is by rotations.
	 * Here we rotate right
	 * @param node to be rotated
	 */
	private void rotateRight(Node node){
		Node papa = node.getFamily(Node.FATHER);
		Node grandpa = papa.getFamily(Node.FATHER);
		//
		papa.addToFamily(node.getFamily(Node.LEFT), Node.RIGHT);
		node.addToFamily(new Node(), Node.FATHER);
		node.addToFamily(papa, Node.LEFT);
		if(!grandpa.hasInitialized())
			root = node;
		else{
			// find the place that papa was in to replace him
			int member;
			if(grandpa.getKey() < node.getKey())
				member = Node.RIGHT;
			else
				member = Node.LEFT;
			grandpa.addToFamily(node, member);
		}
	}

	/**
	 * As learned in DAST the way to fix unbalanced tree is by rotations.
	 * Here we rotate left
	 * @param node to be rotated
	 */
	private void rotateLeft(Node node){
		Node papa = node.getFamily(Node.FATHER);
		Node grandpa = papa.getFamily(Node.FATHER);
		papa.addToFamily(node.getFamily(Node.RIGHT), Node.LEFT);
		node.addToFamily(new Node(), Node.FATHER);
		node.addToFamily(papa, Node.RIGHT);
		if(!grandpa.hasInitialized())
			root = node;
		else{
			int member;
			// find the place that papa was in to replace him
			if(grandpa.getKey() < node.getKey())
				member = Node.RIGHT;
			else
				member = Node.LEFT;
			grandpa.addToFamily(node, member);
		}
	}

	/**
	 * Does a tree contains a given input
	 * 
	 * @param searchVal Value to search for
	 * @return the depth (root = 0) of the node if found, otherwise return -1
	 */
	public int contains(int searchVal){
		Node node = findNode(root, searchVal, 0);
		if(node.getKey() != searchVal)
			return -1;
		return node.getDepth();
	}

	/**
	 * @return number of nodes in the tree
	 */
	public int size(){
		return numOfNode;
	}
	
	/**
	 * @return iterator to the AVL tree. The return iterator can pass over the
	 * tree nodes in ascending order.
	 */
	public Iterator<Integer> iterator(){
		Node smallest = root;
		if(smallest.hasInitialized()){
			while(smallest.getFamily(Node.LEFT).hasInitialized()){
				smallest = smallest.getFamily(Node.LEFT);
			}
		}
		TreeIterator iter = new TreeIterator(smallest);
		itHolder.add(iter);
		return iter;
	}

	/**
	 * @return the root of the tree, the nodes that fathers all nodes
	 */
	public Node getRoot() {
		return root;
	}

	/**
	 * Helps find the tree node with the correct value, needed to check if a
	 * node exist, to add one and to delete.
	 * @param cur the current node to search from to his kids
	 * @param value the value of the node we search
	 * @param depthUpdate while we search we update the depth via depthUpdate
	 * @return The node with the correct value. if it wasn't found, it returns
	 * 			what should have been its father
	 */
	private Node findNode(Node cur, int value, int depthUpdate){
		if(!cur.hasInitialized())
			return cur;
		cur.setDepth(depthUpdate);
		if(cur.getKey() == value )
			return cur;
		if(cur.getKey() > value){
			if(!cur.getFamily(Node.LEFT).hasInitialized())
				return cur;
			return findNode(cur.getFamily(Node.LEFT), value, depthUpdate+1);
		}
		if(!cur.getFamily(Node.RIGHT).hasInitialized())
			return cur;
		return findNode(cur.getFamily(Node.RIGHT), value, depthUpdate+1);
	}
}

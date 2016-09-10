package oop.ex5.data_structures;

import java.util.Iterator;

public class TreeIterator implements Iterator<Integer> {
	
	private Node curNode;
	private boolean started;
	
	/**
	 * Construct the iterator, starts with the smallest node
	 * @param smallest - the nodes where the iterator starts
	 */
	public TreeIterator(Node smallest){
		curNode = smallest;
		started = false;
	}
	
	@Override
	public boolean hasNext() {
		if(!curNode.hasInitialized())
			return false;
		if(curNode.getFamily(Node.RIGHT).hasInitialized())
			return true;
		Node nextNode = curNode;
		while(nextNode.getFamily(Node.FATHER).hasInitialized()){
			if(nextNode.getKey() < nextNode.getFamily(Node.FATHER).getKey())
				return true;
			nextNode = nextNode.getFamily(Node.FATHER);
		}
		return false;
	}

	@Override
	public Integer next() {
		if(!started){
			started = true;
			return curNode.getKey();
		}
		curNode = nextNode();
		return curNode.getKey();
	}
	
	/**
	 * Works thans to that AVL is A BST so all the lefts are smaller the the
	 * node which in turn is smaller than all the rights.
	 * @return the smallest node that is bigger from cur node.
	 */
	private Node nextNode(){
		Node nNode;
		if(curNode.getFamily(Node.RIGHT).hasInitialized()){
			nNode = curNode.getFamily(Node.RIGHT);
			while(nNode.getFamily(Node.LEFT).hasInitialized()){
				nNode = nNode.getFamily(Node.LEFT);
			}
			return nNode;
		}else{
			nNode = curNode;
			if(nNode.getFamily(Node.RIGHT).hasInitialized()){
				nNode = nNode.getFamily(Node.RIGHT);
				while(nNode.getFamily(Node.LEFT).hasInitialized()){
					nNode = nNode.getFamily(Node.LEFT);
				}
				return nNode;
			}
			while(nNode.getFamily(Node.FATHER).hasInitialized()){
				if(nNode.getFamily(Node.FATHER).getKey() < curNode.getKey())
					nNode = nNode.getFamily(Node.FATHER);
				else{
					return nNode.getFamily(Node.FATHER);
				}
			}
			return nNode;
		}		
	}

	@Override
	public void remove() {}
	
	/**
	 * If a node is deleted it may change the cur node, so it changes
	 * accordingly. 
	 * @param replacer - The nodes that takes the "replaced" place.
	 * @param replaced - The node with the original value to be deleted.
	 */
	public void update(Node replacer, Node replaced){
		if(curNode.getKey() == replacer.getKey()){
			if(replacer.equals(replaced))
				curNode = nextNode();
			else{
				curNode = replaced;
				started = false;
			}
		}else if(curNode.equals(replaced)){
			curNode = nextNode();
		}
	}

}

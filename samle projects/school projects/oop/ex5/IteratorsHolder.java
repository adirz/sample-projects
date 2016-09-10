package oop.ex5.data_structures;

import java.util.LinkedList;

public class IteratorsHolder {
	private LinkedList<TreeIterator> iters;
	
	/**
	 * constructor that crates a new holder for the iterators of the tree.
	 */
	public IteratorsHolder(){
		iters = new LinkedList<TreeIterator>();
	}
	
	/**
	 * In order for the iterator to be able to update is is needed to be added
	 * to the holder.
	 * @param iter
	 */
	public void add(TreeIterator iter){
		iters.add(iter);
	}
	
	/**
	 * If an iterator is no longer in use, please remove it from the holder to
	 * conserve in space and memory.
	 * @param iter - iterator to remove.
	 */
	public void remove(TreeIterator iter){
		iters.remove(iter);
	}

	/**
	 * If a node is deleted it may change the cur node, so it changes
	 * accordingly for all iterators.
	 * @param replacer - The nodes that takes the "replaced" place.
	 * @param replaced - The node with the original value to be deleted.
	 */
	public void update(Node replacer, Node replaced){
		for(int i = 0; i < iters.size(); i++){
			iters.get(i).update(replacer, replaced);
		}
	}
}

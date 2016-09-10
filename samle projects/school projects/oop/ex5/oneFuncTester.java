package oop.ex5.data_structures;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.junit.Test;

public class oneFuncTester {
	
	@Test
	public void test01() {
		AvlTree tree = new AvlTree();
	}

	@Test
	public void test02() {

		AvlTree tree = new AvlTree();
		tree.add(1);
		tree.add(5);
		tree.add(9);
		tree.add(7);
	}
	
	@Test
	public void test03() {

		AvlTree tree = new AvlTree();
		assertEquals(tree.add(5), true);
	}

	@Test
	public void test04() {

		AvlTree tree = new AvlTree();
		assertEquals(tree.add(5), true);
		assertEquals(tree.add(5), false);
	}

	@Test
	public void test05() {

		AvlTree tree = new AvlTree();
		assertEquals(tree.contains(5), -1);
	}

	@Test
	public void test06() {

		AvlTree tree = new AvlTree();
		assertEquals(tree.add(5), true);
		assertEquals(tree.contains(5), 0);
	}
	
	@Test
	public void test07() {

		AvlTree tree = new AvlTree();
		assertEquals(tree.add(5), true);
		assertEquals(tree.delete(5), true);
	}

	@Test
	public void test08() {

		AvlTree tree = new AvlTree();
		tree.add(1);
		tree.add(2);
		tree.add(3);

		AvlTree tree2 = new AvlTree(tree);
		Iterator<Integer> it1 = tree.iterator();
		Iterator<Integer> it2 = tree2.iterator();
		
		while (it1.hasNext())
			assertEquals(it1.next(), it2.next());

	}

	@Test
	public void test09() {

		AvlTree tree = new AvlTree();
		tree.add(1);
		tree.add(3);
		tree.add(5);
		tree.add(7);

		AvlTree tree2 = new AvlTree(tree);
		Iterator<Integer> it1 = tree.iterator();
		Iterator<Integer> it2 = tree2.iterator();
		while (it1.hasNext())
			assertEquals(it1.next(), it2.next());
		assertEquals(tree.size(), 4);
	}

	@Test
	public void test10() {

		AvlTree tree = new AvlTree();
		assertEquals(tree.delete(5), false);
	}

	@Test
	public void test11() {

		AvlTree tree = new AvlTree();
		assertEquals(tree.size(), 0);
	}

	@Test
	public void test12() {

		AvlTree tree = new AvlTree();
		assertEquals(tree.add(5), true);
		assertEquals(tree.size(), 1);
	}

	@Test
	public void test13() {

		AvlTree tree = new AvlTree();
		assertEquals(tree.add(5), true);
		assertEquals(tree.delete(5), true);
		assertEquals(tree.size(), 0);
	}

	@Test
	public void test14() {

		AvlTree tree = new AvlTree();
		assertEquals(tree.add(5), true);
		assertEquals(tree.delete(6), false);
		assertEquals(tree.size(), 1);
	}
	
	@Test
	public void test15() {
		AvlTree tree = new AvlTree();
		assertEquals(tree.add(5), true);
		assertEquals(tree.add(3), true);
		assertEquals(tree.add(4), true);
		assertEquals(1, tree.contains(5));
		assertEquals(0, tree.contains(4));
		assertEquals(1, tree.contains(3));
	}
	
	@Test
	public void test16() {

		AvlTree tree = new AvlTree();
		assertEquals(tree.add(5), true);
		assertEquals(tree.add(7), true);
		assertEquals(tree.add(6), true);
		assertEquals(tree.contains(5), 1);
		assertEquals(tree.contains(6), 0);
		assertEquals(tree.contains(7), 1);
	}
	
	@Test
	public void test17() {

		AvlTree tree = new AvlTree();
		assertEquals(tree.add(5), true);
		assertEquals(tree.add(3), true);
		assertEquals(tree.add(1), true);
		assertEquals(1, tree.contains(5));
		assertEquals(0, tree.contains(3));
		assertEquals(1, tree.contains(1));
	}
	
	@Test
	public void test18() {

		AvlTree tree = new AvlTree();
		assertEquals(tree.add(5), true);
		assertEquals(tree.add(7), true);
		assertEquals(tree.add(9), true);
		assertEquals(tree.contains(5), 1);
		assertEquals(tree.contains(7), 0);
		assertEquals(tree.contains(9), 1);
	}
	
	@Test
	public void test19() {

		AvlTree tree = new AvlTree();
		assertEquals(tree.add(5), true);
		assertEquals(tree.add(6), true);
		assertEquals(tree.add(3), true);
		assertEquals(tree.add(4), true);
		assertEquals(tree.delete(6), true);
		assertEquals(tree.contains(5), 1);
		assertEquals(tree.contains(4), 0);
		assertEquals(tree.contains(3), 1);
		assertEquals(-1, tree.contains(6));
	}
	
	@Test
	public void test20() {

		AvlTree tree = new AvlTree();
		assertEquals(tree.add(5), true);
		assertEquals(tree.add(3), true);
		assertEquals(tree.add(7), true);
		assertEquals(tree.add(6), true);
		assertEquals(tree.delete(3), true);
		assertEquals(tree.contains(5), 1);
		assertEquals(tree.contains(6), 0);
		assertEquals(tree.contains(7), 1);
		assertEquals(tree.contains(3), -1);
	}
	
	@Test
	public void test21() {

		AvlTree tree = new AvlTree();
		assertEquals(tree.add(5), true);
		assertEquals(tree.add(6), true);
		assertEquals(tree.add(3), true);
		assertEquals(tree.add(1), true);
		assertEquals(tree.delete(6), true);
		assertEquals(tree.contains(5), 1);
		assertEquals(tree.contains(3), 0);
		assertEquals(tree.contains(1), 1);
		assertEquals(tree.contains(6), -1);
	}

	@Test
	public void test22() {

		AvlTree tree = new AvlTree();
		assertEquals(tree.add(5), true);
		assertEquals(tree.add(3), true);
		assertEquals(tree.add(7), true);
		assertEquals(tree.add(9), true);
		assertEquals(tree.delete(3), true);
		assertEquals(tree.contains(5), 1);
		assertEquals(tree.contains(7), 0);
		assertEquals(tree.contains(9), 1);
		assertEquals(tree.contains(3), -1);
	}

	@Test
	public void test23() {

		AvlTree tree = new AvlTree();
		tree.add(1);
		tree.add(2);
		tree.add(3);
		tree.add(4);
		tree.add(5);
		tree.add(6);
		tree.add(7);
		assertEquals(tree.size(), 7);
		assertEquals(tree.contains(1), 2);
		assertEquals(tree.contains(2), 1);
		assertEquals(tree.contains(3), 2);
		assertEquals(tree.contains(4), 0);
		assertEquals(tree.contains(5), 2);
		assertEquals(tree.contains(6), 1);
		assertEquals(tree.contains(7), 2);
	}

	@Test
	public void test24() {

		AvlTree tree = new AvlTree();
		tree.add(10);
		tree.add(8);
		tree.add(5);
		tree.add(2);
		tree.add(3);
		tree.add(1);
		tree.add(9);
		tree.add(7);
		tree.add(4);
		tree.add(6);

		assertEquals(tree.size(), 10);
		assertEquals(tree.contains(1), 3);
		assertEquals(tree.contains(2), 2);
		assertEquals(tree.contains(3), 1);
		assertEquals(tree.contains(4), 2);
		assertEquals(tree.contains(5), 0);
		assertEquals(tree.contains(6), 3);
		assertEquals(tree.contains(7), 2);
		assertEquals(tree.contains(8), 1);
		assertEquals(tree.contains(9), 3);
		assertEquals(tree.contains(10), 2);
	}

	@Test
	public void test25() {

		AvlTree tree = new AvlTree();
		tree.add(2);
		tree.add(3);
		tree.add(8);
		tree.add(4);
		tree.add(1);
		tree.add(6);
		tree.add(5);
		tree.add(7);
		assertEquals(tree.size(), 8);
		assertEquals(tree.contains(1), 2);
		assertEquals(tree.contains(2), 1);
		assertEquals(tree.contains(3), 0);
		assertEquals(tree.contains(4), 2);
		assertEquals(tree.contains(5), 3);
		assertEquals(tree.contains(6), 1);
		assertEquals(tree.contains(7), 3);
		assertEquals(tree.contains(8), 2);
	}

	@Test
	public void test26() {
		int [] a = new int[11];
		a[0] = 6;
		a[1] = 3;
		a[2] = 8;
		a[3] = 2;
		a[4] = 5;
		a[5] = 7;
		a[6] = 10;
		a[7] = 1;
		a[8] = 4;
		a[9] = 9;
		a[10] = 11;

		AvlTree tree = new AvlTree(a);
		tree.add(6);
		tree.add(3);
		tree.add(8);
		tree.add(2);
		tree.add(5);
		tree.add(7);
		tree.add(10);
		tree.add(1);
		tree.add(4);
		tree.add(9);
		tree.add(11);
		assertEquals(tree.size(), 11);
		assertEquals(tree.contains(1), 3);
		assertEquals(tree.contains(2), 2);
		assertEquals(tree.contains(3), 1);
		assertEquals(tree.contains(4), 3);
		assertEquals(tree.contains(5), 2);
		assertEquals(tree.contains(6), 0);
		assertEquals(tree.contains(7), 2);
		assertEquals(tree.contains(8), 1);
		assertEquals(tree.contains(9), 3);
		assertEquals(tree.contains(10), 2);
		assertEquals(tree.contains(11), 3);
		assertEquals(tree.delete(6), true);
		assertEquals(tree.size(), 10);
		assertEquals(tree.contains(1), 3);
		assertEquals(tree.contains(2), 2);
		assertEquals(tree.contains(3), 1);
		assertEquals(tree.contains(4), 2);
		assertEquals(tree.contains(5), 0);
		assertEquals(tree.contains(6), -1);
		assertEquals(tree.contains(7), 2);
		assertEquals(tree.contains(8), 1);
		assertEquals(tree.contains(9), 3);
		assertEquals(tree.contains(10), 2);
		assertEquals(tree.contains(11), 3);
	}
	
	@Test
	public void test27() {
		int [] a = new int[13];
		a[0] = 5;
		a[1] = 3;
		a[2] = 10;
		a[3] = 2;
		a[4] = 4;
		a[5] = 8;
		a[6] = 12;
		a[7] = 1;
		a[8] = 7;
		a[9] = 9;
		a[10] = 11;
		a[11] = 13;
		a[12] = 6;
		AvlTree tree = new AvlTree(a);
		assertEquals(tree.size(), 13);
		assertEquals(tree.contains(1), 3);
		assertEquals(tree.contains(2), 2);
		assertEquals(tree.contains(3), 1);
		assertEquals(tree.contains(4), 2);
		assertEquals(tree.contains(5), 0);
		assertEquals(tree.contains(6), 4);
		assertEquals(tree.contains(7), 3);
		assertEquals(tree.contains(8), 2);
		assertEquals(tree.contains(9), 3);
		assertEquals(tree.contains(10), 1);
		assertEquals(tree.contains(11), 3);
		assertEquals(tree.contains(12), 2);
		assertEquals(tree.contains(13), 3);
		assertEquals(tree.delete(2), true);
		assertEquals(tree.size(), 12);
		assertEquals(tree.contains(1), 3);
		assertEquals(tree.contains(2), -1);
		assertEquals(tree.contains(3), 2);
		assertEquals(tree.contains(4), 3);
		assertEquals(tree.contains(5), 1);
		assertEquals(tree.contains(6), 3);
		assertEquals(tree.contains(7), 2);
		assertEquals(tree.contains(8), 0);
		assertEquals(tree.contains(9), 2);
		assertEquals(tree.contains(10), 1);
		assertEquals(tree.contains(11), 3);
		assertEquals(tree.contains(12), 2);
		assertEquals(tree.contains(13), 3);
	}
	
	@Test
	public void test28() {

		AvlTree tree = new AvlTree();
		tree.add(2);
		tree.add(1);
		tree.add(1);
		assertEquals(tree.size(), 2);
		assertEquals(tree.contains(1), 1);
	}

	@Test
	public void test29() {

		AvlTree tree = new AvlTree();
		tree.add(3);
		tree.add(4);
		tree.add(2);
		tree.add(1);
		assertEquals(tree.size(), 4);
		assertEquals(tree.contains(1), 2);
		assertEquals(tree.delete(4), true);
		assertEquals(tree.contains(2), 0);
		assertEquals(tree.delete(4), false);

		AvlTree tree2 = new AvlTree(tree);
		Iterator<Integer> it1 = tree.iterator();
		Iterator<Integer> it2 = tree2.iterator();
		while (it1.hasNext())
			assertEquals(it1.next(), it2.next());

	}
/**
	@Test
	public void test30() {
		AvlTree tree = new AvlTree();
		assertEquals(tree.add(79), true);
		assertEquals(tree.add(49), true);
		assertEquals(tree.add(71), true);
		assertEquals(tree.add(84), true);
		assertEquals(tree.add(28), true);
		assertEquals(tree.add(82), true);
		assertEquals(tree.add(84), false);
		assertEquals(tree.add(19), true);
		assertEquals(tree.add(11), true);
		assertEquals(tree.add(4), true);
		assertEquals(tree.add(10), true);
		assertEquals(tree.add(51), true);
		assertEquals(tree.add(54), true);
		assertEquals(tree.add(82), false);
		assertEquals(tree.add(28), false);
		assertEquals(tree.add(25), true);
		assertEquals(tree.add(91), true);
		assertEquals(tree.add(65), true);
		assertEquals(tree.add(33), true);
		assertEquals(tree.add(55), true);
		assertEquals(tree.add(12), true);
		assertEquals(tree.add(75), true);
		assertEquals(tree.add(40), true);
		assertEquals(tree.add(55), false);
		assertEquals(tree.add(12), false);
		assertEquals(tree.add(78), true);
		assertEquals(tree.add(45), true);
		assertEquals(tree.add(12), false);
		assertEquals(tree.add(5), true);
		assertEquals(tree.add(24), true);
		assertEquals(tree.add(87), true);
		assertEquals(tree.add(17), true);
		assertEquals(tree.add(16), true);
		assertEquals(tree.add(16), false);
		assertEquals(tree.add(24), false);
		assertEquals(tree.add(67), true);
		assertEquals(tree.add(73), true);
		assertEquals(tree.add(27), true);
		assertEquals(tree.add(89), true);
		assertEquals(tree.add(2), true);
		assertEquals(tree.add(48), true);
		assertEquals(tree.add(73), false);
		assertEquals(tree.add(9), true);
		assertEquals(tree.add(37), true);
		assertEquals(tree.add(78), false);
		assertEquals(tree.add(56), true);
		assertEquals(tree.add(30), true);
		assertEquals(tree.add(87), false);
		assertEquals(tree.add(51), false);
		assertEquals(tree.add(95), true);
		assertEquals(tree.add(40), false);
		assertEquals(tree.add(42), true);
		assertEquals(tree.add(31), true);
		assertEquals(tree.add(11), false);
		assertEquals(tree.add(13), true);
		assertEquals(tree.add(90), true);
		assertEquals(tree.add(8), true);
		assertEquals(tree.add(50), true);
		assertEquals(tree.add(6), true);
		assertEquals(tree.add(97), true);
		assertEquals(tree.add(60), true);
		assertEquals(tree.add(46), true);
		assertEquals(tree.add(76), true);
		assertEquals(tree.add(10), false);
		assertEquals(tree.add(78), false);
		assertEquals(tree.add(16), false);
		assertEquals(tree.add(81), true);
		assertEquals(tree.add(23), true);
		assertEquals(tree.add(13), false);
		assertEquals(tree.add(2), false);
		assertEquals(tree.add(96), true);
		assertEquals(tree.add(74), true);
		assertEquals(tree.add(9), false);
		assertEquals(tree.add(15), true);
		assertEquals(tree.add(39), true);
		assertEquals(tree.add(10), false);
		assertEquals(tree.add(44), true);
		assertEquals(tree.add(59), true);
		assertEquals(tree.add(51), false);
		assertEquals(tree.add(79), false);
		assertEquals(tree.add(4), false);
		assertEquals(tree.add(48), false);
		assertEquals(tree.add(95), false);
		assertEquals(tree.add(29), true);
		assertEquals(tree.add(27), false);
		assertEquals(tree.add(96), false);
		assertEquals(tree.add(39), false);
		assertEquals(tree.add(7), true);
		assertEquals(tree.add(83), true);
		assertEquals(tree.add(3), true);
		assertEquals(tree.add(97), false);
		assertEquals(tree.add(78), false);
		assertEquals(tree.add(91), false);
		assertEquals(tree.add(14), true);
		assertEquals(tree.add(55), false);
		assertEquals(tree.add(37), false);
		assertEquals(tree.add(62), true);
		assertEquals(tree.add(25), false);
		assertEquals(tree.add(70), true);
		assertEquals(tree.add(25), false);
		assertEquals(tree.contains(79), 4);
		assertEquals(tree.contains(49), 4);
		assertEquals(tree.contains(71), 2);
		assertEquals(tree.contains(84), 3);
		assertEquals(tree.contains(28), 1);
		assertEquals(tree.contains(82), 1);
		assertEquals(tree.contains(84), 3);
		assertEquals(tree.contains(19), 3);
		assertEquals(tree.contains(11), 2);
		assertEquals(tree.contains(4), 5);
		assertEquals(tree.contains(10), 5);
		assertEquals(tree.contains(51), 0);
		assertEquals(tree.contains(54), 5);
		assertEquals(tree.contains(82), 1);
		assertEquals(tree.contains(28), 1);
		assertEquals(tree.contains(25), 4);
		assertEquals(tree.contains(91), 3);
		assertEquals(tree.contains(65), 4);
		assertEquals(tree.contains(33), 3);
		assertEquals(tree.contains(55), 4);
		assertEquals(tree.contains(12), 6);
		assertEquals(tree.contains(75), 4);
		assertEquals(tree.contains(40), 2);
		assertEquals(tree.contains(55), 4);
		assertEquals(tree.contains(12), 6);
		assertEquals(tree.contains(78), 3);
		assertEquals(tree.contains(45), 4);
		assertEquals(tree.contains(12), 6);
		assertEquals(tree.contains(5), 3);
		assertEquals(tree.contains(24), 5);
		assertEquals(tree.contains(87), 4);
		assertEquals(tree.contains(17), 6);
		assertEquals(tree.contains(16), 5);
		assertEquals(tree.contains(16), 5);
		assertEquals(tree.contains(24), 5);
		assertEquals(tree.contains(67), 5);
		assertEquals(tree.contains(73), 5);
		assertEquals(tree.contains(27), 5);
		assertEquals(tree.contains(89), 2);
		assertEquals(tree.contains(2), 5);
		assertEquals(tree.contains(48), 3);
		assertEquals(tree.contains(73), 5);
		assertEquals(tree.contains(9), 4);
		assertEquals(tree.contains(37), 4);
		assertEquals(tree.contains(78), 3);
		assertEquals(tree.contains(56), 3);
		assertEquals(tree.contains(30), 4);
		assertEquals(tree.contains(87), 4);
		assertEquals(tree.contains(51), 0);
		assertEquals(tree.contains(95), 5);
		assertEquals(tree.contains(40), 2);
		assertEquals(tree.contains(42), 5);
		assertEquals(tree.contains(31), 5);
		assertEquals(tree.contains(11), 2);
		assertEquals(tree.contains(13), 5);
		assertEquals(tree.contains(90), 4);
		assertEquals(tree.contains(8), 6);
		assertEquals(tree.contains(50), 5);
		assertEquals(tree.contains(6), 6);
		assertEquals(tree.contains(97), 5);
		assertEquals(tree.contains(60), 5);
		assertEquals(tree.contains(46), 5);
		assertEquals(tree.contains(76), 5);
		assertEquals(tree.contains(10), 5);
		assertEquals(tree.contains(78), 3);
		assertEquals(tree.contains(16), 5);
		assertEquals(tree.contains(81), 5);
		assertEquals(tree.contains(23), 6);
		assertEquals(tree.contains(13), 5);
		assertEquals(tree.contains(2), 5);
		assertEquals(tree.contains(96), 4);
		assertEquals(tree.contains(74), 6);
		assertEquals(tree.contains(9), 4);
		assertEquals(tree.contains(15), 4);
		assertEquals(tree.contains(39), 5);
		assertEquals(tree.contains(10), 5);
		assertEquals(tree.contains(44), 6);
		assertEquals(tree.contains(59), 6);
		assertEquals(tree.contains(51), 0);
		assertEquals(tree.contains(79), 4);
		assertEquals(tree.contains(4), 5);
		assertEquals(tree.contains(48), 3);
		assertEquals(tree.contains(95), 5);
		assertEquals(tree.contains(29), 5);
		assertEquals(tree.contains(27), 5);
		assertEquals(tree.contains(96), 4);
		assertEquals(tree.contains(39), 5);
		assertEquals(tree.contains(7), 5);
		assertEquals(tree.contains(83), 4);
		assertEquals(tree.contains(3), 4);
		assertEquals(tree.contains(97), 5);
		assertEquals(tree.contains(78), 3);
		assertEquals(tree.contains(91), 3);
		assertEquals(tree.contains(14), 6);
		assertEquals(tree.contains(55), 4);
		assertEquals(tree.contains(37), 4);
		assertEquals(tree.contains(62), 6);
		assertEquals(tree.contains(25), 4);
		assertEquals(tree.contains(70), 6);
		assertEquals(tree.contains(25), 4);
		assertEquals(tree.delete(37), true);
		assertEquals(tree.delete(35), false);
		assertEquals(tree.delete(34), false);
		assertEquals(tree.delete(47), false);
		assertEquals(tree.delete(24), true);
		assertEquals(tree.delete(44), true);
		assertEquals(tree.delete(1), false);
		assertEquals(tree.delete(29), true);
		assertEquals(tree.delete(19), true);
		assertEquals(tree.delete(8), true);
		assertEquals(tree.delete(36), false);
		assertEquals(tree.delete(4), true);
		assertEquals(tree.delete(7), true);
		assertEquals(tree.delete(50), true);
		assertEquals(tree.delete(49), true);
		assertEquals(tree.delete(32), false);
		assertEquals(tree.delete(3), true);
		assertEquals(tree.delete(23), true);
		assertEquals(tree.delete(44), false);
		assertEquals(tree.delete(34), false);
		assertEquals(tree.delete(3), false);
		assertEquals(tree.delete(18), false);
		assertEquals(tree.delete(32), false);
		assertEquals(tree.delete(9), true);
		assertEquals(tree.delete(43), false);
		assertEquals(tree.delete(1), false);
		assertEquals(tree.delete(19), false);
		assertEquals(tree.delete(44), false);
		assertEquals(tree.delete(1), false);
		assertEquals(tree.delete(36), false);
		assertEquals(tree.contains(79), 4);
		assertEquals(tree.contains(71), 2);
		assertEquals(tree.contains(84), 3);
		assertEquals(tree.contains(28), 1);
		assertEquals(tree.contains(82), 1);
		assertEquals(tree.contains(84), 3);
		assertEquals(tree.contains(11), 2);
		assertEquals(tree.contains(10), 4);
		assertEquals(tree.contains(51), 0);
		assertEquals(tree.contains(54), 5);
		assertEquals(tree.contains(82), 1);
		assertEquals(tree.contains(28), 1);
		assertEquals(tree.contains(25), 4);
		assertEquals(tree.contains(91), 3);
		assertEquals(tree.contains(65), 4);
		assertEquals(tree.contains(33), 3);
		assertEquals(tree.contains(55), 4);
		assertEquals(tree.contains(12), 5);
		assertEquals(tree.contains(75), 4);
		assertEquals(tree.contains(40), 2);
		assertEquals(tree.contains(55), 4);
		assertEquals(tree.contains(12), 5);
		assertEquals(tree.contains(78), 3);
		assertEquals(tree.contains(45), 3);
		assertEquals(tree.contains(12), 5);
		assertEquals(tree.contains(5), 3);
		assertEquals(tree.contains(87), 4);
		assertEquals(tree.contains(17), 6);
		assertEquals(tree.contains(16), 5);
		assertEquals(tree.contains(16), 5);
		assertEquals(tree.contains(24), -1);
		assertEquals(tree.contains(67), 5);
		assertEquals(tree.contains(73), 5);
		assertEquals(tree.contains(27), 5);
		assertEquals(tree.contains(89), 2);
		assertEquals(tree.contains(2), 4);
		assertEquals(tree.contains(48), 4);
		assertEquals(tree.contains(73), 5);
		assertEquals(tree.contains(78), 3);
		assertEquals(tree.contains(56), 3);
		assertEquals(tree.contains(30), 4);
		assertEquals(tree.contains(87), 4);
		assertEquals(tree.contains(51), 0);
		assertEquals(tree.contains(95), 5);
		assertEquals(tree.contains(40), 2);
		assertEquals(tree.contains(42), 4);
		assertEquals(tree.contains(31), 5);
		assertEquals(tree.contains(11), 2);
		assertEquals(tree.contains(13), 4);
		assertEquals(tree.contains(90), 4);
		assertEquals(tree.contains(6), 5);
		assertEquals(tree.contains(97), 5);
		assertEquals(tree.contains(60), 5);
		assertEquals(tree.contains(46), 5);
		assertEquals(tree.contains(76), 5);
		assertEquals(tree.contains(10), 4);
		assertEquals(tree.contains(78), 3);
		assertEquals(tree.contains(16), 5);
		assertEquals(tree.contains(81), 5);
		assertEquals(tree.contains(13), 4);
		assertEquals(tree.contains(2), 4);
		assertEquals(tree.contains(96), 4);
		assertEquals(tree.contains(74), 6);
		assertEquals(tree.contains(9), -1);
		assertEquals(tree.contains(15), 3);
		assertEquals(tree.contains(39), 4);
		assertEquals(tree.contains(10), 4);
		assertEquals(tree.contains(59), 6);
		assertEquals(tree.contains(51), 0);
		assertEquals(tree.contains(79), 4);
		assertEquals(tree.contains(4), -1);
		assertEquals(tree.contains(48), 4);
		assertEquals(tree.contains(95), 5);
		assertEquals(tree.contains(27), 5);
		assertEquals(tree.contains(96), 4);
		assertEquals(tree.contains(39), 4);
		assertEquals(tree.contains(83), 4);
		assertEquals(tree.contains(97), 5);
		assertEquals(tree.contains(78), 3);
		assertEquals(tree.contains(91), 3);
		assertEquals(tree.contains(14), 5);
		assertEquals(tree.contains(55), 4);
		assertEquals(tree.contains(37), -1);
		assertEquals(tree.contains(62), 6);
		assertEquals(tree.contains(25), 4);
		assertEquals(tree.contains(70), 6);
		assertEquals(tree.contains(25), 4);
		Iterator<Integer> it = tree.iterator();
		assertTrue(it.hasNext());
		while(it.hasNext()) {
			it.next();
		}
		assertFalse(it.hasNext());
	}
	
	@Test
	public void test31() {
		assertEquals(AvlTree.findMinNodes(0), 1);
		assertEquals(AvlTree.findMinNodes(1), 2);
		assertEquals(AvlTree.findMinNodes(2), 4);
		assertEquals(AvlTree.findMinNodes(3), 7);
	}
	**/
	public AvlTree crTree() {
		AvlTree tree = new AvlTree();
		Random random = new Random();
		int number;
		ArrayList<Integer> arr = new ArrayList<Integer>(10000000);
		for (int i = 0; i <= 10000; i++) {
			number = random.nextInt(1000000);
			if (!arr.contains(number)) {
				tree.add(number);
				arr.add(number);
			}

		}

		int counter = 0;
		for (int num : arr) {
			counter++;
			if (counter == 2000)
				break;

			tree.delete(num);

		}
		return tree;
	}


	public boolean checkIfnotAVL(Node node) {
		boolean a = false;
		boolean b = false;
		if (node.getFamily(Node.LEFT) != null)
			a = checkIfnotAVL(node.getFamily(Node.LEFT));
		if (node.getFamily(Node.RIGHT) != null)
			b = checkIfnotAVL(node.getFamily(Node.RIGHT));
		if (Math.abs(node.getFamily(Node.LEFT).getHeight() - 
				node.getFamily(Node.RIGHT).getHeight()) >= 2) {
			System.out.println("ERRORR");
			return true;
		}
		return a && b;
	}
	/**
	@Test
	public void testRandom(){
		AvlTree tree=crTree();
		assertFalse(checkIfnotAVL(tree.getRoot()));
	}
	
	**/
	@Test
	public void test33() {

		AvlTree tree = new AvlTree();
		int[] li = {1,2,3,4,5,6,7,8,9};
		for(int i : li) {
			tree.add(i);
		}
		int j = 0,i;
		Iterator<Integer> iterator;
		for (iterator = tree.iterator(); iterator
				.hasNext();) {
			i = iterator.next();
			assertEquals(li[j], i);
			
			j++;
		}

//		Iterator<Integer> iterator= tree.iterator();
//		while(iterator.hasNext()) {
//			int i = iterator.next();
//			assertEquals(li[j], i);
//			
//			j++;
//		}
		assertFalse(iterator.hasNext());
		
		iterator = tree.iterator();
		tree.add(10);
		tree.add(100);
		tree.delete(1);
		assertEquals((int)iterator.next(), 2);
		tree.delete(3);
		tree.delete(4);
		assertEquals((int)iterator.next(), 5);
		assertEquals((int)iterator.next(), 6);
		tree.delete(7);
		tree.delete(8);
		tree.delete(9);
		assertEquals((int)iterator.next(), 10);
		assertEquals((int)iterator.next(), 100);
		tree.add(101);
		tree.add(102);
		tree.delete(102);
		tree.delete(101);
		assertFalse(iterator.hasNext());
	}
	
	@Test
	public void test34() {
		AvlTree tree = new AvlTree();
		Iterator<Integer> iterator;
		iterator = tree.iterator();
		assertFalse(iterator.hasNext());
		try {
			iterator.next();
		}
		catch (Exception e) {
			assertEquals(e.getClass().getSimpleName(), "NoSuchElementException");
		}
		tree.add(1);
		tree.delete(1);
		iterator = tree.iterator();
		assertFalse(iterator.hasNext());
		try {
			iterator.next();
		}
		catch (Exception e) {
			assertEquals(e.getClass().getSimpleName(), "NoSuchElementException");
		}
	}
	
	@Test
	public void test35() {
		AvlTree tree = new AvlTree();
		tree.add(1);
		tree.add(2);
		tree.add(3);
		assertEquals(tree.getRoot().getHeight(), 2);
		assertEquals(tree.getRoot().getKey(), 2);
		assertEquals(tree.getRoot().getFamily(Node.LEFT).getHeight(), 1);
		assertEquals(tree.getRoot().getFamily(Node.LEFT).getKey(), 1);
		assertEquals(tree.getRoot().getFamily(Node.LEFT).getFamily(Node.FATHER).getKey(), 2);
		assertEquals(tree.getRoot().getFamily(Node.RIGHT).getHeight(), 1);
		assertEquals(tree.getRoot().getFamily(Node.RIGHT).getKey(), 3);
		assertEquals(tree.getRoot().getFamily(Node.RIGHT).getFamily(Node.FATHER).getKey(), 2);
	}
	
	@Test
	public void test36() {
		AvlTree tree = new AvlTree();
		tree.add(3);
		tree.add(2);
		tree.add(1);
		assertEquals(tree.getRoot().getHeight(), 1);
		assertEquals(tree.getRoot().getKey(), 2);
		assertEquals(tree.getRoot().getFamily(Node.LEFT).getHeight(), 0);
		assertEquals(tree.getRoot().getFamily(Node.LEFT).getKey(), 1);
		assertEquals(tree.getRoot().getFamily(Node.LEFT).getFamily(Node.FATHER).getKey(), 2);
		assertEquals(tree.getRoot().getFamily(Node.RIGHT).getHeight(), 0);
		assertEquals(tree.getRoot().getFamily(Node.RIGHT).getKey(), 3);
		assertEquals(tree.getRoot().getFamily(Node.RIGHT).getFamily(Node.FATHER).getKey(), 2);
	}
	
	@Test
	public void test37() {
		AvlTree tree = new AvlTree();
		tree.add(3);
		tree.add(1);
		tree.add(2);
		assertEquals(tree.getRoot().getHeight(), 1);
		assertEquals(tree.getRoot().getKey(), 2);
		assertEquals(tree.getRoot().getFamily(Node.LEFT).getHeight(), 0);
		assertEquals(tree.getRoot().getFamily(Node.LEFT).getKey(), 1);
		assertEquals(tree.getRoot().getFamily(Node.LEFT).getFamily(Node.FATHER).getKey(), 2);
		assertEquals(tree.getRoot().getFamily(Node.RIGHT).getHeight(), 0);
		assertEquals(tree.getRoot().getFamily(Node.RIGHT).getKey(), 3);
		assertEquals(tree.getRoot().getFamily(Node.RIGHT).getFamily(Node.FATHER).getKey(), 2);
	}
	
	@Test
	public void test38() {
		AvlTree tree = new AvlTree();
		tree.add(1);
		tree.add(3);
		tree.add(2);
		assertEquals(tree.getRoot().getHeight(), 1);
		assertEquals(tree.getRoot().getKey(), 2);
		assertEquals(tree.getRoot().getFamily(Node.LEFT).getHeight(), 0);
		assertEquals(tree.getRoot().getFamily(Node.LEFT).getKey(), 1);
		assertEquals(tree.getRoot().getFamily(Node.LEFT).getFamily(Node.FATHER).getKey(), 2);
		assertEquals(tree.getRoot().getFamily(Node.RIGHT).getHeight(), 0);
		assertEquals(tree.getRoot().getFamily(Node.RIGHT).getKey(), 3);
		assertEquals(tree.getRoot().getFamily(Node.RIGHT).getFamily(Node.FATHER).getKey(), 2);
	}
	
	@Test
	public void test39() {
		AvlTree tree = new AvlTree();
		tree.add(10);
		tree.add(1);
		Iterator iterator = tree.iterator();
		assertEquals(iterator.next(), 1);
		assertEquals(iterator.next(), 10);
	}
}


public class Tester {

	public static void main(String[] args) {
		String ssss = "hash Hash guitar piano violin DAST CLIDS Infi Hello Bye Test Algebra Boo DAST CLIDS Shalom Kushkush hello hi naal DAST CLIDS oink";
		String[] ccc = ssss.split(" ");
		OpenHashSet ohs = new OpenHashSet();
		
		for(int i =0; i< ccc.length; i++){
			ohs.add(ccc[i]);
		}

//		boolean bo = ohs.add("Hi");
//		System.out.println(bo);
//		bo = ohs.add("Hello");
//		System.out.println(bo);
//		bo = ohs.add("Bye");
//		System.out.println(bo);

		int size = ohs.size();
		System.out.println(size);
		
		
		
//		int cap = ohs.capacity();
//		System.out.println(cap);
	}

}

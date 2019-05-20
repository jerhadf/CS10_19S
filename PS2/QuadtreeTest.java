public class QuadtreeTest {
	public static void main(String[] args) { 
		Dot dotA = new Dot(50, 50); 
		
		PointQuadtree<Dot> sample1 = new PointQuadtree<Dot>(dotA, 0, 100, 100, 0); 
		
		sample1.insert(new Dot(75, 25)); // quadrant 1
		sample1.insert(new Dot(25, 25)); // quadrant 2 
		sample1.insert(new Dot(25, 75)); // quadrant 3
		sample1.insert(new Dot(75, 75)); // quadrant 4
		
		// these points are within circle (75, 75, 5) 
		sample1.insert(new Dot(76, 77));
		sample1.insert(new Dot(77, 76));
		sample1.insert(new Dot(77, 78));
		sample1.insert(new Dot(80, 75));
		sample1.insert(new Dot(70, 75));
		sample1.insert(new Dot(74, 70));
		
		// these points are outside circle (75, 75, 5)
		sample1.insert(new Dot(90, 43));
		sample1.insert(new Dot(34, 48));
		
		System.out.println(sample1.allPoints()); 
		System.out.println(sample1.size()); 
		System.out.println(sample1.findInCircle(75, 75, 100)); 
	}
	
	public static void printAll(PointQuadtree<Dot> tree) { 
		System.out.println(tree.getPoint()); 
		if(tree.hasChild(1)) { 
			System.out.println("has child 1"); 
			printAll(tree.getChild(1)); 
		}
		if(tree.hasChild(2)) { 
			System.out.println("has child 2"); 
			printAll(tree.getChild(2)); 
		}
		if(tree.hasChild(3)) { 
			System.out.println("has child 3"); 
			printAll(tree.getChild(3)); 
		}
		if(tree.hasChild(4)) { 
			System.out.println("has child 4");
			printAll(tree.getChild(4)); 
		}
	}
}

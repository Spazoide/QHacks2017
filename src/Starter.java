public class Starter {
	public static void main(String[] args) {
		
		TrackerInstance a = new TrackerInstance();
		Thread t = new Thread(a);
		t.start();
		
		
	}
}

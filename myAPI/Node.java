package scripts.myAPI;

public abstract class Node {

	public Node() {}
	
	public abstract boolean checkTask() throws InterruptedException;
	
	public abstract void doTask() throws InterruptedException;

	public abstract String status() throws InterruptedException;
		
}

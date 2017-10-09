package groups;

public class Group {
	
	private int id;
	
	public Group(){
		id = Groups.getFreeId();
		Groups.addGroup(this);
	}
	
	public int getId(){
		return id;
	}

}

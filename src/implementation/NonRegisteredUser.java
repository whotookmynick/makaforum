package implementation;

public class NonRegisteredUser extends domainLayer.userData{
	protected String _userName;
	
	public NonRegisteredUser(){
		_userName = "Guest";
	}

	public String get_userName() {
		return _userName;
	}

	public void set_userName(String name) {
		_userName = name;
	}
}

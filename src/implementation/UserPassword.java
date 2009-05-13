package implementation;

public class UserPassword {

	protected long _userID;
	protected String _password;
	
	public UserPassword(long userID,String password){
		_userID = userID;
		_password = password;
	}
	
	public long get_userID() {
		return _userID;
	}
	public void set_userID(long _userid) {
		_userID = _userid;
	}
	public String get_password() {
		return _password;
	}
	public void set_password(String _password) {
		this._password = _password;
	}
}

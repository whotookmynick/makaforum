package implementation;

public class RegisteredUser{ //extends NonRegisteredUser {
	//protected String _password;
	protected long _lastLogInTime;
	protected long _signUpTime;
	protected int _numOfMessages;
	protected long _uID;
	protected String _userName;

	/**
	 * @author moti and roee
	 */
	protected int _userType;		//  -1 = nonRegistered
									//   0 = member
									//	 1 = moderator
									//	 2 = administrator

	public RegisteredUser(String userName,long ID){
		super();
		_userName = userName;
		//_password = password;
		_numOfMessages = 0;
		_uID = ID;
		_signUpTime = System.currentTimeMillis();
	}

	public long get_lastLogInTime() {
		return _lastLogInTime;
	}

	public void set_lastLogInTime(long logInTime) {
		_lastLogInTime = logInTime;
	}

	public long get_signUpTime() {
		return _signUpTime;
	}

	public void set_signUpTime(long upTime) {
		_signUpTime = upTime;
	}

	public int get_numOfMessages() {
		return _numOfMessages;
	}

	public void set_numOfMessages(int ofMessages) {
		_numOfMessages = ofMessages;
	}

	public long get_uID() {
		return _uID;
	}

	public void set_uID(long _uid) {
		_uID = _uid;
	}

	public String get_userName() {
		return _userName;
	}

	public void set_userName(String name) {
		_userName = name;
	}

	/**
	 * @author moti and roee
	 * @param type
	 */
	private void setUserType(int type){
		_userType = type;
	}

	/**
	 * @author moti and roee
	 */
	public void setModerator(){
		setUserType(1);
	}

	/**
	 * @author moti and roee
	 */
	public void setMember(){
		setUserType(0);
	}

	/**
	 * @author moti and roee
	 */
	public int getUserType(){
		return _userType;
	}
}

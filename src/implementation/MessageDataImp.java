package implementation;

import org.compass.annotations.*;

import domainLayer.MessageData;

@Searchable (root = false)
public class MessageDataImp implements MessageData {
	
	@SearchableId (name = "messagedataID")
	protected long _mID;
	@SearchableProperty (name = "contentMessageDataImp")
	protected String _msgElement;
	
	/**
	 * This constructor exists for the compass to work with
	 */
	private MessageDataImp(){
		
	}
	
	public MessageDataImp(String msgElement){
		_msgElement = msgElement;
	}
	
	public String toString(){
		return _msgElement;
	}
	
	public String get_msgElement() {
		return _msgElement;
	}

	public void set_msgElement(String element) {
		_msgElement = element;
	}

	@Override
	public Object displayMessageData() {
		return this.toString();
	}

	@Override
	public String textToDisplay() {
		return this.toString();
	}
	
	public void setMid(long mid)
	{
		_mID = mid;
	}
	
	public long getMid()
	{
		return _mID;
	}
}

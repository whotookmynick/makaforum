package implementation;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;

import domainLayer.MessageData;

@Searchable
public class MessageDataImp implements MessageData {
	@SearchableProperty (name = "contentMessageDataImp")
	protected String _msgElement;
	@SearchableId
	protected long _mID;
	
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
}

package implementation;

import domainLayer.MessageData;

public class MessageDataImp implements MessageData {
	protected String _msgElement;
	
	public String toString(){
		return _msgElement;
	}
	
	public MessageDataImp(String msgElement){
		_msgElement = msgElement;
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
}

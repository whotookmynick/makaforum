package implementation;

import domainLayer.MessageData;
import org.compass.annotations.*;
/**
 * This class is mainly meant to help right things to the persistence system
 * @author aradno
 *
 */
@Searchable (alias = "message")
@SearchConverter (name = "messagedataimp", type=MessageDataImpConverter.class)
public class Message {
	@SearchableProperty
	@SearchableMetaData (name = "content", converter = "messagedataimp")
	protected MessageDataImp _msgBody;
	@SearchableProperty (name = "time")
	protected long _msgPostTime;
	/**
	 * This field is a reference to the _uid field in RegisteredUser
	 */
	@SearchableProperty (name = "author")
	protected long _msgPosterID;

	protected long _fatherMessageID;	// -1  for a new message
	@SearchableId (name = "MID")
	protected long _mID;
	
//	@SearchableProperty (name = "content")
//	protected String _messageContent;

	/**
	 * This constructor exists just for the Compass search engine to use.
	 */
	private Message(){
	}
	
	public Message(MessageData msgBody,long msgposterID,long fatherID,long mID){
		_msgBody = (MessageDataImp)msgBody;
		_msgPosterID = msgposterID;
		_fatherMessageID = fatherID;
		_mID = mID;
		msgBody.setMid(_mID);
		_msgPostTime = System.currentTimeMillis();
	}

	/**
	 * @author Roee
	 * @param msgBody
	 * @param msgposterID
	 * @param fatherID
	 * @param mID
	 */
	public Message(MessageData msgBody,long msgposterID,long mID){
		this(msgBody,msgposterID,-1,mID);
	}

	public MessageData get_msgBody() {
		return _msgBody;
	}

	public void set_msgBody(MessageData body) {
		_msgBody = (MessageDataImp)body;
	}

	public long get_msgPostTime() {
		return _msgPostTime;
	}

	public void set_msgPostTime(long postTime) {
		_msgPostTime = postTime;
	}

	public long get_msgPosterID() {
		return _msgPosterID;
	}

	public void set_msgPosterID(long posterID) {
		_msgPosterID = posterID;
	}

	public long get_fatherMessageID() {
		return _fatherMessageID;
	}

	public void set_fatherMessageID(long messageID) {
		_fatherMessageID = messageID;
	}

	public long get_mID() {
		return _mID;
	}

	public void set_mID(long _mid) {
		_mID = _mid;
	}

}

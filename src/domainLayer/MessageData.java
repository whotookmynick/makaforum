package domainLayer;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;

/**
 * This interface defines the Message data which does not have to be a String.
 * @author aradno
 *
 */
@Searchable (root = true)
public interface MessageData {

	/**
	 * This method is meant to be used to return all of the textual data that the message
	 * Contains.
	 * @return
	 */
	//@SearchableProperty (name = "content")
	public String textToDisplay();
	
	/**
	 * This method will display the message data fully with all addendas.
	 * @return
	 */
	public Object displayMessageData();
	
	public void setMid(long mid);
}

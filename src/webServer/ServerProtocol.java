package webServer;

/**
 * This interface defines what the server will do for each message it receives.
 * @author aradno
 *
 */
interface ServerProtocol {

	/**
	 * This is what the server will run for each incoming message received.
	 * @param msg
	 * @return
	 */
	String processMessage(String msg);

	/**
	 * Tests if the message received is the end of all the messages and that means
	 * you need to close the connection.
	 * @param msg
	 * @return
	 */
	boolean isEnd(String msg);

}

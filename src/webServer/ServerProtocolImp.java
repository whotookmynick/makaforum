package webServer;

import domainLayer.TheController;

public class ServerProtocolImp implements ServerProtocol {

	TheController _controller;
	
	public ServerProtocolImp(TheController controller){
		_controller = controller;
	}
	
	@Override
	public boolean isEnd(String msg) {
		boolean ans;
		ans = msg.contentEquals("bye");
		return ans;
	}

	/**
	 * NOT YET IMPLEMENTED
	 */
	@Override
	public String processMessage(String msg) {
		// TODO Auto-generated method stub
		return null;
	}

}

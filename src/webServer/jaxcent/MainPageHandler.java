package webServer.jaxcent;

import webServer.ServerProtocolImp;
import implementation.ControlerFactory;
import domainLayer.TheController;

import jaxcent.*;

public class MainPageHandler extends jaxcent.JaxcentPage{

	ServerProtocolImp _protocolHandler;
	HtmlTable _messageTable;
	
	public MainPageHandler() {
		TheController controller = ControlerFactory.getControler();
		_protocolHandler = new ServerProtocolImp(controller);
		String []tempRow = {"1","2","Test message","time"};
		_messageTable = new HtmlTable(this, "messageTable");
		try {
//			_messageTable.deleteAllRows();
			//_messageTable.insertRow(-1, tempRow);
			String getAllMessagesString = "display -1";
			String []messages = {_protocolHandler.processMessage(getAllMessagesString)};
			_messageTable.insertRow(-1, messages);
		} catch (Jaxception e) {
			e.printStackTrace();
		}
	}
}

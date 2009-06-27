package webServer.jaxcent;

import java.util.Arrays;
import java.util.Map;

import org.w3c.dom.html.HTMLElement;

import UI.TUI;
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
		HtmlInputSubmit loginButton = new HtmlInputSubmit(this,"loginSubmit"){
			protected void onClick(Map pageData)
			{
				loginClicked();
			}
		};
		HtmlInputSubmit registerButton = new HtmlInputSubmit(this,"registerSubmit"){
			protected void onClick(Map pageData)
			{
				registerClicked();
			}
		};
		HtmlInputSubmit newMessageButton = new HtmlInputSubmit(this,"newMessageSubmit"){
			protected void onClick(Map pageData)
			{
				addMessageClicked();
			}
		};
		_messageTable = new HtmlTable(this, "messageTable"){
			protected void onRowDeleted(int rowIndex){
				deleteRowInMessageTable(rowIndex);
			}
			protected void onCellEdited(int rowIndex,int colIndex,String oldContent,String newContent){
				editCellInMessageTable(rowIndex, colIndex, oldContent, newContent);
			}
		};
		initTable(true);
	}



	protected void initTable(boolean isOnLoad) {
		try {
			//			_messageTable.deleteAllRows();
			//_messageTable.insertRow(-1, tempRow);
			if (!isOnLoad)
			{
				_messageTable.deleteFromBottom(_messageTable.getNumRows()-1);
			}
			String getAllMessagesString = "display -1";
			String messages = _protocolHandler.processMessage(getAllMessagesString);
			String []seperated = messages.split("\n");
			for (int i = 0; i < seperated.length-1; i++)
			{
				String []currRow = TUI.parseIncomingReceivedMessages(seperated[i]);
				final String msgID = currRow[0];
				String []currRowWithReply = Arrays.copyOf(currRow, currRow.length+1);
				currRowWithReply[currRowWithReply.length-1] = "<a " + "\" id=\"reply" + msgID +"\" href=\"\">reply</a>";
				_messageTable.insertRow(-1, currRowWithReply);
				HtmlElement replyElement = new HtmlElement(this,"reply" + currRow[0])
				{
					protected void onClick()
					{
						replyTo(msgID);
					}
				};
				if (isOnLoad)
				{
					_messageTable.addDeleteButtons(1, -1, "delete", null);
				}
				_messageTable.enableCellEditing(1, 2, -1, 2, true, false, null);
			}
		} catch (Jaxception e) {
			e.printStackTrace();
		}
	}

	
	
	protected void deleteRowInMessageTable(int rowIndex)
	{
		try {
			String msgIDString = _messageTable.getCellContent(rowIndex, 0);
			String answer = _protocolHandler.processMessage("delete "+msgIDString);
			updateStatus(formatServerAnswer(answer));
			
		} catch (Jaxception e) {
			e.printStackTrace();
		}
	}
	
	protected void editCellInMessageTable(int rowIndex,int colIndex,String oldContent,String newContent)
	{
		String msgIDString;
		try {
			msgIDString = _messageTable.getCellContent(rowIndex, 0);
			String answer = _protocolHandler.processMessage("edit " + newContent +" " + msgIDString);
			updateStatus(formatServerAnswer(answer));
		} catch (Jaxception e) {
			e.printStackTrace();
		}

	}
	
	protected void loginClicked()
	{
		try {
			String result = handleRegisterAndLogin("login");
			if (result.indexOf("succesfully") >= 0)
			{
				HtmlInputSubmit loginButton = new HtmlInputSubmit(this,"loginSubmit");
				HtmlInputSubmit registerButton = new HtmlInputSubmit(this,"registerSubmit");
				HtmlInputText userNameInput = new HtmlInputText(this,"usernamelogin");
				HtmlInputPassword userPasswordInput = new HtmlInputPassword(this,"userpasswordlogin");
				loginButton.setDisabled(true);
				registerButton.setDisabled(true);
				userNameInput.setDisabled(true);
				userPasswordInput.setDisabled(true);
			}
		} catch (Jaxception e) {
			e.printStackTrace();
		}
	}
	
	protected void registerClicked()
	{
		handleRegisterAndLogin("register");
	}
	
	protected String handleRegisterAndLogin(String actionName)
	{
		try {
			HtmlInputText userNameInput = new HtmlInputText(this,"usernamelogin");
			HtmlInputPassword userPasswordInput = new HtmlInputPassword(this,"userpasswordlogin");
			String userName = userNameInput.getValue();
			String pass = userPasswordInput.getValue();
			String answer = _protocolHandler.processMessage(actionName + " " + userName +" " + pass);
			answer = formatServerAnswer(answer);
			updateStatus(answer);
			return answer;
			
		} catch (Jaxception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected void addMessageClicked()
	{
		try {
			HtmlTextArea newMessageArea = new HtmlTextArea(this,"newMessageArea");
			String msgContent = newMessageArea.getInnerText();
			String answer = _protocolHandler.processMessage("message " + msgContent);
			newMessageArea.setValue("");
			initTable(false);
			updateStatus(formatServerAnswer(answer));
		} catch (Jaxception e) {
			e.printStackTrace();
		}
	}
	
	protected void replyTo(String msgID)
	{
		System.out.println("replied");
		//TODO complete method reply 
	}
	
	protected void updateStatus(String newStatus)
	{
		try {
			HtmlElement statusElement = new HtmlElement(this,"status");
			statusElement.setInnerHTML(newStatus);
		} catch (Jaxception e) {
			e.printStackTrace();
		}
	}
	protected String formatServerAnswer(String serverAnswer)
	{
		serverAnswer = serverAnswer.substring(0,serverAnswer.length()-3);
		//System.out.println(repliedMessage);
		String displayString = serverAnswer.substring("print".length()+1);
		return displayString;
	}

}

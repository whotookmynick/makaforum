package webServer.jaxcent;

import java.util.Arrays;
import java.util.Map;
import java.util.Vector;

import UI.TUI;
import webServer.ServerProtocolImp;
import implementation.ControlerFactory;
import domainLayer.TheController;

import jaxcent.*;

public class MainPageHandler extends jaxcent.JaxcentPage{

	ServerProtocolImp _protocolHandler;
	HtmlTable _messageTable;
	Vector<String> _msgIDs;
	Vector<String> _siteMap;

	public MainPageHandler() {
		TheController controller = ControlerFactory.getControler();
		_protocolHandler = new ServerProtocolImp(controller);
		_siteMap = new Vector<String>();
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
		_msgIDs = new Vector<String>();
		_messageTable = new HtmlTable(this, "messageTable"){
			protected void onRowDeleted(int rowIndex){
				deleteRowInMessageTable(rowIndex);
			}
			protected void onCellEdited(int rowIndex,int colIndex,String oldContent,String newContent){
				editCellInMessageTable(rowIndex, colIndex, oldContent, newContent);
			}
		};
		_siteMap.add("-1");
		initTable(true,"-1");
	}



	protected void initTable(boolean isOnLoad,String fatherMsgID) {
		try {
			//			_messageTable.deleteAllRows();
			//_messageTable.insertRow(-1, tempRow);
			_msgIDs.clear();
			if (!isOnLoad)
			{
				_messageTable.deleteFromBottom(_messageTable.getNumRows()-1);
			}
			String getAllMessagesString = "display " + fatherMsgID;
			String messages = _protocolHandler.processMessage(getAllMessagesString);
			String []seperated = messages.split("\n");
			for (int i = 0; i < seperated.length-1; i++)
			{
				String []currRow = TUI.parseIncomingReceivedMessages(seperated[i]);
				final String msgID = currRow[0];
				currRow[0] = "<a href=\"\" id=\"msglink"+msgID+"\">" + currRow[0] + "</a>";
				_msgIDs.add(msgID);
				String []currRowWithReply = Arrays.copyOf(currRow, currRow.length+1);
				currRowWithReply[currRowWithReply.length-1] = "<a " + "\" id=\"reply" + msgID +"\" href=\"\">reply</a>";
				_messageTable.insertRow(-1, currRowWithReply);
				final int rowNum = i;
				HtmlElement replyElement = new HtmlElement(this,"reply" + msgID)
				{
					protected void onClick()
					{
						replyTo(msgID,rowNum);
					}
				};
				HtmlElement msgElement = new HtmlElement(this, "msglink" + msgID)
				{
					protected void onClick()
					{
						msgLinkClicked(msgID);
					}
				};
			}
			_messageTable.addDeleteButtons(1, -1, "delete", null);
			_messageTable.enableCellEditing(1, 2, -1, 2, true, false, null);
			initSiteMap();
		} catch (Jaxception e) {
			e.printStackTrace();
		}
	}

	
	
	protected void deleteRowInMessageTable(int rowIndex)
	{
		String msgIDString = _msgIDs.get(rowIndex-1);
		_msgIDs.remove(rowIndex-1);
		String answer = _protocolHandler.processMessage("delete "+msgIDString);
		updateStatus(formatServerAnswer(answer));
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
			initTable(false,"-1");
			updateStatus(formatServerAnswer(answer));
		} catch (Jaxception e) {
			e.printStackTrace();
		}
	}
	
	protected void replyTo(final String msgID,int rowIndex)
	{
		try{
//		System.out.println("replied");
		String replyCell = 
			"<tr>" +
			"<td>" +
				"<textarea id=\"replyMessageArea" + msgID +"\" +" +
				" rows=\"4\" cols=\"40\">Reply To Message</textarea>" +
				"<br><input type=\"submit\""+
							"id=\"replySubmit"+ msgID +"\" value=\"Submit\">" +
				"</td></tr>";
		String []rowToInsert = {replyCell};
		_messageTable.insertRow(rowIndex+2, rowToInsert);
		HtmlInputSubmit replySubmit = new HtmlInputSubmit(this, "replySubmit"+msgID){
			protected void onClick()
			{
				replySubmitClicked(msgID);
			}
		};
		}
		catch (Jaxception e) {
			e.printStackTrace();
		}
	}
	
	protected void replySubmitClicked(String msgID)
	{
		try {
			HtmlTextArea replyTextArea = new HtmlTextArea(this, "replyMessageArea"+msgID);
			String actionString = "reply " + replyTextArea.getValue() +" " + msgID;
			String answer = _protocolHandler.processMessage(actionString);
			updateStatus(formatServerAnswer(answer));
			initTable(false,"-1");
		} catch (Jaxception e) {
			e.printStackTrace();
		}
	}
	
	protected void msgLinkClicked(String msgID){
		_siteMap.add(msgID);
		initTable(false,msgID);
	}
	
	protected void initSiteMap()
	{
		try {
			HtmlElement siteMap = new HtmlElement(this, "sitemap");
//			siteMap.setData("");
			String newContent = "";
			for (int i =0;i < _siteMap.size();i++){
				newContent += "<b>>></b><a href=\"\"" + 
				"id=\"sitemaplink"+i+"\">" + _siteMap.elementAt(i)+
				"</a>";
			}
			siteMap.setInnerHTML(newContent);
			for (int i = 0; i < _siteMap.size();i++)
			{
				final int currIndex = i;
				HtmlElement sitemapLink = new HtmlElement(this, "sitemaplink" + i){
					protected void onClick()
					{
						siteMapLinkClicked(currIndex);
					}
				};
			}
		} catch (Jaxception e) {
			e.printStackTrace();
		}

	}
	
	protected void siteMapLinkClicked(int index)
	{
		for (int i = index+1; i < _siteMap.size();i++)
		{
			_siteMap.remove(i);
		}
		initTable(false, _siteMap.elementAt(index));
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

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
	String _currUserID;
	int _currUserType;

	public MainPageHandler() {
		_currUserID = "";
		_currUserType = -1;
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
		_siteMap.add("home");
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
				String msgIDCellString ="<input id=\"msgIDRow"+(i+1)+
										"\" type=\"hidden\" value=\""+msgID+"\"/>"+
										"<a href=\"\" id=\"msglink"+msgID+"\">" +msgID + "</a>"; 
				currRow[0] = msgIDCellString;
				_msgIDs.add(msgID);
				String []currRowWithReply = Arrays.copyOf(currRow, currRow.length+2);
				currRowWithReply[currRowWithReply.length-2] = "<a " + "\" id=\"reply" + msgID +"\" href=\"\">reply</a>";
				currRowWithReply[currRowWithReply.length-1] = "<a " + "\" id=\"delete" + msgID +"\" href=\"\">delete</a>";
				_messageTable.insertRow(-1, currRowWithReply);
				final int rowNum = i;
				HtmlElement replyElement = new HtmlElement(this,"reply" + msgID)
				{
					protected void onClick()
					{
						replyTo(msgID,rowNum);
					}
				};
				HtmlElement deleteElement = new HtmlElement(this,"delete" + msgID)
				{
					protected void onClick()
					{
						deleteRowInMessageTable(rowNum+1);
					}
				};
				HtmlElement msgElement = new HtmlElement(this, "msglink" + msgID)
				{
					protected void onClick()
					{
						msgLinkClicked(msgID);
					}
				};
				if (_currUserType < 0 )
				{
					replyElement.setVisible(false);
				}
				if (!currRowWithReply[1].contentEquals(_currUserID) & !(_currUserType>0))
				{
					deleteElement.setVisible(false);
				}
				else
				{
					_messageTable.enableCellEditing(i+1, 2, i+1, 2, true, false, null);
				}
				
			}
//			_messageTable.addDeleteButtons(1, -1, "delete", null);
//			_messageTable.enableCellEditing(1, 2, -1, 2, true, false, null);
			initSiteMap();
		} catch (Jaxception e) {
			e.printStackTrace();
		}
	}

	
	
	protected void deleteRowInMessageTable(int rowIndex)
	{
		try {
			String msgIDString = _msgIDs.get(rowIndex-1);
			_msgIDs.remove(rowIndex-1);
			String answer = _protocolHandler.processMessage("delete "+msgIDString);
			_messageTable.deleteRow(rowIndex);
			updateStatus(formatServerAnswer(answer));
		} catch (Jaxception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void editCellInMessageTable(int rowIndex,int colIndex,String oldContent,String newContent)
	{
		String msgIDString;
		try {
			HtmlInputHidden msgIDInput = new HtmlInputHidden(this,"msgIDRow"+rowIndex);
			msgIDString = msgIDInput.getValue();
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
				HtmlDiv addMessageDiv = new HtmlDiv(this,"addMessageDiv");
				HtmlDiv moderatordiv = new HtmlDiv(this,"moderatordiv");
				HtmlInputSubmit adminSubmit = new HtmlInputSubmit(this,"adminSubmit");
				loginButton.setDisabled(true);
				registerButton.setDisabled(true);
				userNameInput.setDisabled(true);
				userPasswordInput.setDisabled(true);
				addMessageDiv.setVisible(true);
				_currUserID = _protocolHandler.get_connectedUser().get_uID() + "";
				_currUserType = _protocolHandler.get_connectedUser().get_userType();
				if (_currUserType > 0)
				{
					moderatordiv.setVisible(true);
				}
				if (_currUserType > 1)
				{
					adminSubmit.setVisible(true);
				}
				siteMapLinkClicked(_siteMap.size()-1);
//				initTable(false, _siteMap.get(_siteMap.size()-1));
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
//			String msgContent = newMessageArea.getInnerText();
			String msgContent = newMessageArea.getValue();
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
			String newContent = "<b>>></b><a href=\"\" id=\"sitemaplinkhome\">Home</a>";
			for (int i =1;i < _siteMap.size();i++){
				newContent += "<b>>></b><a href=\"\"" + 
				"id=\"sitemaplink"+i+"\">" + _siteMap.elementAt(i)+
				"</a>";
			}
			siteMap.setInnerHTML(newContent);
			HtmlElement homeLink = new HtmlElement(this, "sitemaplinkhome"){
				protected void onClick()
				{
					siteMapLinkClicked(0);
				}
			};
			for (int i = 1; i < _siteMap.size();i++)
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
		if (index == 0)
		{
			initTable(false, "-1");
			return;
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

package webServer.jaxcent;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.Vector;

import UI.TUI;
import UI.UIObserver;
import webServer.ServerProtocolImp;
import implementation.ControlerFactory;
import implementation.RegisteredUser;
import domainLayer.TheController;

import jaxcent.*;

public class MainPageHandler extends jaxcent.JaxcentPage implements UIObserver{

	ServerProtocolImp _protocolHandler;
	HtmlTable _messageTable;
	HtmlTable _searchTable;
	Vector<String> _msgIDs;
	Vector<String> _siteMap;
	String _currUserID;
	int _currUserType;
	Calendar _loadTime;

	public MainPageHandler() {
		_currUserID = "";
		_currUserType = -1;
		TheController controller = ControlerFactory.getControler();
		_protocolHandler = new ServerProtocolImp(controller);
		controller.registerObserver(this);
		_siteMap = new Vector<String>();
		//HtmlInputSubmit loginButton = new HtmlInputSubmit(this,"loginSubmit"){
		HtmlInputButton loginButton = new HtmlInputButton(this,"loginSubmit"){
			protected void onClick(Map pageData)
			{
//				loginClicked();
				try {
					decideLoginButton(this.getValue());
				} catch (Jaxception e) {
					e.printStackTrace();
				}
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
		HtmlInputSubmit moderatorButton = new HtmlInputSubmit(this, "moderatorSubmit")
		{
			protected void onClick(Map pageData)
			{
				moderatorClicked();
			}
		};
		HtmlInputSubmit adminButton = new HtmlInputSubmit(this, "adminSubmit")
		{
			protected void onClick(Map pageData)
			{
				adminClicked();
			}
		};
		HtmlInputRadio dateRadio = new HtmlInputRadio(this, "searchTypeDate")
		{
			protected void onClick(String value)
			{
				try {
					dateRadioOnClick(value);
					this.setChecked(true);
				} catch (Jaxception e) {
					e.printStackTrace();
				}
			}
		};
		HtmlInputRadio contentRadio = new HtmlInputRadio(this, "searchTypeContent")
		{
			protected void onClick(String value)
			{
				try {
					dateRadioOnClick(value);
					this.setChecked(true);
				} catch (Jaxception e) {
					e.printStackTrace();
				}
			}
		};
		HtmlInputRadio authorRadio = new HtmlInputRadio(this, "searchTypeAuthor")
		{
			protected void onClick(String value)
			{
				try {
					dateRadioOnClick(value);
					this.setChecked(true);
				} catch (Jaxception e) {
					e.printStackTrace();
				}
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
		/********************* SHOULD I ADD DELETE AND/OR EDIT CAPABILITY TO SEARCH TABLE?*/		
		_searchTable = new HtmlTable(this, "searchTable");
		_siteMap.add("home");
		_loadTime = Calendar.getInstance();
		initTable(true,"-1");
	}

	protected void onUnload()
	{
		System.out.println(_currUserID + " is unloading now");
		TheController controller = ControlerFactory.getControler();
		controller.unregisterObserver(this);
		Calendar endCal = Calendar.getInstance();
		controller.updateHoursOfConnected(_loadTime.get(Calendar.HOUR_OF_DAY),endCal.get(Calendar.HOUR_OF_DAY));
	}

	protected void initTable(boolean isOnLoad,String fatherMsgID) {
		try {
			//			_messageTable.deleteAllRows();
			//_messageTable.insertRow(-1, tempRow);
			_msgIDs.clear();
			_messageTable.deleteAllRows();
			//			if (!isOnLoad)
			//			{
			//				_messageTable.deleteFromBottom(_messageTable.getNumRows()-1);
			//			}
			String getAllMessagesString = "display " + fatherMsgID;
			String messages = _protocolHandler.processMessage(getAllMessagesString);
			String []seperated = messages.split("\n");
			for (int i = 0; i < seperated.length-1; i++)
			{
				String []currRow = TUI.parseIncomingReceivedMessages(seperated[i]);
				final String msgID = currRow[0];
				String msgIDCellString ="<input id=\"msgIDRow"+(i)+
				"\" type=\"hidden\" value=\""+msgID+"\"/>"+
				"<a href=\"\" id=\"msglink"+msgID+"\">" +msgID + "</a>"; 
				currRow[0] = msgIDCellString;
				_msgIDs.add(msgID);
				String []currRowWithReply = Arrays.copyOf(currRow, currRow.length+2);
				currRowWithReply[currRowWithReply.length-2] = "<a " + "\" id=\"reply" + msgID +"\" href=\"\">reply</a>";
				currRowWithReply[currRowWithReply.length-1] = "<a " + "\" id=\"delete" + msgID +"\" href=\"\">delete</a>";
				String userIDString = currRowWithReply[1];
				String userNameString = _protocolHandler.processMessage("swapIdName " + userIDString);
				userNameString = userNameString.substring("print ".length(),userNameString.length() -2);
				currRowWithReply[1] = "<input type=\"hidden\" value=\"" + userIDString + "\">" + userNameString;
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
				if (!userIDString.contentEquals(_currUserID) & !(_currUserType>0))
				{
					deleteElement.setVisible(false);
				}
				else
				{
					//_messageTable.enableCellEditing(i+1, 2, i+1, 2, true, false, null);
					_messageTable.enableCellEditing(i, 2, i, 2, true, false, null);
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

	protected void decideLoginButton(String value)
	{
		if (value.contentEquals("Login"))
		{
			loginClicked();
		}
		else
		{
			logOffClicked();
		}
	}
	
	protected void loginClicked()
	{
		try {
			String result = handleRegisterAndLogin("login");
			if (result.indexOf("succesfully") >= 0)
			{
				//HtmlInputSubmit loginButton = new HtmlInputSubmit(this,"loginSubmit");
				HtmlInputButton loginButton = new HtmlInputButton(this,"loginSubmit");
//				{
//					protected void onClick()
//					{
//						logOffClicked();
//					}
//				};
				HtmlInputSubmit registerButton = new HtmlInputSubmit(this,"registerSubmit");
				HtmlInputText userNameInput = new HtmlInputText(this,"usernamelogin");
				HtmlInputPassword userPasswordInput = new HtmlInputPassword(this,"userpasswordlogin");
				HtmlDiv addMessageDiv = new HtmlDiv(this,"addMessageDiv");
				HtmlDiv moderatordiv = new HtmlDiv(this,"moderatordiv");
				HtmlDiv editUserDiv = new HtmlDiv(this, "editUserDiv");
				HtmlInputSubmit adminSubmit = new HtmlInputSubmit(this,"adminSubmit");
				HtmlAnchor showChartsLink = new HtmlAnchor(this, "showChartsLink");
				registerButton.setDisabled(true);
				userNameInput.setDisabled(true);
				userPasswordInput.setDisabled(true);
				addMessageDiv.setVisible(true);
				loginButton.setValue("Log-Off");
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
				if (_currUserType == 1)
				{
					showChartsLink.setVisible(true);
				}
				editUserDiv.setVisible(true);
				HtmlInputSubmit editUserNameSubmit = new HtmlInputSubmit(this, "editUserSubmit")
				{
					@Override
					protected void onClick() {
						editUserNameClicked();
					}
				};
				HtmlInputSubmit editPassSubmit = new HtmlInputSubmit(this, "editPasswordSubmit")
				{
					@Override
					protected void onClick() {
						editPasswordClicked();
					}
				};
				siteMapLinkClicked(_siteMap.size()-1);
				//				initTable(false, _siteMap.get(_siteMap.size()-1));
			}
		} catch (Jaxception e) {
			e.printStackTrace();
		}
	}

	protected void logOffClicked() {
		try {
			String answer = _protocolHandler.processMessage("logoff");
			updateStatus(formatServerAnswer(answer));
			HtmlInputButton loginButton = new HtmlInputButton(this,"loginSubmit");
			HtmlInputSubmit registerButton = new HtmlInputSubmit(this,"registerSubmit");
			HtmlInputText userNameInput = new HtmlInputText(this,"usernamelogin");
			HtmlInputPassword userPasswordInput = new HtmlInputPassword(this,"userpasswordlogin");
			HtmlDiv addMessageDiv = new HtmlDiv(this,"addMessageDiv");
			HtmlDiv moderatordiv = new HtmlDiv(this,"moderatordiv");
			HtmlDiv editUserDiv = new HtmlDiv(this, "editUserDiv");
			HtmlInputSubmit adminSubmit = new HtmlInputSubmit(this,"adminSubmit");
			HtmlAnchor showChartsLink = new HtmlAnchor(this, "showChartsLink");
			registerButton.setEnabled(true);
			userNameInput.setEnabled(true);
			userPasswordInput.setEnabled(true);
			addMessageDiv.setVisible(false);
			moderatordiv.setVisible(false);
			adminSubmit.setVisible(false);
			showChartsLink.setVisible(false);
			editUserDiv.setVisible(false);
//			{
//				protected void onClick()
//				{
//					loginClicked();
//				}
//			};
			loginButton.setValue("Login");
		} catch (Jaxception e) {
			e.printStackTrace();
		}

	}

	private void editUserNameClicked() 
	{
		try {
			HtmlInputText newUserText = new HtmlInputText(this,"usernameedit");
			String newUserName = newUserText.getValue();
			String answer = _protocolHandler.processMessage("user " + newUserName);
			updateStatus(formatServerAnswer(answer));
		} catch (Jaxception e) {
			e.printStackTrace();
		}
	}

	private void editPasswordClicked()
	{
		try {
			HtmlInputText newPassText = new HtmlInputText(this,"userpasswordedit");
			String newUserName = newPassText.getValue();
			String answer = _protocolHandler.processMessage("pass " + newUserName);
			updateStatus(formatServerAnswer(answer));
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
			//initTable(false,"-1");
			siteMapLinkClicked(_siteMap.size()-1);
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
		try {
			_searchTable.setVisible(false);
			_searchTable.deleteAllRows();
			_messageTable.setVisible(true);
		} catch (Jaxception e) {
			e.printStackTrace();
		}
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

	@Override
	public void updateUI(String updateString) {
		siteMapLinkClicked(_siteMap.size()-1);
	}

	private void searchMessageClicked(Map pageData) {
		try {
			String searchType="content",searchContent="";
			HtmlInputRadio contentRadio = new HtmlInputRadio(this,"searchTypeContent");
			if (contentRadio.getChecked())
				searchType = contentRadio.getValue();
			contentRadio = new HtmlInputRadio(this,"searchTypeAuthor");
			if (contentRadio.getChecked())
				searchType = contentRadio.getValue();
			contentRadio = new HtmlInputRadio(this,"searchTypeDate");
			if (contentRadio.getChecked())
				searchType = contentRadio.getValue();
			//HtmlInputText contentText = new HtmlInputText(this,"searchContent");
			searchContent = (String)pageData.get("searchContent");
			initSearchTable(searchType.toLowerCase(),searchContent);
		} catch (Jaxception e) {
			e.printStackTrace();
		}

	}

	private void initSearchTable(String searcType,String searchContent)
	{
		try {
			_messageTable.setVisible(false);
			_messageTable.deleteAllRows();
			_searchTable.deleteAllRows();
			_searchTable.setVisible(true);
			_msgIDs.clear();
			String getAllMessagesString = "search " + searcType + " " + searchContent;
			String messages = _protocolHandler.processMessage(getAllMessagesString);
			if (messages.contentEquals("print No messages found\\e"))
			{
				String []noMessageFound = {"No Messages Found"};
				_searchTable.insertRow(-1, noMessageFound);
				return;
			}
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
				//String []currRowWithReply = Arrays.copyOf(currRow, currRow.length+2);
				//currRowWithReply[currRowWithReply.length-2] = "<a " + "\" id=\"reply" + msgID +"\" href=\"\">reply</a>";
				//currRowWithReply[currRowWithReply.length-1] = "<a " + "\" id=\"delete" + msgID +"\" href=\"\">delete</a>";
				String []currRowWithScore = Arrays.copyOf(currRow,currRow.length+1);
				String contentOfMsg = currRowWithScore[2];
				String currMsgScore = contentOfMsg.substring(contentOfMsg.indexOf("%score")+6);
				currRowWithScore[2] = contentOfMsg.substring(0,contentOfMsg.indexOf("%score"));
				currRowWithScore[currRowWithScore.length-1] = currMsgScore;
				_searchTable.insertRow(-1, currRowWithScore);
				final int rowNum = i;
				/*				HtmlElement replyElement = new HtmlElement(this,"reply" + msgID)
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
				 */
				HtmlAnchor msgElement = new HtmlAnchor(this, "msglink" + msgID)
				{
					protected void onClick()
					{
						searchLinkClicked(msgID);
					}
				};
				/*
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
				 */
			}
			//			_messageTable.addDeleteButtons(1, -1, "delete", null);
			//			_messageTable.enableCellEditing(1, 2, -1, 2, true, false, null);
			initSiteMap();
		} catch (Jaxception e) {
			e.printStackTrace();
		}
	}

	protected void searchLinkClicked(String msgID) {
		try {
			_searchTable.setVisible(false);
			_searchTable.deleteAllRows();
			_messageTable.setVisible(true);
			initTable(false, msgID);
		} catch (Jaxception e) {
			e.printStackTrace();
		}
	}

	protected void moderatorClicked() {
		try {
			HtmlInputText newModerName = new HtmlInputText(this, "moderatorName");

			String answer = _protocolHandler.processMessage("moderator " + newModerName.getValue());
			updateStatus(formatServerAnswer(answer));
			newModerName.setValue("");
		} catch (Jaxception e) {

			e.printStackTrace();
		}

	}

	protected void adminClicked() {
		try {
			HtmlInputText newModerName = new HtmlInputText(this, "moderatorName");

			String answer = _protocolHandler.processMessage("admin " + newModerName.getValue());
			updateStatus(formatServerAnswer(answer));
			newModerName.setValue("");
		} catch (Jaxception e) {

			e.printStackTrace();
		}		

	}

	private void dateRadioOnClick(String value) {
		try {
			final String finalValue = value;
			final HtmlInputText searchContent = new HtmlInputText(this, "searchContent");
			final HtmlInputText beginDate = new HtmlInputText(this, "startDateCalendar");
			final HtmlInputText endDate = new HtmlInputText(this, "endDateCalendar");
			if (value.contentEquals("Date"))
			{
				searchContent.setVisible(false);
				beginDate.setVisible(true);
				endDate.setVisible(true);
				HtmlInputSubmit searchButton = new HtmlInputSubmit(this, "searchSubmit")
				{
					protected void onClick(Map pageData)
					{
						try {
							String beginDateString = beginDate.getValue();
							String endDateString = endDate.getValue();
							String content = beginDateString + " " + endDateString;
							initSearchTable("date", content);
						} catch (Jaxception e) {
							e.printStackTrace();
						}
					}
				};
			}
			else
			{
				searchContent.setVisible(true);
				beginDate.setVisible(false);
				endDate.setVisible(false);
				HtmlInputSubmit searchButton = new HtmlInputSubmit(this, "searchSubmit")
				{
					protected void onClick(Map pageData)
					{
						try {
							String content = searchContent.getValue();
							initSearchTable(finalValue.toLowerCase(), content);
						} catch (Jaxception e) {
							e.printStackTrace();
						}
					}
				};
			}
		} catch (Jaxception e) {
			e.printStackTrace();
		}
	}

}

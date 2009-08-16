package UI;
import implementation.*;
import Exceptions.UserAlreadyExistsException;
import Exceptions.UserDoesNotExistException;
import domainLayer.TheController;
import implementation.RegisteredUser;
import UI.*;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * EnterPage.java
 *
 * Created on 14/06/2009, 19:44:24
 */

/**
 *
 * @author Roee
 */
public  class EnterPage extends javax.swing.JFrame implements UIc {

    private int curShowLog;
    private Vector<String> allMessage=new Vector<String>();
    private  TUI client = new TUI();
    private long  selectedFatherMsg=-1;
    private long _selectedPrevMsg=-1;
    private long MessageIdSelctd=-1;

    /** Creates new form EnterPage */
    public EnterPage() {
        initComponents();

       curShowLog=1;
       this.NewMessage.setVisible(false);
     this.delete.setVisible(false);
       this.Edit.setVisible(false);
       this.client.connectSocketsToServer("localhost", 1234);
       intialFatherMessag(-1);
       this.InsertSubMessage.setSelected(false);

    }
    public  void intialFatherMessag(long fatherID){

       String[] columnNames = {"MessageID",
                        "PosterID",
                        "Content",
                        "PosterDate"};
       

          Object [][] data=null;
        try {
            System.out.println("testing 1....");
            System.out.println(fatherID);
            System.out.println(this.client.sendMessageAndWaitForReply("display " + fatherID));
            data = displayMessage(this.client.sendMessageAndWaitForReply("display " + fatherID));
        } catch (IOException ex) {
            Logger.getLogger(EnterPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
          if (data==null){
              System.out.println("i get an empty value.....");
          }
         MessageTable.setModel(new javax.swing.table.DefaultTableModel(data,columnNames));

    }
public  void intialSearchMessag(Object [][] data){
//    for (int i=0;i<data.length;i++){
//        System.out.println(data[i][0].toString());
//        System.out.println(data[i][1]);
//        System.out.println(data[i][2]);
//        System.out.println(data[i][3]);
//    }
          String[] columnNames = {"MessageID",
                        "PosterID",
                        "Content",
                        "PosterDate"};
           MessageTable.setModel(new javax.swing.table.DefaultTableModel(data,columnNames));
}
public Object [][] displayMessage(String answer){
    if ((answer.compareTo("No messages found")==0) || 
            (answer.compareTo("print " )==0)){
        Object [][] ans =new Object[0][4];
        return ans;
    }

    String [] seperated = answer.split("\n");
      Object [][] ans =new Object[seperated.length][4];
      if (seperated.length==1){
                   String currMsg = seperated[0];
                   System.out.println( currMsg.substring(currMsg.indexOf("+1")+2,currMsg.indexOf("+2")));
                  String currMsgID = currMsg.substring(currMsg.indexOf("+1")+2,currMsg.indexOf("+2"));
                  String currMsgPosterID = currMsg.substring(currMsg.indexOf("+2")+2,currMsg.indexOf("+3"));
                  String currMsgData = DestroyPre(currMsg.substring(currMsg.indexOf("+3")+2,currMsg.indexOf("+4")));
                  String currMsgTime = currMsg.substring(currMsg.indexOf("+4")+2);
                  Date temp = new Date(Long.parseLong(currMsgTime));
                  ans[0][0]=currMsgID;
                  ans[0][1]=currMsgPosterID;
                  ans[0][2]=currMsgData;
                 // ans[0][3]=currMsgTime;
                   ans[0][3]=temp;
      }else{
            for (int i = 0; i < seperated.length-1; i++)
            {
                  String currMsg = seperated[i];
                  String currMsgID = currMsg.substring(currMsg.indexOf("+1")+2,currMsg.indexOf("+2"));
                  String currMsgPosterID = currMsg.substring(currMsg.indexOf("+2")+2,currMsg.indexOf("+3"));
                  String currMsgData = DestroyPre(currMsg.substring(currMsg.indexOf("+3")+2,currMsg.indexOf("+4")));
                  String currMsgTime = currMsg.substring(currMsg.indexOf("+4")+2);
                  Date temp = new Date(Long.parseLong(currMsgTime));
                  ans[i][0]=currMsgID;
                  ans[i][1]=currMsgPosterID;
                  ans[i][2]=currMsgData;
                  //ans[i][3]=currMsgTime;
                   ans[i][3]=temp;
            }
      }
      return ans;
}
private String DestroyPre(String sent){
    if (sent.contains("%")){
    return sent.substring(0,sent.indexOf("%"));
    }
    return sent;
}

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        logIn = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        Registerd = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        MessageTable = new javax.swing.JTable();
        NewMessage = new javax.swing.JButton();
        Edit = new javax.swing.JButton();
        pass = new javax.swing.JPasswordField();
        InsertSubMessage = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton2 = new javax.swing.JButton();
        delete = new javax.swing.JButton();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 102, 204));
        setForeground(java.awt.Color.white);
        setResizable(false);

        jLabel1.setText("User Name");

        jLabel2.setText("Password");

        logIn.setText("Login");
        logIn.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        logIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logInActionPerformed(evt);
            }
        });

        jLabel3.setText("Status");
        jLabel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setText("NO DATA");
        jLabel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Registerd.setText("Registerd");
        Registerd.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Registerd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RegisterdActionPerformed(evt);
            }
        });

        MessageTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "MessageID", "PosterID", "Content", "PosterDate"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        MessageTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MessageTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(MessageTable);

        NewMessage.setText("New Message");
        NewMessage.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        NewMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewMessageActionPerformed(evt);
            }
        });

        Edit.setText("Edit");
        Edit.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditActionPerformed(evt);
            }
        });

        InsertSubMessage.setText("Insert sub Message");

        jButton1.setText("Back To Main");
        jButton1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel5.setText("change privellages");
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });

        jCheckBox1.setText("Manipulat Message");

        jButton2.setText("Search");
        jButton2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        delete.setText("delete");
        delete.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(681, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 654, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(InsertSubMessage)
                            .addGap(18, 18, 18)
                            .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 613, Short.MAX_VALUE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(107, 107, 107)
                                    .addComponent(NewMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(44, 44, 44)
                                    .addComponent(Edit, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(32, 32, 32)
                                    .addComponent(delete, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(8, 8, 8)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(10, 10, 10)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(pass, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(logIn, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(Registerd, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE))))
                            .addGap(114, 114, 114)))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(logIn)
                    .addComponent(Registerd)
                    .addComponent(pass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(InsertSubMessage)
                            .addComponent(jCheckBox1)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NewMessage)
                    .addComponent(Edit)
                    .addComponent(delete))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addContainerGap())
        );

        jButton1.getAccessibleContext().setAccessibleName("Back to Main");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void logInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logInActionPerformed
        if (this.curShowLog==1){
            String ans;
            try {
           ans = client.sendMessageAndWaitForReply("login " + this.jTextField1.getText() + " " + this.pass.getText());
           System.out.println(ans);
         if (ans.equalsIgnoreCase("user logged in succesfully "
)){
               System.out.println("i am here1");
        this.logIn.setText("LogOut "+this.jTextField1.getText());
        this.curShowLog=0;
       this.NewMessage.setVisible(true);
     this.delete.setVisible(true);
       this.Edit.setVisible(true);
       this.jTextField1.disable();
       this.pass.disable();
       this.jLabel4.setText(this.jTextField1.getText()+" is login now ");
         }else
         {
        this.jLabel4.setText("The user name or/and password is incorect.... ");
        this.pass.setText("");
       this.jTextField1.setText("");
        System.out.println("i am here3");
         }
        } catch (IOException ex) {
            Logger.getLogger(EnterPage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
        if (this.curShowLog==0){
         try {
          this.client.sendMessageAndWaitForReply("logoff");
              } catch (IOException ex) {
                  Logger.getLogger(EnterPage.class.getName()).log(Level.SEVERE, null, ex);
              }
        this.logIn.setVisible(true);
        this.logIn.setText("LogIn");
        this.curShowLog=1;
        this.NewMessage.setVisible(false);
     this.delete.setVisible(false);
       this.Edit.setVisible(false);
      this.jTextField1.enable();
       this.pass.enable();
       this.pass.setText("");
       this.jTextField1.setText("");
        }
        }
}//GEN-LAST:event_logInActionPerformed

    private void RegisterdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RegisterdActionPerformed

            if ( this.pass.getText().equalsIgnoreCase("")  ||  this.jTextField1.getText().equalsIgnoreCase("") ){
                this.jLabel4.setText("you can't register without password or user name");
            }else{
                 try {
            this.client.sendMessageAndWaitForReply("register " + this.jTextField1.getText() + " " + this.pass.getText());
              this.jLabel4.setText(this.jTextField1.getText()+ " Registration successfull");

        } catch (IOException ex) {
            Logger.getLogger(EnterPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        }

}//GEN-LAST:event_RegisterdActionPerformed

    private void MessageTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MessageTableMouseClicked

 ListSelectionModel selectionModel =  this.MessageTable.getSelectionModel();
int selctedRow=selectionModel.getMinSelectionIndex();
                      Object cur=MessageTable.getModel().getValueAt(selctedRow, 0);
                      Object curMessage=MessageTable.getModel().getValueAt(selctedRow, 2);
                      if (cur!=null){
                      long curId= Long.parseLong( ((String)cur).trim());
                      if (this.jCheckBox1.isSelected()){
                          this.MessageIdSelctd=curId;
                      }else{
                      
                  if (!this.InsertSubMessage.isSelected()){

                       System.out.println(curId+ " Iam cur message Number");
                              MessageAPL MessageAP =new MessageAPL((String) curMessage );
                                 MessageAP.show();
                                  this.selectedFatherMsg=-1;
                                 System.out.println(selectedFatherMsg+ " noe change");
                                 this.MessageIdSelctd=curId;
                  }else
                  {
                  this._selectedPrevMsg=this.selectedFatherMsg;
                  this.selectedFatherMsg=curId;
                  System.out.println("i am print from hereeeeee--------------------");
                intialFatherMessag(curId);
                  }
                      }
                      }
    }//GEN-LAST:event_MessageTableMouseClicked

 public void upDateNewMessage(){
     System.out.println(this.selectedFatherMsg+ " start work");
      intialFatherMessag(this.selectedFatherMsg);
 }






    private void NewMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewMessageActionPerformed
        // TODO add your handling code here:

        //NewMessage curNewMsg=new NewMessage(this.client,selectedFatherMsg);
        System.out.println(selectedFatherMsg);
           NewMessage curNewMsg=new NewMessage(this.client,selectedFatherMsg,this);
        curNewMsg.show();

        

    }//GEN-LAST:event_NewMessageActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        System.out.println(this.selectedFatherMsg);
        if (this._selectedPrevMsg != this._selectedPrevMsg){
        intialFatherMessag(this._selectedPrevMsg);
        }else{
            this.selectedFatherMsg=-1;
            this._selectedPrevMsg=-1;
            intialFatherMessag(this._selectedPrevMsg);
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void EditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditActionPerformed
        // TODO add your handling code here:

        EditMessage edit=new EditMessage(this.client, MessageIdSelctd,this.selectedFatherMsg,this);
        edit.show();
    }//GEN-LAST:event_EditActionPerformed

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        // TODO add your handling code here:
        changeprivellages cur=new changeprivellages(client);
        cur.show();


    }//GEN-LAST:event_jLabel5MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        Search curSearch=new Search(client, this);
        curSearch.show();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        try {
            // TODO add your handling code here:
            System.out.println(MessageIdSelctd);
          this.jLabel4.setText( client.sendMessageAndWaitForReply("delete " + this.MessageIdSelctd));
        } catch (IOException ex) {
            Logger.getLogger(EnterPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_deleteActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EnterPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Edit;
    private javax.swing.JCheckBox InsertSubMessage;
    private javax.swing.JTable MessageTable;
    private javax.swing.JButton NewMessage;
    private javax.swing.JButton Registerd;
    private javax.swing.JButton delete;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton logIn;
    private javax.swing.JPasswordField pass;
    // End of variables declaration//GEN-END:variables
////// here we implement the Interfae Methods.
    public void UpDatelogMeIn(String userName) {
       
    }
    public void UpDateregisterNewUser (String userName){
      
    }

    public void UpDateLogOutRegister(String userName){
        this.jLabel4.setText(userName + "Is LogOut Now");;
    }
}

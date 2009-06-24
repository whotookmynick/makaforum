/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package UI;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Roee
 */
public class SelectionListener implements  ListSelectionListener   {
     JTable table;

        // It is necessary to keep the table since it is not possible
        // to determine the table from the event's source
        SelectionListener(JTable table) {
            this.table = table;
        }

    public void valueChanged(ListSelectionEvent e) {
   

               if (e.getSource() == table.getSelectionModel()
                  && table.getRowSelectionAllowed()) {
                // Column selection changed
                int first = e.getFirstIndex();
                int last = e.getLastIndex();
            } else if (e.getSource() == table.getColumnModel().getSelectionModel()
                   && table.getColumnSelectionAllowed() ){
                // Row selection changed
                int first = e.getFirstIndex();
                int last = e.getLastIndex();
            }

            if (e.getValueIsAdjusting()) {
                // The mouse button has not yet been released
            }
        }


    }



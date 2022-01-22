package Poly_factoring;

import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ButtonGroup;

import java.awt.GridLayout;

public class  OperationPanel extends JPanel
{
   private JRadioButton [] buttons ;
   private ButtonGroup bg ;
   private Operation mySelected ;

   public OperationPanel(Operation [] operations)
      {
      super() ;
      setLayout(new GridLayout(4,2)) ;
      add(new JLabel("Operation")) ;
      add(new JLabel("")) ;
      buttons = new JRadioButton [operations.length] ;
      bg = new ButtonGroup() ;
      int i = 0 ;
      for (final Operation oper:operations)
         {
         JRadioButton j = oper.makeButton() ;
         j.addActionListener(
            e -> OperationPanel.this.setSelectedOperation(oper)) ;
         buttons[i++] = j;
         this.add(j) ;
         bg.add(j) ;
         }
      }

   private void setSelectedOperation(Operation oo)
      {
      mySelected = oo; 
      }
   
   public Operation getSelectedOperation()
      {
      return mySelected ;
      }
  
}

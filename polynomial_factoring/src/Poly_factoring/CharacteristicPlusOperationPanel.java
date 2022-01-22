package Poly_factoring;

import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.GridLayout;

public class  CharacteristicPlusOperationPanel extends JPanel
{
   private CharacteristicPanel  myCharacteristicPanel ;
   private OperationPanel myOperationPanel;


   public CharacteristicPlusOperationPanel(Operation [] operations)
      {
      super() ;
      setLayout(new GridLayout(1,2));
      myCharacteristicPanel = new CharacteristicPanel() ;
      myOperationPanel = new OperationPanel(operations) ;
      this.add(myCharacteristicPanel) ;
      this.add(myOperationPanel) ;
      }

   public long getCharacteristic()
      {
      return myCharacteristicPanel.getCharacteristic(); 
      }
   
   public FiniteField getField()
      {
      return new FiniteField(getCharacteristic()) ;
      }
   
   public Operation getSelectedOperation()
      {
      return myOperationPanel.getSelectedOperation() ;
      }
  
}

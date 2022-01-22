package Poly_factoring;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;

import java.awt.GridLayout;

public class  CharacteristicPanel extends JPanel
{
   private JTextField myNumberText ;

   public CharacteristicPanel()
      {
      super() ;
      setLayout(new GridLayout(2,1));
      this.add(new JLabel("Characteristic")) ;
      myNumberText = new JTextField(10) ;
      this.add(myNumberText) ;
      }

   public long getCharacteristic()
      {
      try
         {
         long value = Long.parseLong(myNumberText.getText()) ;
         return value ;
         }
      catch (NumberFormatException e)
         {
         myNumberText.setText("17") ;
         return getCharacteristic() ;
         }
      }
}

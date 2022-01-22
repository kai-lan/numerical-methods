package Poly_factoring;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;

/**
 * A NumberPanel is a class whose objects have an editable text field
 * and an explantory label.  They are intended for reading long integers
 * in a GUI.
 */

public class NumberPanel extends JPanel {
   
   private JTextField element1 ;



   public NumberPanel(String label, int textFieldSize)
      {
      super() ;
      setLayout(new GridLayout(2,1));
      this.add(new JLabel(label)) ;
      element1 = new JTextField(textFieldSize) ;
      this.add(element1) ;
      }
     
   public void setValue(long ell)
      {
      element1.setText(String.format("%d",ell)) ;
      }

   public long getValue() throws NumberFormatException
      {
      try
          {
          return Long.parseLong(element1.getText()) ;
          }
      catch(NumberFormatException nfe)
          {
          element1.setForeground(Color.RED);
          repaint();
          throw nfe;
          }
      }

}

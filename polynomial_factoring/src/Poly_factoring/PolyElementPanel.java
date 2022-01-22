package Poly_factoring;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;

public class PolyElementPanel extends JPanel {
   
   private JTextField element1 ;



   public PolyElementPanel(String label, int textFieldSize)
      {
      super() ;
      setLayout(new GridLayout(2,1));
      this.add(new JLabel(label)) ;
      element1 = new JTextField(textFieldSize) ;
      this.add(element1) ;
      }
     
   public void setValue(Polynomial p)
      {
      element1.setText(p.toString()) ;
      }

   public Polynomial getPoly(FiniteField ff) throws NumberFormatException
      {
      return Polynomial.parse(ff, element1.getText()) ;
      }

}

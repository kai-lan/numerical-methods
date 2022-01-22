package Poly_factoring;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.border.Border;
import javax.swing.BorderFactory;

import javax.swing.*;

/**
 * A subpanel of Kai's Calculator object dedicated to FiniteField
 * calculations.  The sibling class is {@link PolyPanel}.
 */

public class FieldPanel extends JPanel {
   private static Border fieldOperationsBorder =
     BorderFactory.createTitledBorder(
         BorderFactory.createLineBorder(Color.black),
         "Operations in a field") ;
   
   private static Operation [] theFieldOperations = {
      Operation.Add,
      Operation.Subtract,
      Operation.Multiply,
      Operation.Divide,
      Operation.Inverse
   } ;
   
   /*
    * The directly accessible subobjects.
    */
   private CharacteristicPlusOperationPanel coPanel ;
   private NumberPanel element1 ;
   private NumberPanel element2 ;
   private OutputPanel output ;



   public FieldPanel()
      {
      super() ;
      setLayout(new GridLayout(4,1));
      setBorder(fieldOperationsBorder);
      coPanel = new CharacteristicPlusOperationPanel(
         theFieldOperations) ;
      element1 = new NumberPanel("Element 1", 10) ;
      element2 = new NumberPanel(
         "Element 2 (No reading if inverse is selected)", 10) ;
      output = new OutputPanel() ;
      this.add(coPanel) ;
      this.add(element1) ;
      this.add(element2) ;
      this.add(output) ;
      this.getMainButton().addActionListener(
         e -> display(getResult())) ;
      }
     
   /**
    * Display a number in the output area.
    * @param ell, the number to display.
    */
   public void display(long ell)
      {
      display(""+ell) ;
      } 

   /**
    * Display a String in the output area.
    * @param s, the number to display.
    */
   public void display(String s)
      {
      output.display(s) ;
      }

   /**
    * compute the result of the FiniteField operation specified
    * specified by the input subpanels. 
    * @return the result of the computation.
    */
   public long getResult()
      {
      try
         {
         FiniteField ff = coPanel.getField() ;
         long result = 0 ;
         Operation oo = coPanel.getSelectedOperation() ;
         long elt1 = element1.getValue() ;
         long elt2 = element2.getValue() ;
         switch(oo)
            {
            case Add:
               result = ff.add(elt1,elt2) ;
               break ;
            case Subtract:
               result = ff.subtract(elt1,elt2) ;
               break ;
            case Multiply:
               result = ff.multiply(elt1,elt2) ;
               break ;
            case Divide:
               result = ff.divide(elt1,elt2) ;
               break ;
            case Inverse:
               result = ff.inverse(elt1) ;
               break ;
            default:
               result = 134123414L ;
               break;
            }
         return result ;
         }
      catch (NumberFormatException nfe)
         {
         return 13412341234L ;
         }
      }
   
   /**
    * returns the Button responsible for causing computations
    * to happen.
    * @return -- the computation causing button.
    */
   public JButton getMainButton()
      {
      return output.getMainButton() ;
      }

}

package Poly_factoring;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;



/**
 * A subpanel of Kai's Calculator object dedicated to Polynomial
 * calculations.  The sibling class is {@link FieldPanel}.
 */
public class PolyPanel extends JPanel
{
   private static Border polyOperationsBorder =
     BorderFactory.createTitledBorder(
        		BorderFactory.createLineBorder(Color.black),
            "Operations on polynomials over a field");

   private static Operation [] thePolyOperations = {
      Operation.Add,
      Operation.Subtract,
      Operation.Multiply,
      Operation.Divide,
      Operation.GCD
   } ;

   /*
    * The directly accessible subobjects.
    */
   private CharacteristicPlusOperationPanel coPanel ;
   private PolyElementPanel element1 ;
   private PolyElementPanel element2 ;
   private Polynomial result2 ;
   private OutputPanel output ;
   
   public PolyPanel()
      {
      super() ; 
      setLayout(new GridLayout(4,1));
      setBorder(polyOperationsBorder);
      coPanel = new CharacteristicPlusOperationPanel(
         thePolyOperations) ;
      this.add(coPanel) ;
      element1 = new PolyElementPanel(
         "Polynomial 1 "+
         "(Separating by comma from high to low order, "+
         "eg, 2, 0, 1 for 2x^2 + 1)",
         20) ;
      element2 = new PolyElementPanel(
         "Polynomial 2", 20) ;
      output = new OutputPanel() ;
      this.add(element1) ;
      this.add(element2) ;
      this.add(output) ;
      this.getMainButton().addActionListener(
         e -> display(getResult())) ;
      }
   
   /**
    * Display a {@link Polynomial} in the output area.
    * @param p, the {@link Polynomial} to display.
    */
   public void display(Polynomial p)
      {
      if (result2==null)
         display(""+p) ;
      else
         display(
            String.format(
               "%s [remainder=%s]%n", p.toString(), result2.toString())) ;
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
    * compute the result of the {@link Polynomial} operation specified
    * specified by the input subpanels. 
    * @return the result of the computation.
    */
   public Polynomial getResult()
      {
      try
         {
         FiniteField ff = coPanel.getField() ;
         Polynomial result = new Polynomial(
            ff,
            Polynomial.getUnitPoly(ff).asCoefficients()) ;
         Operation oo = coPanel.getSelectedOperation() ;
         Polynomial elt1 = element1.getPoly(ff) ;
         Polynomial elt2 = element2.getPoly(ff) ;
         result2 = null ;
         switch(oo)
            {
            case Add:
               result = Polynomial.polyAdd(elt1,elt2) ;
               break ;
            case Subtract:
               result = Polynomial.polySubtract(elt1,elt2) ;
               break ;
            case Multiply:
               result = Polynomial.polyMultiply(elt1,elt2) ;
               break ;
            case Divide:
               result2 = new Polynomial(ff,new ArrayList<Long>()) ;
               result = Polynomial.polyDivide(elt1,elt2,result2) ;
               break ;
            case GCD:
               result = Polynomial.polyGCD(elt1,elt2) ;
               break ;
            default:
               result = Polynomial.getUnitPoly(ff) ;
               break;
            }
         return result ;
         }
      catch (NumberFormatException nfe)
         {
         return Polynomial.getZeroPoly(new FiniteField(177L)) ;
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

package Poly_factoring;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;

/**
 * the OutputPanel is the 4th subpanel of both
 * {@link FieldPanel} and {@link PolyPanel}.
 * It contains a JButton for triggering computatons (not wired in this
 * class) and a JTextArea for displaying results.
 *
 * <p>
 *  Its usuable methods are {@code display} and {@code getMainButton}.
 * </p>
 */
public class OutputPanel extends JPanel {
   
   private JButton myButton ;
   private JTextArea myDisplay ;


   public OutputPanel()
      {
      super() ;
      setLayout(new GridLayout(2,1));
      this.add(makeButtonPanel()) ;
      this.add(makeTextArea()) ;
      }
     
   public void display(String s)
      {
      myDisplay.setForeground(Color.BLACK) ;
      myDisplay.setText(s) ;
      repaint() ;
      }

   public JButton getMainButton()
      {
      return myButton ;
      }

   private JPanel makeButtonPanel()
      {
      JPanel answer = new JPanel() ; // flow layout
      answer.add(new JLabel("Output")) ;
      myButton = new JButton("Enter") ;
      answer.add(myButton) ;
      return answer ;
      }

   private JTextArea makeTextArea()
      {
      JTextArea answer = new JTextArea(5,30) ; // flow layout
      answer.setEditable(false) ;
      answer.setFont(new Font("Arial", Font.PLAIN, 16)) ;
      answer.setForeground(Color.GRAY) ;
      answer.setText("The future is here already") ;
      myDisplay=answer;
      return answer ;
      }

}

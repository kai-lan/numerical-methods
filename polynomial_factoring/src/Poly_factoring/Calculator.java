

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;

/**
 * Kai's Calculator class (respelled) with the complexity moved to
 * subPanels.
 */

public class Calculator extends JFrame
{

  public Calculator()
      {
      super("Field Theory Calculator");
	   this.setSize(500,600);
	   this.setLayout(new GridLayout(2, 1));
	   this.getContentPane().add(new FieldPanel()) ;
	   this.getContentPane().add(new PolyPanel()) ;
      this.setVisible(true);
      }
   
}

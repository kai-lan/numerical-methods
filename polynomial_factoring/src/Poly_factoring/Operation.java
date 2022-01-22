package Poly_factoring;

import javax.swing.JRadioButton;

public enum Operation
{
    Add, Subtract, Multiply, Divide, Inverse, GCD ;

    public static Operation fromText(String s)
    {
    switch(s)
        {
        case "Add":      return Add ;
        case "Subtract": return Subtract ;
        case "Multiply": return Multiply ;
        case "Divide":   return Divide ;
        case "Inverse":  return Inverse ;
        case "GCD":      return GCD ;
        default:         return null ;
        }
    }

    public JRadioButton makeButton()
    {
    return new JRadioButton(toString()) ;
    }
}

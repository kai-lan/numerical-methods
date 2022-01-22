package Poly_factoring;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;

public class Calulator implements ActionListener {
	
	JFrame frame;
	JPanel field, input1, charac, operation, element, output1, out1, 
			poly, input2, chara, opera, ele, output2, out2;
	JLabel charac1, operation1, element1, element3,
			chara1, opera1, ele1, ele3;
	JTextField charac2, element2, element4,
				chara2, ele2, ele4;
	ButtonGroup bg, bg1;
	JRadioButton add, sub, mul, div, inv,
					add1, sub1, mul1, div1, gcd;
	JButton comp1, comp2;
	JTextArea result1, result2;
	
	public void run() {
		frame = new JFrame("Field Theory Calculator");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setSize(500,600);
	    frame.setLayout(new GridLayout(2, 1));
	    frame.setVisible(true);
	    
	    //**********************************************
	    // This is for basic operations in finite fields
	    //**********************************************
        field = new JPanel(); frame.getContentPane().add(field);
        field.setLayout(new GridLayout(3, 1));
        field.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Operations in a field"));
        	
        	input1 = new JPanel();	field.add(input1);
        	input1.setLayout(new GridLayout(1, 2));
        	
        		charac = new JPanel();	input1.add(charac);
        		charac.setLayout(new GridLayout(2,1));
        		charac1 = new JLabel("Characteristic");	charac.add(charac1);
        		charac2 = new JTextField(10);	charac.add(charac2);
        		
        		
        		operation = new JPanel();	input1.add(operation);
        		operation.setLayout(new GridLayout(4,2));
        		operation1 = new JLabel("Operation");	operation.add(operation1);
        		operation.add(new JLabel());
        		bg = new ButtonGroup();
        		add = new JRadioButton("Add");	operation.add(add);	bg.add(add);
        		sub = new JRadioButton("Subtract");	operation.add(sub);	bg.add(sub);
        		mul = new JRadioButton("Multiply");	operation.add(mul);	bg.add(mul);
        		div = new JRadioButton("Divide");	operation.add(div); bg.add(div);
        		inv = new JRadioButton("Inverse"); operation.add(inv); bg.add(inv);
        		
        	element = new JPanel();	field.add(element);
        	element.setLayout(new GridLayout(4,1));
       		element1 = new JLabel("Element 1");	element.add(element1);
       		element2 = new JTextField(10);	element.add(element2);
       		element3 = new JLabel("Element 2 (No reading if inverse is selected)");	element.add(element3);
       		element4 = new JTextField(10);	element.add(element4);

        	output1 = new JPanel();	field.add(output1);
        	output1.setLayout(new GridLayout(2,1));
        		out1 = new JPanel(); output1.add(out1);
        		out1.add(new JLabel("Result"));
        		comp1 = new JButton("Enter"); comp1.addActionListener(this); comp1.setActionCommand("field");	out1.add(comp1);	
        	result1 = new JTextArea(5, 38);	output1.add(result1); result1.setEditable(false); result1.setFont(new Font("Arial", Font.PLAIN, 16));
              
        
        //***************************************************************
	    // This is for basic operations on polynomials over finite fields
	    //***************************************************************
        poly  = new JPanel(); frame.getContentPane().add(poly);
        poly.setLayout(new GridLayout(3, 1));
        poly.setBorder(BorderFactory.createTitledBorder(
        		BorderFactory.createLineBorder(Color.black), "Operations on polynomials over a field"));
        
    			input2 = new JPanel();	poly.add(input2);
    			input2.setLayout(new GridLayout(1, 2));
    	
    				chara = new JPanel();	input2.add(chara);
    				chara.setLayout(new GridLayout(2,1));
    				chara1 = new JLabel("Characteristic");	chara.add(chara1);
    				chara2 = new JTextField(10);	chara.add(chara2);
    		
    		
    				opera = new JPanel();	input2.add(opera);
    				opera.setLayout(new GridLayout(4,2));
    				opera1 = new JLabel("Operation");	opera.add(opera1);
    				opera.add(new JLabel());
    				bg1 = new ButtonGroup();
    				add1 = new JRadioButton("Add");	opera.add(add1);	bg1.add(add1);
    				sub1 = new JRadioButton("Subtract");	opera.add(sub1);	bg1.add(sub1);
    				mul1 = new JRadioButton("Multiply");	opera.add(mul1);	bg1.add(mul1);
    				div1 = new JRadioButton("Divide");	opera.add(div1); bg1.add(div1);
    				gcd = new JRadioButton("GCD"); opera.add(gcd); bg1.add(gcd);
    		
    			ele = new JPanel();	poly.add(ele);
    			ele.setLayout(new GridLayout(4,1));
    			ele1 = new JLabel("Polynomial 1 (Separating by comma from high to low order, eg, 2, 0, 1 for 2x^2 + 1)");	ele.add(ele1);
    			ele2 = new JTextField(10);	ele.add(ele2);
    			ele3 = new JLabel("Polynomial 2");	ele.add(ele3);
    			ele4 = new JTextField(10);	ele.add(ele4);

    			output2 = new JPanel();	poly.add(output2);
    			output2.setLayout(new GridLayout(2,1));
    				out2 = new JPanel(); output2.add(out2);
    				out2.add(new JLabel("Result"));
    				comp2 = new JButton("Enter");	comp2.addActionListener(this); comp2.setActionCommand("poly"); out2.add(comp2);
    			result2 = new JTextArea(5, 38);	output2.add(result2); result2.setEditable(false); result2.setFont(new Font("Arial", Font.PLAIN, 16));
        
	
	}
		
	int result;
	int cha, e1, e2;
	String str;
	int[] outcome;
	ArrayList<int[]> val;
	int[] f, g;

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if ("field".equals(e.getActionCommand())) {
			
			fieldHandler();
	    } 
		if("poly".equals(e.getActionCommand())) {

			PolyHandler();
	    }
		
	}
	
	private void fieldHandler() {
		
		try {
		cha = Integer.parseInt(charac2.getText());
		e1 = Integer.parseInt(element2.getText());
		e2 = Integer.parseInt(element4.getText());
		} catch(NumberFormatException e){
			
		}
		if(add.isSelected()) {
			result = FiniteField.add(e1, e2, cha);
		}
		if(sub.isSelected()) {
			result = FiniteField.subtract(e1, e2, cha);
		}
		if(mul.isSelected()) {
			result = FiniteField.multiply(e1, e2, cha);
		}
		if(div.isSelected()) {
			result = FiniteField.divide(e1, e2, cha);
		}
		if(inv.isSelected()) {
			result = FiniteField.findInverse(cha, e1);
		}
		result1.setText(Integer.toString(result));
	}
	
	private void PolyHandler() {
		
		try {
		cha = Integer.parseInt(chara2.getText());
		str = ele2.getText();
		f = Arrays.stream(str.split(","))
			    .map(String::trim).mapToInt(Integer::parseInt).toArray();
		str = ele4.getText();
		g = Arrays.stream(str.split(","))
			    .map(String::trim).mapToInt(Integer::parseInt).toArray();
		} catch(NullPointerException e) {
			
		} catch(NumberFormatException e) {
			
		}
		
		if(add1.isSelected()) {
			outcome = Polynomial.polyAdd(f, g, cha);
			str = Polynomial.displayPoly(outcome);
		}
		if(sub1.isSelected()) {
			outcome = Polynomial.polySubtract(f, g, cha);
			str = Polynomial.displayPoly(outcome);
		}
		if(mul1.isSelected()) {
			outcome = Polynomial.polyMultiply(f, g, cha);
			str = Polynomial.displayPoly(outcome);
		}
		if(div1.isSelected()) {
			val = Polynomial.polyDivide(f, g, cha);
			str = "Quotient: " + Polynomial.displayPoly(val.get(0)) + "\nRemainder: " + Polynomial.displayPoly(val.get(1));
		}
		if(gcd.isSelected()) {
			outcome = Polynomial.findGCD(f, g, cha);
			str = Polynomial.displayPoly(outcome);
		}
		result2.setText(str);
	}
	
}

package Poly_factoring;

import java.util.Stack;

/**
 * Some basic operations in a finite field Z_p
 * @author Kai
 *
 */
public class FiniteField {

	/**
	 * Add two integers in field of given characteristic
	 * @param a	The first summand
	 * @param b	The second summand
	 * @param charac The characteristic of finite field Z_p or infinite
	 * @return	The sum of a and b
	 */
	public static int add(int a, int b, int charac) {
		
		if(charac == 0) {
			return a + b;
		}
			
		return modular(charac, a + b);
	}
	
	/**
	 * Subtract two integers in the given field
	 * @param a	The minuend, the integer to subtract from
	 * @param b	The subtrahend
	 * @param charac	The characteristic of finite field Z_p or infinite
	 * @return	The difference of a and b
	 */
	public static int subtract(int a, int b, int charac) {
		
		if(charac == 0) {
			return a - b;
		}
			
		return modular(charac, a - b);
	}
	
	/**
	 * Multiply two integers in field Z_p or Z
	 * @param a	The first factor
	 * @param b	The second factor
	 * @param charac	The characteristic of finite field Z_p or 0
	 * @return	The product of a and b
	 */
	public static int multiply(int a, int b, int charac) {
		
		if(charac == 0) {
			return a * b;
		}
		return modular(charac, a * b);
	}
	
	
	/**
	 * Apply long division on an integer with given divisor 
	 * @param dividend	The integer to be divided
	 * @param divisor	The divisor
	 * @param charac	The characteristic of the field, prime or 0
	 * @return	The quotient
	 */
	public static int divide(int dividend, int divisor, int charac) {
		
		if(charac == 0) {
			return dividend/divisor;
		}
		
		return multiply(dividend, findInverse(charac, divisor), charac);
	}
	
	/**
	 * Given p between 2 and 99,999,999 and a nonzero relatively prime to p:
	 * find the inverse of a in field Z_p by Euclidean algorithm
	 * @param p the prime for the field Z_p
	 * @param a the element in Z_p
	 * @return the inverse of a in Z_p
	 */
	public static int findInverse(int p, int a) {
		
		int result;
		int factor1, coeff1, factor2, coeff2, remainder;
		boolean done;
		Tuple equa1, equa2;
		Stack<Tuple> stack = new Stack<Tuple>();
		
		/* Handle invalid input and special cases. */
		/* Check range of p. */
		if(p > 99999999 || p < 2) {
			System.out.println("Only number between 2 and 99999999 is supported for p. ");
			return 0;
		}
		/* Check range of a in integer range. */
		if(a > Integer.MAX_VALUE || a < Integer.MIN_VALUE) {
			System.out.println("Large a causes data overflow!");
			return 0;
		}
		/* Wrap a in range between 0 and p-1 */
		a = modular(p, a);
		
		/* Check nonzero a. */
		if(a == 0) {
			System.out.println("a has to be nonzero. ");
			return 0;
		}
		if(a == 1) {
			return 1;
		}
		
		
		
		if(!isCoPrime(p, a)) {
			System.out.println("An inverse does not exist.");
			return 0;
		}
		
		factor1 = p; coeff1 = 1; factor2 = a; coeff2 = - p/a; remainder = p%a;
		stack.push(new Tuple(coeff1, coeff2));
		
		/* Continuously apply Euclidean algorihm until the remainder is 1. */
		while(remainder != 1) {
			factor1 = factor2;
			factor2 = remainder;
			coeff2 = - factor1/factor2;
			remainder = factor1%factor2;
			stack.push(new Tuple(coeff1, coeff2));
		}
		
		/* Trace back to find x, y such that ax + py = 1. */
		done = false;
		while(!done) {
			equa1 = stack.pop();
			if(stack.isEmpty()) {
				done = true;
				stack.push(equa1);
			}
			else {
				equa2 = stack.pop();
				equa2.setFirstCoeff(equa2.getFirstCoeff() * equa1.getSecondCoeff());
				equa2.setSecondCoeff(equa2.getSecondCoeff() * equa1.getSecondCoeff()
						+ equa1.getFirstCoeff());
				stack.push(equa2);
			}
		}
		
		result = stack.pop().getSecondCoeff();
		result = modular(p, result);
		
		return result;
	}
	
	/**
	 * Find a mod p
	 * @param p
	 * @param a
	 * @return a mod p
	 */
	public static int modular(int p, int a) {
		
		if(a >= 0) {
			return a%p;
		}
		
		while(a < 0) {
			a += p;
		}
		return a;
	}
	
	/**
	 * Check if two numbers are co-prime
	 * @param a the first integer
	 * @param b the second integer
	 * @return true if a and b are co-prime, false otherwise
	 */
	private static boolean isCoPrime(int a, int b) {
		
		return gcd(a, b) == 1 || gcd(a, b) == -1;
	}
	
	/**
	 * Find the greatest common divisor of integers a and b
	 * @param a
	 * @param b	The greatest common divisor of integers a and b
	 * @return
	 */
	private static int gcd(int a, int b) {
		int x = a, y = b;
		int q, r;
		
		q = x/y;
		r = x%y;
		if(r == 0) {
			return y;
		}
		
		while(r != 0) {
			x = y; y = r;
			q = x/y; r = x%y;
		}
		return y;
	}
	/**
	 * Test if the given number p is a prime
	 * @return true if p is a prime, false otherwise
	 */
	private static boolean isPrime(int p) {
		return true;
	}
	
}

/**
 * This class creates a pair to store coefficients of 
 * an equation of the form ax + by = c as (x, y)
 */
class Tuple{
	
	int x, y;
	
	Tuple(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	int getFirstCoeff() {
		return x;
	}
	
	int getSecondCoeff() {
		return y;
	}

	
	void setFirstCoeff(int x) {
		this.x = x;
	}
	
	void setSecondCoeff(int y) {
		this.y = y;
	}
	
}

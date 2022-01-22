package Poly_factoring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//********************************************************
//	Main services:
//	1. Find the GCD of two numbers
//	2. Find the GCD of two polynomials in finite field Z_p
//	Additional services:
//	1. Long division on a polynimial given a divisor
//********************************************************
public class Polynomial {

	/**
	 * Add two polynomials in a given field
	 * @param f
	 * @param g
	 * @return The sum of polynomials f and g
	 */
	public static int[] polyAdd(int[] f, int[] g, int charac ) {
		
		int[] ff, gg;
		if(f.length >= g.length) {
			ff = f.clone();
			gg = g.clone();
		} else {
			ff = g.clone();
			gg = f.clone();
		}
		int k = ff.length - gg.length;
		for (int i = k; i < ff.length; i ++) {
			ff[i] = FiniteField.add(ff[i], gg[i - k], charac);
		}
		return ff;
	}
	
	/**
	 * Subtract two polynomials in a given field
	 * @param f
	 * @param g
	 * @return The difference of polynomials f and g
	 */
	public static int[] polySubtract(int[] f, int[] g, int charac ) {
		
		int[] ff = f.clone();
		int k = f.length - g.length;
		for (int i = k; i < ff.length; i ++) {
			ff[i] = FiniteField.subtract(ff[i], g[i - k], charac);
		}
		return ff;
	}
	
	/**
	 * Multiply two polynomials
	 * @param p1
	 * @param p2
	 * @param charac
	 * @return The product of two polynomials
	 */
	public static int[] polyMultiply(int[] p1, int[] p2, int charac) {
		
		int[] f = p1.length <= p2.length ? p1 : p2,
			  g = p1.length <= p2.length ? p2 : p1;
		int p = f.length, q = g.length;
		int[] h = new int[p + q - 1];
		int index;
		
		
		for(int k = 0; k <= p - 1; k ++) {
			index = h.length - 1 - k;
			h[index] = 0;
			for(int i = 0; i <= k; i ++) {
				h[index] = FiniteField.add(h[index], 
						FiniteField.multiply(f[p - 1 - i], g[q -1 - k + i], charac), charac);
			}
		}
		for(int k = p; k <= q - 1; k ++) {
			index = h.length -1 - k;
			h[index] = 0;
			for(int i = 0; i <= p - 1; i ++) {
				h[index] = FiniteField.add(h[index], 
						FiniteField.multiply(f[p - 1 - i], g[q -1 - k + i], charac), charac); 
			}
		}
		for(int k = q; k < h.length; k ++) {
			index = h.length -1 - k;
			h[index] = 0;
			for(int i = k - q +1; i <= p - 1; i ++) {
				h[index] = FiniteField.add(h[index], 
						FiniteField.multiply(f[p - 1 - i], g[q -1 - k + i], charac), charac); 
			}
		}
		return h;
	}
	
	/**
	 * Apply long division on polynomials in a given finite field Z_p
	 * Note that the leading coefficient has to be nonzero
	 * @param dividend	The polynomial to be divided 
	 * @param divisor	The divisor polynomial
	 * @return	The quotient polynomial and the remainder polynomial
	 */
	
	public static ArrayList<int[]> polyDivide(int[] dividend, int[] divisor, int charac) {
		
		ArrayList<Integer> quotient = new ArrayList<Integer>(), remainder = new ArrayList<Integer>();
		int[] a = dividend.clone(), b = divisor.clone();
		int m = a.length, n = b.length;
		ArrayList<int[]> result = new ArrayList<int[]>();
		
		if(charac != 0 && FiniteField.modular(charac, b[0]) == 0) {
			System.out.println("The leading coefficient has to be nonzero!");
			return null;
		}
		
		/* If the degree of the dividend is smaller than that of the divisor */
		if(m < n || (charac == 0 && a[0] % b[0] != 0)) {
			quotient.add(0);
			for (int integer : a) {
				remainder.add(integer);
			}
			result.add(convertListToArray(quotient));
			result.add(convertListToArray(remainder));
			return result;
		}	
		
		/* compute the quotient */
		boolean done = false;
		int	r;
		for(int current = 0; current <= m - n; current ++) {
			
			if(done) {
				quotient.add(0);
				continue;
			}
			else {
				done = (charac == 0 && a[current] % b[0] != 0);
				r = FiniteField.divide(a[current], b[0], charac);
				quotient.add(r);
				
				for(int i = 0; i < n; i ++) {
					if(current + i < m) {
						a[current + i] = FiniteField.subtract(a[current + i], r *b[i], charac);
					}
				}
			}
			
		}
		/* Find the remainder from the dividend*/
		/* Find the first nonzero term in the dividend */
		int i = 0;
		while(a[i] == 0) {
			i ++;
			/* Remainder is 0 */
			if(i == m) {
				remainder.add(0);
				result.add(convertListToArray(quotient));
				result.add(convertListToArray(remainder));
				return result;
			}
		}
		
		/* The remainder is nonzero */
		for(int index = i; index < m; index ++) {
				remainder.add(a[index]);
		}	
		result.add(convertListToArray(quotient));
		result.add(convertListToArray(remainder));
		return result;
	}
	
	/**
	 * Find the greatest common divisor (GCD) of two polynomials
	 * @param poly1 The first polynomial
	 * @param poly2 The second polynomial
	 * @param charac The characteristic
	 * @return The greatest common divisor (GCD) of poly1 and poly2
	 */
	public static int[] findGCD(int[] poly1, int[] poly2, int charac) {
		
		if(charac == 0) {
			System.out.println("Updating ~~~");
			return null;
		}
		int[] f, g;
		if(poly1.length == poly2.length) {
			f = poly1[0] >= poly2[0] ? poly1 : poly2;
			g = poly1[0] >= poly2[0] ? poly2 : poly1;
		}
		else if(poly1.length > poly2.length) {
			f = poly1;
			g = poly2;
		}
		else {
			f = poly2;
			g = poly1;
		}
		
		int[] zero = {0};
		
		ArrayList<int[]> result = polyDivide(f, g, charac);
		
		int[] q = result.get(0), r = result.get(1);
			
		while(!Arrays.equals(r, zero)) {
								f = g; g = r;
			result = polyDivide(f, g, charac);
			q = result.get(0);
			r = result.get(1);
				
		} 
		return g;

		
	}
	
	/**
	 * Display the polynomial given the int array
	 * @param poly The polynomial to be displayed
	 * @return	Display of the polynomial
	 */
	public static String displayPoly(int[] poly) {
		
		String str = "";
		
		int power;
		
		for(int i = 0; i < poly.length; i ++) {
			if(poly[i] != 0) {
				power = poly.length - i - 1;
				str += power != 0? " + " + poly[i] + "x^" + power:" + " + poly[i];
			}
		}
		if(str == "") {
			str += 0;
		} else {
		str = str.substring(2, str.length());
		}
		return str;
	}
	/**
	 * Convert a list of integers to an int array
	 * @param list The list to be converted
	 * @return	The resulting int array
	 */
	private static int[] convertListToArray(List<Integer> list) {
		int[] arr = new int[list.size()];
		for(int i = 0; i < arr.length; i ++) {
			arr[i] = list.get(i);
		}
		return arr;
	}
}

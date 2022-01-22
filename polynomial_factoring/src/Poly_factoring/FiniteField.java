package Poly_factoring;

import java.util.Stack;

/**
 * Some basic operations in a finite field Z_p
 * @author Kai
 *
 * Changed by dgc, so that FiniteField's are objects that store the
 * characteristic.  TODO.  check that the characteristic is prime; allow
 * characteristic 0 fields.  (This class really should be called PrimeField.)
 *
 */
public class FiniteField {

	/*
    * David:
    * I'm going to transform this code, and write notes while I do.
    *
    * 1. I'm going to switch from int to long, because, 
    *    why would we want to stick with int
    * 2. I'm going to add two private static functions that are similar
    *    to modular, normal1 and normal2.
    * 3. I'm going to make the characteristic be >0, and make
    *    FiniteField be a class that tracks its characteristic.
    * 4. I've separated inverse (non-static) and findInverse (static).
    * 5. I'm writing a slightly different version of findInverse.
    *
    * Future work.  Add a non-static FiniteField.Element class.
    *   This would allow for assuming that input values were normalized.
    */

   private long characteristic ;

   private long n1(long a) { return normal(a,characteristic) ; }
   private long n2(long a) { return normal2(a,characteristic) ; }
   
   public FiniteField(long c)
      {
      // test that c is prime ... TODO
      characteristic = c;
      }
   
   /**
	 * Add two integers in field of given characteristic
	 * @param a	The first summand
	 * @param b	The second summand
	 * @return	The sum of a and b
	 */
	public long add(long a, long b)
      {
		return n1(a + b);
	}
	
	/**
	 * Subtract two integers in the given field
	 * @param a	The minuend, the integer to subtract from
	 * @param b	The subtrahend
	 * @return	The difference of a and b
	 */
	public long subtract(long a, long b)
      {
		return n1(a - b);
      }
	
	/**
	 * Multiply two integers in field Z_p or Z
	 * @param a	The first factor
	 * @param b	The second factor
	 * @return	The product of a and b
	 */
	public long multiply(long a, long b)
      {
		return n1(a * b);
      }
	
	
	/**
	 * Find the inverse of x.  
    * Assumes that characteristic is prime, or at least co-prime to x.
	 * @param x.  the number whose inverse is sought.
	 * @return	The inverse
	 */
   public long inverse(long x)
      {
      return findInverseDGC(x, characteristic) ;
      }
   
   /**
	 * Apply long division on an integer with given divisor 
	 * @param dividend	The integer to be divided
	 * @param divisor	The divisor
	 * @return	The quotient
	 */
	public long divide(long dividend, long divisor)
      {
		return n2(multiply(n1(dividend), inverse(divisor)));
      }
	
	/**
	 * Given p and x nonzero relatively prime to p:
	 * find the inverse of a in field Z_p by Euclidean algorithm
	 * @param p the prime for the field Z_p
	 * @param x the element in Z_p
	 * @return the inverse of x in Z_p
	 */
   private static long findInverseDGC(long x, long p)
      {
      LongBox r = new LongBox(2) ;
      LongBox s = new LongBox(2) ;
      long z = extendedEuclidean(x, p, r, s) ;
      if (z!=1)
         {
         error(String.format("inverse: %d and %d are not relatively prime",
                             x, p)) ; 
         }
      return r.get() ;
      }

   /*
    * parameterize error behaviour through this routine.
    * Allows for future addition of custom exceptions
    */
   private static void error(String s)
      {
      System.err.println(s) ;
      System.exit(255) ;
      }

   /**
    * LongBox is a helper class.
    * 
    *   It allows for the return of Long results through parameters.
    */
   public static class LongBox /* helper class, provides call by reference.*/
   {
      private long contents;
      public LongBox(long b) { contents=b ; }
      public long get() { return contents; }
      public void put(long x)
         {
         contents=x;
         }
   }

   /**
	 * Given a and b
	 * find the inverse of a in field Z_p by Euclidean algorithm
	 * @param a one element
	 * @param b another element.
	 * @param r a box.  on return a*r.get()+b*s.get() is the gcd.
	 * @param s a box.  on return a*r.get()+b*s.get() is the gcd.
	 * @return the gcd of a and b.  Always non-negative.
	 */
   public static long extendedEuclidean(
      long a,
      long b,
      LongBox r,
      LongBox s)
      {
      if (b<0)
         {
         long g = extendedEuclidean(a,-b,r,s) ;
         s.put(-s.get()) ;
         return g ;
         }
      else if (b==0)
         {
         r.put((a<0) ? -1 : 1) ;
         s.put(954689122534L) ; // why not?
         // The silly value in s gets trimmed below when r is normalized.
         return r.get()*a ; 
         }
      else // recursion
         {
         long quotient = a/b ;
         if (a-b*quotient<0)
            --quotient ;
         long remainder = a - quotient*b ;
         assert remainder>=0 && remainder<b;
         // recursion here.  NB! r and s are reversed, see below.
         long g = extendedEuclidean(b,remainder,s,r) ;
         // g == b*s.get() + remainder*r.get()
         //   == b*s.get() + (a - quotient * b) *r.get() ;
         //   == a * r.get() + b * (s.get() - quotient * r.get()) ;
         s.put( s.get() - quotient * r.get()) ;
         // r,s work...but let's normalize r to [0,b).
         // we subtract a*r*k from one term, and add it to the other
         long k = r.get() / b ;
         if (r.get()-b*k <0) --k ;
         r.put(r.get()-b*k) ; s.put(s.get()+a*k) ;
         // r is now in [0,b).  a*r+b*s is still equal to g.
         return g ;
         }
      }
	
	
	/**
	 * Find a mod p, no assumptions on a.  p is assumed to be &gt;0.
	 * @param a
	 * @param p
	 * @return a mod p
	 */
	public static long normal(long a, long p)
      {
      return ((a%p)+p)%p ;
      }

   public long normal(long a)
      {
      return normal(a,characteristic) ;
      }
	
	/**
	 * Find a mod p, a≥0.  p is assumed to be &gt;0.
	 * This is slightly faster than normal1.
    * @param a
	 * @param p
	 * @return a mod p
	 */
	private static long normal2(long a, long p)
      {
      return (a%p) ;
      }
	
   
   /**
    * Test if the given number p is a prime
	 * @return true if p is a prime, false otherwise
	 */
	private static boolean isPrime(long p) {
		return true;
	}
	
}


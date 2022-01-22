package Poly_factoring;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.List;
import java.util.function.LongBinaryOperator;
import java.util.function.BinaryOperator;
//********************************************************
//	Main services:
//	1. Find the GCD of two numbers
//	2. Find the GCD of two polynomials in finite field Z_p
//	Additional services:
//	1. Long division on a polynimial given a divisor
//********************************************************

/**
 * <p>
 *  provide a {@code Polynomial} over a {@link FiniteField} class.
 * Each polynimial contains a reference to the finite field over
 * which it is defined.
 * </p><p>
 * Binary operations should only be legal if their operanda are defined
 * over the same finite field.  As currently coded, this is not checked.
 * </p>
 */
public class Polynomial
{

   private static Polynomial zero = null ; // set by getZero
   private static Polynomial one  = null ; // set by getOne
   
   private FiniteField myField ;
   private ArrayList<Long>  coefficients ;

   /*
    * constructors
    */
   public Polynomial(FiniteField ff, List<Long> coeffs)
      {
      myField = ff;
      coefficients = new ArrayList<Long>(coeffs) ;
      }
   
   /*
    * static methods for generating Polynomial's from other data.
    */
   public static Polynomial getZeroPoly(FiniteField ff)
      {
      if (zero==null)
         zero = new Polynomial(ff, emptyArrayList());
      return zero ;
      }
      
   public static Polynomial getUnitPoly(FiniteField ff)
      {
      if (one==null)
         {
         one = new Polynomial(ff, oneEltArrayList(1L));
         }
      return one ;
      }

   public static Polynomial getConstanttPoly(FiniteField ff, long value)
      {
      switch((int) value)
         {
         case 0: return getZeroPoly(ff) ;
         case 1: return getUnitPoly(ff) ;
         default: return new Polynomial(ff, oneEltArrayList(value));
         }
      }

   /**
    * given a FiniteField, and string containing representations
    * of the coeffiecients of the polynimial in DECREASING degree
    * order, return the associated {@code Polynomial}.
    * @return - the resulting polynimial.
    */
   public static Polynomial parse(FiniteField ff, String s)
      {
      List<Long> inDAata = Arrays
       .stream(s.split(","))
       .map(String::trim)
       .map(Long::parseLong)
       .collect(java.util.stream.Collectors.toList());
      java.util.Collections.reverse(inDAata);
      return new Polynomial(ff, inDAata) ;
      }

   
   /*
    * getters
    */
   
   public ArrayList<Long> asCoefficients() { return coefficients; }
   public FiniteField getField() { return myField; }

   public int degree()
      {
      trim() ;
      return Math.max(0,coefficients.size()-1);
      }
   

   public long coefficientOfXToThe(int k)
      {
      if (k<0 || k>=coefficients.size())
         return 0 ;
      else return coefficients.get(k) ;
      }

   public Polynomial shift(int n)
      {
      if (n<=0)
         {
         FiniteField     ff = this.getField() ;
         ArrayList<Long> cs = this.asCoefficients() ;
         return new Polynomial(ff, cs.subList(-n, cs.size())) ;
         }
      else
         {
         ArrayList<Long> cs = new ArrayList<Long>(
            n+this.asCoefficients().size());
         for (int i=0;i<n;++i) cs.add(0L) ;
         cs.addAll(this.asCoefficients()) ;
         return new Polynomial(this.getField(), cs) ;
         }
      }
      

   /**
    * Add two polynomials in a given field
    * @param f
    * @param g
    * @return The sum of polynomials f and g
    */
   public static Polynomial polyAdd(Polynomial a, Polynomial b)
      {
      assert a.getField().equals(b.getField()) ;
      return al2P((x,y)->Polynomial.binOp(x,y,a.getField()::add))
       .apply(a,b) ;
      }
   
   /**
    * Subtract two polynomials in a given field
    * @param f
    * @param g
    * @return The difference of polynomials f and g
    */
   public static Polynomial polySubtract(Polynomial f, Polynomial g)
      {
      assert f.getField().equals(g.getField()) ;
      return al2P((x,y)->Polynomial.binOp(x,y,f.getField()::subtract))
       .apply(f,g) ;
      }

   /*
    * helper method for multiplication.
    * this method adds addand*(scale x^shift) to base.
    * It works at the level of ArrayLists rather than 
    * polys. 
    */
   private static void scaleShiftAndAddTo(
      ArrayList<Long> addand,
      FiniteField ff,
      long scale,
      int shift,
      ArrayList<Long> base)
      {
      int i=0, j=shift;
      while (j<base.size() && i<addand.size())
         {
         base.set(
            j, ff.add(base.get(j), ff.multiply(scale, addand.get(i)))) ;
         ++i; ++j; 
         }
      while (i<addand.size())
         {
         base.add(ff.multiply(scale, addand.get(i))) ;
         ++i;
         }
      }

   /**
    * Multiply two polynomials over the same field.
    * This implementation is O(f.degree() × g.degree()).
    * Clever people will someday implement Karatsuba.
    * @param f
    * @param g
    * @return The sum of polynomials f and g
    */
   public static Polynomial polyMultiply(
      Polynomial a1,
      Polynomial a2)
      {
      FiniteField ff = a1.getField() ;
      ArrayList<Long> cs = new ArrayList<Long>(a1.degree()+a2.degree()+1) ;
      int i=0 ;
      for (Long aa : a1.asCoefficients())
         {
         scaleShiftAndAddTo(a2.asCoefficients(),ff,aa,i,cs) ;
         ++i ;
         }
      return new Polynomial(ff, cs) ;
      }

	
   /**
    * Divides two polynomials over the same field.
    * This implementation is classic long division.
    * Clever people will someday inverses modulo x^n, and do division
    * by multiplication
    * @param dividend
    * @param divisor
    * @param remainder.  If this parameter is non-null, its coefficients
    *    are RESET by this method to be the coefficents of the remainer.
    * @return The quotient of polynomials dividend and divisor.
    */
   public static Polynomial polyDivide(
      Polynomial dividend,
      Polynomial divisor,
      Polynomial remainder)
      {
      dividend.trim() ;
      divisor.trim() ;
      final int divisorDegree = divisor.degree() ;
      final int dividendDegree = dividend.degree() ;
      FiniteField ff = dividend.getField() ;
      long divLeadingInverse = ff.inverse(
         divisor.coefficientOfXToThe(divisorDegree)) ;
      ArrayList<Long> rs = new ArrayList<Long>(dividend.asCoefficients()) ;
      ArrayList<Long> qs = new ArrayList<Long>(
         Math.max(0,dividendDegree-divisorDegree+1)) ;
      while (qs.size()<dividendDegree-divisorDegree+1)
         qs.add(0L) ;
      int jj = rs.size()-1 ;
      int kk = jj - divisorDegree ;
      while (kk>=0)
         {
         long rjC = rs.get(jj) ;
         if (rjC!=0)
            {
            long qkC = ff.multiply(rjC,divLeadingInverse) ;
            qs.set(kk,qkC) ;
            scaleShiftAndAddTo(
               divisor.asCoefficients(),
               ff, ff.subtract(0,qkC), kk, rs) ;
            }
         --jj; --kk;
         }
      if (remainder!=null)
         {
         remainder.coefficients = rs ;
         remainder.trim();
         assert (divisorDegree==0 && remainder.degree()==0)
          || (remainder.degree()<divisorDegree) ;
         }
      Polynomial answer = new Polynomial(ff, qs) ;
      answer.trim() ;
      return answer ;
      }

	
	/**
	 * Find the greatest common divisor (GCD) of two polynomials
	 * @param polyA The first polynomial
	 * @param polyB The second polynomial
	 * @return The greatest common divisor (GCD) of poly1 and poly2
	 */
   public static Polynomial polyGCD(Polynomial polyA, Polynomial polyB)
      {
      if (polyB.degree()==0)
         return polyA.asMonic() ;
      else
         {
         Polynomial polyR = new Polynomial(polyA.getField(),
                                             oneEltArrayList(0)) ;
         polyDivide(polyA, polyB, polyR) ;
         return polyGCD(polyB, polyR) ;
         }
      }

	/**
	 * return a monic version of this polynomial (or the zero polynomial).
    * @return the monic version.
	 */
   public Polynomial asMonic()
      {
      FiniteField ff = this.getField() ;
      long ell = this.coefficientOfXToThe(this.degree()) ;
      if (ell==0) return getZeroPoly(ff) ;
      else return polyMultiply(
         this, getConstanttPoly(ff, ff.inverse(ell))) ;
      }
	
	/**
	 * Convert a {@code Polynomial} to a {@code String} representation.
    * The polynomial is printed with variable symbol x.
    * Coefficients are printed from in INCREASING order.
    * Terms coresponding 0 coefficients are suppressed entirely.
    * 1 comfficients are not explicity printed.
    * Explicit coefficients and powers of x are separated by "·".
    * @return the string version.
	 */
   public String toString()
      {
      StringBuilder sb = new StringBuilder() ;
      String comma = ""; 
      int degree = 0 ;
      for (Long ell:this.asCoefficients())
         {
         if (ell!=0)
            {
            sb.append(comma) ; 
            comma = "" ;
            if (ell!=1)
               {
               sb.append(String.format("%d",ell)) ;
               comma="·" ;
               }
            if (degree>0)
               sb.append(comma+"x") ;
            if (degree>1)
               sb.append(String.format("^%d",degree)) ;
            comma = " + ";
            }
         ++degree ;
         }
      return sb.toString() ;
      }
	
   
   private void trim()
      {
      int n=coefficients.size() ;
      while (n>0 && coefficients.get(n-1)==0)
         {
         coefficients.remove(n-1) ;
         --n ;
         }
      }

   private static BinaryOperator<Polynomial> al2P(
      BinaryOperator<ArrayList<Long>> g)
      {
      return (p1,p2) -> new Polynomial(
         p1.getField(),
         g.apply(p1.asCoefficients(), p2.asCoefficients())) ;
                                        
      }
   
   private static ArrayList<Long> binOp(ArrayList<Long> p1,
                                           ArrayList<Long> p2,
                                           BinaryOperator<Long> f)
         {
         final int n1 = Math.min(p1.size(),p2.size()) ;
         final int n2 = Math.max(p1.size(),p2.size()) ;
         ArrayList<Long> answer = new ArrayList<Long>(
            Math.max(p1.size(), p2.size())) ;
         for (int i=0;i<n2;++i)
            {
            Long x = (i<p1.size()) ? p1.get(i) : Long.valueOf(0) ;
            Long y = (i<p2.size()) ? p2.get(i) : Long.valueOf(0) ;
            answer.add(f.apply(x,y)) ;
            }
         return answer ;
         }
   
   private Polynomial asPoly(ArrayList<Long> coefficients)
         {
         return new Polynomial(this.getField(), coefficients) ;
         }

   private static ArrayList<Long> emptyArrayList()
      {
      return new ArrayList<Long>() ;
      }

   private static ArrayList<Long> oneEltArrayList(long v)
      {
      ArrayList<Long> answer = new ArrayList<Long>(1) ;
      answer.add(v) ;
      return answer;
      }
}

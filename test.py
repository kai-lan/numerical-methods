import linear_system.exact_sol as sol
import linear_system.least_square_sol as lsol
import numpy as np
import nonlinear_system.root_finding as rf
import math
import sympy as sp
from sympy.parsing.sympy_parser import parse_expr

def f(x):
    return 2 * x - math.cos(x)

def df(x):
    return 2 + math.sin(x)

def g(x):
    return math.exp(x) - 1/x


x = sp.Symbol('x')
expr = 2 * x - sp.cos(x)
print('f =', expr)
deriv = sp.diff(expr, x)
print('f\' =', deriv)
func = sp.lambdify(x, expr, 'numpy')
func_der = sp.lambdify(x, deriv, 'numpy')

string = "2*x - cos(x)"
func1 = parse_expr(string)
print('g =', func1)
func1_der = sp.diff(func1, x)
print('g\' =', func1_der)
func1 = sp.lambdify(x, func1, 'numpy')
func1_der = sp.lambdify(x, func1_der, 'numpy')

#x, iteration, err, summary = rf.bisection_method(f, 0, 1, tol=pow(10, -5))
#print(summary)

sol, iteration, err, summary = rf.newton_method(func1, func1_der, 0.5, tol=pow(10, -15), ite=2)
print(summary)
"""
For nonlinear system, root finding methods are implemented to find the roots.
-   Bisection method
-   Fixed point iteration(maybe)
-   Newton's method
-   Secant method
"""
import sys
from tabulate import tabulate

""" Find roots for scalar nonlinear equations """

def sign(x):
    if x <= 0:
        return False
    else:
        return True


''' bisection method: given a function with zero between a left end and a right end,
    and a tolerance level for f(x_k)
    --------------------------------------------------------------------------------
    Parameters:
    f: function
    left_end: left-end point
    right_end: right-end point
    ite: number of iterations as threshold
    tol: the tolerance of error for f(x) as threshold
    Return:
    mid: the root
    counter: number of iterations
    err: the error
    report: a summary of iterations
    ---------------------------------------------------------------------------------
    Note: specify at least one of ite and tol, or both, in which case both thresholds 
    will be satisfied.
'''
def bisection_method(f, left_end, right_end, ite = 0, tol = 0):
    counter = 0  # count the number of iterations
    summary = []  # record the values of root-searching in the iterations
    left = left_end
    right = right_end
    mid = (left + right) / 2
    if tol == 0:
        tol = abs(f(mid))
    summary.append([counter, mid])
    if sign(f(left)) == sign(f(right)):
        sys.exit('The function evaluated at the two end points are of the same sign.')
    elif sign(f(left)) == sign(f(mid)):
        left = mid
    else:
        right = mid
    while abs(f(mid)) >= tol or counter < ite:
        counter += 1
        mid = (left + right) / 2
        if sign(f(left)) == sign(f(mid)):
            left = mid
        else:
            right = mid
        summary.append([counter, mid])
    err = abs(f(mid))
    report = "-" * 85 + "\n"
    report += 'Bisection method: \n x = ' + str(mid) + ', iteration = ' + str(counter) \
              + ', absolute error = ' + str(err) + "\n"
    report += "-" * 85 + "\n"
    report += tabulate(summary, headers=['Iteration k', 'Bisection x(k)'],
                       tablefmt="github", floatfmt=(".16f"), numalign="center")
    return mid, counter, err, report




''' Newton' method: given a function continuously differentiable
    with an initial point, converging at second order
    ------------------------------------------------------------
    Parameters:
    f: function
    df: derivative of f
    start: the initial point
    ite: number of iterations as threshold
    tol: the tolerance of error for f(x) as threshold
    Return:
    x0: the root
    counter: number of iterations
    err: the error
    report: a summary of iterations
    ---------------------------------------------------------------------------------
    Note: specify at least one of ite and tol, or both, in which case both thresholds 
    will be satisfied.
'''
def newton_method(f, df, start, ite = 0, tol = 0):
    counter = 0
    x0 = start
    if tol == 0:
        tol = abs(f(x0))
    summary = [[counter, x0]]
    while abs(f(x0)) >= tol or counter < ite:
        counter += 1
        x1 = x0 - f(x0)/df(x0)
        summary.append([counter, x1])
        x0 = x1
    err = abs(f(x0))
    report = "-" * 85 + "\n"
    report += 'Newton\'s method: \n x = ' + str(x0) + ', iteration = ' + str(counter) \
              + ', absolute error = ' + str(err) + "\n"
    report += "-" * 85 + "\n"
    report += tabulate(summary, headers=['Iteration k', 'Bisection x(k)'],
                       tablefmt="github", floatfmt=(".16f"), numalign="center")
    return x0, counter, err, report

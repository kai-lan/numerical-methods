import numpy as np

# Solve a tridiagonal system Ax = b, where A consists of a, d, c 
# three diagonal lines from down to up.
def tridiagonal(a, d, c, b):
    n = len(b)
    x = np.zeros(n)
    for k in range(n-1):
        r = a[k]/d[k]
        d[k+1] -= r * c[k]
        b[k+1] -= r * b[k]
        
    x[n-1] = b[n-1]/d[n-1]
    for k in range(n-2, -1, -1):
        x[k] = (b[k] - c[k]*x[k+1])/d[k]
    
    return x

def f(x, y, i, j):
    return (y[i] - y[j])/(x[i] - x[j])

# Spline (cubic) interpolation with not-a-knot condition in R^2
# Return the coefficients of piecewise functions: 
# a - constant term, b - x, c - x^2, d - x^3
def not_a_knot(x, y):
    x = np.array(x)
    y = np.array(y)
    n = len(x) - 1
    # The coefficient of each piece of polynomial from low power to high
    # coefficients of constant
    a = y[0: n]; b = np.zeros(n); c = np.zeros(n+1); d = np.zeros(n)
    h = np.zeros(n)
    # Define the three diagonals for the trigonal system
    z = np.zeros(n-1)
    mid = np.zeros(n-1); top = np.zeros(n-2); bot = np.zeros(n-2)
    # The distance between two endpoints
    h[0: n] = x[1: n+1] - x[0: n]
    
    # The middle diagononal
    mid[0] = (h[1] + h[0])*(2*h[1] + h[0])
    mid[1: n-2] = 2*(h[2: n-1] + h[1: n-2])
    mid[n-2] = (h[n-1] + h[n-2])*(h[n-1]+2*h[n-2])
    # The bottom diagonal
    bot[0: n-3] = h[1: n-2]
    bot[n-3] = (h[n-1] + h[n-2])*(h[n-2] - h[n-1])
    # The top diagonal
    top[0] = (h[1] + h[0])*(h[1] - h[0])
    top[1: n-2] = h[2: n-1]
    
    z[0] = 3*h[1]*(f(x, y, 1, 2) - f(x, y, 0, 1)) 
    for k in range(1, n-2):
        z[k] = 3*(f(x, y, k+1, k+2) - f(x, y, k, k+1))
        
    # ceofficients of x^2
    c[1: n] = tridiagonal(bot, mid, top, z)
    c[0] = ((h[0] + h[1])*c[1] - h[0]*c[2])/h[1]
    c[n] = ( (h[n-2] + h[n-1])*c[n-1] - h[n-1]*c[n-2] )/h[n-2]
    # ceofficients of x
    for k in range(n):
        b[k] = f(x, y, k, k+1) - (h[k]/3)*(c[k+1] + 2*c[k])
    # coefficient of x^3
    d[0: n] = (c[1: n+1] - c[0: n])/(3*h[0: n])
    
    return [a, b, c, d]

# Return the value of interpolant evaluated at certain point
def not_a_knot_evaluate(x, y, t):
    a, b, c, d = not_a_knot(x, y)
    n = len(x) - 1
    result = []
    for s in t:
        if s < x[0]:
            result.append(a[0] + b[0]*(s-x[0]) + c[0]*pow(s-x[0], 2) + d[0]*pow(s-x[0], 3)) 
        elif s >= x[n]:
            result.append(a[n-1] + b[n-1]*(s-x[n-1]) + c[n-1]*pow(s-x[n-1], 2) + d[n-1]*pow(s-x[n-1], 3))
        else:
            for k in range(n):
                if x[k] <= s < x[k+1]:
                    result.append(a[k] + b[k]*(s-x[k]) + c[k]*pow(s-x[k], 2) + d[k]*pow(s-x[k], 3))
    return result

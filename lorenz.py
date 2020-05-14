"""
Lorenz system
"""
import linear_system.exact_sol as solve
from linear_system.least_square_sol import l2_norm
import numpy as np
import matplotlib.pyplot as plt

# Lorenz system
def func(x, y, z):
    return [sigma*(y - x), x*(rho - z) - y, x*y - beta*z]

# Sigma, beta, rho paramaters
sigma = 10
beta = 8/3
rho = 28

T = 100  # total time
h = pow(10, -3)  # step size
N = int(T/h)  # number of iterations

"""
Backward Euler's method
"""

x = np.zeros(N + 1)
y = np.zeros(N + 1)
z = np.zeros(N + 1)

# Initial condition
x[0] = 1  # 1
y[0] = -1  # -1
z[0] = 30  # 30

# Tolerance of error in root finding
tol = pow(10, -5)


def jacobian_F(x1):
    return [[- h * sigma - 1, h * sigma, 0],
            [h * (rho - x1[2]), - h - 1, - h * x1[0]],
            [h * x1[1], h * x1[0], - h * beta - 1]]


def F(x0, x1):
    return [x0[0] + h * sigma * (x1[1] - x1[0]) - x1[0],
            x0[1] + h * (x1[0] * (rho - x1[2]) - x1[1]) - x1[1],
            x0[2] + h * (x1[0] * x1[1] - beta * x1[2]) - x1[2]]


def next_x(v):
    v0 = v  # Initially, assume x(n+1)_0 = x(n)
    while l2_norm(F(v, v0)) >= tol:
        v1 = v0 + solve.gaussian_eli_partial_pivot_vec(jacobian_F(v), -np.array(F(v, v0)))
        v0 = v1
    return v0


for n in range(N):
    v = [x[n], y[n], z[n]]
    r = next_x(v)
    x[n + 1], y[n + 1], z[n + 1] = r[0], r[1], r[2]

fig1 = plt.figure()
ax = plt.subplot()
plt.title('Backward Euler', pad=2)
ax.plot(x, z)
ax.set_xlim(-20, 20)
ax.set_ylim(0, 50)

fig2 = plt.figure()
ax = plt.subplot()
ax.plot(y, z)

plt.show()
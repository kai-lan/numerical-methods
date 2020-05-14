"""
Solve a linear system for an exact solution up to machine precision
-   Diagonal system
-   Upper or lower triangular system
-   General system, with different level of singularity
"""
import input_error as err
import numpy as np


""" Solve a diagonal system
    -----------------------
    A: the system
    b: the vector
"""
def diagonal_sys(A, b):
    if len(A) != len(b):
        raise err.DimensionError(A, b)
    n = len(A)
    x = np.zeros(n)
    for i in range(n):
        x[i] = b[i] / A[i][i]
    return x


""" Solve a triangular system"""

""" Forward substitution
    --------------------
    L: the lower triangular system
    b: the vector
    unit: true/false to treat the diagonal entries of L as 1's
"""
def forward_sub(L, b, unit=False):
    if len(L) != len(b):
        raise err.DimensionError(L, b)
    n = len(L)
    x = np.zeros(n)
    if not unit:
        x[0] = b[0] / L[0][0]
    else:
        x[0] = b[0]
    for i in range(1, n):
        s = b[i]
        for j in range(0, i):
            s = s - L[i][j] * x[j]
        if not unit:
            x[i] = s / L[i][i]
        else:
            x[i] = s
    return x


""" Backward substitution
    ---------------------
    U: the upper triangular system
    b: the vector
"""
def backward_sub(U, b):
    if len(U) != len(b):
        raise err.DimensionError(U, b)
    n = len(U)
    x = np.zeros(n)
    x[n - 1] = b[n - 1] / U[n - 1][n - 1]
    for i in range(n - 2, -1, -1):
        s = b[i]
        for j in range(i + 1, n):
            s = s - U[i][j] * x[j]
        x[i] = s / U[i][i]
    return x


""" Solve a general system """

""" Naive Gaussian Elimination 
    --------------------------
    A: the system
    b: the vector
"""
""" Forward elimination """
def forward_eli(A, b):
    B = np.array(A)
    if len(B) != len(b):
        raise err.DimensionError(B, b)
    n = len(B)
    for k in range(0, n - 1):
        for i in range(k + 1, n):
            r = B[i][k] / B[k][k]
            B[i][k] = 0  # optional: easy to read
            for j in range(k + 1, n):
                B[i][j] = B[i][j] - r * B[k][j]
            b[i] = b[i] - r * b[k]
    return [B, b]
""" Naive gaussian elimination """
def naive_gaussian_eli(A, b):
    U, y = forward_eli(A, b)
    x = backward_sub(U, y)
    return x


""" LU decomposition
    ----------------
    A: system
    b: vector
"""
""" Decompose a matrix"""
def lu_decomp(A):
    B = np.array(A)
    n = len(B)
    for k in range(0, n - 1):
        for i in range(k + 1, n):
            B[i][k] = B[i][k] / B[k][k]
            for j in range(k + 1, n):
                B[i][j] = B[i][j] - B[i][k] * B[k][j]
    return B
""" Solve the linear system with LU decomposition"""
def lu_decomp_sol(A, b):
    B = lu_decomp(A)
    y = forward_sub(B, b, True)
    x = backward_sub(B, y)
    return x


""" Gaussian Elimination with Partial Pivoting
    ------------------------------------------
    A: system
    b: vector
"""
""" Find the maximum entry in the column A(k,k) to A(n,k) """
def find_max(A, k, n):
    maximum = abs(A[k][k])
    row = k
    for i in range(k + 1, n):
        if maximum < abs(A[i][k]):
            row = i
            maximum = abs(A[i][k])
    return row
""" swap row1 and row2 """
def swap_rows(A, b, row1, row2):
    row = np.array(A[row1])
    c = np.array(b[row1])
    A[row1] = A[row2]
    b[row1] = b[row2]
    A[row2] = row
    b[row2] = c
    return [A, b]
""" Gaussian Elimination with partial pivoting"""
def gaussian_eli_partial_pivot(A, b):
    B = np.array(A)
    if len(B) != len(b):
        raise err.DimensionError(B, b)
    n = len(B)
    for k in range(n - 1):
        l = find_max(B, k, n)
        swap_rows(B, b, k, l)
        for i in range(k + 1, n):
            r = B[i][k] / B[k][k]
            B[i][k] = 0
            for j in range(k + 1, n):
                B[i][j] = B[i][j] - r * B[k][j]
            b[i] = b[i] - r * b[k]
    x = backward_sub(B, b)
    return x


""" PLU decomposition
    -----------------
    A: system
    b: vector
"""
""" PLU decompose a matrix """
def plu_decomp(A):
    B = np.array(A)
    n = len(B)
    P = np.array(range(n))
    for k in range(n):
        l = find_max(B, k, n)
        swap_rows(B, P, k, l)
        for i in range(k + 1, n):
            B[i][k] = B[i][k] / B[k][k]
            for j in range(k + 1, n):
                B[i][j] = B[i][j] - B[i][k] * B[k][j]
    return B, P
""" Solve the system with PLU decomposition """
def plu_decomp_sol(A, b):
    B, P = plu_decomp(A)
    n =len(B)
    Pb = np.zeros(n)
    for k in range(n):
        Pb[k] = b[P[k]]
    y = forward_sub(B, Pb, True)
    x = backward_sub(B, y)
    return x


""" Gaussian Jordan Elimination
    ---------------------------
    A: system
    b: vector
"""
def gaussian_jordan_eli(A, b):
    B = np.array(A)
    n = len(B)
    x = np.zeros(n)
    for k in range(n - 1):
        for i in range(k + 1, n):
            r = B[i][k] / B[k][k]
            b[i] = b[i] - r * b[k]
            B[i][k] = 0  # optional
            for j in range(k + 1, n):
                B[i][j] = B[i][j] - r * B[k][j]
    for k in range(n - 1, 0, -1):
        for i in range(k):
            b[i] = b[i] - (B[i][k] / B[k][k]) * b[k]
            B[i][k] = 0  # optional
        x[k] = b[k] / B[k][k]
    x[0] = b[0] / B[0][0]
    return x


''' Find inverse of a matrix by LU decomposition
    --------------------------------------------
    A: the matrix
'''
def lu_decomp_inv(A):
    n = len(A)
    C = lu_decomp(A)
    B = []
    for k in range(n):
        e = np.zeros(n)
        e[k] = 1
        y = forward_sub(C, e, True)
        x = backward_sub(C, y)
        B.append(x)
    B = np.array(B).transpose()
    return B


""" Vectorized version to solve linear systems """

""" Vectorized Forward substitution
    -------------------------------
    L: lower triangular
    b: vector
    unit: true/false to treat the diagonal entries of L as 1's
"""
def forward_sub_vec(L, b, unit=False):
    n = len(L)
    x = np.zeros(n)
    if not unit:
        x[0] = b[0] / L[0][0]
    else:
        x[0] = b[0]
    for i in range(1, n):
        s = b[i] - np.dot(L[i], x)
        if not unit:
            x[i] = s / L[i][i]
        else:
            x[i] = s
    return x

""" Vectorized Forward substitution
    -------------------------------
    U: upper triangular
    b: vector
"""
def backward_sub_vec(U, b):
    n = len(U)
    x = np.zeros(n)
    x[n - 1] = b[n - 1] / U[n - 1][n - 1]
    for i in range(n - 2, -1, -1):
        s = b[i] - np.dot(U[i], x)
        x[i] = s / U[i][i]
    return x


""" Vectorized Naive Gaussian Elimination
    -------------------------------------
    A: system
    b: vector
"""
def naive_gaussian_eli_vec(A, b):
    B = np.array(A)
    n = len(B)
    for k in range(0, n - 1):
        for i in range(k + 1, n):
            r = B[i][k] / B[k][k]
            B[i][k + 1: n] = np.subtract(B[i][k + 1: n], r * B[k][k + 1: n])
            b[i] = b[i] - r * b[k]
    x = backward_sub_vec(B, b)
    return x


""" Vectorized LU decomposition to solve linear systems
    ---------------------------------------------------
    A: system
    b: vector
"""
def lu_decomp_vec(A):
    B = np.array(A)
    n = len(B)
    for k in range(0, n - 1):
        for i in range(k + 1, n):
            B[i][k] = B[i][k] / B[k][k]
            B[i][k + 1: n] = np.subtract(B[i][k + 1: n], B[i][k] * B[k][k + 1: n])
    return B
def lu_decomp_sol_vec(A, b):
    B = lu_decomp_vec(A)
    y = forward_sub_vec(B, b, True)
    x = backward_sub_vec(B, y)
    return x


""" Vectorized Gaussian Elimination with Partial Pivoting 
    -----------------------------------------------------
    A: system
    b: vector
"""
def gaussian_eli_partial_pivot_vec(A, b):
    B = np.array(A)
    n = len(B)
    for k in range(n - 1):
        l = find_max(B, k, n)
        swap_rows(B, b, k, l)
        for i in range(k + 1, n):
            r = B[i][k] / B[k][k]
            B[i][k + 1: n] = np.subtract(B[i][k + 1: n], r * B[k][k + 1: n])
            b[i] = b[i] - r * b[k]
    x = backward_sub_vec(B, b)
    return x


""" Vectorized PLU decompostion for solving linear systems
    ------------------------------------------------------
    A: system
    b: vector
"""
def plu_decomp_vec(A):
    B = np.array(A)
    n = len(B)
    P = np.array(range(n))
    for k in range(n):
        l = find_max(B, k, n)
        swap_rows(B, P, k, l)
        for i in range(k + 1, n):
            B[i][k] = B[i][k] / B[k][k]
            B[i][k + 1: n] = np.subtract(B[i][k + 1: n], B[i][k] * B[k][k + 1: n])
    return B, P

def plu_decomp_sol_vec(A, b):
    B, P = plu_decomp_vec(A)
    n = len(B)
    Pb = np.zeros(n)
    for k in range(n):
        Pb[k] = b[P[k]]
    y = forward_sub_vec(B, Pb, True)
    x = backward_sub_vec(B, y)
    return x

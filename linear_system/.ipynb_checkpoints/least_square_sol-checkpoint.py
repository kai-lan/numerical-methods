"""
Find the least square solution to a linear system with no exact solution
The following methods are implemented:
-   Cholesky decomposition
-   QR decomposition with Gran-Schmid algorithm and householder transform
"""
import numpy as np
import linear_system.exact_sol as sol


""" Cholesky decomposition to solve the normal equation
    A: the system
    b: the vector
"""
""" Given a SPD matrix, find the decomposed upper triangular matrix """
def cholesky_decomp(A):
    B = np.array(A)
    n = len(B)
    for k in range(0, n - 1):
        for i in range(k + 1, n):
            r = B[i][k] / B[k][k]
            B[i][k + 1: n] = np.subtract(B[i][k + 1: n], r * B[k][k + 1: n])
    for k in range(n):
        r = np.sqrt(B[k][k])
        B[k][k: n] = B[k][k: n] / r
    return B
""" Compute (A^T)A and (A^T)b """
def normal_equ(A, b):
    A_trans = np.array(A).transpose()
    b_trans = np.array(b).transpose()
    return [A_trans.dot(A), A_trans.dot(b_trans)]
""" Find the lease square solution using method of Cholesky decomposition """
def cholesky_method(A, b):
    B, z = normal_equ(A, b)
    G_trans = cholesky_decomp(B)
    G = G_trans.transpose()
    y = sol.forward_sub_vec(G, z)
    x = sol.backward_sub_vec(G_trans, y)
    return x


""" QR decomposition by Gran-Schmid orthogonalization 
    -------------------------------------------------
    A: system
    b: vector
"""
""" Find the L2 norm of the given vector """
def l2_norm(v):
    sum_ = 0
    for entry in v:
        sum_ += entry ** 2
    return np.sqrt(sum_)

""" Decompose the matrix A into an orthogonal matrix Q and an upper triangular R
    by Gran-schmid orthogonalization algorithm
"""
def qr_decomp_gran_sch(A):
    row = len(A)
    B = np.array(A).transpose()
    col = len(B)
    q = np.zeros((col, row))
    R = np.zeros((col, col))
    for j in range(col):
        q[j] = B[j]
        for i in range(j):
            R[i][j] = np.dot(q[i], B[j])
            q[j] = q[j] - R[i][j] * q[i]
        R[j][j] = l2_norm(q[j])
        q[j] = np.divide(q[j], R[j][j])
    return [q.transpose(), R]

""" Solve for least square solution by QR decomposition
    computing Q by Granch-Schmid orthogonalization
"""
def qr_gran_sch_method(A, b):
    Q, R = qr_decomp_gran_sch(A)
    c = np.array(Q).transpose().dot(b)
    x = sol.backward_sub_vec(R, c)
    return x


""" QR decomposition by Householder transformation
    -------------------------------------------------
    A: system
    b: vector
"""
""" Decompose the matrix A into an orthogonal matrix Q 
    and an upper triangular R by Householder transformation
"""
def qr_decomp_householder_trans(A):
    m = len(A)
    n = len(A[0])
    B = np.array(A)
    Q = np.zeros((m, m))
    for k in range(m):
        Q[k][k] = 1
    for k in range(n):
        a = B[k: m, k]
        e = np.zeros(m - k)
        e[0] = 1
        if a[0] > 0:
            u = a + l2_norm(a) * e
        else:
            u = a - l2_norm(a) * e
        u = u / l2_norm(u)
        u = np.array(u)
        B[k: m, k: n] = B[k: m, k: n] - 2 * np.outer(u, u).dot(B[k: m, k: n])
        Q[k: m, 0: m] = Q[k: m, 0: m] - 2 * np.outer(u, u).dot(Q[k: m, 0: m])
    Q = Q.transpose()
    R = B[0: n, 0: n]
    return [Q, R]

""" Solve for least square solution by QR decomposition
    computing Q, R by Householder transformation
"""
def qr_householder_trans_method(A, b):
    Q, R = qr_decomp_householder_trans(A)
    c = np.array(Q).transpose().dot(b)
    c = c[0: len(c)-1]
    x = sol.backward_sub_vec(R, c)
    return x
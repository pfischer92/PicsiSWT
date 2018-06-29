package utils;

/**
 * Matrix class (data type = double)
 * 
 * @author Christoph Stamm
 *
 */
public class Matrix {
	private double[][] m_data; // matrix
	
	/**
	 * Creates a matrix with m rows and n columns
	 * @param m number of rows
	 * @param n number of columns
	 */
	public Matrix(int m, int n) {
		m_data = new double[m][n];
	}
	
	/**
	 * Creates a matrix and uses data as internal matrix data
	 * @param data
	 */
	public Matrix(double[][] data) {
		m_data = data;
	}
	
	public int nRows() { return m_data.length; }
	public int nCols() { return m_data[0].length; }
	public double el(int i, int j) { return m_data[i][j]; }
	
	/**
	 * Matrix addition: A += m
	 * @param m adds matrix m to this
	 */
	public void plus(Matrix m) {
		assert nRows() == m.nRows() && nCols() == m.nCols() : "different dimensions";
		
		for (int i=0; i < nRows(); i++) {
			for (int j=0; j < nCols(); j++) {
				m_data[i][j] += m.m_data[i][j];
			}
		}
	}

	/**
	 * Matrix subtraction: A -= m
	 * @param m subtracts matrix m from this
	 */
	public void minus(Matrix m) {
		assert nRows() == m.nRows() && nCols() == m.nCols() : "different dimensions";
		
		for (int i=0; i < nRows(); i++) {
			for (int j=0; j < nCols(); j++) {
				m_data[i][j] -= m.m_data[i][j];
			}
		}
	}
	
	/**
	 * Matrix multiplication with scalar d: A = d*A
	 * @param d
	 */
	public void multiply(double d) {
		for (int i=0; i < nRows(); i++) {
			for (int j=0; j < nCols(); j++) {
				m_data[i][j] *= d;
			}
		}
	}
	
	/**
	 * Matrix-vector multiplication y = A*v, where A is this matrix and v the input vector
	 * @param v input vector
	 * @return resulting vector
	 */
	public double[] multiply(double[] v) {
		assert nCols() == v.length : "incompatible dimensions";
		double[] res = new double[nRows()];
		
		for (int i=0; i < nRows(); i++) {
			double sum = 0;
			for (int j=0; j < nCols(); j++) {
				sum += m_data[i][j]*v[j];
			}
			res[i] = sum;
		}
		return res;
	}
	
	/**
	 * Matrix-matrix multiplication A*m, where A is this matrix and m the input matrix
	 * @param m 
	 * @return resulting matrix
	 */
	public Matrix multiply(Matrix m) {
		assert nCols() == m.nRows() : "incompatible dimensions";
		Matrix res = new Matrix(nRows(), m.nCols());
		
		for (int i=0; i < res.nRows(); i++) {
			for (int j=0; j < res.nCols(); j++) {
				double sum = 0;
				for (int k=0; k < nCols(); k++) {
					sum += m_data[i][k]*m.m_data[k][j];
				}
				res.m_data[i][j] = sum;
			}
		}
		return res;
	}
	
	/**
	 * Matrix transposition
	 * @return transposed matrix
	 */
	public Matrix transpose() {
		Matrix res = new Matrix(nCols(), nRows());
		
		for (int i=0; i < nRows(); i++) {
			for (int j=0; j < nCols(); j++) {
				res.m_data[j][i] = m_data[i][j];
			}
		}
		return res;
	}
	
	/**
	 * Matrix inversion
	 * @return inverted matrix
	 */
	public Matrix inverse() {
		if (m_data[2][0] == 0 && m_data[2][1] == 0 && m_data[2][2] == 1) {
			double den = m_data[0][0]*m_data[1][1] - m_data[0][1]*m_data[1][0];
			assert den != 0 : "matrix is singular";
			
			return new Matrix(new double[][] {
				{ m_data[1][1]/den,-m_data[0][1]/den, (m_data[0][1]*m_data[1][2] - m_data[0][2]*m_data[1][1])/den }, 
				{-m_data[1][0]/den, m_data[0][0]/den, (m_data[0][2]*m_data[1][0] - m_data[0][0]*m_data[1][2])/den }, 
				{ 0, 0 , 1 }
			});
		} else {
			double den = m_data[0][0]*m_data[1][1]*m_data[2][2] + m_data[0][1]*m_data[1][2]*m_data[2][0] + m_data[0][2]*m_data[1][0]*m_data[2][1]
					   - m_data[0][0]*m_data[1][2]*m_data[2][1] - m_data[0][1]*m_data[1][0]*m_data[2][2] - m_data[0][2]*m_data[1][1]*m_data[2][0];
			assert den != 0 : "matrix is singular";

			return new Matrix(new double[][] {
				{ (m_data[1][1]*m_data[2][2] - m_data[1][2]*m_data[2][1])/den, (m_data[0][2]*m_data[2][1] - m_data[0][1]*m_data[2][2])/den, (m_data[0][1]*m_data[1][2] - m_data[0][2]*m_data[1][1])/den },
				{ (m_data[1][2]*m_data[2][0] - m_data[1][0]*m_data[2][2])/den, (m_data[0][0]*m_data[2][2] - m_data[0][2]*m_data[2][0])/den, (m_data[0][2]*m_data[1][0] - m_data[0][0]*m_data[1][2])/den },
				{ (m_data[1][0]*m_data[2][1] - m_data[1][1]*m_data[2][0])/den, (m_data[0][1]*m_data[2][0] - m_data[0][0]*m_data[2][1])/den, (m_data[0][0]*m_data[1][1] - m_data[0][1]*m_data[1][0])/den }
			});
		}
	}
	
	/**
	 * Creates a quadratic identity matrix of size n
	 * @param n
	 * @return identity matrix
	 */
	public static Matrix identy(int n) {
		Matrix res = new Matrix(n, n);
		
		for (int i=0; i < n; i++) {
			res.m_data[i][i] = 1;
		}
		return res;
	}
	
	/**
	 * Creates a 3x3 rotation matrix for 2D rotation with homogeneous coordinates
	 * @param alpha angle
	 * @return rotation matrix
	 */
	public static Matrix rotation(double alpha) {
		double cosa = Math.cos(alpha);
		double sina = Math.sin(alpha);
		
		return new Matrix(new double[][] {{ cosa, sina, 0 }, { -sina, cosa, 0 }, { 0, 0 , 1 }});
	}

	/**
	 * Creates a 3x3 translation matrix for 2D translations with homogeneous coordinates
	 * @param dx x translation
	 * @param dy y translation
	 * @return translation matrix
	 */
	public static Matrix translation(double dx, double dy) {
		return new Matrix(new double[][] {{ 1, 0, dx }, { 0, 1, dy }, { 0, 0 , 1 }});
	}

	/**
	 * Creates a 3x3 scaling matrix for 2D scaling with homogeneous coordinates
	 * @param sx x scaling factor
	 * @param sy y scaling factor
	 * @return scaling matrix
	 */
	public static Matrix scaling(double sx, double sy) {
		return new Matrix(new double[][] {{ sx, 0, 0 }, { 0, sy, 0 }, { 0, 0 , 1 }});
	}

	public static Matrix getVector3(double u, double v) {
		return new Matrix(new double[][] {{u}, {v}, {1}});
	}

}

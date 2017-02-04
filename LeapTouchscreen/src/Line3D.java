import com.leapmotion.leap.Vector;

import Jama.Matrix;

public class Line3D {

	Vector dir;
	Vector pos;

	public Line3D(Vector pos, Vector dir) {
		this.pos = pos;
		this.dir = dir;
	}

	public Vector getPOI(Line3D line) {

		double[][] vals = new double[2][3];
		double[][] ans = new double[3][1];
	
			
		for (int i = 0; i < 3; i++) {
			vals[0][i] = dir.get(i);
			vals[1][i] = -1*line.dir.get(i);
			ans[i][0] = pos.get(0)-line.pos.get(i);
		}
			
		
		Matrix A = new Matrix(vals);
		Matrix b = new Matrix(ans);
		Matrix x = A.solve(b);
		
		return x.get;

	}
}

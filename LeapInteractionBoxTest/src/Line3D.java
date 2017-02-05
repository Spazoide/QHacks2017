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

		double[][] vals = new double[3][2];
		double[][] ans = new double[3][1];
	
			
		for (int i = 0; i < 2; i++) {
			vals[i][0] = -1*dir.get(i);
			vals[i][1] = line.dir.get(i);
			ans[i][0] = pos.get(i)-line.pos.get(i);
		}
			
		
		Matrix A = new Matrix(vals);
		Matrix b = new Matrix(ans);
		Matrix x = A.solve(b);
		
		return pos.plus(dir.times((float) x.get(0,0)));

	}
}

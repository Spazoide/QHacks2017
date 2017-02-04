import java.awt.Point;

import com.leapmotion.leap.Vector;

public class Plane {
	Vector v1;
	Vector v2;
	Vector normal;
	float d;
	
	Vector[] vertices;
	Vector[] dirs;
	Line3D[] lineBounds; 	//4 lines representing the bounding sides of the plane
	
	

	public Plane(Vector[][] p) {
		v1 = new Vector(p[1][0].minus(p[0][0]));
		v2 = new Vector(p[2][0].minus(p[0][0]));
		
		vertices = new Vector[]{p[0][0],p[1][0],p[2][0],p[3][0]};
		dirs = new Vector[]{p[0][1],p[1][1],p[2][1],p[3][1]};
		
		lineBounds = new Line3D[]{new Line3D(p[0][0], v1),
								  new Line3D(p[0][0], v2),
								  new Line3D(p[1][0], v2),
								  new Line3D(p[2][0], v1)};
		
		
		
		normal = v1.cross(v2).normalized();
		
		d = 0;
		for (int i = 0; i < 3; i++) {
			d += normal.get(i) * p[0][0].get(i);
		}
		
		
	}

	public Vector getPOI(Vector pos, Vector dir) {
		float t = (d - normal.get(0) * pos.get(0) - normal.get(1) * pos.get(1) - normal.get(2) * pos.get(2))
				/ (normal.get(0) * dir.get(0) + normal.get(1) * dir.get(1) + normal.get(2) * dir.get(2));

		return new Vector(pos.get(0) + dir.get(0) * t, pos.get(1) + dir.get(1) * t, pos.get(2) + dir.get(2) * t);
	}
	
	private int map(float xmin, float xmax, int vmin, int vmax, float f) {
		return (int)((f - xmin) / (xmax - xmin) * (vmax - vmin) + vmin);
	}
	
	public int[] getPOIScaled(Vector pos, Vector dir, int width, int height){
		Vector currentPos = getPOI(pos, dir);
		Line3D xline = new Line3D(currentPos, v1);
		Line3D yline = new Line3D(currentPos, v2);
		
		float ymin = yline.getPOI(lineBounds[0]).getY();
		float ymax = yline.getPOI(lineBounds[3]).getY();
		float xmin = xline.getPOI(lineBounds[1]).getX();
		float xmax = xline.getPOI(lineBounds[2]).getX();
		
//		System.out.printf("X: %.2f - %.2f \t Y: %.2f - %.2f\n",xmin,xmax,ymin,ymax);
		return new int[] {map(xmin,xmax,0,width,currentPos.getX()), map(ymin,ymax,0,height,currentPos.getY())};
		
	}

}

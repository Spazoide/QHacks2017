import java.awt.Point;

import com.leapmotion.leap.Vector;

public class Plane {
	Vector v1;
	Vector v2;
	Vector normal;
	float d;
	
	Line3D[] lineBounds; 	//4 lines representing the bounding sides of the plane
	
	

	public Plane(Vector p1, Vector p2, Vector p3) {
		v1 = new Vector(p2.minus(p1));
		v2 = new Vector(p3.minus(p1));
		
		
		lineBounds = new Line3D[]{new Line3D(p1, v1),
								  new Line3D(p1, v2),
								  new Line3D(p2, v2),
								  new Line3D(p3, v1)};
		
		normal = v1.cross(v2).normalized();
		d = 0;
		for (int i = 0; i < 3; i++) {
			d += normal.get(i) * p1.get(i);
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
	
	public Vector getCorrectedDirection(Vector pos, Vector dir, Vector[] cornerDirections, int width, int height){
		double maxLen = Math.sqrt(width*width/4.+height*height/4);
		int[][] cornerCoords = { { 0, 0 }, { width, 0 }, { 0, height },
				{ width, height },
				{ width / 2, height / 2 } };
		
		double cornerWeights[] = new double[4];
		double sum = 0;
		
		for (int i = 0; i < cornerWeights.length; i++) {
			double tempDist = pos.distanceTo(lineBounds[])
			cornerWeights = 
		}
		
		return null;
	}
	

}

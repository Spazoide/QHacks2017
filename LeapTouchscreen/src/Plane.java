import com.leapmotion.leap.Vector;

public class Plane {
	Vector v1;
	Vector v2;
	Vector normal;
	float d;
	
	

	public Plane(Vector p1, Vector p2, Vector p3) {
		v1 = new Vector(p1.minus(p2));
		v2 = new Vector(p1.minus(p3));
		normal = v1.cross(v2);
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
	
	

}
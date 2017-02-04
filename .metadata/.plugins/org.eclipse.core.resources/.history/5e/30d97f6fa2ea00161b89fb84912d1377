import com.leapmotion.leap.Vector;

public class Plane {
	Vector v1;
	Vector v2;
	Vector normal;
	
	public Plane(Vector p1, Vector p2, Vector p3){
		v1 = new Vector(p1.minus(p2));
		v2 = new Vector(p1.minus(p3));
		normal = v1.cross(v2);
	}
}

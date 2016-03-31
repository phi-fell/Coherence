package io.proffitt.coherence.math;

public class AABB {
	public double	rx, ry, rz;
	public double	cx, cy, cz;
	public AABB(double w, double h, double l) {
		rx = w;
		ry = h;
		rz = l;
		cx = 0;
		cy = 0;
		cz = 0;
	}
	public AABB(AABB aabb, Transform t) {
		cx = aabb.cx + t.getPosition().x;
		cy = aabb.cy + t.getPosition().y;
		cz = aabb.cz + t.getPosition().z;
		rx = aabb.rx * t.getScale().x;
		ry = aabb.ry * t.getScale().y;
		rz = aabb.rz * t.getScale().z;
	}
	public AABB(float[] verts, int stride) {//assumes format of {x1,y1,z1,(stride values),...,xn,yn,zn,(stride values)}
		Vector3f min = new Vector3f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
		Vector3f max = new Vector3f(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
		for (int i = 0; i < verts.length; i += stride) {
			min.x = Math.min(min.x, verts[i]);
			min.y = Math.min(min.y, verts[i + 1]);
			min.z = Math.min(min.z, verts[i + 2]);
			max.x = Math.max(max.x, verts[i]);
			max.y = Math.max(max.y, verts[i + 1]);
			max.z = Math.max(max.z, verts[i + 2]);
		}
		Vector3f dim = max.minus(min).times(0.5f);
		Vector3f center = min.plus(dim);
		rx = dim.x;
		ry = dim.y;
		rz = dim.z;
		cx = center.x;
		cy = center.y;
		cz = center.z;
	}
	public double getBoundingSphereRadius() {
		return Math.sqrt((rx * rx) + (ry * ry) + (rz * rz));
	}
	public double getInnerSphereRadius() {
		double rxy = rx > ry ? rx : ry;
		return rxy > rz ? rxy : rz;
	}
}

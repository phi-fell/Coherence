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
	public AABB(float[] verts) {//assumes format of {x1,y1,z1,nx1,ny1,nz1,...,xn,yn,zn,nxn,nyn,nzn}
		Vector4f min = new Vector4f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, 0);
		Vector4f max = new Vector4f(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, 0);
		for (int i = 0; i < verts.length; i += 6) {
			min.x = Math.min(min.x, verts[i]);
			min.y = Math.min(min.y, verts[i + 1]);
			min.z = Math.min(min.z, verts[i + 2]);
			max.x = Math.max(max.x, verts[i]);
			max.y = Math.max(max.y, verts[i + 1]);
			max.z = Math.max(max.z, verts[i + 2]);
		}
		Vector4f dim = max.minus(min).times(0.5f);
		Vector4f center = min.plus(dim);
		rx = dim.x;
		ry = dim.y;
		rz = dim.z;
		cx = center.x;
		cy = center.y;
		cz = center.z;
	}
}

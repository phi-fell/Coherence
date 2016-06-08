package io.proffitt.coherence.world;

import io.proffitt.coherence.math.Vector3f;
import io.proffitt.coherence.resource.Shader;

public class Light{
	float		r, g, b, intensity;
	Vector3f	pos;
	public Light(Vector3f position, float red, float green, float blue, float strength) {
		pos = position;
		r = red;
		g = green;
		b = blue;
		intensity = strength;
	}
	public float getEffect(Vector3f loc){
		//TODO: finish up this function
		float mult = ((new Vector3f(r,g,b)).getLength() * intensity) / pos.minus(loc).getLenSq();
		mult /= mult+1;
		return mult;
	}
	public void bind(Shader s, int i) {
		s.setUniformFloat("lights[" + i + "].pos", pos.x, pos.y, pos.z);
		s.setUniformFloat("lights[" + i + "].color", r * intensity, g * intensity, b * intensity);
	}
}
package io.proffitt.coherence.ai;

import io.proffitt.coherence.math.Vector4f;

public class EnemyAI implements EntityAI {

	@Override
	public Vector4f getMoveVector(double dt) {
		Vector4f ret = new Vector4f(0.1f, 0, 0.1f , 1f/(float)dt);
		ret.divideByW();;
		return ret;
	}
}

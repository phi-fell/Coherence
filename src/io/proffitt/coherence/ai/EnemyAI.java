package io.proffitt.coherence.ai;

import io.proffitt.coherence.math.Vector3f;

public class EnemyAI implements EntityAI {
	@Override
	public Vector3f getMoveVector(double dt) {
		Vector3f ret = new Vector3f(0.1f * (float) dt, 0, 0.1f * (float) dt);
		return ret;
	}
}

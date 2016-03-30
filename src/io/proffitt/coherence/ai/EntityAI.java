package io.proffitt.coherence.ai;

import io.proffitt.coherence.math.Vector3f;

public interface EntityAI {
	public Vector3f getMoveVector(double dt);
}

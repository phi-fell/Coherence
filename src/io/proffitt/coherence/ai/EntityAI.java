package io.proffitt.coherence.ai;

import io.proffitt.coherence.math.Vector4f;

public interface EntityAI {
	public Vector4f getMoveVector(double dt);
}

package io.proffitt.coherence.world.component;

import io.proffitt.coherence.world.Entity;

public interface Component {
	default public void draw() {
		//do nothing
		//visible components must override this method.
	}
	default public void update() {
		//do nothing
		//many components should override this method.
	}
	default public void setParent(Entity entity){
		//do nothing
		//any component which needs to interact with others should override this method.
	}
}
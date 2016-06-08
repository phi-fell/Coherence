package io.proffitt.coherence.world.component;

public interface Component {
	default public void draw(){
		//do nothing
		//visible components must override this method.
	}
}

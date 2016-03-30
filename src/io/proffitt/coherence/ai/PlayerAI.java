package io.proffitt.coherence.ai;

import io.proffitt.coherence.graphics.Camera;
import io.proffitt.coherence.graphics.Window;
import io.proffitt.coherence.math.Vector3f;
import io.proffitt.coherence.resource.ResourceHandler;
import io.proffitt.coherence.settings.Configuration;

public class PlayerAI implements EntityAI {
	Window	w;
	Camera	c;
	public PlayerAI(Window wind, Camera cam) {
		w = wind;
		c = cam;
	}
	@Override
	public Vector3f getMoveVector(double dt) {
		if (ResourceHandler.get().getConfig("globals").get("console").getBool()) {
			return new Vector3f();
		} else {
			boolean forward = false;
			boolean backward = false;
			boolean left = false;
			boolean right = false;
			float speed = 5;
			boolean jumping = false;
			Configuration config = ResourceHandler.get().getConfig("keybindings");
			for (int i = 0; i < 350; i++) {
				String key = "" + i;
				if (speed < 50 && config.get(key) != null && config.get(key).getString().equals("SPRINT") && w.isKeyDown(i)) {
					speed = 50;
				}
				if (!jumping && config.get(key) != null && config.get(key).getString().equals("JUMP") && w.isKeyDown(i)) {
					jumping = true;
				}
				forward = forward || (config.get(key) != null && config.get(key).getString().equals("FORWARD") && w.isKeyDown(i));
				backward = backward || (config.get(key) != null && config.get(key).getString().equals("BACKWARD") && w.isKeyDown(i));
				left = left || (config.get(key) != null && config.get(key).getString().equals("LEFT") && w.isKeyDown(i));
				right = right || (config.get(key) != null && config.get(key).getString().equals("RIGHT") && w.isKeyDown(i));
			}
			int zMod = (forward == backward) ? 0 : (forward ? -1 : 1);
			int xMod = (left == right) ? 0 : (left ? -1 : 1);
			Vector3f ret = c.getMoveVector(zMod * speed, xMod * speed).plus(new Vector3f(0, jumping ? 1 : 0, 0));
			//c.translate(ret.x, ret.y, ret.z);
			return ret;
		}
	}
}

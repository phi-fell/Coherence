package io.proffitt.coherence.graphics;

import java.nio.FloatBuffer;

public class HDRCalculator implements Runnable {
	Thread						t;
	Float						HDRmaxMutex	= new Float(1f);
	private volatile boolean	running		= false;
	FloatBuffer					pbuffer;
	int							width, height;
	volatile float				HDRmax		= 1;
	public HDRCalculator() {
	}
	public void calculate(FloatBuffer data, int w, int h) {
		synchronized (this) {
			if (!running) {
				running = true;
			} else {
				return;
			}
		}
		width = w;
		height = h;
		pbuffer = data;
		t = new Thread(this, "HDR Calculator");
		t.start();
	}
	public float getValue() {
		float ret;
		synchronized (HDRmaxMutex) {
			ret = HDRmax;
		}
		return ret;
	}
	@Override
	public void run() {
		double avg = 0;
		float max = 0;
		pbuffer.clear();
		for (int i = 0; i < pbuffer.capacity(); i += 3) {
			float brightness = pbuffer.get(i) + pbuffer.get(i + 1) + pbuffer.get(i + 2);
			avg += Math.log1p(brightness);
			max = Math.max(max, brightness);
		}
		avg /= width * height;
		avg = Math.expm1(avg);
		float ABSOLUTE_MINIMUM_HDR = 0.3f;
		float ABSOLUTE_MAXIMUM_HDR = 10f;
		float newHDRmax = (float) Math.min(Math.max(Math.max(avg * 5, max / 5), ABSOLUTE_MINIMUM_HDR), ABSOLUTE_MAXIMUM_HDR);
		synchronized (HDRmaxMutex) {
			HDRmax = newHDRmax;
		}
		synchronized (this) {
			running = false;
		}
	}
}

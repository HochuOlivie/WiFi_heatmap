package sample;

/**
 * Helper class for frame rate calculation
 */
public class FpsCounter {

	final long[] frameTimes = new long[100];
	int frameTimeIndex = 0;
	boolean arrayFilled = false;
	double frameRate;

	double decimalsFactor = 1000; // we want 3 decimals
	
	public void update(long now) {

		long oldFrameTime = frameTimes[frameTimeIndex];
		frameTimes[frameTimeIndex] = now;
		frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;

		if (frameTimeIndex == 0) {
			arrayFilled = true;
		}

		if (arrayFilled) {

			long elapsedNanos = now - oldFrameTime;
			long elapsedNanosPerFrame = elapsedNanos / frameTimes.length;
			frameRate = 1_000_000_000.0 / elapsedNanosPerFrame;

		}
	}

	public double getFrameRate() {
		// return frameRate;
		return ((int) (frameRate * decimalsFactor)) / decimalsFactor; // reduce to n decimals
	}
	
}

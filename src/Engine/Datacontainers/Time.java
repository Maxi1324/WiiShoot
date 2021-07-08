package Engine.Datacontainers;

public class Time {

	private float timescale = 2;
	private float deltaTime = .01f;
	private float unscaledDeltaTime;
	private int fps = 0;

	private long lastcurrentTime = 0;

	private final float chechTime = .1f;

	private float counter;
	private int fpscount;

	public Time() {
		lastcurrentTime = System.currentTimeMillis();
	}

	public void calcDeltaTime() {
		long newdeltatime = System.currentTimeMillis();
		long time = newdeltatime;
		newdeltatime = lastcurrentTime - newdeltatime;
		deltaTime = -((float)newdeltatime ) / (float)1000;
		this.unscaledDeltaTime = deltaTime;
		deltaTime = deltaTime * timescale;
		lastcurrentTime = time;
//		fps = (int)(1/getDeltaTime());
		counter += deltaTime;
		fpscount++;
		if (counter >= chechTime) {
			fps = (int) ((float) fpscount * (float) (1 / chechTime));
			fpscount = 0;
			counter = 0;
		}
	}

	public int getFPS() {
		return fps;
	}

	public float getTimescale() {
		return timescale;
	}

	public float getDeltaTime() {
		return deltaTime;
	}

	public void setTimescale(float timescale) {
		this.timescale = timescale;
	}

	public float getUnscaledDeltaTime() {
		return unscaledDeltaTime;
	}

	public void setUnscaledDeltaTime(float unscaledDeltaTime) {
		this.unscaledDeltaTime = unscaledDeltaTime;
	}
	
	
}

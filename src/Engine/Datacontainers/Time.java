package Engine.Datacontainers;

public class Time{

	private float timescale = 1;
	private  float deltaTime = 1;

	private long lastcurrentTime = 0;
	
	public Time() {
		lastcurrentTime = System.currentTimeMillis();
	}

	public void calcDeltaTime() {
		long newdeltatime = System.currentTimeMillis();
		long time = System.currentTimeMillis();
		newdeltatime = lastcurrentTime - newdeltatime;
		deltaTime = -(newdeltatime * timescale) / 1000;
		lastcurrentTime = time;
	}

	public float getTimescale() {
		return timescale;
	}

	public float getDeltaTime() {
		return deltaTime;
	}
	
}

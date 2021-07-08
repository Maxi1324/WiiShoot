package Engine;

import java.util.ArrayList;

import Engine.Components.Allgemein.Colliders.BoxCollider;
import Engine.Components.Allgemein.Colliders.Collider;
import Engine.Datacontainers.Vector3;

public class ColliderManager {

	private final int MAXTHREADS = 10;

	protected ArrayList<Collider> colliders = new ArrayList<Collider>();
	private ArrayList<SubColManager> threads = new ArrayList<ColliderManager.SubColManager>();

	public ColliderManager() {
		for (int i = 0; i < MAXTHREADS; i++) {
			threads.add(new SubColManager(i));
		}
	}

	public void startProcesColls() {
		processColls();
	}

	private void processColls() {
		for (Collider cols : colliders) {
			cols.setIsCollidingReal(false);
		}
		int proThread = colliders.size() / MAXTHREADS + 1;
		for (int i = 0; i < MAXTHREADS; i++) {
			threads.get(i).startProcessCols(proThread);
		}

		for (boolean fertig = false; fertig;) {
			int sum = 0;
			for (SubColManager subcolman : threads) {
				if (subcolman.getFertig())
					sum++;
			}
			if (sum == colliders.size())
				fertig = true;
		}
//		for (Collider cols : colliders) {
//			if (cols != null)
//				cols.calcColliding(colliders, new Vector3());
//		}
	}

	public ArrayList<Collider> getColliders() {
		return colliders;

	}

	private class SubColManager {
		int threadNum;
		boolean fertig;
		
		int proThread;
		boolean start;
		public SubColManager(int threadNum) {
			this.threadNum = threadNum;
//			Thread thread = new Thread(() -> {
//				while(true) {
//					if(start) {
//						System.out.println("da");
//						start = false;
//						processCols(proThread);
//					}
//				}
//			});
//			thread.start();
		}

		public void startProcessCols(int proThread) {
			this.fertig = false;
//			this.proThread = proThread;
//			start = true;
			processColls(proThread);
		}

		private void processColls(int proThread) {
			for (int i = 0; i < proThread; i++) {
				if (colliders.size() <= (proThread * threadNum) + i)
					break;
				Collider col = colliders.get((proThread * threadNum) + i);
				col.calcColliding(colliders, new Vector3());
			}
		}

		public boolean getFertig() {
			return fertig;
		}
	}
}

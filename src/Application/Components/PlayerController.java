package Application.Components;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import Application.GameObjects.Player;
import Application.Scenes.StartMenu;
import Engine.Component;
import Engine.World;

public class PlayerController extends Component {

	private Player player;
	private int controller;

	public PlayerController(int controller) {
		this.controller = controller;
	}

	@Override
	public void start() {
		super.start();
		player = (Player) getParrent();
	}

	@Override
	public void update() {
		super.update();
		int rotation = (int) getTransform().getRotation().getY();
		rotation = rotation - (rotation / 360 * 360);
		if (rotation < 0)
			rotation = 360 + rotation;
		float hori = World.getInputSystem().getAxis("Hori" + controller);
		float verti = World.getInputSystem().getAxis("Verti" + controller);
		boolean a = World.getInputSystem().getButton("A" + controller);
		boolean b = World.getInputSystem().getButton("B" + controller);
		if (StartMenu.isOnline() && !StartMenu.isHost()) {
			try {
				DatagramSocket socket = new DatagramSocket();
				byte[] buf = (player + ";" + hori + ";" + verti + ";" + a + ";" + b).getBytes();
//				DatagramPacket packet = new DatagramPacket(buf,0, buf.length,);
//				socket.send(packet);
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (rotation == 0) {
			if (hori > .5f) {
				player.turnRight();
			}
			if (hori < -.5f) {
				player.turnLeft();
			}
		}
		if (rotation == 90) {
			if (verti > .5f) {
				player.turnLeft();
			}
			if (verti < -.5f) {
				player.turnRight();
			}
		}
		if (rotation == 180) {
			if (hori > .5f) {
				player.turnLeft();
			}
			if (hori < -.5f) {
				player.turnRight();
			}
		}
		if (rotation == 270) {
			if (verti > .5f) {
				player.turnRight();
			}
			if (verti < -.5f) {
				player.turnLeft();
			}
		}
		if (a) {
			if (player.shoot() && controller > 0 && controller < 5) {
				World.getInput().rumbleGamePad(controller - 1, .1f, .1f, 100);
			}
		}
		if (b) {
			if (player.shootBulletPlus() && controller > 0 && controller < 5) {
				World.getInput().rumbleGamePad(controller - 1, .2f, .2f, 100);
			}
		}
	}

	public static void move(float hori, float verti, boolean a, boolean b, Player player) {
		int rotation = (int) player.getTransform().getRotation().getY();
		rotation = rotation - (rotation / 360 * 360);
		if (rotation < 0)
			rotation = 360 + rotation;

		if (rotation == 0) {
			if (hori > .5f) {
				player.turnRight();
			}
			if (hori < -.5f) {
				player.turnLeft();
			}
		}
		if (rotation == 90) {
			if (verti > .5f) {
				player.turnLeft();
			}
			if (verti < -.5f) {
				player.turnRight();
			}
		}
		if (rotation == 180) {
			if (hori > .5f) {
				player.turnLeft();
			}
			if (hori < -.5f) {
				player.turnRight();
			}
		}
		if (rotation == 270) {
			if (verti > .5f) {
				player.turnRight();
			}
			if (verti < -.5f) {
				player.turnLeft();
			}
		}
//		if (a) {
//			if (player.shoot() && controller > 0 && controller < 5) {
//				World.getInput().rumbleGamePad(controller - 1, .1f, .1f, 100);
//			}
//		}
//		if (b) {
//			if (player.shootBulletPlus() && controller > 0 && controller < 5) {
//				World.getInput().rumbleGamePad(controller - 1, .2f, .2f, 100);
//			}
//		}
	}
}

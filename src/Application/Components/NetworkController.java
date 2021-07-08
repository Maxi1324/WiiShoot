package Application.Components;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.sun.org.apache.xml.internal.security.transforms.Transform;

import Engine.Component;
import Engine.World;
import Engine.Datacontainers.Vector3;
import Application.Datacontrainer.PlayerDat;
import Application.GameObjects.Bullet;
import Application.GameObjects.Player;
import Application.Scenes.Map00;
import Application.Scenes.StartMenu;;

public class NetworkController extends Component {

	private final float MAXABWEICHUNG = .5f;
	private boolean running;

	private ArrayList<PlayerDat> dats;

	public NetworkController(ArrayList<PlayerDat> dat) {
		this.dats = dat;
	}

	@Override
	public void start() {
		running = true;
		DatagramSocket s = StartMenu.getDsocket();
		super.start();
		if (StartMenu.isHost()) {
			String nach = StartMenu.getNachricht();
			String[] nachs = nach.split(":");
			for (int i = 0; i < nachs.length; i++) {
				String[] nachwerte = nachs[i].split(";");
				ArrayList<Player> plays = ((Map00) getParrent().getParrenScene()).getPlayers();
				new Thread(() -> {
					while (running) {
						try {
							String newMessage = "";
							for (int j = 0; j < plays.size(); j++) {
								Player play = plays.get(j);
								Vector3 pos = play.getTransform().getPosition();
								Vector3 rot = play.getTransform().getRotation();
								String str = play.getPlayernum() + ";" + pos.getX() + ";" + pos.getY() + ";"
										+ pos.getZ() + ";" + rot.getX() + ";" + rot.getY() + ";" + rot.getZ() + ";"
										+ play.getBulls() + ";";
								newMessage += str + ":";
							}
							newMessage = newMessage.substring(0, newMessage.length() - 1);
							byte[] buf = newMessage.getBytes();
							InetAddress address;
							address = InetAddress.getByName(nachwerte[0].substring(1));
							DatagramPacket packet = new DatagramPacket(buf, 0, buf.length, address,
									Integer.parseInt(nachwerte[1]));
							s.send(packet);

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();

			}
			new Thread(() -> {
				while (running) {
					try {
						ArrayList<Player> plays = ((Map00) getParrent().getParrenScene()).getPlayers();
						byte[] buf2 = new byte[300];
						DatagramPacket packet2 = new DatagramPacket(buf2, buf2.length);
						s.receive(packet2);
						String input = new String(buf2);
						String[] inputs2 = input.split(";");
						float[] inputs = strArrToIntArr(inputs2);
						for (int j = 0; j < plays.size(); j++) {
							if (plays.get(j).getPlayernum() == inputs[0]) {
								Player play = plays.get(j);
								PlayerController.move(inputs[1], inputs[2], inputs[3] == 1, inputs[4] == 1, play);
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
		} else {
			new Thread(() -> {
				String[] nachwerte = StartMenu.getNachricht().split(";");
				try {
					InetAddress address = InetAddress.getByName(nachwerte[0].substring(1));

					while (running) {
						try {
							byte[] buf = new byte[300];
							DatagramPacket packet = new DatagramPacket(buf, buf.length);
							s.receive(packet);
							ArrayList<Player> players = ((Map00) getParrent().getParrenScene()).getPlayers();
							String res = new String(buf);
							String[] playerdats = res.split(":");
							ArrayList<Player> usedPlays = (ArrayList<Player>) ((Map00) getParrent().getParrenScene())
									.getPlayers().clone();
							for (String playerdat : playerdats) {
//							System.out.println(res);
								float[] dat = strArrToIntArr(playerdat.split(";"));
								Player play = players.get((int) dat[0]);
								usedPlays.remove(play);
								Vector3 pos = new Vector3(dat[1], dat[2], dat[3]);
								Vector3 rot = new Vector3(dat[4], dat[5], dat[6]);
								if (Vector3.distance(pos, play.getTransform().getPosition()) > MAXABWEICHUNG)
									play.getTransform().setPosition(pos);
								if (Vector3.distance(rot, play.getTransform().getRotation()) > MAXABWEICHUNG)
									play.getTransform().setRotation(rot);
							}
							for (Player play : usedPlays) {
								play.hit();
							}
//							s.receive(p);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					for (PlayerDat dat : dats) {
						if (dat.getController() != -1) {
							int controller = dat.getController();
							float hori = World.getInputSystem().getAxis("Hori" + controller);
							float verti = World.getInputSystem().getAxis("Verti" + controller);
							boolean a = World.getInputSystem().getButton("A" + controller);
							boolean b = World.getInputSystem().getButton("B" + controller);
							String str = dat.getPlayer() + 4 + ";" + hori + ";" + verti + ";" + ((a) ? 1 : 0) + ";"
									+ ((b) ? 1 : 0);
							byte[] buf = str.getBytes();
							DatagramPacket packet = new DatagramPacket(buf, 0, buf.length, address,
									Integer.parseInt(nachwerte[1]));
							s.send(packet);
						}
					}
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}).start();
		}
	}

	private static float[] strArrToIntArr(String[] strs) {
		float[] intArr = new float[strs.length];
		for (int i = 0; i < strs.length; i++) {
			try {
				intArr[i] = Float.parseFloat(strs[i]);
			} catch (NumberFormatException e) {
				intArr[i] = 0;
			}
		}
		return intArr;
	}
}

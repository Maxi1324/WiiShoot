package Application.Datacontrainer;

public class PlayerDat {
	private byte controller;
	private byte player;
	
	public PlayerDat(byte controller, byte player) {
		super();
		this.controller = controller;
		this.player = player;
	}
	
	public PlayerDat() {
	}

	public byte getController() {
		return controller;
	}

	public void setController(byte controller) {
		this.controller = controller;
	}

	public byte getPlayer() {
		return player;
	}

	public void setPlayer(byte player) {
		this.player = player;
	}

}
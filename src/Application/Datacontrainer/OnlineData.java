package Application.Datacontrainer;

import java.net.Socket;

public class OnlineData {
	private String ip;
	private String port;
	private boolean isHost;
	private String gameName;
	private Socket server;

	public OnlineData(String ip, String port, boolean isHost, String name, Socket server) {
		super();
		this.ip = ip;
		this.port = port;
		this.isHost = isHost;
		this.gameName = name;
		this.server = server;
	}

	public Socket getServer() {
		return server;
	}

	public void setServer(Socket server) {
		this.server = server;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public boolean isHost() {
		return isHost;
	}

	public void setHost(boolean isHost) {
		this.isHost = isHost;
	}

}

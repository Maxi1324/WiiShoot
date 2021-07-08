package Engine.Components.Allgemein;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import Engine.Component;
import Engine.World;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class AudioPlayer extends Component {

	private MediaPlayer player;
	private Media media;
	
	private String str;
	private boolean autoplayer;
	private boolean loop;
	private boolean isPlaying;
	
	public AudioPlayer(String str,boolean autoplay,boolean loop) {
		this.autoplayer = autoplay;
		this.str = str;
		this.loop = loop;
	}
	
	@Override
	public void start() {
		media = new Media(new File(str).toURI().toString());
		player = new MediaPlayer(media);
		player.seek(Duration.ZERO);
		if(loop)player.setCycleCount(MediaPlayer.INDEFINITE);
		player.setAutoPlay(autoplayer);
		if(autoplayer)isPlaying=true;
	}
	
	@Override
	public boolean onDelete() {
		player.stop();
		return super.onDelete();
	}
	
	@Override
	public void update() {
	}
	
	public void play() {
		if(player == null)start();
		player.seek(Duration.ZERO);
		if(player != null) {
			player.play();
			isPlaying= true;
		}
	}
	
	public void stop() {
		if(player != null) {
			player.stop();
			isPlaying = false;
		}
	}
	
	public String getStr() {
		return str;
	}

	public MediaPlayer getPlayer() {
		return player;
	}

	public void setPlayer(MediaPlayer player) {
		this.player = player;
	}

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

	public boolean isAutoplayer() {
		return autoplayer;
	}

	public void setAutoplayer(boolean autoplayer) {
		this.autoplayer = autoplayer;
	}

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}
	
	
}

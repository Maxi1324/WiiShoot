package Application.sonstiges;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import Application.Datacontrainer.OnlineData;
import Application.Scenes.StartMenu;
import Engine.World;
import Engine.Datacontainers.SimpleFunction;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;

public class OnlineDialog {
	public OnlineDialog(World world, SimpleFunction func) {
		try {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Onlinegame?");
			alert.setHeaderText("Do you want to play a onlinegame");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				TextInputDialog dialog = new TextInputDialog("localhost");
				dialog.setTitle("ip?");
				dialog.setHeaderText("Enter the Ip of the Server");
				Optional<String> result2 = dialog.showAndWait();
				if (result2.isPresent()) {
					String[] strs = result2.get().split(":");
					String ip = "localhost";
					String port = "13000";
					if (strs.length == 2) {
						ip = strs[0];
						port = strs[1];
					}
					Socket server = null;
					server = new Socket(ip, Integer.parseInt(port));
					
					
					List<String> choices = new ArrayList<>();
					choices.add("create a new Game"); 
					send(server, "showGa");
					String str = getMessage(server);
					String[] strs1 = str.split(":");
					String sesName =null;
					for (String str1 : strs1) {
						String[] strs2 = str1.split(";");
						if (strs2.length == 2) {
							choices.add("name: " + strs2[0] + " HostIP: " + strs2[1]);
							sesName = strs2[0];
						}
					}

					ChoiceDialog<String> dialog3 = new ChoiceDialog<>("create a new Game", choices);
					dialog3.setTitle("JoinOrCreate");
					dialog3.setHeaderText("Choose on Option");
					dialog3.setContentText("You can shoose an existing Onlinegame or create a new one");

					Optional<String> result3 = dialog3.showAndWait();
					OnlineData data = null;
					if (result3.isPresent()) {
						switch (result3.get()) {
						case "create a new Game":
							TextInputDialog dialog4 = new TextInputDialog("cool game");
							dialog4.setTitle("GameName?");
							dialog4.setHeaderText("Enter a name for your game");
							Optional<String> result4 = dialog4.showAndWait();
							if (result4.isPresent()) {
								send(server, "create;" + result4.get());
								data = new OnlineData(ip, port, true, result4.get(), server);
							} else {
								func.function();
							}
							break;
						default:
							send(server, "join00;"+sesName);
							data = new OnlineData(ip, port, false,sesName, server);
							break;
						}
						world.changeScene(new StartMenu(data));
					} else {
						func.function();
					}
				} else {
					func.function();
				}
			} else {
				func.function();
			}
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	public static String getMessage(Socket s) throws IOException{
		InputStreamReader in = new InputStreamReader(s.getInputStream());
		BufferedReader bf = new BufferedReader(in);
		String read = bf.readLine();
		return read;
	}
	
	public static String getMessagetime(Socket s) throws IOException, TimeoutException{
		InputStreamReader in = new InputStreamReader(s.getInputStream());
		BufferedReader bf = new BufferedReader(in);
		String read = bf.readLine();
		return read;
	}
	
	
	public static void send(Socket s, String str) throws IOException {
		PrintWriter pr = new PrintWriter(s.getOutputStream());
		pr.println(str);
		pr.flush();
	}
	
}

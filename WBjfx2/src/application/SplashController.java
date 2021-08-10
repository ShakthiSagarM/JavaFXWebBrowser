package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SplashController implements Initializable{
	@FXML
	private StackPane rootPane;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		new SplashScreen().start();
	}
	
	class SplashScreen extends Thread{
		@Override
		public void run()
		{
			try {
				Thread.sleep(1000);
				Platform.runLater(new Runnable() {
					@Override 
					public void run() {
						Parent root = null;
						try { 
							root = FXMLLoader.load(getClass().getResource("Login.fxml"));
						}  catch (IOException e) {
							e.printStackTrace();
						}
						Scene scene = new Scene(root);		
						Stage stage = new Stage();
						stage.getIcons().add(new Image("rv.png"));
						stage.setTitle("JavaFX Web Browser - Login");
						stage.setResizable(false);
						stage.setScene(scene);
						stage.show();
				
						rootPane.getScene().getWindow().hide();
						
					}
			});
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}

package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {
    
    TabPane root;
            
    @Override
    public void start(Stage stage) throws IOException {
    	Parent intro = FXMLLoader.load(getClass().getResource("SplashScreen.fxml"));
    	Scene scene = new Scene(intro);
        stage.getIcons().add(new Image("rv.png"));
		stage.setTitle("JavaFX Web Browser");
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
                
    }

   

    public static void main(String[] args) {
        launch(args);
    }
    
}
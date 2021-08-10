package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class LoginController implements Initializable  {
	
	@FXML
	private Button loginbtn,singupbtn;
	@FXML
	private Label statuslabel;
	@FXML
	private TextField usernameTF;
	@FXML
	private PasswordField passwordPF;
	
	public void login (ActionEvent event) {
		if(usernameTF.getText().isBlank()== false && passwordPF.getText().isBlank()== false) {
			validlogin();
			//statuslabel.setText("Login Successful.");
		}
		else {
			statuslabel.setText("Invalid Entry. Enter again.");
		}
	}
	
	public void signup(ActionEvent event) throws IOException {
		Stage prevstage=(Stage) statuslabel.getScene().getWindow();
		prevstage.close();
		Stage stage = new Stage();
		Parent signup = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
		Scene scene = new Scene(signup);		
		stage.getIcons().add(new Image("rv.png"));
		stage.setTitle("JavaFX Web Browser - SignUp");
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
	}

	private void validlogin() {
		DatabaseConnection connectNow = new DatabaseConnection();
		Connection connectDB =connectNow.getConnection();
		
		String verifyLogin = "SELECT count(1) FROM useracc WHERE username = '"+usernameTF.getText()+"' AND password ='"+passwordPF.getText()+"'";
		
		try {
			Statement statement = connectDB.createStatement();
			ResultSet queryResult = statement.executeQuery(verifyLogin);
			
			while(queryResult.next()) {
				if(queryResult.getInt(1)==1) {
					//statuslabel.setText("Congo");
					Stage prevstage=(Stage) statuslabel.getScene().getWindow();
					prevstage.close();
					FXMLLoader loader = new FXMLLoader(getClass().getResource("Intro.fxml"));
					Parent root = (Parent) loader.load();
					IntroController ic = loader.getController();
					ic.getUsername(usernameTF.getText());
					Scene scene = new Scene(root);		
					Stage stage = new Stage();
					stage.getIcons().add(new Image("rv.png"));
					stage.setTitle("JavaFX Web Browser");
					stage.setResizable(false);
					stage.setScene(scene);
					stage.show();
				}else {
					statuslabel.setText("Invalid Entry. Enter again.");
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

}

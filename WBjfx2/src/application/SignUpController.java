package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
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

public class SignUpController implements Initializable {
	
	@FXML
	private Button loginbtn,singupbtn;
	@FXML
	private Label statuslabel;
	@FXML
	private TextField usernameTF,firstnameTF,lastnameTF;
	@FXML
	private PasswordField passwordPF,cpasswordPF;
	
	
	public void login(ActionEvent event) throws IOException
	{
		Stage prevstage=(Stage) statuslabel.getScene().getWindow();
		prevstage.close();
		Stage stage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		Scene scene = new Scene(root);		
		stage.getIcons().add(new Image("rv.png"));
		stage.setTitle("JavaFX Web Browser - SignUp");
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
	}
	
	public void register (ActionEvent event) {
		if(usernameTF.getText().isBlank()== false && firstnameTF.getText().isBlank()== false && lastnameTF.getText().isBlank()== false && passwordPF.getText().isBlank()== false  && cpasswordPF.getText().isBlank()== false  ) {
			//statuslabel.setText("Login Successful.");
			if(passwordPF.getText().equals(cpasswordPF.getText()))
			{
				validregister();
			}
			else {
				statuslabel.setText("Passwords dont match.");
			}
		}
		else {
			statuslabel.setText("Invalid Entry. Enter again.");
		}
	}
	
	private void validregister(){
		DatabaseConnection connectNow = new DatabaseConnection();
		Connection connectDB =connectNow.getConnection();
		
		String firstname= firstnameTF.getText();
		String lastname= lastnameTF.getText();
		String username= usernameTF.getText();
		String password= passwordPF.getText();
		
		String p1= "INSERT INTO useracc(firstname,lastname,username,password) VALUES('";
		String p2= firstname + "','"+lastname+"','"+username+"','"+password+"')";
		String insertReg = p1+p2;
		Statement statement = null;
		try {
			statement = connectDB.createStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			statement.executeUpdate(insertReg);
			statuslabel.setText("You are registered successfully. Click on Login.");	
			String Usertab= "CREATE TABLE `javafx`.`"+usernameTF.getText()+"` (`id` INT NOT NULL AUTO_INCREMENT, `title` VARCHAR(100) NOT NULL, `url` VARCHAR(600) NOT NULL, PRIMARY KEY (`id`),UNIQUE INDEX `idnew_table_UNIQUE` (`id` ASC) VISIBLE)";
			statement.executeUpdate(Usertab);
			String UserBM = "CREATE TABLE `javafx`.`"+usernameTF.getText()+"_bookmarks` (`id` INT NOT NULL AUTO_INCREMENT,`title` VARCHAR(45) NOT NULL,`url` VARCHAR(600) NOT NULL,UNIQUE INDEX `url_UNIQUE` (`url` ASC) VISIBLE,PRIMARY KEY (`id`),UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE);";
			statement.executeUpdate(UserBM);
		}
		catch(SQLIntegrityConstraintViolationException e) {
			e.printStackTrace();
			statuslabel.setText("The Username is already taken.");
		} catch (SQLException e) {
			e.printStackTrace();
			statuslabel.setText("The Username is already taken.");
		}
		
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

}

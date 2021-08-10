package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

public class HistController implements Initializable{
	@FXML
	private TextArea console;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		console.setEditable(false);

	}
	
	public void fromBC(String text)
	{
		console.appendText(text);
		console.appendText("\n");
	}
}

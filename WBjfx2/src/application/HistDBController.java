package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.util.Callback;

public class HistDBController implements Initializable{
	@FXML
	private TableView<history> histTv;
	@FXML
	private TableColumn<history,String> titleTC;
	@FXML
	private TableColumn<history,String> urlTC;
	@FXML
	private TableColumn copyTC;
	private String username;
	private ObservableList<history> list;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//console.setEditable(false);
	}
	@FXML
	private void popuTab(ActionEvent Event) {
		
		try {
			//System.out.println(username);
			DatabaseConnection connectNow = new DatabaseConnection();
			Connection connectDB =connectNow.getConnection();
			list = FXCollections.observableArrayList();
			String query= "SELECT title,url FROM "+username;
			Statement statement;
			statement = connectDB.createStatement();
			ResultSet queryResult = statement.executeQuery(query);
			while(queryResult.next()) {
				history h =new history();
				h.setUrl(queryResult.getString("url"));
				h.setTitle(queryResult.getString("title"));
				list.add(h);
			}
			titleTC.setCellValueFactory(new PropertyValueFactory<>("title"));
			urlTC.setCellValueFactory(new PropertyValueFactory<>("url"));
			histTv.setItems(list);
			Callback<TableColumn<history,String>,TableCell<history,String>> cellFactory=(param)->{
				final TableCell<history,String> cell = new TableCell<history,String>(){
					@Override
					public void updateItem(String item, boolean empty) {
							super.updateItem(item, empty);
							if(empty) {
								setGraphic(null);
								setText(null);
							}
							else {
								final Button copybtn = new Button("Copy URL");
								copybtn.setOnAction(event->{
								history h=getTableView().getItems().get(getIndex());
								Alert a= new Alert(Alert.AlertType.INFORMATION);
								a.setContentText(h.getUrl());
								a.show();
								Clipboard clipboard = Clipboard.getSystemClipboard();
								ClipboardContent content = new ClipboardContent();
								content.putString(h.getUrl());
								clipboard.setContent(content);
								});
								setGraphic(copybtn);
								setText(null);
							}
					};
				};
				return cell;
			};
			
			copyTC.setCellFactory(cellFactory);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void getUsername(String un)
    {
    	username= un;
    	//System.out.println(username);
    }

}

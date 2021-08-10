package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class IntroController implements Initializable {

	
	TabPane root;
	@FXML
	Button btn;
	private Stage stage;
	public String username;
	


	    final void addNewTab() throws IOException{
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("Browser.fxml"));
			 Parent browser = (Parent) loader.load();
			BrowserController BController = loader.getController();
			BController.getUsername(username);
	            Tab browserTab = new Tab("New Tab", browser);
	            root.getTabs().add(root.getTabs().size() - 1, browserTab);
	            root.getSelectionModel().select(browserTab);
	    }
	    
	    
	    public void getUsername(String un)
	    {
	    	username= un;
	    }

		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
				btn.setOnAction(event -> {
					Parent browser =null;
					try {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("Browser.fxml"));
						 browser = (Parent) loader.load();
						BrowserController BController = loader.getController();
						BController.getUsername(username);
						//System.out.println(username);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					Tab browserTab = new Tab("New Tab", browser);
					Tab addTab = new Tab("+", null);
					addTab.setClosable(false);        
					addTab.setOnSelectionChanged(new EventHandler<Event>() {
						@Override
						public void handle(Event event) {
							try {
								addNewTab();
							} catch (IOException e) {
								e.printStackTrace();
							} 
						}
					});
					root = new TabPane(browserTab, addTab);
		        	stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		        	Scene scene = new Scene(root);
		        	stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		        		@Override
		            	public void handle(WindowEvent t) {
		            		Platform.exit();
		                	System.exit(0);
		            	}
		        	});
		        	stage.getIcons().add(new Image("rv.png"));
					stage.setTitle("JavaFX Web Browser - "+ username);
					stage.setResizable(false);
					stage.setScene(scene);
					stage.show();
		    });
			
		}
		
	}

package application;


import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.JOptionPane;
 
public class BrowserController implements Initializable {
	
	private int n; 
	List<Double> list = new ArrayList<>(Arrays.asList(0.25, 0.5, 0.75, 1.00 , 1.25 , 1.50 ,1.75 ,2.00));
	ListIterator<Double> i = list.listIterator();
	@FXML
	TabPane bmTP;
    @FXML
    BorderPane browserBP;
    @FXML
    WebView browserWV;
    private WebEngine engine;
    @FXML
    TextField addressBarTF;
    @FXML
    ProgressIndicator progressPI;
    @FXML
    Label statusL;
    private WebHistory history;
    private String homePage;
    private String hissite,histit;
    private double webZoom;
    public String username,urlDB,title;
	

    
    
    private void init() {
    	n=3;
    	webZoom = list.get(n);
    	engine =browserWV.getEngine();
    	homePage = "https://www.google.com/";
		addressBarTF.setText(homePage);
		loadPage();
        browserWV.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>(){
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                statusL.setText("loading... " + browserWV.getEngine().getLocation());                
                progressPI.setVisible(true);
                if(newValue == Worker.State.SUCCEEDED) {
                    addressBarTF.setText(browserWV.getEngine().getLocation());
                    addtodb();
                    statusL.setText("loaded");
                    progressPI.setVisible(false);
                    if(browserBP.getParent() != null) {
                        TabPane tp = (TabPane)browserBP.getParent().getParent();                           
                        for(Tab tab : tp.getTabs()) {
                            if(tab.getContent() == browserBP) {
                            	String title = browserWV.getEngine().getTitle();
                                tab.setText(title);
                                break;
                            }
                        }                                                
                    }
                }
                
            }
            
        });       
		
	}
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	  
    }    
    
	@FXML
    private void browserBackButtonAction(ActionEvent event) {
        if(browserWV.getEngine().getHistory().getCurrentIndex() <= 0) {
            return;
        }
        browserWV.getEngine().getHistory().go(-1);        
    }
    
    @FXML
    private void browserForwardButtonAction(ActionEvent event) {
        if((browserWV.getEngine().getHistory().getCurrentIndex() + 1) >= browserWV.getEngine().getHistory().getEntries().size()) {
            return;
        }
        browserWV.getEngine().getHistory().go(1);
    }
    
    @FXML
    private void browserGoButtonAction(ActionEvent event) {
        String url = addressBarTF.getText().trim();
        if(url.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No URL provided.");
            return;
        }
        if(!url.startsWith("http://") && !url.startsWith("https://")) { 
        	if(!url.startsWith("www."))
        	{
        		JOptionPane.showMessageDialog(null, "Only URLs to be entered in the AddressBar.");
        		return;
        	}
        	else {
        		url = "https://"+url;
        	}
        }
        addressBarTF.setText(url);
        loadPage();
        
        
    }
    @FXML
    private void displayHistory(ActionEvent event) throws IOException {
    	
		FXMLLoader loader = new FXMLLoader(getClass().getResource("History.fxml"));
		Parent hist = (Parent) loader.load();
		HistController hisController = loader.getController();
		history = engine.getHistory();
		ObservableList<WebHistory.Entry> entries = history.getEntries();	
		for(WebHistory.Entry entry : entries) {	
			String his = entry.getUrl()+" "+ entry.getLastVisitedDate();
			hisController.fromBC(his);
		}
		Stage stage = new Stage();
		Scene scene = new Scene(hist);
    	
    	stage.getIcons().add(new Image("rv.png"));
		stage.setTitle("JavaFX Web Browser - "+username+ " - Tab History");
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
		
        
    }
    @FXML 
    private void pastHist(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("HistDB.fxml"));
		Parent histDB = (Parent) loader.load();
		HistDBController hisController = loader.getController();
		hisController.getUsername(username);
		Stage stage = new Stage();
		Scene scene = new Scene(histDB);
    	stage.getIcons().add(new Image("rv.png"));
		stage.setTitle("JavaFX Web Browser - "+username+ " - User History [From Database]");
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
 	   
    }
    
    public void getUsername(String un) {
    	username  = un;
    	init();
    }
    
    



	@FXML
    private void browserStopReloadButtonAction(ActionEvent event) {                
        if(browserWV.getEngine().getLoadWorker().isRunning()) {
            browserWV.getEngine().getLoadWorker().cancel();
            statusL.setText("loaded");
            progressPI.setVisible(false);            
        } else {            
            browserWV.getEngine().reload();
        }
        
    }
    
    @FXML
    private void browserHomeButtonAction(ActionEvent event) {
        browserWV.getEngine().loadContent("<html><title>New Tab</title></html>");
        addressBarTF.setText(homePage);
		loadPage();       
    }
    @FXML
    private void zoomIn(ActionEvent event) {
    	n=n+1;
		webZoom=list.get(n);
		browserWV.setZoom(webZoom);
    }
    @FXML
    private void zoomOut(ActionEvent event) {
    	n=n-1;
		webZoom=list.get(n);
		browserWV.setZoom(webZoom);
    }
    private void loadPage() {
		engine.load(addressBarTF.getText());
		
	}
    private void addtodb()
    {
    	hissite=browserWV.getEngine().getLocation();
		histit=browserWV.getEngine().getTitle();
		DatabaseConnection connectNow = new DatabaseConnection();
		Connection connectDB =connectNow.getConnection();
		String url= hissite;
		String title= histit;
		
		String inserthis= "INSERT INTO `javafx`.`"+username+"` (`title`, `url`) VALUES ('"+title+"', '"+url+"')";
		
		Statement statement = null;
		try {
			statement = connectDB.createStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			statement.executeUpdate(inserthis);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    private void loadBM(ActionEvent event) {
        List<history> l = new ArrayList<>();
    	bmTP.getTabs().clear();
    	Tab tb = new Tab("->");
    	tb.setClosable(false);
    	DatabaseConnection connectNow = new DatabaseConnection();
		Connection connectDB =connectNow.getConnection();
		String sqlQuery = "SELECT title,url FROM "+username+"_bookmarks";
		List<Tab> tablist = new ArrayList<>(); 
		ContextMenu contextMenu = new ContextMenu();
		
        MenuItem menuItem1 = new MenuItem("copy");
        contextMenu.getItems().add(menuItem1);
		ResultSet resultSet;
		Statement statement;
		try {
			statement = connectDB.createStatement();
			resultSet = statement.executeQuery(sqlQuery);
			//statusL.setText("Bookmark loaded.");
			while (resultSet.next()) {
				urlDB=resultSet.getString("url");
				title = resultSet.getString("title");
				history h =new history();
				h.setTitle(resultSet.getString("title"));
				h.setUrl(resultSet.getString("url"));
				l.add(h);
				
				Tab tab= new Tab(title);
				tablist.add(tab);
				tab.setContextMenu(contextMenu);
				tab.setOnCloseRequest(e -> {
					String delQ= "DELETE FROM "+username+"_bookmarks WHERE title='"+title+"'";
					
					try {
						statement.executeUpdate(delQ);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					statusL.setText("Bookmark deleted.");
	            }
	    );
				tab.setOnSelectionChanged(e->{
					if(tab.isSelected()) {
						for(history model : l) {
				            String t= model.getTitle();
				            String u = model.getUrl();
				            String t1=tab.getText();
				            if(t1.equals(t)) {
				            	Alert a= new Alert(Alert.AlertType.INFORMATION);
				            	a.setTitle("Bookmark URL copied");
								a.setContentText(h.getUrl());
								a.show();
								Clipboard clipboard = Clipboard.getSystemClipboard();
								ClipboardContent content = new ClipboardContent();
								content.putString(u);
								clipboard.setContent(content);
								statusL.setText("Bookmark URL copied to clipboard.");
				            }
				        }
					}
				});

			}
			bmTP.getTabs().add(tb);
			bmTP.getTabs().addAll(tablist); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
    }

	@FXML
    private void addBM(ActionEvent event) {
    	hissite=browserWV.getEngine().getLocation();
		histit=browserWV.getEngine().getTitle();
		DatabaseConnection connectNow = new DatabaseConnection();
		Connection connectDB =connectNow.getConnection();
		String url= hissite;
		String title= histit;		
		Statement statement = null;
		try {
			statement = connectDB.createStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			String inserthis= "INSERT INTO `javafx`.`"+username+"_bookmarks` (`title`, `url`) VALUES ('"+title+"', '"+url+"')";
			statusL.setText("Bookmark added successfully.");
			statement.executeUpdate(inserthis);
		} catch(SQLIntegrityConstraintViolationException e) {
			e.printStackTrace();
			statusL.setText("It is already bookmarked.");
		} catch (SQLException e) {
			e.printStackTrace();
			statusL.setText("It is already bookmarked.");
		}
		
    }
  
}
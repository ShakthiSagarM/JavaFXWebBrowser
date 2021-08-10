package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class history {
	private final StringProperty url = new SimpleStringProperty();
	private final StringProperty title = new SimpleStringProperty();
	public String getTitle() {
		return title.get();
	}
	public void setTitle(String value) {
		title.set(value);
	}
	public StringProperty titleProperty() {
		return title;
	}

	public String getUrl() {
		return url.get();
	}
	public void setUrl(String value) {
		url.set(value);
	} 
	public StringProperty urlProperty() {
		return url;
	}

	
	
}

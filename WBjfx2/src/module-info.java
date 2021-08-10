module WBjfx {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.web;
	requires javafx.graphics;
	requires javafx.base;
	requires java.logging;
	requires java.desktop;
	requires java.sql;
	requires mysql.connector.java;
	
	opens application to javafx.graphics, javafx.fxml,javafx.base;
}

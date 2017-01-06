package wb;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class WebBrowser extends Application {

	private Scene scene;
	private Button goButton, backButton, forwardButton,
			 addbookmarkButton;
	private ComboBox<String> bookmarksComboBox;
	private Menu fileMenu,helpMenu;
	private TextField addressField;
	private WebView webView;
	private WebEngine webEngine;

	private String homeAddress = "www.google.ca";
	private ArrayList<String> addresses = new ArrayList<String>();
	private int addressPointer = -1;

	@Override
	public void start(Stage stage) throws Exception {
		System.out.println(Thread.currentThread().getName());

		// The vertical box that will hold two horizontal boxes.
		VBox vBox = new VBox();
		vBox.setAlignment(Pos.CENTER);

		// Horizontal boxes that will host buttons and address field.
		HBox hBox0 = new HBox();
		hBox0.setAlignment(Pos.CENTER);
		HBox hBox1 = new HBox();
		hBox1.setAlignment(Pos.TOP_LEFT);

		// Buttons for navigation.

		MenuBar mainBar = new MenuBar();
		MenuBar mainBar2= new MenuBar();
		goButton = new Button("Go");
		backButton = new Button("Back");
		
		forwardButton = new Button("Forward");
		
		fileMenu = new Menu("File");
		helpMenu=new Menu("Help");
		
		new Button("Help");
		
		MenuItem getHelp = new MenuItem("Get Help for Java Class");
		
		
		MenuItem showHist = new MenuItem("Show History");
	
		MenuItem about = new MenuItem("About");
		helpMenu.getItems().addAll(getHelp,showHist,about);
		
		
		addbookmarkButton = new Button("Add bookmarks");
		
		MenuItem openItem = new MenuItem("Open");
		openItem.setOnAction(event -> System.out.println("Clicked open")  );
		
		MenuItem exitItem = new MenuItem("Exit");
		exitItem.setOnAction(event -> { System.out.println("Bye!"); Platform.exit(); } );
		
		fileMenu.getItems().addAll(openItem, exitItem);

		// ComboBox for bookmarks
		bookmarksComboBox = new ComboBox<String>();
		bookmarksComboBox.setMaxWidth(200);
		bookmarksComboBox.setMinWidth(200);
		bookmarksComboBox.setPromptText("Bookmarks");
//		bookmarksComboBox.valueProperty().addListener(bookmarks);

		// Add listeners to the buttons.		
		goButton.setOnAction(go);	
		backButton.setOnAction(back);
		forwardButton.setOnAction(forward);
		addbookmarkButton.setOnAction(addbookmark);

		// The TextField for entering URLs.
		addressField = new TextField("Enter URLs here...");
		addressField.setPrefColumnCount(50);
		addressField.setOnAction(go);
		mainBar.getMenus().addAll(fileMenu);
		mainBar2.getMenus().addAll(helpMenu);
		// Add all out navigation nodes to the hbox.
		hBox0.getChildren().addAll(backButton, addressField,addbookmarkButton, forwardButton);
		hBox1.getChildren().addAll(mainBar, bookmarksComboBox,mainBar2);
		vBox.getChildren().addAll(hBox1, hBox0);

		// WebView that displays the page.
		webView = new WebView();

		// The engine that manages the pages.
		webEngine = webView.getEngine();
		webEngine.setJavaScriptEnabled(true);
		homeAddress = extractAddress(homeAddress);
		webEngine.load("http://" + homeAddress);

		BorderPane root = new BorderPane();
		root.setPrefSize(800, 600);

		// Add every node into the BorderPane.
		root.setTop(vBox);
		root.setCenter(webView);

		// The scene is where all the actions in JavaFX take place. A scene
		// holds
		// all Nodes, whose root node is the BorderPane.
		scene = new Scene(root);

		// the stage hosts the scene.
		stage.setTitle("Web Browser");
		stage.setScene(scene);

		loadRandomAddress(homeAddress);


		stage.show();
	}

	private EventHandler<ActionEvent> reload = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			webEngine.reload();
			resetButtons();
		}
	};

	private EventHandler<ActionEvent> go = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			String address = null;
			loadRandomAddress(address);
		}
	};
	
	
	private EventHandler<ActionEvent> home = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			loadRandomAddress(homeAddress);
		}
	};
	
	private EventHandler<ActionEvent> openbookmark = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			
			String address = bookmarksComboBox.getSelectionModel().getSelectedItem();
			loadRandomAddress(address);		
		}
	};
	
	private void loadRandomAddress(String address) {
		if(address == null)
			address = addressField.getText();
		address = extractAddress(address);
		System.out.println(address);
		webEngine.load("http://" + address);
		addressField.setText(webEngine.getLocation());

		removeObsoleteAddresses();
		addresses.add(address);
		resetButtons();
	}

	private void removeObsoleteAddresses() {
		addressPointer++;
		while (addresses.size() - 1 >= addressPointer) {
			addresses.remove(addresses.size() - 1);
		}
	}
	

	private EventHandler<ActionEvent> back = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {

			addressPointer--;
			if (addressPointer >= 0) {
				loadPointedAddress();
			} else {
				addressPointer = 0;
			}
			resetButtons();
		}
	};

	private EventHandler<ActionEvent> forward = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {

			addressPointer++;
			if (addressPointer <= addresses.size() - 1) {
				loadPointedAddress();
			} else {
				addressPointer = addresses.size() - 1;
			}
			resetButtons();
		}
	};

	private void loadPointedAddress() {
		System.out.println(addresses.get(addressPointer));
		webEngine.load("http://" + addresses.get(addressPointer));
		addressField.setText(webEngine.getLocation());
	}

	private EventHandler<ActionEvent> sethome = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {

			homeAddress = addressField.getText();
			homeAddress = extractAddress(homeAddress);
			System.out.println(homeAddress);
			resetButtons();

		}
	};
	
	private EventHandler<ActionEvent> addbookmark = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {

			String address = webEngine.getLocation();
			address = extractAddress(address);
			System.out.println(address);
			bookmarksComboBox.getItems().add(address);
		}
	};

	private void resetButtons() {
		System.out.println(addresses);
		System.out.println(addressPointer);

		if (addressPointer <= 0)
			backButton.setDisable(true);
		else
			backButton.setDisable(false);

		if (addressPointer >= addresses.size() - 1)
			forwardButton.setDisable(true);
		else
			forwardButton.setDisable(false);
	}

	private String extractAddress(String fullAddress) {
		String result = fullAddress;
		if (fullAddress.startsWith("http://") ) {
			result = fullAddress.substring("http://".length());
		}
		else if (fullAddress.startsWith("https://")) {
			result = fullAddress.substring("https://".length());
		} 
		return result;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
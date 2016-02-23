import Util.ProductType;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	@Override
    public void start(Stage primaryStage) throws Exception {
		//Initialise ProductType sets
		ProductType.initialize();
		
       primaryStage.setTitle("Intermätt");
       SplitPane myPane = (SplitPane) FXMLLoader.load(getClass().getResource("fxml/main.fxml"));
       Scene scene = new Scene(myPane);
       primaryStage.setScene(scene);
       primaryStage.show();
       primaryStage.setMinWidth(1366);
       primaryStage.setMinHeight(768);
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}

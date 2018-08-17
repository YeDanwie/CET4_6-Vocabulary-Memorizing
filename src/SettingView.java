import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class SettingView {
	private BorderPane bPane;
	private Button clear_memorizing;
	private Button clear_exercising;
	
	public SettingView() {
		VBox vbox = new VBox();
		
		//清空单词记忆按钮
		clear_memorizing = new Button("清空单词记忆进度");
		clear_memorizing.setStyle("-fx-background-radius: 20;"
								+ "-fx-font-size: 20;");
		vbox.getChildren().add(clear_memorizing);
		VBox.setMargin(clear_memorizing, new Insets(150, 0, 20, 200));
		clear_memorizing.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				Main.delete_memorizing = true;//设置标志，等到程序关闭后进行删除工作
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("提示框");
				alert.setHeaderText(null);
				alert.setContentText("设定成功！\n程序将在关闭后清空单词记忆进度");
				alert.getDialogPane().setStyle("-fx-font-size:15");
				alert.show();
			}
		});
		
		//清空单词练习按钮
		clear_exercising = new Button("清空单词练习进度");
		clear_exercising.setStyle("-fx-background-radius: 20;"
								+ "-fx-font-size: 20;");
		vbox.getChildren().add(clear_exercising);
		VBox.setMargin(clear_exercising, new Insets(40, 0, 20, 200));
		clear_exercising.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				Main.delete_exercising = true;//设置标志，等到程序关闭后进行删除工作
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("提示框");
				alert.setHeaderText(null);
				alert.setContentText("设定成功！\n程序将在关闭后清空单词练习进度");
				alert.getDialogPane().setStyle("-fx-font-size:15");
				alert.show();
			}
		});
		
		bPane = new BorderPane();
		bPane.setCenter(vbox);
	}
	
	public BorderPane getSettingPane() {
		return bPane;
	}
}

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
		
		//��յ��ʼ��䰴ť
		clear_memorizing = new Button("��յ��ʼ������");
		clear_memorizing.setStyle("-fx-background-radius: 20;"
								+ "-fx-font-size: 20;");
		vbox.getChildren().add(clear_memorizing);
		VBox.setMargin(clear_memorizing, new Insets(150, 0, 20, 200));
		clear_memorizing.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				Main.delete_memorizing = true;//���ñ�־���ȵ�����رպ����ɾ������
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("��ʾ��");
				alert.setHeaderText(null);
				alert.setContentText("�趨�ɹ���\n�����ڹرպ���յ��ʼ������");
				alert.getDialogPane().setStyle("-fx-font-size:15");
				alert.show();
			}
		});
		
		//��յ�����ϰ��ť
		clear_exercising = new Button("��յ�����ϰ����");
		clear_exercising.setStyle("-fx-background-radius: 20;"
								+ "-fx-font-size: 20;");
		vbox.getChildren().add(clear_exercising);
		VBox.setMargin(clear_exercising, new Insets(40, 0, 20, 200));
		clear_exercising.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				Main.delete_exercising = true;//���ñ�־���ȵ�����رպ����ɾ������
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("��ʾ��");
				alert.setHeaderText(null);
				alert.setContentText("�趨�ɹ���\n�����ڹرպ���յ�����ϰ����");
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

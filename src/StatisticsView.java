import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * @description 
 * ͳ����ϰ���ݵ�ҳ�棬��ҳ����ʾ�û���������������ñ�״ͼ��ʾ����ٷֱ�
 */
public class  StatisticsView {
	private BorderPane statisticsPane;
	public static int CORRECT_COUNT = 0, WRONG_COUNT = 0;//��¼�û�������ȷ�ʹ��������
	private Button start_button;
	public static PieChart pieChart;
	public static Text process_text;
	
	public StatisticsView(ArrayList<String> vocabulary_list) {
		statisticsPane = new BorderPane();
		statisticsPane.setPadding(new Insets(30, 30, 30, 30));
		
		//���Ʊ���
		BackgroundImage bImage = new BackgroundImage(new Image("file:Images\\blue_sky.jpg"),
				BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		statisticsPane.setBackground(new Background(bImage));
		
		//��ʾ��ϰ��Ŀ����
		process_text = new Text("���Ѿ���ϰ�� " + (CORRECT_COUNT+WRONG_COUNT) + " ����"
								+ "\n���лش�׼ȷ������ͼ��ʾ:");
		process_text.setFont(Font.font("", 20));
		process_text.setFill(Color.WHITE);
		statisticsPane.setTop(process_text);
		
		//���Ʊ�״ͼ
		pieChart = new PieChart(); 
		pieChart.setLabelsVisible(false);
		pieChart.setScaleX(0.8); pieChart.setScaleY(0.8);
		pieChart.setStyle("-fx-font-size:18");
		ObservableList<PieChart.Data> data_list = FXCollections.observableArrayList();
		
		/*
		 * �û���ϰ��Ŀ����Ϊ0ʱ������ٷֱȳ���Ϊ0��������쳣����˶��һ���ж�
		 * �û���ϰ��Ŀ����Ϊ0ʱ��Ĭ����ȷ100%������0%
		 */
		if(CORRECT_COUNT+WRONG_COUNT==0)
			data_list.addAll(new PieChart.Data("��ȷ "+"100%", 100), new PieChart.Data("���� "+"0%", WRONG_COUNT));
		else
			data_list.addAll(new PieChart.Data("��ȷ "+(100*CORRECT_COUNT/(CORRECT_COUNT + WRONG_COUNT)+"%"), CORRECT_COUNT), new PieChart.Data("���� "+(100*WRONG_COUNT/(CORRECT_COUNT + WRONG_COUNT)+"%"), WRONG_COUNT));
		pieChart.setData(data_list);
		statisticsPane.setCenter(pieChart);
		
		//���ơ���ʼ��ϰ����ť
		start_button = new Button(" �� ʼ �� ϰ ");
		start_button.setStyle("-fx-background-radius: 20;"
				+ "-fx-font-size: 20;");
		statisticsPane.setBottom(start_button);
		BorderPane.setAlignment(start_button, Pos.CENTER); 
		BorderPane.setMargin(start_button, new Insets(30, 10, 10, 10));
	}
	
	public BorderPane getStatisticsPane() {
	    return statisticsPane;
	}
	
	public Button getStartButton() {
		return start_button;
	}
}

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
 * 统计练习数据的页面，该页面显示用户练题的数量，并用饼状图表示正误百分比
 */
public class  StatisticsView {
	private BorderPane statisticsPane;
	public static int CORRECT_COUNT = 0, WRONG_COUNT = 0;//记录用户答题正确和错误的数量
	private Button start_button;
	public static PieChart pieChart;
	public static Text process_text;
	
	public StatisticsView(ArrayList<String> vocabulary_list) {
		statisticsPane = new BorderPane();
		statisticsPane.setPadding(new Insets(30, 30, 30, 30));
		
		//绘制背景
		BackgroundImage bImage = new BackgroundImage(new Image("file:Images\\blue_sky.jpg"),
				BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		statisticsPane.setBackground(new Background(bImage));
		
		//显示练习题目数量
		process_text = new Text("你已经练习了 " + (CORRECT_COUNT+WRONG_COUNT) + " 道题"
								+ "\n其中回答准确率如下图所示:");
		process_text.setFont(Font.font("", 20));
		process_text.setFill(Color.WHITE);
		statisticsPane.setTop(process_text);
		
		//绘制饼状图
		pieChart = new PieChart(); 
		pieChart.setLabelsVisible(false);
		pieChart.setScaleX(0.8); pieChart.setScaleY(0.8);
		pieChart.setStyle("-fx-font-size:18");
		ObservableList<PieChart.Data> data_list = FXCollections.observableArrayList();
		
		/*
		 * 用户练习题目数量为0时，计算百分比除数为0，会出现异常，因此多加一次判断
		 * 用户练习题目数量为0时，默认正确100%，错误0%
		 */
		if(CORRECT_COUNT+WRONG_COUNT==0)
			data_list.addAll(new PieChart.Data("正确 "+"100%", 100), new PieChart.Data("错误 "+"0%", WRONG_COUNT));
		else
			data_list.addAll(new PieChart.Data("正确 "+(100*CORRECT_COUNT/(CORRECT_COUNT + WRONG_COUNT)+"%"), CORRECT_COUNT), new PieChart.Data("错误 "+(100*WRONG_COUNT/(CORRECT_COUNT + WRONG_COUNT)+"%"), WRONG_COUNT));
		pieChart.setData(data_list);
		statisticsPane.setCenter(pieChart);
		
		//绘制“开始练习”按钮
		start_button = new Button(" 开 始 练 习 ");
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

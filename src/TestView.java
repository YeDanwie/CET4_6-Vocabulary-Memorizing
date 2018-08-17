import java.util.ArrayList;
import java.util.Collections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * @description 
 * 练习题目的页面，题目是简单的单词翻译，题库是未练习正确的单词
 * 题型有两种，一种是根据中文选择英文，另一种是根据英文选择中文。单词和题型随机。
 */
public class TestView {
	private VBox vbox;
	private Button aButton;
	private Button bButton;
	private Button cButton;
	private Button dButton;
	private int choice;
	private int answer;
	private int answer_index;
	private ArrayList<String> vocabulary_list;
	private ArrayList<Integer> unmarked_list;//未练习正确的单词
	private Text question;
	private ImageView back_button;
	
	public TestView(ArrayList<String> vocabulary_list) {
		this.vocabulary_list = vocabulary_list;
		unmarked_list = new ArrayList<Integer>();
		//默认词库中的单词均未练习过
		for(int i = 0; i < vocabulary_list.size(); i++)
			unmarked_list.add(i);
		
		vbox = new VBox();
		
		//绘制背景
		BackgroundImage bImage = new BackgroundImage(new Image("file:Images\\blue_sky.jpg"),
				BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		vbox.setBackground(new Background(bImage));
		
		aButton = new Button();
		bButton = new Button();
		cButton = new Button();
		dButton = new Button();
		
		//绘制题目文本
		question = new Text();
		question.setFill(Color.WHITE);
		question.setFont(Font.font("", 20));
		vbox.getChildren().add(question);
		VBox.setMargin(question, new Insets(30, 0, 30, 40));
		
		//绘制选项A按钮
		aButton.setStyle("-fx-background-radius: 20;"
						+ "-fx-font-size: 20;");
		vbox.getChildren().add(aButton);
		VBox.setMargin(aButton, new Insets(20, 40, 20, 40));
		aButton.prefWidthProperty().bind(vbox.widthProperty());
		aButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				//0,1,2,3分别代表用户选择的A,B,C,D选项
				choice = 0;
				
				if(unmarked_list.size()!=0)
					judge();
				
				//进入下一题
				getQuiz();
			}
		});
		
		//绘制选项B按钮
		bButton.setStyle("-fx-background-radius: 20;"
						+ "-fx-font-size: 20;");
		vbox.getChildren().add(bButton);
		VBox.setMargin(bButton, new Insets(20, 40, 20, 40));
		bButton.prefWidthProperty().bind(vbox.widthProperty());
		bButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				choice = 1;
				
				if(unmarked_list.size()!=0)
					judge();
				
				getQuiz();
			}
		});
		
		//绘制选项C按钮
		cButton.setStyle("-fx-background-radius: 20;"
						+ "-fx-font-size: 20;");
		vbox.getChildren().add(cButton);
		VBox.setMargin(cButton, new Insets(20, 40, 20, 40));
		cButton.prefWidthProperty().bind(vbox.widthProperty());
		cButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				choice = 2;

				if(unmarked_list.size()!=0)
					judge();
				
				getQuiz();
			}
		});
		
		//绘制选项D按钮
		dButton.setStyle("-fx-background-radius: 20;"
						+ "-fx-font-size: 20;");
		vbox.getChildren().add(dButton);
		VBox.setMargin(dButton, new Insets(20, 40, 20, 40));
		dButton.prefWidthProperty().bind(vbox.widthProperty());
		dButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				choice = 3;

				if(unmarked_list.size()!=0)
					judge();
				
				getQuiz();
				
			}
		});
		
		//绘制返回按钮(实际是一个图标)
		back_button = new ImageView(new Image("file:Images\\back.png"));
		back_button.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				back_button.setEffect(new DropShadow(3, Color.BLACK));
			}
		});
		
		back_button.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				back_button.setEffect(null);
			}
		});
		
		vbox.getChildren().add(back_button);
		VBox.setMargin(back_button, new Insets(40, 0, 30, 30));
	}
	
	public ImageView getBackButton() {
		return back_button;
	}
	
	public void getQuiz() {
		//首先判断题库(即未答对的单词库)是否还有单词，如果所有单词已被练完，则弹出对话框提示并返回
		if(unmarked_list.size()==0) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(null);
			alert.setHeaderText(null);
			alert.setContentText("你已经练完词库中所有单词");
			alert.show();
			
			return;
		}
		
		ArrayList<String> quiz = new ArrayList<String>();
		
		//随机生成题型，0是英文-中文，1是中文-英文
		int mode = (int)((Math.random())*2);
		
		ArrayList<Integer> four_nums = new ArrayList<Integer>();
		answer_index = (int)(Math.random()*unmarked_list.size());
		int answer_order = unmarked_list.get(answer_index);
		four_nums.add(answer_order);
		
		//随机增加三个干扰选项
		while(four_nums.size()!=4) {
			int random = (int)(Math.random()*vocabulary_list.size());
			if(!four_nums.contains(random))//避免重复
				four_nums.add(random);		
		}
		
		//显示题目
		quiz.add(vocabulary_list.get(answer_order).split("   ")[mode]);
		question.setText("请点击正确释义: " + quiz.get(0));
		
		//four_nums中的第一个总是正确答案，为了让答案分布随机，所以打乱它
		Collections.shuffle(four_nums);
		
		//获取答案和四个选项
		answer = four_nums.indexOf(answer_order);
		for(int i = 0; i < four_nums.size(); i++) {
			quiz.add(vocabulary_list.get(four_nums.get(i)).split("   ")[(mode+1)%2]);
		}
		
		//再按钮上显示选项
		aButton.setText(quiz.get(1));
		bButton.setText(quiz.get(2));
		cButton.setText(quiz.get(3));
		dButton.setText(quiz.get(4));
	}
	
	/**
	 * @description
	 * 判断用户的选择是否正确，正误与否都会播放相应的音效，并且更新统计数据;
	 * 若选择正确，当前题目就会从题库中移除
	 */
	private void judge() {
		if(choice==answer) {
			Media media = new Media(new java.io.File("Music\\correct.mp3").toURI().toString());
			new MediaPlayer(media).play();
			
			StatisticsView.CORRECT_COUNT++;
			int answered_count = StatisticsView.CORRECT_COUNT+StatisticsView.WRONG_COUNT;
			StatisticsView.process_text.setText("你已经练习了 " + answered_count + " 道题"
												+ "\n其中回答准确率如下图所示:");
			ObservableList<PieChart.Data> data_list = FXCollections.observableArrayList();
			data_list.add(new PieChart.Data("正确 "+100*StatisticsView.CORRECT_COUNT/answered_count+"%", StatisticsView.CORRECT_COUNT));
			data_list.add(new PieChart.Data("错误 "+100*StatisticsView.WRONG_COUNT/answered_count+"%", StatisticsView.WRONG_COUNT));
			StatisticsView.pieChart.setData(data_list);
			
			unmarked_list.remove(answer_index);
			
			return;
		} else {
			Media media = new Media(new java.io.File("Music\\wrong.mp3").toURI().toString());
			new MediaPlayer(media).play();
			
			StatisticsView.WRONG_COUNT++;
			int answered_count = StatisticsView.CORRECT_COUNT+StatisticsView.WRONG_COUNT;
			StatisticsView.process_text.setText("你已经练习了 " + answered_count + " 道题"
												+ "\n其中回答准确率如下图所示:");
			ObservableList<PieChart.Data> data_list = FXCollections.observableArrayList();
			data_list.add(new PieChart.Data("正确 "+100*StatisticsView.CORRECT_COUNT/answered_count+"%", StatisticsView.CORRECT_COUNT));
			data_list.add(new PieChart.Data("错误 "+100*StatisticsView.WRONG_COUNT/answered_count+"%", StatisticsView.WRONG_COUNT));
			StatisticsView.pieChart.setData(data_list);
			
			return;
		}
	}
	
	
	
	public VBox getTestBox() {
		return vbox;
	}
	
	public ArrayList<Integer> getUnmarkedList() {
		return unmarked_list;
	}
	
	public void setUnmarkedList(ArrayList<Integer> unmarked_list) {
		this.unmarked_list = unmarked_list; 
	}
}

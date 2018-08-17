import java.util.ArrayList;
import java.util.Collections;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
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
import javafx.stage.Stage;

/**
 * @description 
 * ��ϰ��Ŀ��ҳ�棬��Ŀ�Ǽ򵥵ĵ��ʷ��룬�����δ��ϰ��ȷ�ĵ���
 * ���������֣�һ���Ǹ�������ѡ��Ӣ�ģ���һ���Ǹ���Ӣ��ѡ�����ġ����ʺ����������
 */
public class TestView extends Application {
	private VBox vbox;
	private Button aButton;
	private Button bButton;
	private Button cButton;
	private Button dButton;
	private int choice;
	private int answer;
	private int answer_index;
	private ArrayList<String> vocabulary_list;
	private ArrayList<Integer> unmarked_list;//δ��ϰ��ȷ�ĵ���
	private Text question;
	private ImageView back_button;
	
	public TestView(ArrayList<String> vocabulary_list) {
		this.vocabulary_list = vocabulary_list;
		unmarked_list = new ArrayList<Integer>();
		//Ĭ�ϴʿ��еĵ��ʾ�δ��ϰ��
		for(int i = 0; i < vocabulary_list.size(); i++)
			unmarked_list.add(i);
		
		vbox = new VBox();
		
		//���Ʊ���
		BackgroundImage bImage = new BackgroundImage(new Image("file:Images\\blue_sky.jpg"),
				BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		vbox.setBackground(new Background(bImage));
		
		aButton = new Button();
		bButton = new Button();
		cButton = new Button();
		dButton = new Button();
		
		//������Ŀ�ı�
		question = new Text();
		question.setFill(Color.WHITE);
		question.setFont(Font.font("", 20));
		vbox.getChildren().add(question);
		VBox.setMargin(question, new Insets(30, 0, 30, 40));
		
		//����ѡ��A��ť
		aButton.setStyle("-fx-background-radius: 20;"
						+ "-fx-font-size: 20;");
		vbox.getChildren().add(aButton);
		VBox.setMargin(aButton, new Insets(20, 40, 20, 40));
		aButton.prefWidthProperty().bind(vbox.widthProperty());
		aButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				//0,1,2,3�ֱ�����û�ѡ���A,B,C,Dѡ��
				choice = 0;
				
				if(unmarked_list.size()!=0)
					judge();
				
				//������һ��
				getQuiz();
			}
		});
		
		//����ѡ��B��ť
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
		
		//����ѡ��C��ť
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
		
		//����ѡ��D��ť
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
		
		//���Ʒ��ذ�ť(ʵ����һ��ͼ��)
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
		//�����ж����(��δ��Եĵ��ʿ�)�Ƿ��е��ʣ�������е����ѱ����꣬�򵯳��Ի�����ʾ������
		if(unmarked_list.size()==0) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle(null);
			alert.setHeaderText(null);
			alert.setContentText("���Ѿ�����ʿ������е���");
			alert.show();
			
			return;
		}
		
		ArrayList<String> quiz = new ArrayList<String>();
		
		//����������ͣ�0��Ӣ��-���ģ�1������-Ӣ��
		int mode = (int)((Math.random())*2);
		
		ArrayList<Integer> four_nums = new ArrayList<Integer>();
		answer_index = (int)(Math.random()*unmarked_list.size());
		int answer_order = unmarked_list.get(answer_index);
		four_nums.add(answer_order);
		
		//���������������ѡ��
		while(four_nums.size()!=4) {
			int random = (int)(Math.random()*vocabulary_list.size());
			if(!four_nums.contains(random))//�����ظ�
				four_nums.add(random);		
		}
		
		//��ʾ��Ŀ
		quiz.add(vocabulary_list.get(answer_order).split("   ")[mode]);
		question.setText("������ȷ����: " + quiz.get(0));
		
		//four_nums�еĵ�һ��������ȷ�𰸣�Ϊ���ô𰸷ֲ���������Դ�����
		Collections.shuffle(four_nums);
		
		//��ȡ�𰸺��ĸ�ѡ��
		answer = four_nums.indexOf(answer_order);
		for(int i = 0; i < four_nums.size(); i++) {
			quiz.add(vocabulary_list.get(four_nums.get(i)).split("   ")[(mode+1)%2]);
		}
		
		//�ٰ�ť����ʾѡ��
		aButton.setText(quiz.get(1));
		bButton.setText(quiz.get(2));
		cButton.setText(quiz.get(3));
		dButton.setText(quiz.get(4));
	}
	
	/**
	 * @description
	 * �ж��û���ѡ���Ƿ���ȷ��������񶼻Ქ����Ӧ����Ч�����Ҹ���ͳ������;
	 * ��ѡ����ȷ����ǰ��Ŀ�ͻ��������Ƴ�
	 */
	private void judge() {
		if(choice==answer) {
			Media media = new Media(new java.io.File("Music\\correct.mp3").toURI().toString());
			new MediaPlayer(media).play();
			
			StatisticsView.CORRECT_COUNT++;
			int answered_count = StatisticsView.CORRECT_COUNT+StatisticsView.WRONG_COUNT;
			StatisticsView.process_text.setText("���Ѿ���ϰ�� " + answered_count + " ����"
												+ "\n���лش�׼ȷ������ͼ��ʾ:");
			ObservableList<PieChart.Data> data_list = FXCollections.observableArrayList();
			data_list.add(new PieChart.Data("��ȷ "+100*StatisticsView.CORRECT_COUNT/answered_count+"%", StatisticsView.CORRECT_COUNT));
			data_list.add(new PieChart.Data("���� "+100*StatisticsView.WRONG_COUNT/answered_count+"%", StatisticsView.WRONG_COUNT));
			StatisticsView.pieChart.setData(data_list);
			
			unmarked_list.remove(answer_index);
			
			return;
		} else {
			Media media = new Media(new java.io.File("Music\\wrong.mp3").toURI().toString());
			new MediaPlayer(media).play();
			
			StatisticsView.WRONG_COUNT++;
			int answered_count = StatisticsView.CORRECT_COUNT+StatisticsView.WRONG_COUNT;
			StatisticsView.process_text.setText("���Ѿ���ϰ�� " + answered_count + " ����"
												+ "\n���лش�׼ȷ������ͼ��ʾ:");
			ObservableList<PieChart.Data> data_list = FXCollections.observableArrayList();
			data_list.add(new PieChart.Data("��ȷ "+100*StatisticsView.CORRECT_COUNT/answered_count+"%", StatisticsView.CORRECT_COUNT));
			data_list.add(new PieChart.Data("���� "+100*StatisticsView.WRONG_COUNT/answered_count+"%", StatisticsView.WRONG_COUNT));
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
	
	@Override
	public void start(Stage stage) {
		Scene scene = new Scene(this.getTestBox(), 580, 530);
		this.getTestBox().prefWidthProperty().bind(scene.widthProperty());
		stage.setScene(scene);
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

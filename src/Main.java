import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Optional;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

/**
 * @description
 * javaFx ���������ʼ����������
 * �û����ڸó����ϼ������������ʲ�����С��ϰ
 * ÿ�ιرճ���ǰ�����򶼻��Զ������û���ѧϰ���ȣ��´ο�������ʱ�ָ�����
 * ���û�������ѧϰʱ�����ڳ�����ѡ�������Ӧ��ѧϰ����
 */
public class Main extends Application {
	private TabPane tabPane;
	private Tab dictionary_tab;
	private Tab exercise_tab;	
	private Tab setting_tab;
	private StackPane stackPane1,stackPane2;
	private WordsListView wlView; 
	private WordCardView wcView;
	private TestView testView;
	private StatisticsView statisticsView;
	private ArrayList<String> vocabulary_list;
	private ArrayList<Boolean> isMarked_list;//�����Ⱥ�vocabulary_list��ȣ���¼�����Ƿ��ѱ��û���ס
	private ArrayList<Integer> unmarked_list;//δ����ס�ĵ�����vocabulary_list�е�����
	public static boolean delete_memorizing = false;
	public static boolean delete_exercising = false;
	
	public Main() throws Exception{
		vocabulary_list = new ArrayList<String>();
		isMarked_list = new ArrayList<Boolean>();
		unmarked_list = new ArrayList<Integer>();
		
		//��ȡ�ʿ⣬Ĭ�����е��ʾ�δ����ס
		File dictionary = new File("dictionary.txt");
	    BufferedReader br = new BufferedReader(new FileReader(dictionary));
	    String line;
	    int index=0;
	    while((line=br.readLine())!=null) {
	    	vocabulary_list.add(line);
	    	isMarked_list.add(false);
	    	unmarked_list.add(index++);
	    }
	    br.close();
	    
	    //���ļ��ж�ȡ�û�֮ǰ��ѧϰ����
	    File dir = new File("Data");
	    if(!dir.exists())
	    	dir.mkdirs();
	    
	    File isMarked_file = new File("Data\\isMarked_list.data");
	    if(isMarked_file.exists()) {
	    	DataInputStream dis = new DataInputStream(new FileInputStream(isMarked_file));
	    	for(int i = 0; i < isMarked_list.size(); i++)
	    		isMarked_list.set(i, dis.readBoolean());
	    	dis.close();
	    }
	    	
	    File unmarked_file = new File("Data\\unmarked_list.data");
	    if(unmarked_file.exists()) {
	    	unmarked_list = new ArrayList<Integer>();
	    	DataInputStream dis = new DataInputStream(new FileInputStream(unmarked_file));
	    	while(dis.available() > 0)
	    		unmarked_list.add(dis.readInt());
	    	dis.close();
	    }
	}
	
	@Override
	public void start(Stage stage) {
		tabPane = new TabPane();//ѡ����
		
		//�������ʿ�ѡ������ǵ�Ҫ����ʽ�л�ҳ�棬ʹ���ĸ������StackPane
		stackPane1 = new StackPane();
		dictionary_tab = new Tab("�������ʿ�");
		dictionary_tab.setStyle("-fx-font-size: 18;");
		dictionary_tab.setClosable(false);
		dictionary_tab.setContent(stackPane1);
		tabPane.getTabs().add(dictionary_tab);
		
		//������ϰѡ������ǵ�Ҫ����ʽ�л�ҳ�棬ʹ���ĸ������StackPane
		stackPane2 = new StackPane();
		exercise_tab = new Tab("������ϰ");
		exercise_tab.setStyle("-fx-font-size: 18;");
		exercise_tab.setClosable(false);
		exercise_tab.setContent(stackPane2);
		tabPane.getTabs().add(exercise_tab);
		
		//����ѡ����û����ڴ���յ��ʼ�����Ȼ򵥴���ϰ����
		setting_tab = new Tab("����");
		setting_tab.setStyle("-fx-font-size: 18;");
		setting_tab.setClosable(false);
		setting_tab.setContent(new SettingView().getSettingPane());
		tabPane.getTabs().add(setting_tab);
		
		//���ʿ���ʾ���б���
		wlView = new WordsListView();
		try {
			wlView.addItems(vocabulary_list,isMarked_list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		stackPane1.getChildren().add(wlView.getListView());	
		SelectionModel<BorderPane> wlvSelectionModel = wlView.getListView().getSelectionModel();
		
		
		wcView = new WordCardView(); 
		//����б��ĳһ��ͻ����õ��ʵļ��俨Ƭ
		wlView.getListView().setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				int index = wlvSelectionModel.getSelectedIndex();
				String[] line = vocabulary_list.get(index).split("   ");
				wcView.set(line[0], line[1]);
				
				//����ʽ�л�ҳ�棬�Ӵʿ��б��л������俨Ƭ
				double width = stackPane1.getWidth();
	            KeyFrame start = new KeyFrame(Duration.ZERO,
	                    new KeyValue(wcView.getCardPane().translateXProperty(), width),
	                    new KeyValue(wlView.getListView().translateXProperty(), 0));
	            KeyFrame end = new KeyFrame(Duration.seconds(0.5),
	                    new KeyValue(wcView.getCardPane().translateXProperty(), 0),
	                    new KeyValue(wlView.getListView().translateXProperty(), -width));
	            Timeline slide = new Timeline(start, end);
	            slide.setOnFinished(e -> stackPane1.getChildren().remove(wlView.getListView()));
	            slide.play();
				
				stackPane1.getChildren().add(wcView.getCardPane());
			}
		});
		
		//���ذ�ť��ͬ���ǻ���ʽ�л�ҳ�棬�Ӽ��俨Ƭ�л����ʿ��б�
		wcView.getBackButton().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				double width = stackPane1.getWidth();
	            KeyFrame start = new KeyFrame(Duration.ZERO,
	                    new KeyValue(wcView.getCardPane().translateXProperty(), 0),
	                    new KeyValue(wlView.getListView().translateXProperty(), -width));
	            KeyFrame end = new KeyFrame(Duration.seconds(0.5),
	                    new KeyValue(wcView.getCardPane().translateXProperty(), width),
	                    new KeyValue(wlView.getListView().translateXProperty(), 0));
	            Timeline slide = new Timeline(start, end);
	            slide.setOnFinished(e -> stackPane1.getChildren().remove(wcView.getCardPane()));
	            slide.play();
				
				stackPane1.getChildren().add(wlView.getListView());
			}
		});
		
		//�������һ������ť���¼�����
		wcView.getNextButton().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent me) {
				int currentIndex = wlvSelectionModel.getSelectedIndex();
				//���û����"��һ��"��ťʱ��Ĭ���û��Ѿ���ס��ǰ����
				wlView.getListView().getItems().get(currentIndex).setRight(new ImageView(new Image("file:Images\\correct.png")));
				isMarked_list.set(currentIndex, true);
				unmarked_list.remove(Integer.valueOf(currentIndex));
				
				//��ס�ʿ����е��ʺ󣬵����Ի�����ʾ
				if(unmarked_list.size()==0) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("��ʾ��");
					alert.setHeaderText(null);
					alert.setContentText("���Ѿ���ס�ʿ������еĵ��ʡ�");
					alert.getDialogPane().setStyle("-fx-font-size:15");
					
					ButtonType cancel_button = new ButtonType("���شʿ�");
					ButtonType exercise_button = new ButtonType("ȥ��ϰ");
					alert.getButtonTypes().setAll(cancel_button,exercise_button);
					
					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == cancel_button){
						//�����л����ʿ��б�
						double width = stackPane1.getWidth();
			            KeyFrame start = new KeyFrame(Duration.ZERO,
			                    new KeyValue(wcView.getCardPane().translateXProperty(), 0),
			                    new KeyValue(wlView.getListView().translateXProperty(), -width));
			            KeyFrame end = new KeyFrame(Duration.seconds(0.5),
			                    new KeyValue(wcView.getCardPane().translateXProperty(), width),
			                    new KeyValue(wlView.getListView().translateXProperty(), 0));
			            Timeline slide = new Timeline(start, end);
			            slide.setOnFinished(e -> stackPane1.getChildren().remove(wcView.getCardPane()));
			            slide.play();
						
						stackPane1.getChildren().add(wlView.getListView());
					} else if (result.get() == exercise_button) {
						//�л�����ϰ����ҳ��
					    tabPane.getSelectionModel().select(exercise_tab);
					}
					
					return;
				} 
				else {
					//�����ʾ��һ��δ��ס�ĵ���
					int nextIndex = (int)(Math.random()*unmarked_list.size());
					wlvSelectionModel.select(unmarked_list.get(nextIndex));
					String[] line = vocabulary_list.get(unmarked_list.get(nextIndex)).split("   ");
					wcView.set(line[0],line[1]);
				}
				
				
			}
		});
		
		testView = new TestView(vocabulary_list);
		//��ȡ�û�֮ǰδ��ϰ�ĵ�������
		File test_unmarked_file = new File("Data\\test_unmarked_list.data");
		if(test_unmarked_file.exists()) {
			ArrayList<Integer> test_unmarked_list = new ArrayList<Integer>();
			try {
				DataInputStream dis = new DataInputStream(new FileInputStream(test_unmarked_file));
				while(dis.available() > 0) {
					test_unmarked_list.add(dis.readInt());
				}
				dis.close();
				testView.setUnmarkedList(test_unmarked_list);
			} catch (Exception e) {
				e.printStackTrace();	
			}
		}
		
		//��ȡ�û�֮ǰ�Ĵ�����������
		File counter_file = new File("Data\\counter.data");
		if(counter_file.exists()) {
			try {
				DataInputStream dis = new DataInputStream(new FileInputStream(counter_file));
				StatisticsView.CORRECT_COUNT = dis.readInt();
				StatisticsView.WRONG_COUNT = dis.readInt();
				dis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		statisticsView = new StatisticsView(vocabulary_list);
		
		stackPane2.getChildren().add(statisticsView.getStatisticsPane());
		//���"��ʼ��ϰ"��ť���¼�����
		statisticsView.getStartButton().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				//���Ȼ�ȡ��Ŀ
				testView.getQuiz();
				
				//��ͳ��ҳ�滬���л�����ϰҳ��
				double width = stackPane2.getWidth();
	            KeyFrame start = new KeyFrame(Duration.ZERO,
	                    new KeyValue(testView.getTestBox().translateXProperty(), width),
	                    new KeyValue(statisticsView.getStatisticsPane().translateXProperty(), 0));
	            KeyFrame end = new KeyFrame(Duration.seconds(0.5),
	                    new KeyValue(testView.getTestBox().translateXProperty(), 0),
	                    new KeyValue(statisticsView.getStatisticsPane().translateXProperty(), -width));
	            Timeline slide = new Timeline(start, end);
	            slide.setOnFinished(e -> stackPane2.getChildren().remove(statisticsView.getStatisticsPane()));
	            slide.play();
	            
	            stackPane2.getChildren().add(testView.getTestBox());
			}
		});
		
		//���"����"��ť���¼���������ϰҳ�滬���л���ͳ��ҳ��
		testView.getBackButton().setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				double width = stackPane2.getWidth();
	            KeyFrame start = new KeyFrame(Duration.ZERO,
	                    new KeyValue(testView.getTestBox().translateXProperty(), 0),
	                    new KeyValue(statisticsView.getStatisticsPane().translateXProperty(), -width));
	            KeyFrame end = new KeyFrame(Duration.seconds(0.5),
	                    new KeyValue(testView.getTestBox().translateXProperty(), width),
	                    new KeyValue(statisticsView.getStatisticsPane().translateXProperty(), 0));
	            Timeline slide = new Timeline(start, end);
	            slide.setOnFinished(e -> stackPane2.getChildren().remove(testView.getTestBox()));
	            slide.play();
	            
	            stackPane2.getChildren().add(statisticsView.getStatisticsPane());
			}
		});
		
		//�رճ���ǰ����ʹ���ߵ�ѧϰ����
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				try {
					File isMarked_file = new File("Data\\isMarked_list.data");
					if(!isMarked_file.exists())
						isMarked_file.createNewFile();
					DataOutputStream dos = new DataOutputStream(new FileOutputStream(isMarked_file));
					for(int i = 0; i < isMarked_list.size(); i++)
						dos.writeBoolean(isMarked_list.get(i));
					dos.close();
				
					File unmarked_file = new File("Data\\unmarked_list.data");
					if(!unmarked_file.exists())
						unmarked_file.createNewFile();
					dos = new DataOutputStream(new FileOutputStream(unmarked_file));
					for(int i = 0; i < unmarked_list.size(); i++)
						dos.writeInt(unmarked_list.get(i));
					dos.close();
					
					File test_unmarked_file = new File("Data\\test_unmarked_list.data");
					if(!test_unmarked_file.exists())
						test_unmarked_file.createNewFile();
					dos = new DataOutputStream(new FileOutputStream(test_unmarked_file));
					for(int i = 0; i < testView.getUnmarkedList().size(); i++)
						dos.writeInt(testView.getUnmarkedList().get(i));
					dos.close();
					
					File counter_file = new File("Data\\counter.data");
					if(!counter_file.exists())
						counter_file.createNewFile();
					dos = new DataOutputStream(new FileOutputStream(counter_file));
					dos.writeInt(StatisticsView.CORRECT_COUNT);
					dos.writeInt(StatisticsView.WRONG_COUNT);
					dos.close();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			 
		});
		
		Scene scene = new Scene(tabPane,580,530);
		stage.setScene(scene);
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
		
		//����رպ�����û�����ɾ����Ӧ����
		if(delete_memorizing==true) {
			File file = new File("Data\\isMarked_list.data");
			if(file.exists())
				file.delete();
			
			file = new File("Data\\unmarked_list.data");
			if(file.exists())
				file.delete();
		}
		
		if(delete_exercising==true) {
			File file = new File("Data\\counter.data");
			if(file.exists())
				file.delete();
			
			file = new File("Data\\test_unmarked_list.data");
			if(file.exists())
				file.delete();
		}
	}
}
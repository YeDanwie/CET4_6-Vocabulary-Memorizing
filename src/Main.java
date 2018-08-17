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
 * javaFx 四六级单词记忆程序主类
 * 用户可在该程序上记忆四六级单词并进行小练习
 * 每次关闭程序前，程序都会自动保存用户的学习进度，下次开启程序时恢复进度
 * 当用户想重新学习时，可在程序中选择清空相应的学习进度
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
	private ArrayList<Boolean> isMarked_list;//链表长度和vocabulary_list相等，记录单词是否已被用户记住
	private ArrayList<Integer> unmarked_list;//未被记住的单词在vocabulary_list中的索引
	public static boolean delete_memorizing = false;
	public static boolean delete_exercising = false;
	
	public Main() throws Exception{
		vocabulary_list = new ArrayList<String>();
		isMarked_list = new ArrayList<Boolean>();
		unmarked_list = new ArrayList<Integer>();
		
		//读取词库，默认所有单词均未被记住
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
	    
	    //从文件中读取用户之前的学习进度
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
		tabPane = new TabPane();//选项卡面板
		
		//四六级词库选项卡，考虑到要滑动式切换页面，使它的根面板是StackPane
		stackPane1 = new StackPane();
		dictionary_tab = new Tab("四六级词库");
		dictionary_tab.setStyle("-fx-font-size: 18;");
		dictionary_tab.setClosable(false);
		dictionary_tab.setContent(stackPane1);
		tabPane.getTabs().add(dictionary_tab);
		
		//单词练习选项卡，考虑到要滑动式切换页面，使它的根面板是StackPane
		stackPane2 = new StackPane();
		exercise_tab = new Tab("单词练习");
		exercise_tab.setStyle("-fx-font-size: 18;");
		exercise_tab.setClosable(false);
		exercise_tab.setContent(stackPane2);
		tabPane.getTabs().add(exercise_tab);
		
		//设置选项卡，用户可在此清空单词记忆进度或单词练习进度
		setting_tab = new Tab("设置");
		setting_tab.setStyle("-fx-font-size: 18;");
		setting_tab.setClosable(false);
		setting_tab.setContent(new SettingView().getSettingPane());
		tabPane.getTabs().add(setting_tab);
		
		//将词库显示在列表上
		wlView = new WordsListView();
		try {
			wlView.addItems(vocabulary_list,isMarked_list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		stackPane1.getChildren().add(wlView.getListView());	
		SelectionModel<BorderPane> wlvSelectionModel = wlView.getListView().getSelectionModel();
		
		
		wcView = new WordCardView(); 
		//点击列表的某一项，就会进入该单词的记忆卡片
		wlView.getListView().setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				int index = wlvSelectionModel.getSelectedIndex();
				String[] line = vocabulary_list.get(index).split("   ");
				wcView.set(line[0], line[1]);
				
				//滑动式切换页面，从词库列表切换到记忆卡片
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
		
		//返回按钮，同样是滑动式切换页面，从记忆卡片切换到词库列表
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
		
		//点击“下一个”按钮的事件处理
		wcView.getNextButton().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent me) {
				int currentIndex = wlvSelectionModel.getSelectedIndex();
				//当用户点击"下一个"按钮时，默认用户已经记住当前单词
				wlView.getListView().getItems().get(currentIndex).setRight(new ImageView(new Image("file:Images\\correct.png")));
				isMarked_list.set(currentIndex, true);
				unmarked_list.remove(Integer.valueOf(currentIndex));
				
				//记住词库所有单词后，弹出对话框提示
				if(unmarked_list.size()==0) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("提示框");
					alert.setHeaderText(null);
					alert.setContentText("你已经记住词库中所有的单词。");
					alert.getDialogPane().setStyle("-fx-font-size:15");
					
					ButtonType cancel_button = new ButtonType("返回词库");
					ButtonType exercise_button = new ButtonType("去练习");
					alert.getButtonTypes().setAll(cancel_button,exercise_button);
					
					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == cancel_button){
						//滑动切换到词库列表
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
						//切换到练习单词页面
					    tabPane.getSelectionModel().select(exercise_tab);
					}
					
					return;
				} 
				else {
					//随机显示下一个未记住的单词
					int nextIndex = (int)(Math.random()*unmarked_list.size());
					wlvSelectionModel.select(unmarked_list.get(nextIndex));
					String[] line = vocabulary_list.get(unmarked_list.get(nextIndex)).split("   ");
					wcView.set(line[0],line[1]);
				}
				
				
			}
		});
		
		testView = new TestView(vocabulary_list);
		//读取用户之前未练习的单词索引
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
		
		//读取用户之前的答题正误数量
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
		//点击"开始练习"按钮的事件处理
		statisticsView.getStartButton().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				//首先获取题目
				testView.getQuiz();
				
				//从统计页面滑动切换到练习页面
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
		
		//点击"返回"按钮的事件处理，从练习页面滑动切换到统计页面
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
		
		//关闭程序前保存使用者的学习进度
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
		
		//程序关闭后根据用户设置删除相应进度
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
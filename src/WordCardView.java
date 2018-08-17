import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * @description
 * ��Ƭʽ���䵥�ʵ�ҳ�棬����Ӣ�ĺ����������壬�ɲ��ŷ���
 * �������һ������ť���Ѿ�����ס�ĵ��ʲ����ٳ���
 */
public class WordCardView {
	private BorderPane cardPane;
	private Text english_text; 
	private ImageView imageView;
	private Text chinese_text;
	private Button back_button;
	private Button next_button;
	
	public WordCardView() {
		//���Ʊ���
		cardPane = new BorderPane();
		cardPane.setPadding(new Insets(30, 30, 30, 30));
		BackgroundImage bImage = new BackgroundImage(new Image("file:Images\\blue_sky.jpg"),
				BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		cardPane.setBackground(new Background(bImage));
		
		HBox hbox = new HBox(); 
		hbox.setAlignment(Pos.CENTER);
		hbox.setSpacing(10);
		hbox.setPadding(new Insets(10, 10, 15, 10));
		
		//������ʾӢ�ģ�����ӰЧ��
		english_text = new Text();
		english_text.setFont(Font.font("", FontWeight.BOLD, 50));
		english_text.setEffect(new DropShadow(10, 3, 5, Color.GRAY));
		hbox.getChildren().add(english_text);
		
		//���ŷ�����ͼ��
		imageView = new ImageView(new Image("file:Images\\sound.png"));
		hbox.getChildren().add(imageView);
		
		imageView.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				DropShadow dShadow = new DropShadow(10, Color.GRAY);
				imageView.setEffect(dShadow);
			}
		});
		
		imageView.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				imageView.setEffect(null);
			}
		});
		
		
		VBox vbox = new VBox(); 
		vbox.setAlignment(Pos.CENTER);
		
		vbox.getChildren().add(hbox);
		
		//������ʾ��������
		chinese_text = new Text();
		chinese_text.setFont(Font.font("", 18));
		vbox.getChildren().add(chinese_text);
		cardPane.setCenter(vbox);
		
		BorderPane buttonPane = new BorderPane();
		
		/*
		 * ���ذ�ť
		 * �޸���ɫ����꾭��ʱ��ťû���ˡ�����������Ч��������Լ������¼�����
		 */
		back_button = new Button("����");
		back_button.setStyle("-fx-background-color: #4F94CD;"
							+ "-fx-font-size: 18;"
							+ "-fx-background-radius: 20;");
		back_button.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				back_button.setEffect(new DropShadow(10,Color.GRAY));
			}
		});
		back_button.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				back_button.setEffect(null);
			}
		});			
		buttonPane.setLeft(back_button);
		
		//��һ�����ʵİ�ť��ͬ�ϡ�
		next_button = new Button("��ס��,��һ��");
		next_button.setStyle("-fx-background-color: #4F94CD;"
							+ "-fx-font-size: 18;"
							+ "-fx-background-radius: 20;");
		next_button.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				next_button.setEffect(new DropShadow(10,Color.GRAY));
			}
		});
		next_button.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				next_button.setEffect(null);
			}
		});
		buttonPane.setRight(next_button);
		
		cardPane.setBottom(buttonPane);
	}
	
	//���õ�ǰ��ʾ�ĵ��ʺ�����������
	public void set(String english, String chinese) {
		english_text.setText(english);
		chinese_text.setText(chinese);
		
		//�������ͼ��ʱ������API���ŷ���
		imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				Media media = new Media("http://dict.youdao.com/dictvoice?audio="+english);
				MediaPlayer mplayer = new MediaPlayer(media);
				mplayer.play();
			}
		});
	}
	
	public Button getBackButton() {
		return back_button;
	}
	
	public Button getNextButton() {
		return next_button;
	}
	
	public BorderPane getCardPane() {
		return cardPane;
	}
}

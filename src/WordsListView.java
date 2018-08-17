import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * @description
 * �ʿⵥ���б��ҳ��
 * �Ѿ���ס�ĵ��ʻ��������ϱ��
 */
public class WordsListView{
	//����չʾ���ʵ��б�
	private ListView<BorderPane> listView; 
	
	public ListView<BorderPane> getListView(){
		return listView;
	}
	
	/**
	 * @description ���ݲ��������б�UI����ʾ�����Լ��Ƿ񱻼�ס�ı��
	 * 
	 * @param vocabulary_list �������� 
	 * @param isMarked_list �����Ƿ��ѱ���ס������
	 */
	public void addItems(ArrayList<String> vocabulary_list, ArrayList<Boolean> isMarked_list) throws Exception{
		ObservableList<BorderPane> data = FXCollections.observableArrayList();
	    listView = new ListView<BorderPane>(data);
	    listView.setPrefSize(200, 250);
	    listView.setEditable(true);
	    
	    for(int i = 0; i < vocabulary_list.size(); i++) {
	    	String english = vocabulary_list.get(i).split("   ")[0];
	    	boolean isMarked = isMarked_list.get(i);
	    	data.add(this.getItem(english, isMarked));
	    }
	    
	    listView.setItems(data);
	}
	

	//listView�е�ÿ�����һ��BorderPane
	private BorderPane getItem(String word, boolean isMarked) {
		BorderPane bPane = new BorderPane();
		
		Text text = new Text(word);
		text.setFont(Font.font("", 25));
		bPane.setLeft(text);
		
		if(isMarked)
			bPane.setRight(new ImageView(new Image("file:Images\\correct.png")));
		
		return bPane;
	}

	public static void main(String[] args) {
		
	}
}

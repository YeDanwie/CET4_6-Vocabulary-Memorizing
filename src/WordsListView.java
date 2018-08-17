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
 * 词库单词列表的页面
 * 已经记住的单词会在其后打上标记
 */
public class WordsListView{
	//用于展示单词的列表
	private ListView<BorderPane> listView; 
	
	public ListView<BorderPane> getListView(){
		return listView;
	}
	
	/**
	 * @description 根据参数绘制列表UI，显示单词以及是否被记住的标记
	 * 
	 * @param vocabulary_list 单词链表 
	 * @param isMarked_list 单词是否已被记住的链表
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
	

	//listView中的每个项都是一个BorderPane
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

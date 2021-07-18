import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * 관찰자 패턴 실습
 * UserChatWindow
 * 각 사용자의 채팅창 (View+Controller 클래스) 
 * 모델은 User
 * @author 노기현 
 *
 */
public class UserChatWindow extends Stage{
	private User user;
	private CheckBox isOnline = new CheckBox("온라인");
	private ComboBox<String> roomChoice = new ComboBox<>();
	private TextArea chatArea = new TextArea();
	private TextArea sendArea = new TextArea();
	private Button sendButton = new Button("전송");
	private MenuItem leaveRoomMenuItem = new MenuItem("채팅방 나가기");
	private MenuItem deleteRoomContentsMenuItem = new MenuItem("채팅방 지우기");
	
	public UserChatWindow(User user) {
		this.user = user;
		roomChoice.getItems().addAll(user.getRooms());
		roomChoice.getSelectionModel().select(0);
		roomChoice.getSelectionModel().selectedItemProperty().addListener((o,ov,nv)->changed(o,ov,nv));
		chatArea.setEditable(false);
		isOnline.setSelected(true);
		isOnline.setOnAction(e->onlineStatusChanged());
		leaveRoomMenuItem.setOnAction(e->leaveChatRoom());
		deleteRoomContentsMenuItem.setOnAction(e->deleteRoomContents());
		
		doLayout();
	}
	public void doLayout() {
		BorderPane mainPane = new BorderPane();
		Menu setupMenu = new Menu("설정");
		setupMenu.getItems().addAll(leaveRoomMenuItem, deleteRoomContentsMenuItem);
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().add(setupMenu);
		
		HBox choiceBox = new HBox();
		choiceBox.setPadding(new Insets(10));
		choiceBox.setSpacing(10);
		choiceBox.getChildren().addAll(roomChoice, isOnline);
		roomChoice.setMinWidth(200); 
		
		chatArea.setPrefRowCount(8);
		
		HBox sendBox = new HBox();
		sendBox.setPadding(new Insets(10));
		sendBox.setSpacing(10);
		sendBox.getChildren().addAll(sendArea, sendButton);
		sendArea.setPrefRowCount(2);
		sendArea.setPrefColumnCount(40);
		sendButton.setMinWidth(60);
		sendButton.setMinHeight(60);
		sendButton.setOnAction(e->sendMessage());
		
		mainPane.setTop(choiceBox);
		mainPane.setCenter(chatArea);
		mainPane.setBottom(sendBox);
		
		VBox topPane = new VBox();
		topPane.getChildren().addAll(menuBar, mainPane);
		
		setTitle(user.getUserID());
		setScene(new Scene(topPane,300,300));
	}
	public void changed(
		ObservableValue<? extends String> observable, String oldValue, String roomName) {
		if(roomName!=null) chatArea.setText(prepareOutput(roomName));
	}
	public void onlineStatusChanged() {
		user.setOnline(isOnline.isSelected());
	}
	public void sendMessage() {
		String roomName = roomChoice.getSelectionModel().getSelectedItem();
		String message = sendArea.getText();
		if(message.length()!=0) {
			ChatServer.getServer().sendMessage(roomName, new ChatMessage(user.getUserID(), message));
			sendArea.setText("");
		}
	}
	public void leaveChatRoom() {
		Platform.runLater(new Runnable() {
		    @Override public void run() {
		    	String roomName = roomChoice.getSelectionModel().getSelectedItem();
		    	if(chatConfirmDialog("코리아텍 ChatTalk", 
						roomName+"에서 나가시겠습니까???", 
		    			"나가기", "취소")){
					ChatServer.getServer().deleteUserFromRoom(user.getUserID(), roomName);
					if(user.getRooms().length>=1) {
						roomChoice.getItems().remove(roomName);
						roomChoice.getSelectionModel().select(0);
						roomName = roomChoice.getSelectionModel().getSelectedItem();
						update(roomName);
					}
					else close();
		    	}
		    }
		});
		
	}
	public void deleteRoomContents() {
		String roomName = roomChoice.getSelectionModel().getSelectedItem();
		if(chatConfirmDialog("코리아텍 ChatTalk", 
				roomName+"의 내용을 모두 지우시겠습니까?", 
    			"지우기", "취소")){
			user.deleteRoomContents(roomName);
			update(roomName);
    	}
	}
	public void update(String roomName) {
		roomChoice.getSelectionModel().select(roomName);
		chatArea.setText(prepareOutput(roomName));
	}
	// 여러 메시지를 동시에 출력하기 위한 보조 함수 (매번 모든 메시지를 다시 출력)
	private String prepareOutput(String roomName) {
		List<ChatMessage> chats = user.getRoomLog(roomName).getMessages();
		/*
		String output = "";
		for(ChatMessage chat: chats) {
			output += chat.getUserID()+": "+chat.getMessage()+"\n";
		}
		*/
		return chats.stream().map(c->c.getSenderID()+": "+c.getContent()+"\n")
				.collect(Collectors.joining());
	}
	public static boolean chatConfirmDialog(String title, String content,
			String okButton, String cancelButton){
		Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle(title);
    	alert.setHeaderText(null);
    	alert.setContentText(content);
    	ButtonType buttonTypeOK = new ButtonType(okButton, ButtonData.OK_DONE);
    	ButtonType buttonTypeCancel = new ButtonType(cancelButton, ButtonData.CANCEL_CLOSE);
    	alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);
    	ImageView icon = new ImageView(new Image("koreatech.jpg"));
		icon.setFitHeight(80);
		icon.setPreserveRatio(true);
		alert.setGraphic(icon);
    	Optional<ButtonType> result = alert.showAndWait();
    	return (result.isPresent()&&result.get() == buttonTypeOK);
	}
}

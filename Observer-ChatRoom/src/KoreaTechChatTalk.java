import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * 관찰자 패턴 실습
 * KoreaTechChatTalk.java
 * 채팅 프로그램 
 * @author 노기현 
 *
 */
public class KoreaTechChatTalk extends Application {
	private List<UserChatWindow> chatWindows = new ArrayList<>();
	
	// 그룹 채팅 시뮬레이션
	// 두 개의 채팅방을 만들고 총 5명의 사용자 중 한 방에는 4명, 또 다른 방에는 3명 참여
	public void startTalkSimulation() {
		ChatServer chatServer = ChatServer.getServer();
		chatServer.addUser(new User("홍길동"));
		chatServer.addUser(new User("임꺽정"));
		chatServer.addUser(new User("장길산"));
		chatServer.addUser(new User("장보고"));
		chatServer.addUser(new User("잭스패로우"));
		chatServer.addRoom("객체지향개발론및실습");
		chatServer.addRoom("놀자");
		chatServer.addUserToRoom("홍길동","객체지향개발론및실습");
		chatServer.addUserToRoom("임꺽정","객체지향개발론및실습");
		chatServer.addUserToRoom("장길산","객체지향개발론및실습");
		chatServer.addUserToRoom("장보고","객체지향개발론및실습");
		chatServer.addUserToRoom("홍길동","놀자");
		chatServer.addUserToRoom("임꺽정","놀자");
		chatServer.addUserToRoom("잭스패로우","놀자");
		
		double x = 400;
		double y = 100;
		for(User user: chatServer.getUsers()) {
			UserChatWindow chatWindow = new UserChatWindow(user);
			user.setView(chatWindow);
			chatWindow.setX(x);
			chatWindow.setY(y);
			chatWindow.show();
			chatWindows.add(chatWindow);
			x += 350;
			if(x==1100) {
				y += 350;
				x = 50;
			}
		}
	}
	public void closeAllWindows() {
		for(UserChatWindow chatWindow: chatWindows)
			chatWindow.close();
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		Button startButton = new Button("코리아텍 ChatTalk 시작");	
		Button closeAllButton = new Button("대화창 모두 닫기");
		startButton.setMinWidth(160);
		closeAllButton.setMinWidth(160);
		
		VBox buttonBox = new VBox();
		buttonBox.setPadding(new Insets(10));
		buttonBox.setSpacing(10);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.getChildren().addAll(startButton, closeAllButton);
		
		startButton.setOnAction(e->startTalkSimulation());
		closeAllButton.setOnAction(e->closeAllWindows());
		
		primaryStage.setTitle("KoreaTech ChatTalk");
		primaryStage.setScene(new Scene(buttonBox,300,300));
		primaryStage.setX(50);
		primaryStage.setY(100);
		primaryStage.show();
		primaryStage.getScene().getWindow()
			.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, e->closeAllWindows());
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * 관찰자 패턴 실습
 * User.java 
 * 채팅 프로그램에서 사용자 역할을 함 
 * 가입된 채팅방마다 채팅메시지 목록 유지
 * 채팅방에 대해서는 관찰자, 채팅창에 대해서는 subject
 * @author 노기현 
 *
 */
public class User{
	private String userID;
	private boolean isOnline = true;
	// Map<채팅방, 채팅메시지 목록>
	private Map<String, ChatRoomLog> roomLogs = new HashMap<>();
	// 뷰와 사용자는 1:1 관계. Hard Coded Notification
	private UserChatWindow chatWindow;
	
	public User(String userID) {
		this.userID = Objects.requireNonNull(userID);
	}
	public String getUserID() {
		return userID; 
	}
	public void setOnline(boolean flag) {
		isOnline = flag;
	}
	public boolean isOnline() {
		return isOnline;
	}
	public void joinRoom(String roomName) {
		// roomName에 해당되는 방이 이미 있는 경우에는?
		// 예외 추가
		if (!roomLogs.containsKey(roomName))
			roomLogs.put(roomName, new ChatRoomLog());
	}
	public void leaveRoom(String roomName) {
		roomLogs.remove(roomName);
	}
	public String[] getRooms() {
		/*
		String[] roomNames = new String[roomLogs.size()];
		int i=0;
		for(String name: roomLogs.keySet()) {
			roomNames[i] = name;
			++i;
		}
		return roomNames;
		*/
		String[] roomNames = new String[roomLogs.size()];
		roomLogs.keySet().toArray(roomNames);
		return roomNames;
	}
	public ChatRoomLog getRoomLog(String roomName) {
		return roomLogs.get(roomName);
	}
	public void deleteRoomContents(String roomName) {
		ChatRoomLog roomLog = roomLogs.get(roomName);
		if(roomLog!=null) roomLog.clear();
	}

	public void setView(UserChatWindow chatWindow) {
		this.chatWindow = chatWindow;
	}
	public void notifyView(String roomName) {
		chatWindow.update(roomName);
	}
	
	// 관찰자 패턴에서 update
	// 관찰대상인 ChatRoom이 호출하는 메소드	
	public void update(String roomName, ChatMessage message) {
		if(roomLogs.containsKey(roomName)) {
			roomLogs.get(roomName).addMessage(message);
			notifyView(roomName);
		}
		else throw new IllegalArgumentException("참여하지 않은 참여자에게 update가 이루어졌음");
	}
}

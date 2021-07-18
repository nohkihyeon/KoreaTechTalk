import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * 관찰자 패턴 실습
 * ChatServer.java 
 * 채팅 프로그램에서 통신 서버와 데이터베이스 서버 역할을 함 
 * 채팅룸 목록, 사용자 목록 유지
 * @author 노기현 
 *
 */
public class ChatServer {
	// 데이터베이스 역할을 위한 두 개의 맵
	private Map<String, ChatRoom> chatRooms = new HashMap<>();
	private Map<String, User> users = new HashMap<>();
	private static ChatServer uniqueInstance = null;
	private ChatServer() {}
	// 싱글톤으로 모델링, 전역변수처럼 사용할 수 있음
	public static ChatServer getServer() {
		if(uniqueInstance==null) uniqueInstance = new ChatServer();
		return uniqueInstance;
	}
	// 데이터베이스 역할을 위한 메소드들
	public void addRoom(String roomName) {
		if(!chatRooms.containsKey(roomName)) // 간단 중복 검사
			chatRooms.put(roomName, new ChatRoom(roomName));
	}
	public Optional<ChatRoom> getRoom(String roomName) {
		return Optional.ofNullable(chatRooms.get(roomName));
	}
	public void addUser(User user) {
		if(!users.containsKey(user.getUserID())) // 간단 중복 검사
			users.put(user.getUserID(),user);
	}
	public Optional<User> getUser(String userID) {
		return Optional.ofNullable(users.get(userID));
	}
	public Collection<User> getUsers(){
		return users.values();
	}
	public void addUserToRoom(String userID, String roomName) {
		// 예외 처리 추가 : chatRooms에 roomName이 있어야 추가할 수 있게 한다. 
		if (chatRooms.containsKey(roomName)) 
		{
			User user = users.get(userID);
			ChatRoom chatRoom = chatRooms.get(roomName);
			if(chatRoom.addUser(userID)) // 간단 중복 검사
				user.joinRoom(roomName);
		}
	}
	public void deleteUserFromRoom(String userID, String roomName) {
		// 예외 처리 추가 : chatRooms에 roomName이 있어야지 삭제가능하게 한다. 
		if (chatRooms.containsKey(roomName)) {
			User user = users.get(userID);
			ChatRoom chatRoom = chatRooms.get(roomName);
			chatRoom.deleteUser(userID);
			user.leaveRoom(roomName);
		}
	}
	
	
	// 통신 서버를 위한 메소드들
	// 사용자나 sendMessage를 이용하여 메시지 전송
	// 전송된 메시지는 ChatRoom에 추가됨
	// ChatRoom은 관찰자 패턴을 이용하여 가입된 모든 사용자에게 메시지 전달
	public void sendMessage(String roomName, ChatMessage message) {
		ChatRoom chatRoom = chatRooms.get(roomName);
		chatRoom.newMessage(message); 
	}
	// ChatRoom에서 각 사용자에게 메시지를 실제 전달할 때 사용
	public boolean forwardMessage(String destID, String roomName, ChatMessage message) {
		User receiver  = users.get(destID);
		if(receiver.isOnline()) {
			receiver.update(roomName, message);
			return true;
		}
		else return false;
	}
}

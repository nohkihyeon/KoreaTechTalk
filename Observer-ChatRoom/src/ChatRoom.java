import java.util.HashMap;
import java.util.Map;

/**
 * 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * 관찰자 패턴 실습
 * ChatRoom.java
 * 사용자 목록과 채팅 메시지 목록 유지
 * 채팅룸 목록, 사용자 목록 유지
 * @author 노기현 
 * 관찰자 패턴에서 Subject에 해당함
 */
public class ChatRoom{
	private String roomName;
	private ChatRoomLog roomLog = new ChatRoomLog();
	// 관찰자 목록: Map<사용자ID, 마지막 받은 메시지 색인>
	private Map<String, Integer> userList = new HashMap<>();
	
	public ChatRoom(String name) {
		roomName = name;
	}
	
	public String getRoomName() {
		return roomName;
	}
	
	// 채팅 서버가 채팅방에 새 메시지가 생길 때마다 사용함
	public void newMessage(ChatMessage message) {
		// 만약 여기에 예외 처리를 추가한다면... : 보내는 사람이 톡방에 없는사람이라면 보낼 수 없음
		if (!userList.containsKey(message.getSenderID()))
			return;
		roomLog.addMessage(message);
		updateUsers();
	}
	// 관찰자 추가
	// 사용자가 가입할 때 사용함
	// 사용자가 가입된 이후 발생한 메시지만 받음
	// userList Map에 사용자를 추가해야 함
	// @return 추가에 실패할 경우 false, 성공하면 true
	public boolean addUser(String userID) {
		if (!userList.containsKey(userID)) {
			userList.put(userID, roomLog.size());
			return true;
		}
		return false;
	}
	// 관찰자 삭제
	public void deleteUser(String userID) {
		userList.remove(userID);
	}
	// 관찰자 패턴에서 notifyObservers에 해당
	// 채팅방에 있는 모든 사용자에게 최신 메시지를 전달한다.
	// 이전에 받은 메시지부터 최신 메시지까지 전달해야 함. 
	// 즉, 사용자마다 전달해야 하는 메시지 수가 다를 수 있음
	// 특정 사용자는 현재 오프라인일 수 있음
	public void updateUsers() {
	      if(userList.isEmpty()) return;
	      for(String userID: userList.keySet()) {
	    	  if (userList.get(userID) < roomLog.size()) {
	    		  for (int i = userList.get(userID); i < roomLog.size(); i++) {
	    			  if (ChatServer.getServer().forwardMessage(userID,getRoomName(),
	    					  roomLog.getMessages().get(i))) {
	    				  userList.put(userID, userList.get(userID) + 1);
	    			  }
	    		  }
	    	  }
	    	  else {
	    			if (ChatServer.getServer().forwardMessage(userID, getRoomName(),
	    					roomLog.getMessages().get(roomLog.size()))) {
	    				userList.put(userID, userList.get(userID) + 1);
	    			}
	    		  }
	    	 }
		}
   }




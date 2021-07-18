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
		// 완성하시오.
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



/* 숙제 
 * 1. 이 실습에서는 관찰자 패턴을 사용하여 구현하고 있다. 특히, 채팅방을 관찰 대상, 채팅방의 참여자를 관 찰자로 모델링하였다.
 *    관찰자 패턴은 크게 push 또는 pull 방법으로 구현할 수 있다. 현재 제시된 소스는 어느 방식으로 구현되어 있는지 설명하시오
 *    -> ChatRoom에서 각 사용자에게 메시지를 실제 전달하기 때문에 push 방식이다.
 *    
 * 2. 관찰자인 참여자는 하나만 관찰하는 형태가 아니다. 여러 개의 채팅방을 관찰하고 있을 수 있다. 
 *    종류는 같지만 관찰 대상이 복수일 수 있는 구조이다. 관찰자는 관찰 대상을 어떻게 구분하는지 간단히 설명하시오.
 *    ->  ChatRoom과 User가 모두 개별적으로 유지하고 있기 때문에 각 메소드를 이용해서 구분할 수 있다.
 *    
 * 3. 실제코드를 보면 이 두 객체 외에 관찰대상과 관찰자로 모델링하는 것 이있다. 그것이 무엇인지 제시하시오.
 * 	  -> 관찰대상 : 사용자 목록 / 관찰자 : 채팅창이다.
 * 
 * 4. 현재 제시된 구현을 지금보다 더 효과적으로 모델링하는 방법이 있거나 간결성, 강건성, 효율성을 높일 수 있는 리펙토링이 있다면 제시하여 주세요.
 * 	  -> UserChatWindow.java에서 online을 선택할때 오프라인일때 대화(데이터)를 불러와서 바로 표시한다면 더 효과적으로 구현할 수 있을 것 같습니다. 
 * public void onlineStatusChanged() {
		user.setOnline(isOnline.isSelected());
	}
 * 
 * */

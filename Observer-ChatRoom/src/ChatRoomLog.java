import java.util.ArrayList;
import java.util.List;

/**
 * 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * 관찰자 패턴 실습
 * ChatRoomLog.java
 * 채팅 메시지 목록 
 * ChatRoom과 User가 모두 개별적으로 유지
 * @author 노기현 
 *
 */
public class ChatRoomLog{
	private List<ChatMessage> messages = new ArrayList<>();
	
	public void addMessage(ChatMessage message) {
		messages.add(message);
	}
	public List<ChatMessage> getMessages(){
		return messages;
	}
	public int size() {
		return messages.size();
	}
	public void clear() {
		messages.clear();
	}
}

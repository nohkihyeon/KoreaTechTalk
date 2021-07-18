import java.util.Objects;

/**
 * 한국기술교육대학교 컴퓨터공학부 객체지향개발론및실습
 * 관찰자 패턴 실습
 * ChatMessage.java
 * 채팅 메시지 클래스: 데이터 클래스임
 * 전송자와 전송 메시지 유지
 * @author 노기현 
 *
 */
public class ChatMessage {
	private String senderID;	// 전송자
	private String content;		// 전송 내용
	public ChatMessage(String senderID, String content) {
		this.senderID = Objects.requireNonNull(senderID);
		this.content = Objects.requireNonNull(content);
	}
	public String getContent() {
		return content;
	}
	public String getSenderID() {
		return senderID;
	}
}
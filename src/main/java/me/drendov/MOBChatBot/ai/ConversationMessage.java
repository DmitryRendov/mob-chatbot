package me.drendov.MOBChatBot.ai;

/**
 * Represents a single message in a conversation
 */
public class ConversationMessage {
    
    private final String role; // "user" or "assistant"
    private final String content;
    private final long timestamp;
    
    public ConversationMessage(String role, String content) {
        this.role = role;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }
    
    public String getRole() {
        return role;
    }
    
    public String getContent() {
        return content;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        return "ConversationMessage{role='" + role + "', content='" + 
               (content.length() > 50 ? content.substring(0, 50) + "..." : content) + "'}";
    }
}

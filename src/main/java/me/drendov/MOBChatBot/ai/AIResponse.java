package me.drendov.MOBChatBot.ai;

/**
 * Wrapper class for AI provider responses
 * Contains response content, token usage, and success status
 */
public class AIResponse {
    
    private final String content;
    private final int tokensUsed;
    private final boolean success;
    private final String errorMessage;
    
    private AIResponse(String content, int tokensUsed, boolean success, String errorMessage) {
        this.content = content;
        this.tokensUsed = tokensUsed;
        this.success = success;
        this.errorMessage = errorMessage;
    }
    
    /**
     * Create a successful response
     */
    public static AIResponse success(String content, int tokensUsed) {
        return new AIResponse(content, tokensUsed, true, null);
    }
    
    /**
     * Create a failed response
     */
    public static AIResponse failure(String errorMessage) {
        return new AIResponse(null, 0, false, errorMessage);
    }
    
    public String getContent() {
        return content;
    }
    
    public int getTokensUsed() {
        return tokensUsed;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    @Override
    public String toString() {
        if (success) {
            return "AIResponse{success=true, tokensUsed=" + tokensUsed + ", contentLength=" + 
                   (content != null ? content.length() : 0) + "}";
        } else {
            return "AIResponse{success=false, error='" + errorMessage + "'}";
        }
    }
}

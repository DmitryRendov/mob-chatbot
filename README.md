# MOBChatBot

AI-powered chat bot for private Minecraft servers. Provides private conversations with AI assistants using multiple provider backends.

## Current Status: Phase 3 Complete ✅

### Phase 1: Initial Plugin Structure ✅
- ✅ Main plugin class with lifecycle management
- ✅ Package structure: `commands`, `config`, `ai`, `utils`
- ✅ Maven build configuration with Spigot API 1.21.10
- ✅ Basic commands and permissions

### Phase 2: Configuration System ✅
- ✅ **ConfigManager** - Full implementation with validation
- ✅ **config.yml** - Structured configuration
- ✅ **Auto-generation** - Default config on first run
- ✅ **Hot-reload** - `/mobchatreload` command functional

### Phase 3: AI Provider Integration ✅
- ✅ **AIProvider Interface** - Abstraction for multiple AI backends
  - `sendMessage()` - Async CompletableFuture-based API
  - `isConfigured()` - Configuration validation
  - `initialize()` / `shutdown()` - Lifecycle management
- ✅ **AIResponse** - Response wrapper with content, token usage, success status
- ✅ **ConversationMessage** - Message history tracking
- ✅ **AIProviderFactory** - Factory pattern for provider creation
- ✅ **OpenAIProvider** - Full OpenAI Chat Completions API integration
  - OkHttp client for HTTP requests
  - JSON request/response handling with Gson
  - Error handling for rate limits, auth errors, service issues
  - Token usage tracking
- ✅ **BedrockProvider** - AWS Bedrock Runtime integration
  - AWS SDK for Java (Bedrock Runtime)
  - Claude-3-Haiku model support
  - AWS credentials management
  - Throttling and quota exception handling
- ✅ **ChatCommand** - Fully functional with async AI calls
- ✅ **Integration** - AI provider initialized on startup and config reload

**Dependencies Added:**
- OkHttp 4.12.0 - HTTP client for OpenAI API
- Gson 2.10.1 - JSON parsing
- AWS SDK Bedrock Runtime 2.21.0 - Bedrock integration

**Commands:**
- `/mobchat <message>` - Send message to AI bot (aliases: `/mc`, `/chatbot`)
- `/mobchatreload` - Reload plugin configuration (admin only)

**Permissions:**
- `mobchatbot.use` - Use the chat bot (default: true)
- `mobchatbot.admin` - Admin commands (default: op)

### Build Information

**Requirements:**
- Java 21
- Maven 3.6.3+
- Spigot/Paper 1.21.10

**Build Command:**
```bash
mvn clean package
```

**Output:** `target/mob-chatbot-0.3.0.jar` (13MB with dependencies)

**Installation:**
Copy `mob-chatbot-0.3.0.jar` to your server's `plugins/` folder.

## Planned Features

### Phase 4: Conversation Management (Next)
- Per-player conversation tracking with ConversationManager
- Message history persistence
- Message history storage
- Token counting and limits
- Context window management

### Phase 5: Advanced Features
- Cooldown system (track last message time)
- Daily message limits (reset at midnight)
- Cost tracking
- Cost tracking
- Admin statistics
- Conversation export

## Configuration

See `config.yml` for all available settings:
- AI provider selection (OpenAI, Bedrock, Ollama)
- Model parameters (temperature, max tokens)
- Usage limits (daily messages, cooldowns)
- Bot personality and system prompts

## Development

### Project Structure
```
src/main/java/me/drendov/MOBChatBot/
├── MOBChatBot.java           # Main plugin class
├── commands/                  # Command handlers
│   ├── ChatCommand.java
│   └── ReloadCommand.java
├── config/                    # Configuration management
│   └── ConfigManager.java
├── ai/                        # AI provider implementations
│   ├── AIProvider.java
│   └── ConversationManager.java
└── utils/                     # Utility classes
    └── MessageUtils.java
```

## License

TBD

## Author

Dmitry Rendov powered by Copilot

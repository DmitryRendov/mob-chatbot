# MOBChatBot

AI-powered chat bot for private Minecraft servers. Provides private conversations with AI assistants using multiple provider backends.

## Current Status: Phase 1 Complete ✅

### Implemented Features

**Core Structure:**
- ✅ Main plugin class (`MOBChatBot`) extending JavaPlugin
- ✅ Proper `onEnable()` and `onDisable()` lifecycle methods with logging
- ✅ Package structure: `commands`, `config`, `ai`, `utils`
- ✅ Maven build configuration with Spigot API 1.21.10

**Files Created:**
- `plugin.yml` - Plugin metadata (version 0.1.0, API 1.21)
- `config.yml` - Configuration template with AI provider settings
- `MOBChatBot.java` - Main plugin class
- `ChatCommand.java` - Main chat command handler
- `ReloadCommand.java` - Configuration reload command
- `ConfigManager.java` - Configuration management (stub)
- `AIProvider.java` - AI provider interface
- `ConversationManager.java` - Conversation tracking (stub)
- `MessageUtils.java` - Message formatting utilities

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

**Output:** `target/mob-chatbot-0.1.0.jar`

**Installation:**
Copy `mob-chatbot-0.1.0.jar` to your server's `plugins/` folder.

## Planned Features

### Phase 2: Configuration System
- Implement ConfigManager
- Load and validate config.yml
- Hot-reload support
- Per-player settings

### Phase 3: AI Provider Implementation
- OpenAI integration
- AWS Bedrock support
- Ollama local model support
- Provider factory pattern

### Phase 4: Conversation Management
- Per-player conversation tracking
- Message history storage
- Token counting and limits
- Context window management

### Phase 5: Advanced Features
- Cooldown system
- Daily message limits
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

drendov

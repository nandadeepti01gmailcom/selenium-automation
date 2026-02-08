# Secure Configuration Setup

## Overview
This project uses a secure configuration system to keep sensitive data (like Slack webhook URLs, API keys, passwords) out of version control while maintaining easy local development.

## How It Works
1. **Main Config**: `config.properties` - Contains non-sensitive default configuration, committed to git
2. **Local Config**: `config.local.properties` - Contains sensitive data, **NOT committed to git**
3. **Template**: `config.local.properties.template` - Shows what can be configured locally

The `ConfigReader` automatically loads both files, with local config overriding main config properties.

## Setup Instructions

### First Time Setup
1. Copy the template to create your local config:
   ```bash
   cd src/main/resources
   copy config.local.properties.template config.local.properties
   ```

2. Edit `config.local.properties` with your actual sensitive data:
   ```properties
   slack.notifications.enabled=true
   slack.webhook.url=https://hooks.slack.com/services/YOUR/ACTUAL/WEBHOOK
   ```

3. The file is automatically ignored by git - verify with:
   ```bash
   git status  # config.local.properties should NOT appear
   ```

### For Team Members
When cloning the repository:
1. Copy `config.local.properties.template` to `config.local.properties`
2. Get the actual webhook URL from your team lead or Slack workspace
3. Update `config.local.properties` with the real values

## What to Override
Common properties to put in `config.local.properties`:
- `slack.webhook.url` - Your Slack webhook URL
- `slack.notifications.enabled` - Enable/disable Slack notifications
- `dev.valid.username` / `dev.valid.password` - Test credentials for dev environment
- `staging.valid.username` / `staging.valid.password` - Staging credentials
- Any API keys, tokens, or sensitive URLs

## Security Benefits
✅ Secrets never committed to git history  
✅ Each developer can have their own local configuration  
✅ Easy to set up and maintain  
✅ Template file guides new team members  
✅ No system environment variables needed  

## Testing
The `ConfigReader` will log when it loads the local config:
```
✓ Loaded local configuration overrides from config.local.properties
Environment: dev
```

If no local config exists (optional):
```
ℹ No local config file found (this is optional)
Environment: dev
```

## Important Notes
- **Never commit `config.local.properties` to git**
- Keep your webhook URLs private
- Update the template if you add new sensitive properties
- The local config file is optional - the app works without it

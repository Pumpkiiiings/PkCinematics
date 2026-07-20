# Messages and Translations

All text you see in-game (prefixes, errors, confirmation messages) can be translated or customized to your liking.

The file responsible for this is `messages.yml`, located in the main plugin folder (`plugins/PkCinematics/messages.yml`).

## Reloading Messages

After editing the file, you don't need to restart your server. Simply type in chat or console:

```
/cinematic reload messages
```

## Configuration Example

Feel free to use Minecraft color codes (like `&a`, `&c`, `&e`) to customize each text. The plugin will handle translating them correctly.

```yaml
prefix: "&8[&dPkCinematics&8] &r"

no_permission: "&cYou do not have permission to execute this command."

# Main command help
help_header: "&d=== &lPkCinematics Help &d==="
help_create: "&e/cinematic create <id> &7- Creates a new cinematic and opens the editor"
help_edit: "&e/cinematic edit <id> &7- Opens a cinematic for editing"
help_point: "&e/cinematic point &7- Adds a new camera point where you are standing"
help_actions: "&e/cinematic actions <tick> &7- Opens the menu to add actions at a specific tick"
help_save: "&e/cinematic save &7- Saves the cinematic you are editing and closes the editor"
help_play: "&e/cinematic play <id> &7- Plays a cinematic"
help_stop: "&e/cinematic stop &7- Stops the current cinematic"

# Editor
already_exists: "&cThis cinematic ID already exists."
not_found: "&cCinematic not found."
editor_not_editing: "&cYou are not currently editing any cinematic."
editor_point_added: "&aPoint {index} added at tick {tick}!"
saved: "&aCinematic '{name}' saved successfully!"
editor_invalid_index: "&cThe specified point index is invalid."
editor_tick_updated: "&aTick updated to: {value}"

# GUI Actions
action_add_title: "&aType the main title in chat:"
action_add_subtitle: "&aType the subtitle in chat:"
action_add_message: "&aType the message in chat:"
action_add_sound: "&aType the sound name in chat (e.g., ENTITY_PLAYER_LEVELUP):"
```

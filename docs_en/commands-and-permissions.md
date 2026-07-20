# Commands and Permissions

This is the complete list of commands available in PkCinematics. All commands start with `/cinematic`.

## Command List

| Command | Description |
|---------|-------------|
| `/cinematic create <name>` | Creates a new cinematic and enters editor mode. |
| `/cinematic edit <name>` | Opens an existing cinematic for editing. |
| `/cinematic point` | Adds a camera point at your current position and rotation. |
| `/cinematic point edit <name> <index> <property> <value>` | Modifies an existing point. Valid properties: `time` (tick), `fov`, `interp`, `easing`. |
| `/cinematic actions <tick>` | Opens the visual menu to add or remove actions (messages, sounds, etc) at that exact tick. |
| `/cinematic save` | Saves the cinematic you are editing and exits editor mode. |
| `/cinematic play <name>` | Plays the cinematic only for you. |
| `/cinematic play <name> <player/all>` | Plays the cinematic for a specific player or for the whole server. |
| `/cinematic stop` | Immediately stops the cinematic you are watching. |
| `/cinematic reload <cinematics/triggers/messages/all>` | Reloads the configuration from files without restarting the server. |
| `/cinematic debug` | Toggles detailed debug messages in chat about what happens in the background (ideal for server owners looking for errors). |

## Permissions

The permission system is very simple to ensure security:

* **`pkcinematics.admin`**: Grants access to **all** `/cinematic` commands. This is the only permission that exists and should only be given to server administrators and content creators.

> **Note:** Regular players do not need any permissions to *view* cinematics (either because they were forced with `/cinematic play <name> <player>` or because an automatic Trigger started it).

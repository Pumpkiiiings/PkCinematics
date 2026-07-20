# PkCinematics Documentation

Welcome to the official documentation for PkCinematics. Below you will find detailed guides on how to use every feature of the plugin.

*Note: The detailed articles are currently available in Spanish.*

## Guides

- [Getting Started (Primeros Pasos)](./getting-started.md)
- [In-Game Editor (Editor In-Game)](./editor.md)
- [Interpolation & Easing (Interpolaciones y Easing)](./interpolations.md)
- [Actions Library (Librería de Acciones)](./actions-library.md)
- [Triggers & Conditions (Disparadores y Condiciones)](./triggers-and-conditions.md)
- [YAML Format (Formato YAML)](./yaml-format.md)
- [Config & Messages (Configuración y Mensajes)](./config-messages.md)

## Command Guide & Permissions

All commands start with `/cinematic` and require the `pkcinematics.admin` permission.
Regular players do not need any permissions to view the cinematics.

### General Commands
- `/cinematic menu` - Opens the main cinematic list GUI.
- `/cinematic create <id>` - Creates a new cinematic and opens the editor.
- `/cinematic edit <id>` - Opens an existing cinematic for editing.
- `/cinematic play <id> [player]` - Plays a cinematic for yourself or a specific player/all players.
- `/cinematic stop` - Instantly stops the cinematic you are watching.

### Editor Commands
- `/cinematic point` - Adds a camera point at your exact current location.
- `/cinematic save` - Saves the current cinematic and exits the editor.
- `/cinematic point edit <id> <index> <property> <value>` - Manually edits a point property via command.
- `/cinematic actions <tick>` - Opens the action editor for a specific tick.

### Administrative Commands
- `/cinematic reload <cinematics/triggers/messages/all>` - Reloads specific configuration files without restarting the server.
- `/cinematic debug` - Toggles debug mode to broadcast background events in chat.

---
*For the Spanish version of this index, see [README_ES.md](./README_ES.md)*

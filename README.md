# PkCinematics

PkCinematics is a lightweight and highly customizable cinematic engine plugin for Paper Minecraft servers. It allows you to create smooth camera movements and trigger complex sequences (such as titles, chat messages, sound effects, particles, and time changes) directly in-game.

## Features

- **Smooth Interpolations:** Move the camera fluidly using Linear or Catmull-Rom spline interpolation.
- **Dynamic Easings:** Control the camera speed seamlessly with Linear, Ease In, Ease Out, or Smooth easing types.
- **In-Game GUI Editor:** Build and tweak cinematics without touching any config files using an intuitive graphical interface.
- **Action Timeline:** Trigger precise events on specific ticks (e.g., spawn particles, play sounds, display titles).
- **Waiting System:** Define pauses at specific camera points seamlessly, without breaking smooth interpolation.
- **Triggers:** Automatically launch cinematics based on events like joining the server, reaching certain coordinates, or triggering WorldGuard regions.
- **Skip System:** Option for players to skip cutscenes by pressing Shift.
- **100% Configurable:** Every single message, GUI string, and prompt is fully customizable (0 hardcode system).

## Dependencies

- **Server version:** Paper 1.21.x (Built for 1.21.1)
- **PacketEvents:** Required for camera packet manipulation. Ensure `packetevents-spigot` (2.13.0+) is installed on your server.
- **Triumph GUI:** Included natively (shaded).

## Commands and Permissions

All commands require the `pkcinematics.admin` permission. Regular players do not need any permission to view cinematics.

| Command | Description |
|---------|-------------|
| `/cinematic menu` | Opens the main list of cinematics. |
| `/cinematic create <id>` | Creates a new cinematic and opens the editor. |
| `/cinematic edit <id>` | Opens an existing cinematic for editing. |
| `/cinematic point` | Quick command to add a camera point at your current location and rotation. |
| `/cinematic save` | Saves the cinematic you are currently editing and closes the editor. |
| `/cinematic play <id> [player]` | Plays a cinematic. If no player is specified, it plays for you. |
| `/cinematic stop` | Stops the cinematic you are currently watching. |
| `/cinematic reload` | Reloads all configurations (messages, GUIs, triggers). |
| `/cinematic debug` | Toggles debug mode to see background events in the chat. |

## Quick Start

1. Type `/cinematic menu` and click "Create New Cinematic".
2. Type an ID in chat (e.g., `intro`).
3. Fly to your starting location and click **"Add Point Here"** (or type `/cinematic point`).
4. Fly to the next location and repeat.
5. In the Keyframes menu, left-click a point to adjust the tick (time), interpolation, easing, or add waiting time.
6. Click **"Actions"** to add titles or sounds at specific ticks.
7. Click **"Save and Exit"**, then use `/cinematic play intro` to see it in action!

## Documentation

For a detailed explanation of triggers, conditions, action types, and YAML formatting, check out the documentation in the `/docs` folder!

- [English Documentation](./docs_en/README.md)
- [Documentación en Español](./docs_es/README.md)

---
*Created by Pumpkiiiings*

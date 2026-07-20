# The In-Game Editor (Actions)

PkCinematics includes an in-game Graphical User Interface (GUI) based on inventory menus. This allows you to schedule events (such as playing sounds or displaying titles) at the exact moment you want.

## How to Open the Editor

First, make sure you are editing a cinematic (`/cinematic edit <name>`).

Then, use this command to open the menu for a specific second:

```
/cinematic actions <tick>
```
Example: `/cinematic actions 100` (Opens the editor at tick 100, which is exactly the 5th second of the cinematic).

## Using the Menu

When executing the command, an inventory will open showing:
1. **At the top:** Actions already configured to run at that exact `tick`.
2. **At the bottom:** A green emerald button that says "Add Action".

### Adding an Action
1. Click the **Green Button (+)**.
2. The **Action Catalog** will open. Here you'll see icons representing all available actions (Messages, Titles, Particles, Sounds, Commands, etc.).
3. Click the one you want to add.
4. The chat will ask you to enter the necessary information. For example, if you chose "Message", it will ask you to type the message text in chat.
5. Once you enter the information, the action will be saved and you will return to the menu.

### Removing an Action
If you made a mistake, simply return to the `/cinematic actions <tick>` menu, look for the action item you want to delete at the top, and **Right-Click** on it.

## Available Actions

We have a huge variety of actions you can configure, from playing sounds to temporarily changing the weather for dramatic effect in your cinematic.

Check out the **[Actions Library](actions-library.md)** for a complete list and examples.

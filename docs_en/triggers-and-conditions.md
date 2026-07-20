# Triggers and Conditions

You don't always want to start a cinematic by typing `/cinematic play <player>`. With **Triggers** (Events), you can configure the plugin to do it automatically when something happens on the server.

Triggers are configured using `.yml` files inside the `plugins/PkCinematics/triggers/` folder. You can create as many files as you want.

## Trigger Structure

Example of `my_first_join.yml`:

```yaml
type: first_join
cinematic: epic_intro
conditions:
  - type: gamemode
    gamemodes: [SURVIVAL, ADVENTURE]
```

* **`type`**: The event that will initiate the cinematic.
* **`cinematic`**: The name (id) of the cinematic that will be played.
* **`conditions`**: (Optional) Requirements that the player must meet for the cinematic to start.

## Trigger Types

You can use any of these events in the `type` property:

* **`first_join`**: When a player joins the server for the *first time ever*.
* **`join`**: When a player joins the server (every time).
* **`quit`**: When a player leaves the server (note: you can't show them the cinematic if they already left, this is more useful if you wanted to trigger a global command).
* **`respawn`**: When the player respawns after dying.
* **`death`**: The exact instant they die.
* **`world_change`**: When the player teleports or goes through a portal to another world.
* **`resource_pack_loaded`**: When the player finishes accepting and downloading the server's resource pack.
* **`resource_pack_declined`**: If the player declines downloading the server's resource pack.

## Available Conditions

Inside the `conditions` list, you can add blocks so the cinematic won't start if certain rules are not met.

**1. By Permission (`permission`)**
Requires the player to have a specific permission.
```yaml
  - type: permission
    permission: "vip.watch_intro"
```

**2. By World (`world`)**
Requires the event to have occurred in a specific world.
```yaml
  - type: world
    worlds: ["world", "world_the_end"]
```

**3. By Gamemode (`gamemode`)**
Blocks the cinematic if the player is in creative, for example.
```yaml
  - type: gamemode
    gamemodes: ["SURVIVAL", "ADVENTURE"]
```

**4. Has Played Before (`played_before`)**
Checks if the player is old on the server (`true`) or completely new (`false`).
```yaml
  - type: played_before
    played_before: true
```

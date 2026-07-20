# Actions Library

During your cinematic's timeline, you can trigger scheduled events (Titles, Sounds, Particle Effects, etc.). You can easily add them using the in-game GUI menu (`/cinematic actions <tick>`) or by directly editing the `.yml` file.

Below is a complete list of supported actions.

---

### `title`
Displays a large title and subtitle on the player's screen.
* **title**: Main text. Supports colors (`&a`, `&l`).
* **subtitle**: Bottom text.
* **fadeIn**: Time it takes to fade in (in ticks).
* **stay**: Time it remains on screen.
* **fadeOut**: Time it takes to fade out.

### `message`
Sends a traditional chat message to the player.
* **message**: The message text (supports colors).

### `actionbar`
Displays a small message above the hotbar (where item names appear).
* **message**: The text.

### `sound`
Plays a Minecraft sound for the player.
* **sound**: Official sound name (e.g., `ENTITY_ENDER_DRAGON_GROWL`).
* **volume**: Volume (0.0 to 1.0+).
* **pitch**: Speed/pitch of the sound (0.5 to 2.0).

### `particle`
Spawns particle effects in front of the camera.
* **particle**: Official particle name (e.g., `FLAME`, `EXPLOSION`).
* **count**: Amount of particles.
* **offsetX / offsetY / offsetZ**: Particle spread area.
* **speed**: Speed at which they are shot.

### `potion_effect`
Applies a potion effect to the player (note: the real player is invisible on the ground during the cinematic, so effects like `BLINDNESS` will affect their camera, ideal for transitions).
* **effect**: Potion name (e.g., `BLINDNESS`, `CONFUSION`).
* **duration**: Duration in ticks (e.g., 100 for 5 seconds).
* **amplifier**: Potion level (e.g., 1 for level 2).

### `command`
Executes a command on the server.
* **command**: The command to execute (without the leading `/`).
* **executor**: Who executes it. Options: `PLAYER` (as if the player typed it), or `CONSOLE` (executed by the console).

### `time`
Alters the time only for the viewer!
* **time**: The time value (e.g., `0` for sunrise, `6000` for noon, `18000` for midnight). Useful if you want to quickly turn it to night for dramatic effect.

### `weather`
Alters the weather only for the viewer!
* **weather**: The desired weather. Options: `CLEAR` (Sunny), `DOWNFALL` (Rain/Snow).

### `reset_environment`
Returns the player's Time and Weather to normal (syncs it with the server's real world time). Useful at the end of the cinematic.
* (No extra parameters required).

### `play_cinematic`
Plays another cinematic (useful for linking multiple scenes).
* **cinematic**: The ID/name of the cinematic to be played next.

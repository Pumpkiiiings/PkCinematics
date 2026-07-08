# 📚 Wiki de PkCinematics (Guía para Principiantes)

¡Bienvenido a la wiki de **PkCinematics**! Aquí aprenderás cómo funciona el plugin explicado paso a paso, con peras y manzanas, para que puedas crear las mejores cinemáticas en tu servidor sin dolores de cabeza.

---

## 🍎 1. Conceptos Básicos: Triggers vs Cinemáticas

Imagina que estás preparando un espectáculo de fuegos artificiales:
* **El Trigger (Gatillo/Detonador):** Es el botón rojo que alguien tiene que presionar para que empiece el show. En el juego, el botón rojo puede ser "cuando el jugador entra al servidor" o "cuando el jugador revive".
* **La Cinemática (El Show):** Son los fuegos artificiales en sí. Es la línea de tiempo donde dices: *"En el segundo 1 lanza el cohete rojo, en el segundo 3 pon un sonido de explosión"*.

En PkCinematics, primero creas el "Show" (la cinemática) y luego creas el "Botón" (el trigger) que lo va a encender.

---

## 🍏 2. ¿Cómo funcionan las Cinemáticas? (El Show)

Las cinemáticas viven en la carpeta `plugins/PkCinematics/cinematics/`.

Todo en Minecraft funciona por **Ticks** (el reloj del juego).
👉 **20 Ticks = 1 Segundo de la vida real.**
* 40 ticks = 2 segundos
* 100 ticks = 5 segundos

### Estructura de una Cinemática

Cada archivo `.yml` tiene 3 partes importantes:
1. `duration`: Cuánto dura en total el show (en ticks).
2. `camera`: El recorrido que hará el jugador (puntos en el mapa).
3. `actions`: **Las Funciones** especiales (efectos, sonidos, títulos) que ocurren en momentos exactos.

### ¿Cómo meter Funciones a las Cinemáticas?

Dentro de `actions`, escribes el **tick exacto** en el que quieres que pase algo, seguido de una lista de cosas.

```yaml
actions:
  '0': # En el segundo 0 (Apenas empieza)
    - type: potion_effect
      effect: BLINDNESS
      duration: 60

  '40': # 2 Segundos después (Tick 40)
    - type: title
      title: "&c¡Cuidado!"
      subtitle: "&7Estás entrando a zona de peligro"
      fadeIn: 20
      stay: 60
      fadeOut: 20
    
    - type: sound
      sound: ENTITY_ENDER_DRAGON_GROWL
      volume: 1.0
      pitch: 1.0
```

### 📋 Lista Completa de Funciones (Acciones) que puedes usar:

* **Mensajes Visuales:**
  * `title`: Muestra texto gigante en el centro de la pantalla. (Necesita `title`, `subtitle`, `fadeIn`, `stay`, `fadeOut`).
  * `actionbar`: Muestra texto pequeñito arriba del inventario. (Necesita `text`).
  * `message`: Envía un mensaje normal por el chat. (Necesita `text`). *(Puedes usar `%player_name%`)*.

* **Efectos y Ambiente:**
  * `sound`: Reproduce un ruido. (Necesita `sound` con el nombre en mayúsculas de Minecraft, `volume` y `pitch`).
  * `particle`: Crea partículas mágicas. (Necesita `particle`, `count`, `offsetX`, `offsetY`, `offsetZ`, `speed`).
  * `potion_effect`: Da un efecto de poción como ceguera. (Necesita `effect` y `duration`).

* **Magia de Clima y Hora:**
  * `time`: Cambia si es de día o de noche **solo para ese jugador**. (Necesita `time`, por ejemplo `18000` para noche).
  * `weather`: Cambia si llueve o no **solo para el jugador**. (Necesita `weather: DOWNFALL` o `CLEAR`).
  * `reset_environment`: ⚠️ **MUY IMPORTANTE**. Siempre pon esto en el último tick de tu cinemática si usaste `time` o `weather`, para devolver al jugador a la realidad de tu servidor.

* **Otros:**
  * `command`: Ejecuta un comando. (Necesita `command` y puedes poner `console: true` si quieres que lo ejecute la consola, no el jugador).

---

## 🍐 3. ¿Cómo funcionan los Triggers? (El Botón Rojo)

Los Triggers viven en la carpeta `plugins/PkCinematics/triggers/`. Sirven para decidir **cuándo** y **a quién** se le reproduce la cinemática que creamos arriba.

### Tipos de "Botones" disponibles (Eventos):
Puedes elegir cuándo se activa en la sección `type`:
* `first_join`: La primera vez en la vida que el jugador entra al servidor.
* `join`: Cada vez que el jugador entra al servidor.
* `quit`: Cuando el jugador sale del servidor.
* `respawn`: Cuando el jugador revive tras morir.
* `death`: Justo en el momento que muere.
* `world_change`: Cuando el jugador atraviesa un portal o se teletransporta a un mundo diferente.

### Condiciones (Filtros de Seguridad)
A veces no quieres que el botón funcione para todos. Puedes poner condiciones.

```yaml
trigger:
  type: respawn # Cuando reviva

conditions:
  - type: world
    world: world_the_end # SOLO si revive en el mundo del End
  - type: gamemode
    value: SURVIVAL # SOLO si está en modo supervivencia
```

### Acciones del Trigger
Finalmente, le dices al Trigger qué debe hacer cuando se presione el botón. ¡Aquí es donde llamas a tu cinemática!

```yaml
actions:
  - type: cinematic
    id: intro_epica # Reproduce el archivo 'intro_epica.yml' de tu carpeta de cinemáticas
  - type: message
    text: "Disfruta de la vista."
```

---

## 💡 4. Consejos de Oro

1. **La Pantalla de Carga:** Si usas el trigger `first_join` o `world_change`, el jugador estará en la pantalla de "Descargando Terreno". Si le pones un `title` en el tick `'0'`, no lo verá. **¡Retrasa tus títulos al tick '40' o '60'!**
2. **Usa Ceguera:** Dar ceguera (`BLINDNESS`) en el tick `'0'` es un truco excelente para ocultar que el mundo está cargando.
3. **No olvides el Reset:** Si usaste nieve, lluvia o hiciste de noche, recuerda llamar a la acción `reset_environment` al final de la cinemática para que no se queden atrapados en un mundo oscuro para siempre.

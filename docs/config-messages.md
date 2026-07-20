# Mensajes y Traducciones

Todo el texto que ves dentro del juego (prefijos, errores, mensajes de confirmación) se puede traducir o personalizar a tu gusto.

El archivo responsable de esto es `messages.yml`, ubicado en la carpeta principal del plugin (`plugins/PkCinematics/messages.yml`).

## Recargar Mensajes

Después de editar el archivo, no necesitas reiniciar tu servidor. Simplemente escribe en el chat o consola:

```
/cinematic reload messages
```

## Ejemplo de Configuración

Siéntete libre de usar códigos de color de Minecraft (como `&a`, `&c`, `&e`) para personalizar cada texto. El plugin se encargará de traducirlos correctamente.

```yaml
prefix: "&8[&dPkCinematics&8] &r"

no_permission: "&cNo tienes permisos para ejecutar este comando."

# Ayuda del comando principal
help_header: "&d=== &lPkCinematics Ayuda &d==="
help_create: "&e/cinematic create <id> &7- Crea una nueva cinemática y empieza a editarla"
help_edit: "&e/cinematic edit <id> &7- Abre una cinemática para editarla"
help_point: "&e/cinematic point &7- Añade un nuevo punto de cámara donde estás parado"
help_actions: "&e/cinematic actions <tick> &7- Abre el menú para añadir acciones en un tick específico"
help_save: "&e/cinematic save &7- Guarda la cinemática que estás editando y cierra el editor"
help_play: "&e/cinematic play <id> &7- Reproduce una cinemática"
help_stop: "&e/cinematic stop &7- Detiene la cinemática actual"

# Editor
already_exists: "&cYa existe una cinemática con ese ID."
not_found: "&cCinemática no encontrada."
editor_not_editing: "&cNo estás editando ninguna cinemática actualmente."
editor_point_added: "&a¡Punto {index} añadido en el tick {tick}!"
saved: "&a¡Cinemática '{name}' guardada correctamente!"
editor_invalid_index: "&cEl índice del punto especificado es inválido."
editor_tick_updated: "&aTick actualizado a: {value}"

# Acciones GUI
action_add_title: "&aEscribe en el chat el título principal:"
action_add_subtitle: "&aEscribe en el chat el subtítulo:"
action_add_message: "&aEscribe en el chat el mensaje:"
action_add_sound: "&aEscribe en el chat el nombre del sonido (ej. ENTITY_PLAYER_LEVELUP):"
```

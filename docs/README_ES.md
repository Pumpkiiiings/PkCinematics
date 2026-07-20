# Documentación de PkCinematics

Bienvenido a la documentación oficial de PkCinematics. A continuación encontrarás guías detalladas sobre cómo usar cada característica del plugin.

## Guías y Tutoriales

- [Primeros Pasos](./getting-started.md)
- [Editor In-Game](./editor.md)
- [Interpolaciones y Easing](./interpolations.md)
- [Librería de Acciones](./actions-library.md)
- [Disparadores y Condiciones (Triggers)](./triggers-and-conditions.md)
- [Formato YAML](./yaml-format.md)
- [Configuración y Mensajes](./config-messages.md)

## Guía de Comandos y Permisos

Todos los comandos empiezan por `/cinematic` y requieren el permiso `pkcinematics.admin`.
Los jugadores normales no necesitan permisos para ver las cinemáticas.

### Comandos Generales
- `/cinematic menu` - Abre el menú gráfico principal con la lista de cinemáticas.
- `/cinematic create <id>` - Crea una nueva cinemática y abre el editor.
- `/cinematic edit <id>` - Abre una cinemática existente para editarla.
- `/cinematic play <id> [jugador]` - Reproduce la cinemática para ti mismo, o para el jugador especificado.
- `/cinematic stop` - Detiene la cinemática que estás viendo en este momento.

### Comandos de Edición
- `/cinematic point` - Añade un punto de cámara en tu posición exacta de manera rápida.
- `/cinematic save` - Guarda la cinemática actual y cierra el modo de edición.
- `/cinematic point edit <id> <index> <propiedad> <valor>` - Edita manualmente una propiedad de un punto de la cámara mediante comando.
- `/cinematic actions <tick>` - Abre el editor de acciones para un tick específico.

### Comandos Administrativos
- `/cinematic reload <cinematics/triggers/messages/all>` - Recarga configuraciones específicas sin necesidad de reiniciar el servidor.
- `/cinematic debug` - Activa o desactiva los mensajes detallados de depuración en el chat (muy útil para encontrar errores en la línea de tiempo).

---
*For the English version of this index, see [README.md](./README.md)*

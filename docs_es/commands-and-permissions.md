# Comandos y Permisos

Esta es la lista completa de comandos disponibles en PkCinematics. Todos los comandos empiezan por `/cinematic`.

## Lista de Comandos

| Comando | Descripción |
|---------|-------------|
| `/cinematic create <nombre>` | Crea una nueva cinemática y entra en el modo editor. |
| `/cinematic edit <nombre>` | Abre una cinemática existente para editarla. |
| `/cinematic point` | Añade un punto de cámara en tu posición y rotación actual. |
| `/cinematic point edit <nombre> <index> <propiedad> <valor>` | Modifica un punto existente. Propiedades válidas: `time` (tick), `fov`, `interp`, `easing`. |
| `/cinematic actions <tick>` | Abre el menú visual para añadir o quitar acciones (mensajes, sonidos) en ese tick exacto. |
| `/cinematic save` | Guarda la cinemática que estás editando y sale del modo editor. |
| `/cinematic play <nombre>` | Reproduce la cinemática solo para ti. |
| `/cinematic play <nombre> <jugador/todos>` | Reproduce la cinemática para un jugador específico o para todo el servidor. |
| `/cinematic stop` | Detiene inmediatamente la cinemática que estás viendo. |
| `/cinematic reload <cinematics/triggers/messages/all>` | Recarga la configuración desde los archivos sin reiniciar el servidor. |
| `/cinematic debug` | Activa mensajes detallados en el chat sobre lo que ocurre en segundo plano (ideal para dueños de servidores que buscan errores). |

## Permisos

El sistema de permisos es muy simple para garantizar la seguridad:

* **`pkcinematics.admin`**: Otorga acceso a **todos** los comandos de `/cinematic`. Este es el único permiso que existe y solo debe dársele a los administradores y creadores de contenido del servidor.

> **Nota:** Los jugadores normales no necesitan ningún permiso para *ver* las cinemáticas (ya sea porque se les forzó con `/cinematic play <nombre> <jugador>` o porque un Trigger automático la inició).

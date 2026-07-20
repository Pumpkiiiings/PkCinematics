# PkCinematics

**PkCinematics** es un motor avanzado de cinemáticas para Minecraft basado en **Paper** y **Folia**. Permite a los administradores crear increíbles recorridos de cámara fluidos, añadir efectos visuales y sonoros en momentos exactos (línea de tiempo), y reproducir escenas automáticamente usando un sistema de Triggers.

Todo el procesamiento de la cámara se realiza enviando paquetes directamente a los jugadores (gracias a *PacketEvents*), lo que significa que el plugin es extremadamente ligero y **no** teleporta al jugador real, evitando problemas de chunks, caídas por daño de caída o incompatibilidades de anticheats.

## ✨ Características Principales

* **Editor In-Game Visual:** Crea, edita y ajusta cinemáticas y acciones sin salir del juego.
* **Sistema de Timeline Avanzado:** Configura mensajes, títulos, comandos, sonidos y partículas para que ocurran en `ticks` específicos durante la cinemática.
* **Cámara de Alta Fluidez:**
  * Interpolación matemática **Catmull-Rom** para movimientos de cámara perfectamente curvos y sin cortes.
  * Funciones de **Easing** (Aceleración): Movimientos `Smooth`, `Ease-In` o `Ease-Out` que aportan un toque 100% profesional.
  * **Transición de FOV (Zoom):** Acércate dramáticamente a un objetivo controlando el FOV del jugador en cada punto.
* **Reproducción Automática (Triggers):** Configura cinemáticas para que se reproduzcan solas cuando un jugador entra al servidor por primera vez, cambia de mundo, muere, etc.
* **Soporte Folia / Paper:** Desarrollado utilizando `EntityScheduler` para garantizar la compatibilidad con servidores multihilo.
* **Restaura el Estado:** El plugin recuerda la ubicación, inventario, vida y modo de juego del jugador. Al terminar la cinemática, todo vuelve a la normalidad.

## 📦 Instalación

1. Asegúrate de estar ejecutando un servidor compatible con **Paper**, **Purpur** o **Folia** (Minecraft 1.20 o superior).
2. Necesitarás instalar el plugin **[PacketEvents](https://github.com/retrooper/packetevents/releases)** en la carpeta `plugins`.
3. Descarga **PkCinematics** y colócalo en tu carpeta `plugins/`.
4. Reinicia tu servidor.
5. Usa `/cinematic create <nombre>` para empezar tu primera obra maestra.

## 📚 Documentación y Guías

Toda la documentación detallada sobre cómo sacarle el máximo partido al plugin se encuentra en la carpeta `docs/`. **¡Empieza por aquí!**

1. **[Empezando: Tu primera cinemática](docs/getting-started.md)**
2. **[El Editor GUI In-Game](docs/editor.md)**
3. **[Guía de Movimiento y Cámara (Easing y FOV)](docs/interpolations.md)**
4. **[Comandos y Permisos](docs/commands-and-permissions.md)**
5. **[Librería de Acciones y Efectos](docs/actions-library.md)**
6. **[Triggers (Eventos automáticos)](docs/triggers-and-conditions.md)**
7. **[Configuración Avanzada y YAML](docs/yaml-format.md)**
8. **[Traducciones (messages.yml)](docs/config-messages.md)**

## 🛡️ Soporte y Dependencias

- **Servidor Requerido:** Paper / Purpur / Folia (Basado en `paper-plugin.yml`).
- **Java:** Java 21+.
- **Dependencia Principal:** `PacketEvents` (API 2.13.0).

---
*Hecho con ❤️ para creadores de servidores de Minecraft.*

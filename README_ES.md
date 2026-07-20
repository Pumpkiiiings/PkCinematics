# PkCinematics

PkCinematics es un plugin de motor de cinemáticas ligero y altamente personalizable para servidores de Minecraft (Paper). Te permite crear movimientos de cámara fluidos y secuencias complejas (como títulos, mensajes en el chat, efectos de sonido, partículas y cambios de hora) directamente desde dentro del juego.

## Características

- **Interpolaciones Suaves:** Mueve la cámara de forma fluida usando interpolaciones Linear o Catmull-Rom.
- **Aceleración Dinámica (Easings):** Controla la velocidad de la cámara perfectamente con modos Linear, Ease In, Ease Out o Smooth.
- **Editor GUI Integrado:** Construye y ajusta cinemáticas sin tocar ningún archivo de configuración, usando una interfaz gráfica intuitiva.
- **Línea de Tiempo de Acciones:** Ejecuta eventos precisos en ticks específicos (ej. lanzar partículas, reproducir sonidos, mostrar títulos).
- **Sistema de Espera:** Define pausas en puntos específicos de la cámara de manera natural, sin romper la interpolación.
- **Triggers (Disparadores):** Inicia cinemáticas automáticamente basadas en eventos como entrar al servidor, llegar a ciertas coordenadas o entrar a regiones de WorldGuard.
- **Opción para Saltar:** Permite a los jugadores saltar la cinemática pulsando la tecla Shift.
- **100% Configurable:** Absolutamente todos los mensajes, textos de la GUI y diálogos son personalizables (sistema 0 hardcode).

## Dependencias

- **Versión del Servidor:** Paper 1.21.x (Compilado para 1.21.1)
- **PacketEvents:** Requerido para la manipulación de paquetes de la cámara. Asegúrate de tener `packetevents-spigot` (2.13.0+) instalado en tu servidor.
- **Triumph GUI:** Incluido de forma nativa.

## Comandos y Permisos

Todos los comandos requieren el permiso `pkcinematics.admin`. Los jugadores normales no necesitan ningún permiso para ver las cinemáticas.

| Comando | Descripción |
|---------|-------------|
| `/cinematic menu` | Abre la lista principal de cinemáticas. |
| `/cinematic create <id>` | Crea una nueva cinemática y abre el editor. |
| `/cinematic edit <id>` | Abre una cinemática existente para editarla. |
| `/cinematic point` | Comando rápido para añadir un punto de cámara en tu ubicación y rotación actual. |
| `/cinematic save` | Guarda la cinemática que estás editando y sale del editor. |
| `/cinematic play <id> [jugador]` | Reproduce una cinemática. Si no se especifica jugador, se reproduce para ti. |
| `/cinematic stop` | Detiene la cinemática que estás viendo actualmente. |
| `/cinematic reload` | Recarga todas las configuraciones (mensajes, GUIs, triggers). |
| `/cinematic debug` | Activa el modo debug para ver los eventos de fondo en el chat. |

## Inicio Rápido

1. Escribe `/cinematic menu` y haz clic en "Crear Nueva Cinemática".
2. Escribe un ID en el chat (ej. `intro`).
3. Vuela a tu ubicación inicial y haz clic en **"Añadir Punto Aquí"** (o escribe `/cinematic point`).
4. Vuela a la siguiente ubicación y repite el proceso.
5. En el menú de Keyframes, haz clic izquierdo en un punto para ajustar el tiempo (tick), la interpolación, la aceleración o añadir tiempo de espera.
6. Haz clic en **"Acciones"** para añadir títulos o sonidos en ticks específicos.
7. Haz clic en **"Guardar y Salir"**, y luego usa `/cinematic play intro` para verla en acción.

## Documentación

Para una explicación detallada sobre los triggers, condiciones, tipos de acciones y el formato YAML, ¡revisa la documentación en la carpeta `/docs`!

- [English Documentation](./docs/README.md)
- [Documentación en Español](./docs/README_ES.md)

---
*Creado por Pumpkiiiings*

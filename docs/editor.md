# El Editor In-Game (Acciones)

PkCinematics incluye una Interfaz Gráfica (GUI) integrada en el juego basada en menús de inventario. Esto te permite programar eventos (como reproducir sonidos o mostrar títulos) exactamente en el momento que desees.

## Cómo Abrir el Editor

Primero, asegúrate de estar editando una cinemática (`/cinematic edit <nombre>`).

Luego, usa este comando para abrir el menú en un segundo específico:

```
/cinematic actions <tick>
```
Ejemplo: `/cinematic actions 100` (Abre el editor para el tick 100, es decir, el segundo 5 de la cinemática).

## Usando el Menú

Al ejecutar el comando, se abrirá un inventario mostrando:
1. **En la parte superior:** Las acciones que ya están configuradas para ejecutarse exactamente en ese `tick`.
2. **En la parte inferior:** Un botón verde con forma de esmeralda que dice "Añadir Acción".

### Añadiendo una Acción
1. Haz clic en el **Botón Verde (+)**.
2. Se abrirá el **Catálogo de Acciones**. Aquí verás iconos representando todas las acciones disponibles (Mensajes, Títulos, Partículas, Sonidos, Comandos, etc.).
3. Haz clic en la que desees añadir.
4. El chat te pedirá que introduzcas la información necesaria. Por ejemplo, si elegiste "Message", te pedirá que escribas el texto del mensaje en el chat.
5. Una vez que introduzcas la información, la acción se guardará y volverás al menú.

### Eliminando una Acción
Si te equivocaste, simplemente vuelve al menú `/cinematic actions <tick>`, busca el ítem de la acción que quieres borrar en la parte superior y hazle **Clic Derecho**.

## ¿Qué Acciones hay disponibles?

Tenemos una enorme variedad de acciones que puedes configurar, desde reproducir sonidos hasta cambiar el clima temporalmente para darle dramatismo a tu cinemática. 

Consulta la **[Librería de Acciones](actions-library.md)** para ver la lista completa y ejemplos.

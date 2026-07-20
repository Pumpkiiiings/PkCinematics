# Advanced Configuration: YAML Format

If you are an advanced administrator who prefers to type in your favorite code editor (like Visual Studio Code or Notepad++) instead of using the in-game interface, you can directly open the files saved in `plugins/PkCinematics/cinematics/`.

> [!WARNING]
> Be very careful with indentation (spaces). Bad spacing in YAML will corrupt the file and the cinematic will not load.
> After editing a file, remember to use `/cinematic reload cinematics` on your server.

## Structure of `cinematics/my_scene.yml`

Below is an example file illustrating each section of the format.

```yaml
# Total duration of the cinematic in ticks. 
# Note: If you set a number lower than the last camera point, the plugin will fix it automatically.
duration: 300 

# If true, the player can skip the scene using 'Shift' (Sneak)
skipeable-cinematic: true

# Camera track section. Each key is the exact tick number.
camera:
  '0':
    world: world_the_end
    x: 100.5
    y: 70.0
    z: 0.5
    yaw: 0.0
    pitch: 0.0
    fov: 90.0
    interpolation: LINEAR
    easing: EASE_IN
  '300':
    world: world_the_end
    x: 0.5
    y: 70.0
    z: 0.5
    yaw: 0.0
    pitch: 20.0
    fov: 70.0
    interpolation: LINEAR
    easing: EASE_OUT

# Action track section. Each key is the tick they run at.
actions:
  '0': 
    # Makes it daytime at the start
    - type: time
      time: 6000 
    
    # Spawns particles on screen
    - type: particle
      particle: FIREWORKS_SPARK
      count: 50
      offsetX: 1.0
      offsetY: 1.0
      offsetZ: 1.0
      speed: 0.1
      
  '40': # 2 seconds later, a title appears.
    - type: title
      title: "&5&lTHE END"
      subtitle: "&dPrepare for the final battle"
      fadeIn: 20
```

That's it! Using the YAML format is ideal if you need to copy-paste exact coordinates or add dozens of particles at once.

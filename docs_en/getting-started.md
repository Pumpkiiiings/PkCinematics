# Getting Started: Your First Cinematic

Welcome to PkCinematics! Creating spectacular cinematics in Minecraft has never been easier. Everything can be done from within the game. Follow these steps to create your first masterpiece.

## Step 1: Create an empty cinematic

Join the game and execute the following command to create a new cinematic called "my_intro":

```
/cinematic create my_intro
```

You will immediately enter **Editor Mode**. When you are in this mode, all your actions are saved to the temporary cinematic.

## Step 2: Add Keyframes

A cinematic is based on "Keyframes". The camera will fly from one keyframe to the next.

1. Go to the position where you want the cinematic to start.
2. Look towards where you want the camera to face.
3. Type the command:
   ```
   /cinematic point
   ```
   You just added your first point (Point 0)! By default, it will be at Tick 0 (the start).

4. Move to the next position.
5. Type `/cinematic point` again.
   You have added Point 1! By default, the plugin will place it 60 ticks (3 seconds) after the previous point.

Add as many points as you want.

## Step 3: Adjust Times and Positions

Do you want the second point to take longer to reach? You can edit the points you already created.
For example, if you want the camera to reach Point 1 at Tick 100 (5 seconds) instead of 60:

```
/cinematic point edit my_intro 1 time 100
```

> **Note on Time:** In Minecraft, time is measured in "Ticks". 20 ticks equal 1 second. Therefore, 100 ticks = 5 seconds.

## Step 4: Test the cinematic

While in Editor Mode, you can test how your cinematic is looking at any time without having to save it:

```
/cinematic play my_intro
```

Enjoy the ride. If you don't like something, simply use `/cinematic point edit` or edit the YAML later.

## Step 5: Save changes

It is vital to save your progress! If you close the server or log out without saving, you will lose everything.

```
/cinematic save
```

Congratulations! You have created and saved your first cinematic.

## Next Steps

Now that you know the basics, we recommend reading:
* **[The Action Editor](editor.md)** to add messages and particles at exact moments.
* **[Camera Movement Guide](interpolations.md)** to make the camera perform curved turns and fluid accelerations.

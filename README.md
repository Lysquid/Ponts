# Ponts

A java bridge building game

![screenshot](screenshot.png)

This project was made by three students at INSA Lyon for second year computer science class.

In the game, you build your bridge and launch the simulation to see if it will be solid enough for the car to cross it. You can build from 3 different materials (wood, steel and tarmac) with different proprieties, but beware not to exceed the budget. The game includes 10 levels, and a level editor to create even weirder terrains.

## Credits

The game uses the [JBox2D](https://github.com/jbox2d/jbox2d) library for the physics, an open source port of the C++ library [Box2D](https://box2d.org/).
For the graphics, it relies on Swing with a modern theme provided by the [FlatLaf](https://www.formdev.com/flatlaf/themes/).

The game is heavily inspired by [Poly Birdge](http://polybridge.drycactus.com/), a very cool game developed by Dry Cactus.

## Running the game

Requirement : Java 11 or above

Download the file named `Ponts.jar` in the Releases section, and run it in the terminal by typing :

```bash
java -jar Ponts.jar
```

### Linux only

Give execute permissions to `complile.sh` and `execute.sh` scripts, and run them in this order :

```bash
chmod +x complile.sh execute.sh
./complile.sh
./execute.sh
```

### Alternative

You can run the project by openening it inside VS Code with the Extension Pack for Java installed.

## Demo

[screencast](https://user-images.githubusercontent.com/32977249/201428102-d889df1f-99a6-46f5-9da4-680b92a400e5.webm)

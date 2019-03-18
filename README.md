# Blubber Blasen
The awesome blubber blasen game. Yes, if you are born before 1985 it may look familiar to you.

## Requirements
* Java JDK11
* Gradle 5.0
* libGDX 1.9.9

## Usage
Clone, build and run the application:
```
git clone https://github.com/skerkewitz/blubber-blasen.git
cd blubber-blasen
gradlew run
```

## How to play

Use cursor keys LEFT/RIGHT to move, UP to jump (hold to jump higher) and SPACE to fire a bubble. Trap monster in a bubble, then burst the bubble by touching it. If a monster touches you while not in a bubble you die. And since you have only live: if you die, YOU DIE!

## Arguments

Argument | Describtion 
------------ | -------------
-fullscreen | Run the game in fullscreen. Use ESC on the title screen to exit.
-nopostfx | Disable the PostFX CRT shader.

You can pass argument using gradle like this:

```
gradlew run --args='-fullscreen'
```

## Level Editor
If you would like to create your own maps/level/rooms you can use the [GierzahnEditor](https://github.com/skerkewitz/GierzahnEditor)


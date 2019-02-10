# What is ECS

The abbreviation ECS stands for legacyEntity–component–system. It is an architectural pattern that is mostly used in game development. ECS follows the composition over inheritance principle that allows greater flexibility in defining entities where every object in a game's scene is an legacyEntity (e.g. enemies, bullets, vehicles, etc.). Every legacyEntity consists of one or more components which add behavior or functionality. Therefore, the behavior of an legacyEntity can be changed at runtime by adding or removing components. This eliminates the ambiguity problems of deep and wide inheritance hierarchies that are difficult to understand, maintain and extend. Common ECS approaches are highly compatible and often combined with data-oriented design techniques.

# How does it work

Every "thing" in the game is an legacyEntity. And legacyEntity is nothing more then a ID to reference it.

Next you have components. Components are nothing more then data classes to hold the state for a certain functionality. You can assign any legacyEntity any number of component.   

Lastly you have Systems. A system checks if an legacyEntity has certain components and perform some action on those component. If an legacyEntity does not provide the needed components it is simply ignored by the system.

# Basic rules

* Component have no functions
* Systems have no state
* Shared code lives in Utils
* Complex side effects should be deferred
* Systems can't call other systems     
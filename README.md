# Raycer

This is a personal project by William Martin III and is hosted on GitHub for collaboration purposes only.
It is meant to be an excercise in graphics and software development and should not be considered a good rendering
engine or good code for that matter. The project is in a very rough state at the moment and should not be used
except as an example of spaghetti code.


There is no license currently attached to this project which means all rights allowed under GitHub's Terms and
Conditions are reserved. A license is expected to be chosen in the future.

## Resources
Example stone texture thanks to [RenderTextures](http://www.rendertextures.com/seamless-rock-texture-6/) (note that this
texture has been modified from the original).

Example desert skybox thanks to [komaokc](http://gamebanana.com/members/289553) (creator) (found at [GameBannana]
(http://css.gamebanana.com/textures/2157)) released under the Creative Commons 
[Attribution-NonCommercial-NoDerivatives 4.0 International (CC BY-NC-ND 4.0)]
(http://creativecommons.org/licenses/by-nc-nd/4.0/) license.

## Known TODO
### General
* Add JavaDocs to all exposed methods (and others which require clarification for maintainability)
* Add unit tests
    * There aren't any
* Make a usage guide
* Add code style enforcers

### Engine
* Optimize ray-tracing process with respect to Java
* Add more primitive geometry
* Add the concept of a solid
    * Allow for refraction calculations
    * Allow for ray-collisions within solids
    * Allow for 'fog' effects within solids
    * Sphere solids
    * Shapes enclosed by Triangle primitives
* Fix axis inversions
    * Some axis may be inverted in some calculations
* Allow for distributed ray-tracing

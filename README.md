# Voxel Tweaks

Duncan's personal tweaks for VoxelMap.
This is not a fork of VoxelMap, it is more of an addon mod that will change some stuff about it or add more
functionality.

Features/Changes:

- Swap Y and Z position in the add/edit waypoint GUI to follow regular MC order
- Add a paste button to the add/edit waypoint GUI
    - This will take the first 3 numbers, floor them, and place them in boxes, or only place X and Z if 2 numbers are
      found.
- Add a /vwaypoint command
    - ex: `/vwaypoint Home 150 65 150`
- Add a /vhighlight command
    - ex: `/vhighlight 150 65 150`
- The commands take the same kind of input as the new paste button, since it will take any 3 numbers it finds, it is
  compatible with what the F3+C shortcut puts in the clipboard.

## Developing/Building

- Clone the repository
- Create a directory in the root directory called `modlibs`
- Place a voxelmap fabric jar into the `modlibs` directory
- `./gradlew build` to build
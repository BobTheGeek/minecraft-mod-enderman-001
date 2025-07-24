# Ender Abilities Mod

A Minecraft Forge mod that grants players enderman-like abilities.

## Features

1. **Teleportation** (R key) - Teleport in your line of sight with a 3-second cooldown
2. **Invisibility** (I key) - Become invisible for 30 seconds with a 10-second cooldown  
3. **Summon Enderman** (E key) - Summon a friendly enderman with a 15-second cooldown
4. **Ender Sword** - A powerful sword that:
   - Has higher damage than diamond swords
   - 25% chance to teleport behind enemies when attacking
   - Right-click while crouching for short-range teleport
   - Can be repaired with ender pearls

## Crafting Recipe

**Ender Sword:**
```
 E 
 E 
 S 
```
Where E = Ender Pearl, S = Stick

## Installation

1. Install Minecraft Forge 1.19.2
2. Download the mod jar file
3. Place it in your `mods` folder
4. Launch Minecraft

## Development

This mod is built for Minecraft 1.19.2 with Forge 43.3.0.

### Building from Source

1. Clone this repository
2. Run `./gradlew build`
3. The mod jar will be in `build/libs/`

## Controls

- **R** - Teleport (3s cooldown)
- **I** - Invisibility (10s cooldown)  
- **E** - Summon Enderman (15s cooldown)

## Technical Details

- **Teleportation**: Raycasts up to 32 blocks to find valid teleport locations
- **Invisibility**: Applies vanilla invisibility effect for 30 seconds
- **Enderman Summoning**: Spawns friendly endermen that won't attack the player
- **Ender Sword**: Custom tier with diamond-level stats but higher enchantability
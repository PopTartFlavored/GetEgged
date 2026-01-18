GetEgged allows players to capture mobs into spawn eggs while preserving important data such as names, age, tamed status, variants, colors, and horse stats.  

**Vault economy support** with configurable prices per entity category (animals, monsters, villagers, golems, bosses, default) with creative mode and permission-based bypass.  

**WorldGuard support** with custom flags `getegged-egg` and `getegged-spawn`. **Allowed by default!** You must set to deny for each region. Bypass permission `getegged.worldguard.bypass`

**GriefPrevention support** with bypass permission `getegged.griefprevention.bypass` 

Designed to be lightweight, permission-based, and survival-friendly, with a focus on server control and balance.

## Usage:

Right-click a mob with the configurable egging tool to store it in a spawn egg.  
Obtain the capture item using `/getegged get`  
Configurable egging tool usage `NONE, DURABILITY, or CONSUME` with creative mode and permission-based bypass.  
Price to obtain the egging tool is configurable in the config file.

Please let me know if you find any bugs or behavior that doesn’t seem correct.
You can report issues or suggestions on GitHub:  
<https://github.com/PopTartFlavored/GetEgged/issues>

## ⚠️ Known limitations:

- Entities with items in their inventories cannot be egged. (Excluding saddles and horse armor.)

## 🧩 Permissions:

- \<MOB> names need to be all **uppercase**  
- example: getegged.animal.**SHEEP** or getegged.monster.**ZOMBIE**

`getegged.animal.<MOB>` -Allows egging of specific animal.
  
`getegged.animal.*` - Allows egging of all animals

`getegged.monster.<MOB>` - Allows egging of specific monster
  
`getegged.monster.*` - Allows egging of all monsters

`getegged.golem.<MOB>` - Allows egging of specific golem
  
`getegged.golem.*` - Allows egging of all golems

`getegged.villager.<MOB>` - Allows egging of specific villager type (VILLAGER and WANDERING_TRADER)

`getegged.villager.*` - Allows egging of all villager types

`getegged.boss.<MOB>` - Allows egging of specific boss mob (WITHER and ENDERDRAGON)

`getegged.boss.*` - Allows egging of all boss mobs

`getegged.deny.<MOB>` - Denies egging of speccific mob even if they have another permission that allows it

`getegged.rename` - Allows renaming of mob at spawn if spawn egg has been renamed. Toggleable in config

`getegged.tamed` - Allows egging of tamed animals owned by other players

`getegged.economybypass` - Allows bypass of economy

`getegged.usagebypass` - Allows bypass of egging tool usage/consumption

`getegged.get` - Allows use of `/getegged get`
  
`getegged.give` - Allows use of `/getegged give <player>`
  
`getegged.reload` - Allows use of `/getegged reload`

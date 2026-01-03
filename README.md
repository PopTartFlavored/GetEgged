# GetEgged

## ⚠️ This plugin is still under active development. Behavior and features may change between versions.

GetEgged allows players to capture mobs into spawn eggs while preserving important data such as names, age, tamed status, variants, colors, and horse stats.

Designed to be lightweight, permission-based, and survival-friendly, with a focus on server control and balance.

## Usage:

Right-click a mob with the configurable capture item to store it in a spawn egg.
Obtain the capture item using `/getegged get`

Please let me know if you find any bugs or behavior that doesn’t seem correct.
You can report issues or suggestions on GitHub:
https://github.com/PopTartFlavored/GetEgged/issues

## ⚠️ Known limitations:


Villager profession and trades are not yet restored.


## 📌 Planned features:

- Economy Support

- Capture Item durability/consumption

- Improved villager data restoration

## 🧩 Permissions:

- \<MOB> names need to be all **uppercase**  
- example: getegged.animal.**SHEEP** or getegged.monster.**ZOMBIE**

`getegged.animal.<MOB>` -Allows egging of specific animal.
  
`getegged.animal.*` - Allows egging of all animals

`getegged.monster.<MOB>` - Allows egging of specific monster
  
`getegged.monster.*` - Allows egging of all monsters

`getegged.golem.<MOB>` - Allows egging of specific golem
  
`getegged.golem.*` - Allows egging of all golems

`getegged.villager` - Allows egging of villagers

`getegged.tamed` - Allows egging of tamed animals owned by other players

`getegged.get` - Allows use of `/getegged get`
  
`getegged.give` - Allows use of `/getegged give <player>`
  
`getegged.reload` - Allows use of `/getegged reload`

# KeyBinding Hider

[![AFDIAN](https://img.shields.io/badge/%E7%88%B1%E5%8F%91%E7%94%B5-Gizmo-%23946ce6)](https://afdian.com/a/gizmo)
[![CurseForge Downloads](https://img.shields.io/curseforge/dt/1129419?logo=curseforge&label=CurseForge)](https://www.curseforge.com/minecraft/mc-mods/keybinding-hider)
![MC-1.21.11](https://img.shields.io/badge/MC-1.21.11-blue)
![GitHub License](https://img.shields.io/github/license/gizmo-ds/key-binding-hider-mod?label=License)

A simple mod for hiding specific key bindings in the GUI.

> [!WARNING]  
> This mod was developed by me for my modpack, I cannot guarantee that it will work properly in other situations.

## How to use

Most users can configure hidden key bindings through the in-game configuration screens.  
However, advanced users may also edit the configuration file manually to achieve more complex behavior.

The following example shows the contents of `config/key_binding_hider.json`.

```json5
{
  "HiddenKeyBindingsUseUnknown": true, // Set hidden key bindings to Unknown to prevent key binding conflicts.
  "HiddenKeyPatterns": [ // Key bindings with IDs matching any of these patterns will be hidden.
    "key.hotbar.9" // Hide the hotbar 9 key binding
  ],
  "HiddenCategoryPatterns": [ // Key bindings whose category matches any of these patterns will be hidden.
    "minecraft:multiplayer", // Hide the multiplayer category
    "#jei:.*" // Hide all categories matching this regular expression
  ]
}
```

> By default, entries are treated as plain text.  
> If an entry starts with `#`, the rest of the value is treated as a regular expression.
>
> For example, `#key\.jei\..*` hides all JEI key bindings, and `#jei:.*` hides all JEI-related categories.

## License

This mod is licensed under the MIT License. You can find the full license text in the [LICENSE](LICENSE) file.

# KeyBinding Hider

[![AFDIAN](https://img.shields.io/badge/%E7%88%B1%E5%8F%91%E7%94%B5-Gizmo-%23946ce6)](https://afdian.com/a/gizmo)
[![CurseForge Downloads](https://img.shields.io/curseforge/dt/1129419?logo=curseforge&label=CurseForge)](https://www.curseforge.com/minecraft/mc-mods/keybinding-hider)
![MC-1.21](https://img.shields.io/badge/MC-1.21-blue)
![MC-1.21.1](https://img.shields.io/badge/MC-1.21.1-blue)
![GitHub License](https://img.shields.io/github/license/gizmo-ds/key-binding-hider-mod?label=License)

A simple mod for hiding specific key bindings in the GUI.

> [!WARNING]  
> This mod was developed by me for my modpack, I cannot guarantee that it will work properly in other situations.

## How to use

Edit the `config/key_binding_hider.toml` file to hide the key bindings you want. The mod will hide all KeyBindings
starting with a value in `KeyBindings`

```toml
SetKeyBindingToUnknown = true
KeyBindings = ["key.jei"]
```

For example, hide and disable all JEI KeyBindings.

## License

This mod is licensed under the MIT License. You can find the full license text in the [LICENSE](LICENSE) file.

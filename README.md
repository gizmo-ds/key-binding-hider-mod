# KeyBinding Hider

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

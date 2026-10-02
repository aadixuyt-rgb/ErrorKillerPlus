# Error Killer Plus — Fabric 1.21.4

Clean patch layer for the supplied `ErrorKiller-1_21_4.jar`.

## Changes
- Adds a Minecraft Controls keybind named **Toggle Error Killer**.
- The keybind toggles the existing `error-killer` config on/off and saves it.
- Allows a cobweb in the **off-hand** to trigger the cobweb mode.
- Routes the macro's block/item interaction through `OFF_HAND` while that off-hand cobweb mode is active.
- Keeps the original mod's existing logic rather than adding a new macro implementation.

## Security review of the supplied JAR
The supplied JAR was inspected as a ZIP and its bytecode was scanned for common RAT indicators. I did not find socket/network client code, `Runtime.exec`/`ProcessBuilder`, dynamic class loading, reflection-based payload loading, Base64/Cipher payload handling, or embedded download URLs. It does contain normal local config file I/O and a URL-parsing helper used for clickable text.

This is a static review, not a guarantee that any Java program is mathematically impossible to make malicious.

## Build
1. Put the supplied original file here:
   `libs/ErrorKiller-1_21_4-original.jar`
2. Use JDK 21.
3. Run `./gradlew build`.
4. The patched add-on JAR will be in `build/libs/`.

The build uses Fabric Loom and Yarn for Minecraft 1.21.4. Fabric's 1.21.4 docs require JDK 21, and Fabric's keybinding API provides `KeyBindingHelper.registerKeyBinding`. 

## GitHub
The repository can be built by GitHub Actions as well. The project intentionally does not contain a copied third-party dependency: the supplied original JAR stays in `libs/` locally.

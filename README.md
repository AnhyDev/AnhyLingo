![](https://app.anh.ink/images/ItemLingoLogo.png)

**ItemLingo** is a plugin for Minecraft servers designed to create a multilingual interface. It allows players to receive system messages, messages from other plugins, and to see item names and descriptions in their chosen language. Tested on servers with **`Spigot`**, **`Paper`**, **`Purpur`** cores version **`1.20.2`**. Requires the latest version of the `**ProtocolLib**` plugin.

### Main Features

1.  #### Multilingualism:

    *   Displaying text in the game chat and on the action bar in different languages.
    *   Automatically uses Minecraft client's language settings if the player hasn't set their own language.
2.  #### Language Personalization (Commands for Players):

    *   **`/lingo set [en|es|ua|ru]`** - setting a preferred language.
    *   **`/lingo reset`** - resetting the set language.
    *   **`/lingo get`** - viewing the chosen language.
    *   If a player has not chosen or has reset their language settings, the plugin uses the language settings of the Minecraft client.
3.  #### Automatic Item Renaming:

    *   Renaming the name and lore of custom items to the player's language during player interaction with the item in the inventory.
    *   Automatically sets an NBT tag with the language during renaming, to prevent unnecessary renamings.
    *   The item is not modified in cases where the item's language matches the player's language, or when more than one player is looking at the inventory simultaneously.

> For the **ItemLingo** plugin to track and rename certain items, it is necessary to add a special NBT tag **`ItemLingo`** to them. This tag should contain a string value that corresponds to the key in the language file.
> 
> ##### Adding an NBT Tag to an Item
> 
> *   *   Command for Adding a Tag, example:
>         
>         
>         *   **`/lingo nbt set ItemLingo string:magic_wand`**
>             *   This command adds the NBT tag ItemLingo with the string value `magic_wand` to the item in the player's hands.
>             *   The value `magic_wand` corresponds to the key in the language file.
>     *   Using Other Plugins:
>         
>         
>         *   Administrators can also use the functionality of other plugins to add this NBT tag to items.
>         *   It is important that the NBT tag is named **`ItemLingo`** and has a string value that corresponds to the key from the language file.
> 
> This feature ensures the ability to rename certain items on the server according to the player's chosen language, making the gameplay more interactive and convenient for players using different languages.

#### Configuration (config.yml)

*   **`language`**: Default language for system messages.
*   **`allowed_directories`**: Allowed directories for interaction by commands.

#### Directory Structure

*   **`items`**: Files with translations of names and lore of custom items.
*   **`system`**: Files with translations of system messages and messages from plugins.
*   Files must have a naming format: "`xxx_[language code].yml`", e.g., "`lingo_en.yml`", files that do not meet the standard are not read.

#### Administrator Commands (with itemlingo.manager permission)

1.  File Download Commands:

    *   `**/lingo fl link_to_file directory true|false**`:
        *   Downloading language files to the specified directories in the **ItemLingo** plugin folder.
        *   `link_to_file`: Direct link to the file.
        *   `directory`: Directory where the file is downloaded `items|system`.
        *   `true|false`: Parameter for determining the ability to overwrite the file.
        *   File requirements: valid yml format, file name corresponds to the mask `xxx_[language code].yml`.
    *   `**/lingo fo link_to_file directory true|false**`:
        *   Downloading any files to the `directory` within the server's plugin folders.
        *   <span style="border: 0px solid #d9d9e3; box-sizing: border-box; --tw-border-spacing-x: 0; --tw-border-spacing-y: 0; --tw-translate-x: 0; --tw-translate-y: 0; --tw-rotate: 0; --tw-skew-x: 0; --tw-skew-y: 0; --tw-scale-x: 1; --tw-scale-y: 1; --tw-scroll-snap-strictness: proximity; --tw-ring-offset-width: 0px; --tw-ring-offset-color: #fff; --tw-ring-color: rgba(69,89,164,.5); --tw-ring-offset-shadow: 0 0 transparent; --tw-ring-shadow: 0 0 transparent; --tw-shadow: 0 0 transparent; --tw-shadow-colored: 0 0 transparent; font-weight: 600; color: var(--tw-prose-bold);">Important:</span> Downloading is only possible in directories defined as allowed in the plugin's configuration file (`allowed_directories`).
        *   If a subdirectory within an allowed directory does not exist, it will be automatically created during command execution.
        *   `link_to_file`: Direct link to the file for download.
        *   `directory`: The target directory for the file within the server's plugin directories.
        *   `true|false`: Parameter determining whether it is allowed to overwrite existing files.
2.  **`/lingo dir [directory]`**: Shows the contents of the `directory` folder in the server's plugin directory.

3.  **`/lingo reload`**: Reloading the plugin's configuration.

4.  **`/lingo list <lang>`**: Displays a list of "keys" of all items for which a translation exists in the specified language.

5.  **`/lingo item <lang> <key>`**: Shows the name and lore in the specified language for the specified key.

6.  **`/lingo nbt set <nbt_key> <params...>`**:

    *   Setting the NBT tag nbt_key with params, specifying the data type (e.g., `string:value, int:10, int:10`).
    *   Examples of value types: `int, double, intarray, string`.
7.  **`/lingo list`**: List of NBT tags of the item in the player's hands.

8.  **`/lingo nbt info <nbt_key>`**: Shows the value of a specific NBT tag of the item in the player's hands.

<div class="content-container-y" style="font-size: 16px; box-sizing: border-box; margin: auto; padding: 30px; border-radius: 8px; max-width: 95%; min-width: 80%; font-family: Arial, sans-serif;">

# AnhyLingo

<span style="box-sizing: border-box; text-decoration-line: underline;"><span style="box-sizing: border-box; font-weight: bolder;">AnhyLingo</span></span> is a plugin for Minecraft servers designed to create a multilingual interface. It allows players to receive system messages, messages from other plugins, as well as see names and descriptions of items in their chosen language. Tested on servers with <span style="box-sizing: border-box; font-weight: bolder;">`Spigot`</span>, <span style="box-sizing: border-box; font-weight: bolder;">`Paper`</span>, <span style="box-sizing: border-box; font-weight: bolder;">`Purpur`</span> cores version <span style="box-sizing: border-box; font-weight: bolder;">`1.20.2`</span>. The latest version of the `<span style="box-sizing: border-box; font-weight: bolder;">ProtocolLib</span>` plugin is required for operation.

</div>

<div class="content-container-x" style="font-size: 16px; box-sizing: border-box; margin: auto; padding: 30px; border-radius: 8px; box-shadow: rgba(0, 0, 0, 0.6) 0px 4px 10px; background-color: rgba(28, 28, 28, 0.7); max-width: 95%; min-width: 80%; font-family: Arial, sans-serif;">

## Functionality of the plugin

1.  #### Multilingualism:

    ##### (all players):

    *   Ability to select desired languages using the command.
    *   Automatically use Minecraft client language settings if the player has not set their preferred languages.
    *   Display of text in the game chat and on the action bar in different languages.
    *   Automatic translation of custom items into the selected language during interaction.
2.  #### Working with game items:

    ##### (players with admin permissions):

    *   Ability to create multilingual items.

1.  #### Working with NBT tags:

    ##### (players with admin permissions):

    *   View available NBT item tags.
    *   Installing, changing and deleting NBT tags in items.

1.  #### Work with files:

    ##### (players with admin permissions):

    *   Uploading language files to the plugin folder.
    *   Uploading any files to allowed folderss.
    *   Deleting files from allowed folderss.

</div>

<div class="container content-container-y" style="font-size: 16px; box-sizing: border-box; width: 1256.41px; padding: 30px; margin: auto; max-width: 95%; border-radius: 8px; min-width: 80%; font-family: Arial, sans-serif;">

### Language Personalization with Commands for All Players:

> Players have the option to select languages for system messages, plugin notifications, and the display of game items that support multiple languages. If the language chosen by the player has been added by the administrators, all relevant informational messages will be displayed in that language. In cases where the player has not selected any specific language, the system will use the language settings of the Minecraft client. If the chosen language is not supported in the localization, the player will receive messages in the default language set in the plugin.

1.  #### Set the desired languages

*   <span class="perm" style="box-sizing: border-box; color: #5c5854;">(no permission is required)</span>
*   <span style="box-sizing: border-box; font-weight: bolder;">`/lingo set <langs>`</span>
*   <langs> - can be one or more languages, for example `en` or `en it es`. Plugin will search for the translation in order.

1.  #### Resetting the set language

*   <span class="perm" style="box-sizing: border-box; color: #5c5854;">(no permission is required)</span>
*   <span style="box-sizing: border-box; font-weight: bolder;">`/lingo reset`</span>
*   Clears the list of selected languages, the plugin will receive the language from the minecraft client.

1.  #### Get languages

*   <span class="perm" style="box-sizing: border-box; color: #5c5854;">(no permission is required)</span>
*   <span style="box-sizing: border-box; font-weight: bolder;">`/lingo get`</span>
*   View selected languages for translation.

</div>

<div class="container content-container-x" style="font-size: 16px; box-sizing: border-box; width: 1256.41px; padding: 30px; margin: auto; max-width: 95%; border-radius: 8px; box-shadow: rgba(0, 0, 0, 0.6) 0px 4px 10px; background-color: rgba(28, 28, 28, 0.7); min-width: 80%; font-family: Arial, sans-serif;">

## Administrator Commands

##### (with permission anhylingo.*)

> <span style="box-sizing: border-box; text-decoration-line: underline;"><span style="box-sizing: border-box; font-weight: bolder;">The AnhyLingo plugin for Minecraft servers</span></span> is equipped with administrative commands to manage multilingualism on the server. These commands are only accessible to players with extended administrative rights and allow for customization and control over the plugin's language options. The administrators can use these tools to set the available languages on the server, modify translations of game elements and the interface, and adjust language settings as per the players' needs. This approach promotes inclusivity and accessibility of the game for players from different countries and ensures a more flexible approach to cultural and linguistic diversity on Minecraft

</div>

<div class="container content-container-y" style="font-size: 16px; box-sizing: border-box; width: 1256.41px; padding: 30px; margin: auto; max-width: 95%; border-radius: 8px; min-width: 80%; font-family: Arial, sans-serif;">

### Reloading and information commands:

1.  #### Reloading the plugin

*   <span class="perm" style="box-sizing: border-box; color: #5c5854;">(with permission anhylingo.reload)</span>
*   Reloads the general configuration and language files configurations
*   <span style="box-sizing: border-box; font-weight: bolder;">`/lingo reload`</span>

1.  #### Information about available item localizations

*   <span class="perm" style="box-sizing: border-box; color: #5c5854;">(with permission anhylingo.items.info)</span>
*   Displays a list of "keys" for all items that have a translation in the specified language.

<span style="box-sizing: border-box; font-weight: bolder;">`/lingo items list <lang>`</span>

1.  #### Show localization for key

*   <span class="perm" style="box-sizing: border-box; color: #5c5854;">(with permission anhylingo.items.info)</span>
*   Shows the name and lore in the specified language for the given key.
*   <span style="box-sizing: border-box; font-weight: bolder;">`/lingo item <lang> <key>`</span>

</div>

<div id="nbt" class="container content-container-x" style="font-size: 16px; box-sizing: border-box; width: 1256.41px; padding: 30px; margin: auto; max-width: 95%; border-radius: 8px; box-shadow: rgba(0, 0, 0, 0.6) 0px 4px 10px; background-color: rgba(28, 28, 28, 0.7); min-width: 80%; font-family: Arial, sans-serif;">

### Working with NBT tags:

##### (with permission anhylingo.nbt.*)

1.  #### Adding an NBT tag or changing its value

*   <span class="perm" style="box-sizing: border-box; color: #5c5854;">(with permission , "anhylingo.nbt.set")</span>
*   <span style="box-sizing: border-box; font-weight: bolder;">`/lingo nbt set <nbt_key> <params...>`</span>
*   Setting the NBT tag nbt_key with parameters params, specifying the data type (e.g., `string:value, int:10`).
*   Examples of value types: `int, double, intarray, string`.

1.  #### View NBT tags

*   <span class="perm" style="box-sizing: border-box; color: #5c5854;">(with permission , "anhylingo.nbt.list")</span>
*   <span style="box-sizing: border-box; font-weight: bolder;">`/lingo nbt list`</span>
*   Displays a list of NBT tags for the item currently in the player's hand
*   Examples of value types: `int, double, intarray, string`.

1.  #### Viewing the value of NBT tags

*   <span class="perm" style="box-sizing: border-box; color: #5c5854;">(with permission , "anhylingo.nbt.info")</span>
*   <span style="box-sizing: border-box; font-weight: bolder;">`/lingo nbt info <nbt_key>`</span>
*   Shows the value of a specific NBT tag for the item in the player's hand.

</div>

<div id="files" class="container content-container-y" style="font-size: 16px; box-sizing: border-box; width: 1256.41px; padding: 30px; margin: auto; max-width: 95%; border-radius: 8px; min-width: 80%; font-family: Arial, sans-serif;">

### Working with files

##### (with permission anhylingo.file.*)

1.  #### File Uploading

    *   #### File YAML AnhyLingo uploading

    *   <span class="perm" style="box-sizing: border-box; color: #5c5854;">(with permission anhylingo.file.lingo)</span>

    `<span style="box-sizing: border-box; font-weight: bolder;">/lingo fl (flingo) link_to_file folder true|false</span>`

    *   Uploading language files to subfolders of the <span style="box-sizing: border-box; font-weight: bolder;">AnhyLingo</span> plugin directory.
    *   `link_to_file`: Direct link to the file.
    *   `folder`: Folder where the file is uploaded `items|system`.
    *   `true|false`: Parameter to determine the possibility of overwriting the file.
    *   File requirements: valid yml format, file name corresponds to the pattern `xxx_[language_code].yml`.

    *   #### File uploading

    *   <span class="perm" style="box-sizing: border-box; color: #5c5854;">(with permission anhylingo.file.other)</span>

    `<span style="box-sizing: border-box; font-weight: bolder;">/lingo fo (fother) link_to_file directory true|false</span>`

    *   Uploading any files into the `directory` within the server plugins folder.
    *   <span style="box-sizing: border-box; font-weight: bolder;">Important:</span> Uploading is only possible in the directories defined as allowed in the plugin configuration file (`allowed_directories`).
    *   If a subdirectory within the allowed directory does not exist, it will be automatically created during the execution of the command.
    *   `link_to_file`: Direct link to the file for download.
    *   `directory`: Target directory for the file within the server plugins directories.
    *   `true|false`: Parameter that determines whether overwriting existing files is allowed.

1.  #### Deleting files

    *   #### Deleting a file

    *   <span class="perm" style="box-sizing: border-box; color: #5c5854;">(with permission anhylingo.file.other)</span>

    `<span style="box-sizing: border-box; font-weight: bolder;">/lingo fd (fdel) directory file_name</span>`

    *   <span style="box-sizing: border-box; font-weight: bolder;">Important:</span> Deleting is only possible in the directories defined as allowed in the plugin configuration file (`allowed_del_directories`).
    *   `directory`: File path.
    *   `file_name`: File name

1.  #### File view

    *   #### View folder contents

    *   <span class="perm" style="box-sizing: border-box; color: #5c5854;">(with permission anhylingo.file.view)</span>

    `<span style="box-sizing: border-box; font-weight: bolder;">/lingo dir [directory]</span>`

    *   Displays the contents of the `directory` folder located in the server plugins directory.
    *   If `[directory]` is set to 0, it will show the contents of the `./plugins/` folder.

</div>

<div class="container content-container-x" style="font-size: 16px; box-sizing: border-box; width: 1256.41px; padding: 30px; margin: auto; max-width: 95%; border-radius: 8px; box-shadow: rgba(0, 0, 0, 0.6) 0px 4px 10px; background-color: rgba(28, 28, 28, 0.7); min-width: 80%; font-family: Arial, sans-serif;">

### Automatic Item Renaming:

1.  *   Renaming the name and lore of custom items to the player's language during the player's interaction with the item in the inventory.
    *   Automatic setting of the NBT tag with the language during renaming to prevent unnecessary renamings.
    *   The item is not modified in cases where the language of the item matches the language of the player, or when more than one player is looking at the inventory simultaneously.

> For the <span style="box-sizing: border-box; font-weight: bolder;">AnhyLingo</span> plugin to track and rename certain items, it is necessary to add a special NBT tag <span style="box-sizing: border-box; font-weight: bolder;">`AnhyLingo`</span>. This tag should contain a string value that corresponds to the key in the language file.
> 
> #### Adding an NBT Tag to an Item
> 
> *   *   Command for Adding Tag, example:
>         
>         
>         *   <span style="box-sizing: border-box; font-weight: bolder;">`/lingo nbt set AnhyLingo string:magic_wand`</span>
>             *   This command adds the NBT tag AnhyLingo with the string value `magic_wand` to the item in the player's hand.
>             *   The value `magic_wand` corresponds to the key that must be in the language file.
>     *   Using Other Plugins:
>         
>         
>         *   Administrators can also use the functionality of other plugins to add this NBT tag to items.
>         *   It is important that the NBT tag has the name <span style="box-sizing: border-box; font-weight: bolder;">`AnhyLingo`</span> and a string value that corresponds to the key from the language file.
> 
> This feature provides the ability to rename certain items on the server according to the player's chosen language, making the gameplay more interactive and convenient for players who use different languages.

</div>

<div id="configuration" class="container content-container-y" style="font-size: 16px; box-sizing: border-box; width: 1256.41px; padding: 30px; margin: auto; max-width: 95%; border-radius: 8px; min-width: 80%; font-family: Arial, sans-serif;">

### Configuration (config.yml)

*   <span style="box-sizing: border-box; font-weight: bolder;">`language`</span>: The default language for system messages.
*   <span style="box-sizing: border-box; font-weight: bolder;">`allowed_directories`</span>: Allowed directories for command interaction.

### Directory Structure

*   <span style="box-sizing: border-box; font-weight: bolder;">`items`</span>: Files with translations of names and lore of custom items.

<div class="code-container" style="box-sizing: border-box;">

    # Main item
    magic_wand:
      name: "&bMagic Wand"
      lore:
        - "&7This is a magic wand."
        - "&7It can perform magical feats."
        - "&7Use it wisely."

    # A copy of the main item with the first line of lore changed
    magic_wand_1:
      copy: magic_wand
      lines:
        1: "&cEnhanced Magic Wand"

    # Another copy with changes in the first and third lines of the lore
    magic_wand_2:
      copy: magic_wand
      lines:
        1: "&dAdvanced Magic Wand"
        3: "&dIt holds immense power."

    # Other items
    eternal_sword:
      name: "&cEternal Sword"
      lore:
        - "&7A legendary sword that never dulls."
        - "&7Said to be forged from a fallen star."
        - "&7Its blade cuts through darkness."

    phantom_cloak:
      name: "&5Phantom Cloak"
      lore:
        - "&7A cloak woven from the threads of night."
        - "&7Grants the wearer invisibility in shadows."
        - "&7Whispers secrets of the ancient world."

    skywalker_boots:
      name: "&9Skywalker Boots"
      lore:
        - "&7Boots crafted from the essence of clouds."
        - "&7Allows the wearer to walk on air."
        - "&7Feels lighter than a feather."

    sunshield_helmet:
      name: "&eSunshield Helmet"
      lore:
        - "&7A helmet that shines like the sun."
        - "&7Protects the wearer from all harm."
        - "&7Radiates a warm, comforting glow."

    galeforce_bow:
      name: "&aGaleforce Bow"
      lore:
        - "&7A bow imbued with the spirit of the wind."
        - "&7Arrows fly like swift gusts."
        - "&7Said to never miss its target."

</div>

*   <span style="box-sizing: border-box; font-weight: bolder;">`system`</span>: Files with translations of system messages and messages from plugins.

<div class="code-container" style="box-sizing: border-box;">

    #
    # ██╗████████╗███████╗███╗░░░███╗██╗░░░░░██╗███╗░░██╗░██████╗░░█████╗░
    # ██║╚══██╔══╝██╔════╝████╗░████║██║░░░░░██║████╗░██║██╔════╝░██╔══██╗
    # ██║░░░██║░░░█████╗░░██╔████╔██║██║░░░░░██║██╔██╗██║██║░░██╗░██║░░██║
    # ██║░░░██║░░░██╔══╝░░██║╚██╔╝██║██║░░░░░██║██║╚████║██║░░╚██╗██║░░██║
    # ██║░░░██║░░░███████╗██║░╚═╝░██║███████╗██║██║░╚███║╚██████╔╝╚█████╔╝
    # ╚═╝░░░╚═╝░░░╚══════╝╚═╝░░░░░╚═╝╚══════╝╚═╝╚═╝░░╚══╝░╚═════╝░░╚════╝░
    #
    # Internal system messages of the ItemLingo plugin
    #
    # 1 General
    lingo_err_not_have_permission: "You do not have permission to use this command."
    lingo_err_command_format: "Command format: "
    #
    # 2 Loading configurations
    lingo_err_folder_is_empty: "The folder is empty or an error occurred while reading it."
    lingo_err_folder_is_notexist: "The folder does not exist or it is not a folder."
    lingo_err_reloading_configuration: "Error reloading configuration."
    lingo_created_default_configuration: "Default configuration created."
    lingo_configuration_reloaded: "Configuration reloaded."
    lingo_language_reloaded: "Language files and configuration reloaded."
    #
    # 3 Deleting files
    lingo_err_not_allowed_to_delete: "Not allowed to delete files from this folder: "
    lingo_err_folder_does_not_exist: "Folder does not exist: "
    lingo_file_deleted_successfully: "File successfully deleted: "
    lingo_err_not_allowed_delete_from_this_folder: "Failed to delete file: "
    lingo_err_file_not_found: "File not found: "
    lingo_err_error_deleting_file: "Error deleting file: "
    #
    # 4 Uploading various files
    lingo_err_uploading_not_allowed: "Not allowed to upload to this folder: "
    lingo_err_failed_create_folder: "Failed to create folder: "
    lingo_err_error_loading_file: "Error loading file: "
    lingo_err_file_already_exists: "File already exists: "
    lingo_err_error_in_URL: "Error in URL: "
    lingo_file_uploaded_successfully: "File uploaded successfully: "
    lingo_file_updated_successfully: "File updated successfully: "
    #
    # 5 Loading ItemLingo language files
    lingo_err_filename_not_pattern: "File name does not match the pattern: "
    lingo_err_error_parsing_YAML: "Error parsing YAML: "
    lingo_err_key_not_string: "Key is not a string: "
    lingo_err_failed_create_ItemLang_key: "Failed to create ItemLang for key "
    lingo_err_items_section_is_missing_file: "The 'items' section is missing in the file. "
    lingo_err_unknown_directory_type: "Unknown directory type: "
    #
    # 6 General for file operations
    lingo_file_operation_initiated: "File operation initiated for directory: "
    lingo_file_show_folder_contents: "Click to see the contents of the folder: "
    lingo_file_folder_contents: "Folder contents "
    #
    # 7 language commands
    lingo_err_language_code_2letters: "Language code must be 2 letters. "
    lingo_err_invalid_language_code: "Invalid language code: "
    lingo_you_language: "List of languages you have chosen: "
    lingo_you_have_not_set_language: "You have not chosen any language, using Minecraft settings "
    lingo_cleared_the_language: "You have cleared the language list, Minecraft settings will be used "
    lingo_err_no_item_data_found: "No data found for the specified language and key. "
    lingo_keys_for_language: "Keys for language "
    lingo_language_is_selected: "You have selected the following languages: "
    #
    # 8 NBT
    lingo_NBT_value_set: "NBT value has been set. "
    lingo_err_item_no_NBT_tags: "Your item has no NBT tags. "
    lingo_NBT_keys_for_item: "NBT keys for your item: "
    lingo_err_NBT_tag_not_exist: "NBT tag with such key does not exist. "
    lingo_err_command_only_player: "This command can only be executed by a player. "
    lingo_err_have_nothing_hand: "You have nothing in your hand. "

</div>

*   Files must have the name format: "`xxx_[language_code].yml`", for example, "`lingo_en.yml`", files that do not meet the standard are not read.

</div>
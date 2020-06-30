![Java CI with Gradle](https://github.com/Polymeta/LuckySpongeBlock/workflows/Java%20CI%20with%20Gradle/badge.svg)

# LuckySpongeBlock
Build against SpongeAPI 7 to allow Server Owners to distribute different kinds of luckyblocks to their players!

### What does it do?
LuckySpongeBlock allows you to create as many custom lucky blocks as you want. It uses itemstacksnapshots as serialization and therefore allows you to make highly customizable blocks. Upon placing the lucky block, it triggers the commands you entered into the config and executes them from console on the player. 
This plugin also features a buy command, which players can use to buy a luckyblock at a pre-configured price.

### Command/Permission
This plugin consists of only one command which is as follows:

* `/luckyspongeblock give [<player>] <luckyblockname> [amount]` **Permission:** `luckyspongeblock.give`.
Gives a the chosen luckyblock to the specified player (or yourself if you leave it out). Amount is optional and defaults to 1.

* `/luckyspongeblock buy <luckyblockname>` **Permission:** `luckyspongeblock.buy`.
Attempts to buy the chosen luckyblock. Will fail if you don't have enough money. Command only works if you have an economy plugin installed.

* `/luckyspongeblock reload` **Permission:** `luckyspongeblock.reload`.
Attempts to reload the plugin from its configuration.


### Configuration
Upon first starting your server with this plugin it will automatically generate a default configuration for you already, so you can use it straight out of the box! 
The default configuration also contains plenty of comments to guide you through it, but just for coverage, I will list them here as well:

* luckyblocks: Will contain a list of all your configured luckyblocks which can be bought and given to players ingame.

For each luckyblock you can define the following:

* commands: The commands to be executed when the blocks get placed. All commands will be run from the console. Use %p to get the player that placed the block.

* cost: The cost of the luckyblock. This is the amount the players have to pay to obtain the specified luckyblock

* luckyblockitem: ItemData. Contains things like display name, lore, itemtype, unsafe data, etc. I put in a default block to show some of the possibilities, but it is easiest just to explore (or to google)!

* name: needs to be **individual** will be used as reference for the command arguments

**NOTE**: If you make changes to existing luckyblocks, while they still exist in the world, the old ones **will break**. It is best to add a new luckyblock to the config instead of changing old ones, to ensure nothing breaks!


### TO-DO(?)
Currently, I can't think of additional things to add but, if you feel like something is wrong or missing, please open an issue and I'll see what I can do!

Thank you for looking at my plugin and reading until the end of this text. :)

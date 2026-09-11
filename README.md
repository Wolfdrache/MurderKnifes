# MurderKnifes

MurderKnifes is a Paper plugin for Minecraft 1.19.3 that manages custom murder knives for players.

It provides:

- a GUI for selecting knives
- Vault-based knife purchases
- per-player saved knife ownership and favorites
- protection so custom knives cannot be placed as blocks or fired as projectiles
- a small Bukkit service API for other plugins

## Requirements

- Java 17
- Paper 1.19.3
- Vault
- an economy plugin supported by Vault

## What The Plugin Does

When the plugin starts, it registers `MurderKnfesAPI` in Bukkit's `ServicesManager`.

Other plugins can use that API to:

- open the knife selector GUI for a player
- get the currently selected knife as an `ItemStack`
- check whether an `ItemStack` is a MurderKnifes knife

Internally, MurderKnifes:

- loads knife definitions from `knifes.yml`
- stores purchased and favorite knives in `playerKnifes.yml`
- marks custom knives with persistent data so they can be identified later
- blocks those knives from being used as placeable blocks or projectile ammo

## Depending On MurderKnifes In Another Plugin

There are two parts:

1. your plugin must have access to the `MurderKnfesAPI` class at compile time
2. your plugin should declare MurderKnifes as a runtime dependency in `plugin.yml`

### Runtime Dependency

Add MurderKnifes to your `plugin.yml` so your plugin loads after it:

```yml
depend: [MurderKnifes]
```

If your plugin can still run without MurderKnifes, use:

```yml
softdepend: [MurderKnifes]
```

### Maven Dependency

This project uses the following Maven coordinates:

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
    <groupId>com.github.Wolfdrache</groupId>
    <artifactId>MurderKnifes</artifactId>
    <version>-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
```

If the jar is not published in a remote repository, install or publish it to your local or private Maven repository first.

## Accessing The API

Use Bukkit's `ServicesManager` to get the registered provider:

```java
RegisteredServiceProvider<MurderKnfesAPI> provider =
    getServer().getServicesManager().getRegistration(MurderKnfesAPI.class);

if (provider == null) {
    getLogger().severe("MurderKnifes API not found!");
    return;
}

MurderKnfesAPI murderKnifes = provider.getProvider();
```

You can usually do this in your plugin's `onEnable()` after dependencies have loaded.

## API Methods

### `openKnifeSelector(Player player)`

Opens the MurderKnifes selection GUI for the given player.

Example:

```java
murderKnifes.openKnifeSelector(player);
```

### `ItemStack getKnife(Player player)`

Returns the player's currently selected knife as an `ItemStack`.

If the player has no saved custom selection, the plugin falls back to its default knife.

Example:

```java
ItemStack knife = murderKnifes.getKnife(player);
player.getInventory().addItem(knife);
```

### `boolean isKnife(ItemStack item)`

Checks whether an item is a knife created by MurderKnifes.

Example:

```java
if (murderKnifes.isKnife(item)) {
    // Handle MurderKnifes items differently
}
```

## Example Integration

```java
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.wolfdrache.murderknifes.api.MurderKnfesAPI;

public class ExampleUsage {
    private final MurderKnfesAPI murderKnifes;

    public ExampleUsage(RegisteredServiceProvider<MurderKnfesAPI> provider) {
        this.murderKnifes = provider.getProvider();
    }

    public void giveSelectedKnife(Player player) {
        ItemStack knife = murderKnifes.getKnife(player);
        if (knife != null) {
            player.getInventory().addItem(knife);
        }
    }

    public void openSelector(Player player) {
        murderKnifes.openKnifeSelector(player);
    }
}
```

## Notes

- The API interface name is `MurderKnfesAPI` and must be referenced exactly like that.
- The plugin requires Vault to enable successfully.
- The permission `murderknifes.allKnifes` grants access to all knives without requiring purchase.
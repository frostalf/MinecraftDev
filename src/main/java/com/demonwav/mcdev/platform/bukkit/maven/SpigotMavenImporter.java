/*
 * IntelliJ IDEA Bukkit Support Plugin
 *
 * Written by Kyle Wood (DemonWav)
 * http://demonwav.com
 *
 * MIT License
 */

package com.demonwav.mcdev.platform.bukkit.maven;

import com.demonwav.mcdev.platform.bukkit.SpigotModuleType;

import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.ModuleType;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class SpigotMavenImporter extends BukkitMavenImporter {

    public SpigotMavenImporter() {
        super(SpigotModuleType.getInstance());
    }

    public SpigotMavenImporter(@NotNull final SpigotModuleType type) {
        super(type);
    }

    @NotNull
    @Override
    public ModuleType getModuleType() {
        return JavaModuleType.getModuleType();
    }
}

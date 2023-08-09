/*
 * cp-capes - PlayerListEntryMixin.java
 * Copyright (C) 2023 tanuthedev (https://github.com/tanuthedev)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *  
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package lol.tanu.cpcapes.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import lol.tanu.cpcapes.util.PlayerHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(PlayerListEntry.class)
public class PlayerListEntryMixin {
    @Shadow
    @Final
    private GameProfile profile;
    @Shadow
    @Final
    private Map<MinecraftProfileTexture.Type, Identifier> textures;
    private boolean loadedCapeTexture = false;

    @Inject(method = "getCapeTexture", at = @At("HEAD"))
    private void injectedCapeTexture(CallbackInfoReturnable<Identifier> cir) {
        fetchCapeTexture();
    }

    @Inject(method = "getElytraTexture", at = @At("HEAD"))
    private void injectedElytraTexture(CallbackInfoReturnable<Identifier> cir) {
        fetchCapeTexture();
    }

    private void fetchCapeTexture() {
        if (loadedCapeTexture) return;
        loadedCapeTexture = true;
        Map<MinecraftProfileTexture.Type, Identifier> textures = this.textures;
        PlayerHandler.loadPlayerCape(this.profile, id -> {
            textures.put(MinecraftProfileTexture.Type.CAPE, id);
        });
    }
}

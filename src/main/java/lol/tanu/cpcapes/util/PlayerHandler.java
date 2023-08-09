/*
 * cp-capes - PlayerHandler.java
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
package lol.tanu.cpcapes.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.text.Text;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PlayerHandler {
    private static final Logger LOGGER = LogManager.getLogger("cp-capes");
    public static Map<String, Identifier> capes = new HashMap<>();
    private static String PLAYERNAME = MinecraftClient.getInstance().getSession().getUsername();

    public interface ReturnCapeTexture {
        void response(Identifier id);
    }

    public static void loadPlayerCape(GameProfile player, ReturnCapeTexture response) {
        Util.getMainWorkerExecutor().execute(() -> {
            try {
                String uuid = player.getId().toString();
                // Here resides the URL of the server from which the cape is pulled
                NativeImageBackedTexture nIBT = getCapeFromURL(String.format("http://server.cloaksplus.com/capes/%s.png", player.getName()), player.getName());
                Identifier capeTexture = MinecraftClient.getInstance().getTextureManager().registerDynamicTexture("cpcapes-" + uuid, nIBT);
                capes.put(uuid, capeTexture);
                response.response(capeTexture);
                // This will print only if everything above went OK
                LOGGER.info(String.format("[cp-capes] Cloak loaded (%s).", player.getName()));
            } catch (Exception e) {
                // making the error popup and stacktrace get logged for every cape that didnt load crashes the game,
                // so as a quick-fix i made it pop-up only for the player you're logged in as.
                if (PLAYERNAME.equals(player.getName())) {
                    // this toast took way too long to implement. should've been done earlier lmao
                    SystemToast CPERROR_TOAST = SystemToast.create(
                        MinecraftClient.getInstance(), SystemToast.Type.UNSECURE_SERVER_WARNING, Text.translatable("cpcapes.loaderr.title"), Text.translatable("cpcapes.loaderr.description")
                    );
                    MinecraftClient.getInstance().getToastManager().add(CPERROR_TOAST);
                    // this will print if something in the try block went wrong. also prints the stack trace
                    LOGGER.warn(String.format("[cp-capes] Cloak load has failed (%s).", player.getName()));
                    e.printStackTrace();
                };
            };
        });
    }

    public static NativeImageBackedTexture getCapeFromURL(String capeStringURL, String playerName) {
        try {
            URL capeURL = new URL(capeStringURL);
            return getCapeFromStream(capeURL.openStream());
        } catch (IOException e) {
            // can't have these errors printing for everyone. that'd crash the game (i tried)
            if (PLAYERNAME.equals(playerName)) {
                LOGGER.warn(String.format("[cp-capes] Cloak download has failed, loading will fail! (%s)", playerName));
                e.printStackTrace();
            }
            return null;
        }
    }

    public static NativeImageBackedTexture getCapeFromStream(InputStream image) {
        NativeImage cape = null;
        try {
            cape = NativeImage.read(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (cape != null) {
            NativeImageBackedTexture nIBT = new NativeImageBackedTexture(parseCape(cape));
            return nIBT;
        }
        return null;
    }

    public static NativeImage parseCape(NativeImage image) {
        int imageWidth = 64;
        int imageHeight = 32;
        int imageSrcWidth = image.getWidth();
        int srcHeight = image.getHeight();

        for (int imageSrcHeight = image.getHeight(); imageWidth < imageSrcWidth
                || imageHeight < imageSrcHeight; imageHeight *= 2) {
            imageWidth *= 2;
        }

        NativeImage imgNew = new NativeImage(imageWidth, imageHeight, true);
        for (int x = 0; x < imageSrcWidth; x++) {
            for (int y = 0; y < srcHeight; y++) {
                imgNew.setColor(x, y, image.getColor(x, y));
            }
        }
        image.close();
        return imgNew;
    }
}

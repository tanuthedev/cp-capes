/*
 * cp-capes - SkinOptionsScreenMixin.java
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

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.SkinOptionsScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

@Mixin(SkinOptionsScreen.class)
public abstract class SkinOptionsScreenMixin extends GameOptionsScreen {

	// Here resides the widget identifier/texture path, formatted ("<folder w/ assets name>", "texture/path.png")
	private static final Identifier CPBUTTON_TEXTURE = new Identifier("cpcapes","textures/cpbtn.png");
	private TexturedButtonWidget cpbutton;
	// Here resides the button tooltip translation key
	private static final Text CPBUTTON_TOOLTIP = Text.translatable("cpcapes.button.discord");
	// Here resides the URL you get redirected to when clicking the button
	private final String url = String.format("https://discord.gg/cloaks");

	public SkinOptionsScreenMixin(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

	@Inject(
		at = @At("RETURN"),
		method = "init()V")
	public void iTInject(CallbackInfo info) {
		// The actual button's code
		cpbutton = new TexturedButtonWidget(this.width / 2 - 179, this.height / 6, 20, 20, 0, 0, 20, CPBUTTON_TEXTURE, 32, 64, (buttonWidget) -> {
			Util.getOperatingSystem().open(url);
		});
		cpbutton.setTooltip(Tooltip.of(CPBUTTON_TOOLTIP));
		this.addDrawableChild(cpbutton);
	}
}

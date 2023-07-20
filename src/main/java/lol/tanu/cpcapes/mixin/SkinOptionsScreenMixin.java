package lol.tanu.cpcapes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.SkinOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.math.BigInteger;
import java.util.Random;

@Mixin(SkinOptionsScreen.class)
public abstract class SkinOptionsScreenMixin extends GameOptionsScreen {

	private static final Identifier CLOAKSPLUS_BUTTON_TEXTURE = new Identifier("cpcapes","textures/cpbtn.png");
	private final String url = String.format("https://discord.gg/cloaks");

	public SkinOptionsScreenMixin(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

	@Inject(
		at = @At("RETURN"),
		method = "init()V")
	public void iTInject(CallbackInfo info) {
		this.addDrawableChild(new TexturedButtonWidget(this.width / 2 - 179, this.height / 6, 22, 20, 0, 0, 20, CLOAKSPLUS_BUTTON_TEXTURE, 32, 64, (buttonWidget) -> Util.getOperatingSystem().open(url)));
	}
}

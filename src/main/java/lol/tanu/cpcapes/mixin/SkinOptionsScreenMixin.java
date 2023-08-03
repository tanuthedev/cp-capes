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

	private static final Identifier CPBUTTON_TEXTURE = new Identifier("cpcapes","textures/cpbtn.png");
	private TexturedButtonWidget cpbutton;
	private static final Text CPBUTTON_TOOLTIP = Text.translatable("cpcapes.button.discord");
	private final String url = String.format("https://discord.gg/cloaks");

	public SkinOptionsScreenMixin(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

	@Inject(
		at = @At("RETURN"),
		method = "init()V")
	public void iTInject(CallbackInfo info) {
		cpbutton = new TexturedButtonWidget(this.width / 2 - 179, this.height / 6, 20, 20, 0, 0, 20, CPBUTTON_TEXTURE, 32, 64, (buttonWidget) -> {
			Util.getOperatingSystem().open(url);
		});
		cpbutton.setTooltip(Tooltip.of(CPBUTTON_TOOLTIP));
		this.addDrawableChild(cpbutton);
		// Experimental button code - nonfunctional, to be removed
		/*cpbutton = TexturedButtonWidget.builder(CPBUTTON_TOOLTIP, button -> {
			Util.getOperatingSystem().open(url);
		}).dimensions(this.width / 2 - 179, this.height / 6, 22, 20).tooltip(Tooltip.of(CPBUTTON_TOOLTIP)).build();*/
	}
}

package vazkii.quark.content.client.module;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.quark.base.client.handler.ModKeybindHandler;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.module.QuarkModule;

@LoadModule(category = ModuleCategory.CLIENT, hasSubscriptions = true, subscribeOn = Dist.CLIENT)
public class AutoWalkKeybindModule extends QuarkModule {

	@OnlyIn(Dist.CLIENT)
	private KeyBinding keybind;

	private boolean autorunning;
	private boolean hadAutoJump;

	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientSetup() {
		if(enabled)
			keybind = ModKeybindHandler.init("autorun", "caps.lock", ModKeybindHandler.ACCESSIBILITY_GROUP);
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onMouseInput(InputEvent.MouseInputEvent event) {
		acceptInput();
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		acceptInput();
	}

	private void acceptInput() {
		Minecraft mc = Minecraft.getInstance();

		if(mc.gameSettings.keyBindForward.isKeyDown()) {
			if(autorunning)
				mc.gameSettings.autoJump = hadAutoJump;
			
			autorunning = false;
		}
		
		else if(keybind.isKeyDown()) {
			autorunning = !autorunning;

			if(autorunning) {
				hadAutoJump = mc.gameSettings.autoJump;
				mc.gameSettings.autoJump = true;
			} else mc.gameSettings.autoJump = hadAutoJump;
		}
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onInput(InputUpdateEvent event) {
		Minecraft mc = Minecraft.getInstance();
		if(mc.player != null && autorunning) {
			event.getMovementInput().forwardKeyDown = true;
			event.getMovementInput().moveForward = 1F;
		}
	}

}

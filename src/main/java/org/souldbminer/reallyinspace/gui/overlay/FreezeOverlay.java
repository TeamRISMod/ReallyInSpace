
package org.souldbminer.reallyinspace.gui.overlay;

import org.souldbminer.reallyinspace.procedures.DisplayFreezeOverlayProcedure;
import org.souldbminer.reallyinspace.ReallyinspaceModElements;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.World;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.Minecraft;

import com.google.common.collect.ImmutableMap;

@ReallyinspaceModElements.ModElement.Tag
public class FreezeOverlay extends ReallyinspaceModElements.ModElement {
	public FreezeOverlay(ReallyinspaceModElements instance) {
		super(instance, 318);
	}

	@Override
	public void initElements() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void eventHandler(RenderGameOverlayEvent event) {
		if (!event.isCancelable() && event.getType() == RenderGameOverlayEvent.ElementType.HELMET) {
			int posX = (event.getWindow().getScaledWidth()) / 2;
			int posY = (event.getWindow().getScaledHeight()) / 2;
			PlayerEntity entity = Minecraft.getInstance().player;
			World world = entity.world;
			double x = entity.getPosX();
			double y = entity.getPosY();
			double z = entity.getPosZ();
			if (DisplayFreezeOverlayProcedure.executeProcedure(ImmutableMap.of("x", x, "y", y, "z", z, "world", world))) {
				Minecraft.getInstance().fontRenderer.drawString(event.getMatrixStack(), "Yo u r cold", posX + -26, posY + -29, -12829636);
			}
		}
	}
}

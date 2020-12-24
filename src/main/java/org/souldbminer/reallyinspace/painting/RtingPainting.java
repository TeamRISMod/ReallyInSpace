
package org.souldbminer.reallyinspace.painting;

import org.souldbminer.reallyinspace.ReallyinspaceModElements;

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.RegistryEvent;

import net.minecraft.entity.item.PaintingType;

@ReallyinspaceModElements.ModElement.Tag
public class RtingPainting extends ReallyinspaceModElements.ModElement {
	public RtingPainting(ReallyinspaceModElements instance) {
		super(instance, 77);
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
	}

	@SubscribeEvent
	public void registerPaintingType(RegistryEvent.Register<PaintingType> event) {
		event.getRegistry().register(new PaintingType(16, 16).setRegistryName("rting"));
	}
}

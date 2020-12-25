
package org.souldbminer.reallyinspace.potion;

import org.souldbminer.reallyinspace.ReallyinspaceModElements;

import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.RegistryEvent;

import net.minecraft.util.ResourceLocation;
import net.minecraft.potion.Potion;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effect;

@ReallyinspaceModElements.ModElement.Tag
public class H1N1Potion extends ReallyinspaceModElements.ModElement {
	@ObjectHolder("reallyinspace:h_1_n_1")
	public static final Effect potion = null;
	@ObjectHolder("reallyinspace:h_1_n_1")
	public static final Potion potionType = null;
	public H1N1Potion(ReallyinspaceModElements instance) {
		super(instance, 264);
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
	}

	@SubscribeEvent
	public void registerEffect(RegistryEvent.Register<Effect> event) {
		event.getRegistry().register(new EffectCustom());
	}

	@SubscribeEvent
	public void registerPotion(RegistryEvent.Register<Potion> event) {
		event.getRegistry().register(new PotionCustom());
	}
	public static class PotionCustom extends Potion {
		public PotionCustom() {
			super(new EffectInstance(potion, 3600));
			setRegistryName("h_1_n_1");
		}
	}

	public static class EffectCustom extends Effect {
		private final ResourceLocation potionIcon;
		public EffectCustom() {
			super(EffectType.HARMFUL, -13369549);
			setRegistryName("h_1_n_1");
			potionIcon = new ResourceLocation("reallyinspace:textures/h1n1p.png");
		}

		@Override
		public String getName() {
			return "effect.h_1_n_1";
		}

		@Override
		public boolean isBeneficial() {
			return false;
		}

		@Override
		public boolean isInstant() {
			return false;
		}

		@Override
		public boolean shouldRenderInvText(EffectInstance effect) {
			return true;
		}

		@Override
		public boolean shouldRender(EffectInstance effect) {
			return true;
		}

		@Override
		public boolean shouldRenderHUD(EffectInstance effect) {
			return true;
		}

		@Override
		public boolean isReady(int duration, int amplifier) {
			return true;
		}
	}
}

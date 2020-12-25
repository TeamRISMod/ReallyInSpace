
package org.souldbminer.reallyinspace.entity;

import org.souldbminer.reallyinspace.itemgroup.RISItemGroup;
import org.souldbminer.reallyinspace.ReallyinspaceModElements;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.DamageSource;
import net.minecraft.network.IPacket;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.Entity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.MobRenderer;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;

@ReallyinspaceModElements.ModElement.Tag
public class AgressiveAleinEntity extends ReallyinspaceModElements.ModElement {
	public static EntityType entity = null;
	public AgressiveAleinEntity(ReallyinspaceModElements instance) {
		super(instance, 164);
		FMLJavaModLoadingContext.get().getModEventBus().register(new ModelRegisterHandler());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		entity = (EntityType.Builder.<CustomEntity>create(CustomEntity::new, EntityClassification.MONSTER).setShouldReceiveVelocityUpdates(true)
				.setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new).size(0.6f, 1.8f)).build("agressive_alein")
						.setRegistryName("agressive_alein");
		elements.entities.add(() -> entity);
		elements.items.add(() -> new SpawnEggItem(entity, -65536, -13369549, new Item.Properties().group(RISItemGroup.tab))
				.setRegistryName("agressive_alein_spawn_egg"));
	}

	@SubscribeEvent
	public void addFeatureToBiomes(BiomeLoadingEvent event) {
		boolean biomeCriteria = false;
		if (new ResourceLocation("reallyinspace:marsflat").equals(event.getName()))
			biomeCriteria = true;
		if (new ResourceLocation("reallyinspace:astroids").equals(event.getName()))
			biomeCriteria = true;
		if (new ResourceLocation("reallyinspace:moonbiome").equals(event.getName()))
			biomeCriteria = true;
		if (!biomeCriteria)
			return;
		event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(entity, 20, 4, 4));
	}

	@Override
	public void init(FMLCommonSetupEvent event) {
		DeferredWorkQueue.runLater(this::setupAttributes);
		EntitySpawnPlacementRegistry.register(entity, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
				MonsterEntity::canMonsterSpawn);
	}
	private static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(entity, renderManager -> {
				return new MobRenderer(renderManager, new Modelalien(), 0.5f) {
					@Override
					public ResourceLocation getEntityTexture(Entity entity) {
						return new ResourceLocation("reallyinspace:textures/alein.png");
					}
				};
			});
		}
	}
	private void setupAttributes() {
		AttributeModifierMap.MutableAttribute ammma = MobEntity.func_233666_p_();
		ammma = ammma.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3);
		ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 10);
		ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
		ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 3);
		GlobalEntityTypeAttributes.put(entity, ammma.create());
	}
	public static class CustomEntity extends AnimalEntity {
		public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			this(entity, world);
		}

		public CustomEntity(EntityType<CustomEntity> type, World world) {
			super(type, world);
			experienceValue = 0;
			setNoAI(false);
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		@Override
		protected void registerGoals() {
			super.registerGoals();
			this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false));
			this.targetSelector.addGoal(2, new HurtByTargetGoal(this).setCallsForHelp(this.getClass()));
			this.goalSelector.addGoal(3, new RandomWalkingGoal(this, 0.8));
			this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
			this.targetSelector.addGoal(5, new NearestAttackableTargetGoal(this, PlayerEntity.class, true, true));
		}

		@Override
		public CreatureAttribute getCreatureAttribute() {
			return CreatureAttribute.UNDEFINED;
		}

		@Override
		public net.minecraft.util.SoundEvent getHurtSound(DamageSource ds) {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.hurt"));
		}

		@Override
		public net.minecraft.util.SoundEvent getDeathSound() {
			return (net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.death"));
		}

		@Override
		public AgeableEntity func_241840_a(ServerWorld serverWorld, AgeableEntity ageable) {
			CustomEntity retval = (CustomEntity) entity.create(serverWorld);
			retval.onInitialSpawn(serverWorld, serverWorld.getDifficultyForLocation(new BlockPos(retval.getPosition())), SpawnReason.BREEDING,
					(ILivingEntityData) null, (CompoundNBT) null);
			return retval;
		}

		@Override
		public boolean isBreedingItem(ItemStack stack) {
			if (stack == null)
				return false;
			return false;
		}
	}

	// Made with Blockbench 3.7.4
	// Exported for Minecraft version 1.15
	// Paste this class into your mod and generate all required imports
	public static class Modelalien extends EntityModel<Entity> {
		private final ModelRenderer bb_main;
		private final ModelRenderer cube_r1;
		private final ModelRenderer cube_r2;
		private final ModelRenderer cube_r3;
		private final ModelRenderer cube_r4;
		private final ModelRenderer cube_r5;
		public Modelalien() {
			textureWidth = 64;
			textureHeight = 64;
			bb_main = new ModelRenderer(this);
			bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
			bb_main.setTextureOffset(0, 0).addBox(-5.0F, -23.0F, -2.0F, 11.0F, 14.0F, 6.0F, 0.0F, false);
			bb_main.setTextureOffset(37, 14).addBox(6.0F, -23.0F, -2.0F, 2.0F, 4.0F, 3.0F, 0.0F, false);
			bb_main.setTextureOffset(37, 37).addBox(-7.0F, -23.0F, -2.0F, 2.0F, 4.0F, 3.0F, 0.0F, false);
			bb_main.setTextureOffset(34, 0).addBox(8.0F, -20.0F, -2.0F, 2.0F, 11.0F, 3.0F, 0.0F, false);
			bb_main.setTextureOffset(0, 20).addBox(-4.0F, -34.0F, 1.0F, 10.0F, 10.0F, 3.0F, 0.0F, false);
			bb_main.setTextureOffset(0, 3).addBox(-2.0F, -32.0F, 0.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);
			bb_main.setTextureOffset(0, 0).addBox(3.0F, -32.0F, 0.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);
			bb_main.setTextureOffset(26, 20).addBox(-1.0F, -24.0F, 1.0F, 4.0F, 1.0F, 3.0F, 0.0F, false);
			cube_r1 = new ModelRenderer(this);
			cube_r1.setRotationPoint(5.0F, -34.0F, 1.0F);
			bb_main.addChild(cube_r1);
			setRotationAngle(cube_r1, -0.2182F, 0.3927F, 0.0F);
			cube_r1.setTextureOffset(24, 40).addBox(-1.0F, -6.0F, 0.0F, 2.0F, 6.0F, 1.0F, 0.0F, false);
			cube_r2 = new ModelRenderer(this);
			cube_r2.setRotationPoint(-3.0F, -34.0F, 1.0F);
			bb_main.addChild(cube_r2);
			setRotationAngle(cube_r2, -0.2182F, -0.3927F, 0.0F);
			cube_r2.setTextureOffset(30, 40).addBox(-1.0F, -6.0F, 0.0F, 2.0F, 6.0F, 1.0F, 0.0F, false);
			cube_r3 = new ModelRenderer(this);
			cube_r3.setRotationPoint(-8.0F, -16.0F, 0.0F);
			bb_main.addChild(cube_r3);
			setRotationAngle(cube_r3, 0.0F, -0.1745F, 0.0F);
			cube_r3.setTextureOffset(14, 33).addBox(-1.0F, -4.0F, -2.0F, 2.0F, 11.0F, 3.0F, 0.0F, false);
			cube_r4 = new ModelRenderer(this);
			cube_r4.setRotationPoint(2.0F, 0.0F, -1.0F);
			bb_main.addChild(cube_r4);
			setRotationAngle(cube_r4, 0.0F, -0.0873F, 0.0F);
			cube_r4.setTextureOffset(26, 26).addBox(-1.0F, -11.0F, 0.0F, 4.0F, 11.0F, 3.0F, 0.0F, false);
			cube_r5 = new ModelRenderer(this);
			cube_r5.setRotationPoint(-3.0F, 0.0F, -1.0F);
			bb_main.addChild(cube_r5);
			setRotationAngle(cube_r5, 0.0F, 0.0873F, 0.0F);
			cube_r5.setTextureOffset(0, 33).addBox(-1.0F, -11.0F, 0.0F, 4.0F, 11.0F, 3.0F, 0.0F, false);
		}

		@Override
		public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
			// previously the render function, render code was moved to a method below
		}

		@Override
		public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue,
				float alpha) {
			bb_main.render(matrixStack, buffer, packedLight, packedOverlay);
		}

		public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
			modelRenderer.rotateAngleX = x;
			modelRenderer.rotateAngleY = y;
			modelRenderer.rotateAngleZ = z;
		}
	}
}


package org.souldbminer.reallyinspace.block;

import org.souldbminer.reallyinspace.itemgroup.RISItemGroup;
import org.souldbminer.reallyinspace.ReallyinspaceModElements;

import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.World;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IBlockReader;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.Direction;
import net.minecraft.loot.LootContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.BlockItem;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.block.material.Material;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Block;

import java.util.Random;
import java.util.List;
import java.util.Collections;

@ReallyinspaceModElements.ModElement.Tag
public class DryWeedsBlock extends ReallyinspaceModElements.ModElement {
	@ObjectHolder("reallyinspace:dry_weeds")
	public static final Block block = null;
	public DryWeedsBlock(ReallyinspaceModElements instance) {
		super(instance, 268);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void initElements() {
		elements.blocks.add(() -> new BlockCustomFlower());
		elements.items.add(() -> new BlockItem(block, new Item.Properties().group(RISItemGroup.tab)).setRegistryName(block.getRegistryName()));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientLoad(FMLClientSetupEvent event) {
		RenderTypeLookup.setRenderLayer(block, RenderType.getCutout());
	}

	@SubscribeEvent
	public void addFeatureToBiomes(BiomeLoadingEvent event) {
		Feature<BlockClusterFeatureConfig> feature = new Feature<BlockClusterFeatureConfig>(BlockClusterFeatureConfig.field_236587_a_) {
			@Override
			public boolean generate(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, BlockClusterFeatureConfig config) {
				RegistryKey<World> dimensionType = world.getWorld().getDimensionKey();
				boolean dimensionCriteria = false;
				if (dimensionType == RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("reallyinspace:mars")))
					dimensionCriteria = true;
				if (dimensionType == RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("reallyinspace:mercdim")))
					dimensionCriteria = true;
				if (dimensionType == RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("reallyinspace:venusdimension")))
					dimensionCriteria = true;
				if (!dimensionCriteria)
					return false;
				int generated = 0;
				for (int j = 0; j < 5; ++j) {
					BlockPos blockpos = pos.add(random.nextInt(4) - random.nextInt(4), 0, random.nextInt(4) - random.nextInt(4));
					if (world.isAirBlock(blockpos)) {
						BlockPos blockpos1 = blockpos.down();
						int k = 1 + random.nextInt(random.nextInt(3) + 1);
						k = Math.min(3, k);
						for (int l = 0; l < k; ++l) {
							if (block.getDefaultState().isValidPosition(world, blockpos)) {
								world.setBlockState(blockpos.up(l), block.getDefaultState(), 2);
								generated++;
							}
						}
					}
				}
				return generated > 0;
			}
		};
		event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION)
				.add(() -> (ConfiguredFeature<?, ?>) feature.withConfiguration(
						(new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(block.getDefaultState()), new SimpleBlockPlacer()))
								.tries(64).build())
						.withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(5));
	}
	public static class BlockCustomFlower extends SugarCaneBlock {
		public BlockCustomFlower() {
			super(Block.Properties.create(Material.PLANTS).tickRandomly().doesNotBlockMovement().sound(SoundType.PLANT).hardnessAndResistance(0f, 0f)
					.setLightLevel(s -> 0));
			setRegistryName("dry_weeds");
		}

		@Override
		public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
			return 100;
		}

		@Override
		public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
			return 60;
		}

		@Override
		public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
			List<ItemStack> dropsOriginal = super.getDrops(state, builder);
			if (!dropsOriginal.isEmpty())
				return dropsOriginal;
			return Collections.singletonList(new ItemStack(this, 1));
		}

		@Override
		public PlantType getPlantType(IBlockReader world, BlockPos pos) {
			return PlantType.NETHER;
		}

		@Override
		public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
			if (!state.isValidPosition(world, pos)) {
				world.destroyBlock(pos, true);
			} else if (world.isAirBlock(pos.up())) {
				int i = 1;
				for (; world.getBlockState(pos.down(i)).getBlock() == this; ++i);
				if (i < 3) {
					int j = state.get(AGE);
					if (j == 15) {
						world.setBlockState(pos.up(), getDefaultState());
						world.setBlockState(pos, state.with(AGE, 0), 4);
					} else {
						world.setBlockState(pos, state.with(AGE, j + 1), 4);
					}
				}
			}
		}
	}
}

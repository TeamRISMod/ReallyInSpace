package org.souldbminer.reallyinspace.procedures;

import org.souldbminer.reallyinspace.block.NitrogenBlock;
import org.souldbminer.reallyinspace.ReallyinspaceModElements;
import org.souldbminer.reallyinspace.ReallyinspaceMod;

import net.minecraft.world.IWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

@ReallyinspaceModElements.ModElement.Tag
public class DisplayFreezeOverlayProcedure extends ReallyinspaceModElements.ModElement {
	public DisplayFreezeOverlayProcedure(ReallyinspaceModElements instance) {
		super(instance, 319);
	}

	public static boolean executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("x") == null) {
			if (!dependencies.containsKey("x"))
				ReallyinspaceMod.LOGGER.warn("Failed to load dependency x for procedure DisplayFreezeOverlay!");
			return false;
		}
		if (dependencies.get("y") == null) {
			if (!dependencies.containsKey("y"))
				ReallyinspaceMod.LOGGER.warn("Failed to load dependency y for procedure DisplayFreezeOverlay!");
			return false;
		}
		if (dependencies.get("z") == null) {
			if (!dependencies.containsKey("z"))
				ReallyinspaceMod.LOGGER.warn("Failed to load dependency z for procedure DisplayFreezeOverlay!");
			return false;
		}
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				ReallyinspaceMod.LOGGER.warn("Failed to load dependency world for procedure DisplayFreezeOverlay!");
			return false;
		}
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		IWorld world = (IWorld) dependencies.get("world");
		return ((world.getBlockState(new BlockPos((int) x, (int) y, (int) z))).getBlock() == NitrogenBlock.block.getDefaultState().getBlock());
	}
}

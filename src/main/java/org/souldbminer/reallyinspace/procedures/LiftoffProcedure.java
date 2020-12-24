package org.souldbminer.reallyinspace.procedures;

import org.souldbminer.reallyinspace.ReallyinspaceModElements;
import org.souldbminer.reallyinspace.ReallyinspaceMod;

import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.ResourceLocation;
import net.minecraft.potion.Effects;
import net.minecraft.potion.EffectInstance;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.command.ICommandSource;
import net.minecraft.command.FunctionObject;
import net.minecraft.command.CommandSource;

import java.util.Optional;
import java.util.Map;

@ReallyinspaceModElements.ModElement.Tag
public class LiftoffProcedure extends ReallyinspaceModElements.ModElement {
	public LiftoffProcedure(ReallyinspaceModElements instance) {
		super(instance, 66);
	}

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("entity") == null) {
			if (!dependencies.containsKey("entity"))
				ReallyinspaceMod.LOGGER.warn("Failed to load dependency entity for procedure Liftoff!");
			return;
		}
		if (dependencies.get("x") == null) {
			if (!dependencies.containsKey("x"))
				ReallyinspaceMod.LOGGER.warn("Failed to load dependency x for procedure Liftoff!");
			return;
		}
		if (dependencies.get("y") == null) {
			if (!dependencies.containsKey("y"))
				ReallyinspaceMod.LOGGER.warn("Failed to load dependency y for procedure Liftoff!");
			return;
		}
		if (dependencies.get("z") == null) {
			if (!dependencies.containsKey("z"))
				ReallyinspaceMod.LOGGER.warn("Failed to load dependency z for procedure Liftoff!");
			return;
		}
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				ReallyinspaceMod.LOGGER.warn("Failed to load dependency world for procedure Liftoff!");
			return;
		}
		Entity entity = (Entity) dependencies.get("entity");
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		IWorld world = (IWorld) dependencies.get("world");
		boolean funcran = false;
		boolean found = false;
		double sx = 0;
		double sy = 0;
		double sz = 0;
		if (entity instanceof LivingEntity)
			((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.LEVITATION, (int) 3600, (int) 1, (false), (false)));
		if (world instanceof ServerWorld && ((ServerWorld) world).getServer() != null) {
			Optional<FunctionObject> _fopt = ((ServerWorld) world).getServer().getFunctionManager().get(new ResourceLocation("ris:rcfunc"));
			if (_fopt.isPresent()) {
				FunctionObject _fobj = _fopt.get();
				((ServerWorld) world).getServer().getFunctionManager().execute(_fobj, new CommandSource(ICommandSource.DUMMY, new Vector3d(x, y, z),
						Vector2f.ZERO, (ServerWorld) world, 4, "", new StringTextComponent(""), ((ServerWorld) world).getServer(), null));
			}
		}
	}
}

package com.golem.skyblockutils.injection;

import java.util.Map;

import logger.Logger;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

@IFMLLoadingPlugin.Name("Rawr")
public class FMLPlugin implements IFMLLoadingPlugin {

	public FMLPlugin() {
		Logger.info("mixins initialized");
		MixinBootstrap.init();
		Mixins.addConfiguration("mixins.skyblockutils.json");
		MixinEnvironment
			.getDefaultEnvironment()
			.setSide(MixinEnvironment.Side.CLIENT);
	}

	@NotNull
	@Override
	public String[] getASMTransformerClass() {
		return new String[0];
	}

	@Nullable
	@Override
	public String getModContainerClass() {
		return null;
	}

	@Nullable
	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {}

	@Nullable
	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}


package com.golem.skyblockutils.injection;

import logger.Logger;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;

@IFMLLoadingPlugin.Name("Rawr")
@IFMLLoadingPlugin.MCVersion("1.8.9")
public class FMLPlugin implements IFMLLoadingPlugin {

	@SuppressWarnings("unchecked")
	public FMLPlugin() {
		Logger.info("mixins initialized");
		MixinBootstrap.init();
		Mixins.addConfiguration("mixins.skyblockutils.json");
		MixinEnvironment
			.getDefaultEnvironment()
			.setSide(MixinEnvironment.Side.CLIENT);
//		List<String> tweakClasses = (List<String>) Launch.blackboard.get("TweakClasses");
//		tweakClasses.add("com.golem.skyblockutils.init.Tweaker.loadSBU");
//		tweakClasses.add(loadSBU.class.getName());
//		Logger.debug("Tweak Classes: " + tweakClasses);
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

	@SuppressWarnings("unchecked")
	@Override
	public void injectData(Map<String, Object> data) {
//		// Exercise for the reader: add delegation to the mixin tweaker
//		URL location = getClass().getProtectionDomain().getCodeSource().getLocation();
//		if (location == null) return;
//		if (!"file".equals(location.getProtocol())) return;
//		try {
//			// Add yourself as mixin container
//			MixinBootstrap.getPlatform().addContainer(location.toURI());
//			String file = new File(location.toURI()).getName();
//			// Remove yourself from both the ignore list in order to be eligible to be loaded as a mod.
//			CoreModManager.getIgnoredMods().remove(file);
//			CoreModManager.getReparseableCoremods().add(file);
//		} catch (URISyntaxException e) {
//			e.printStackTrace();
//		}
	}

	@Nullable
	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}


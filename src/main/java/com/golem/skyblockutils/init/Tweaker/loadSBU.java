/*
	Credits to https://moddev.nea.moe/tweakers/#blackboard
	for this tutorial, the code block and the multitude of
	tutorials they've provided for the deep insight into t
	he low level of minecraft!
 */
package com.golem.skyblockutils.init.Tweaker;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

public class loadSBU implements ITweaker {

	@Nullable
	@Override
	public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
//		List<String> tweakClasses = (List<String>) Launch.blackboard.get("TweakClasses");
//		tweakClasses.add("org.spongepowered.asm.launch.MixinTweaker");
//		tweakClasses.add("com.golem.deps.io.github.notenoughupdates.moulconfig.tweaker.DevelopmentResourceTweaker");
		// Exercise for the reader: add delegation to the mixin tweaker
//		URL location = getClass().getProtectionDomain().getCodeSource().getLocation();
//		if (location == null) return;
//		if (!"file".equals(location.getProtocol())) return;
//		try {
//			// Add yourself as mixin container
//			MixinBootstrap.getPlatform().addContainer(location.toURI());
//			String file = new File(location.toURI()).getName();
//			// Remove yourself from both the ignore list in order to be eligible to be loaded as a mod.
//			Logger.debug("Loading SkyBlockUtils from " + file);
//			CoreModManager.getIgnoredMods().remove(file);
//			CoreModManager.getReparseableCoremods().add(file);
//		} catch (URISyntaxException e) {
//			Logger.error(e);
//		}
	}

	/**
	 * @param classLoader - Multiple functions and properties like transformers, cached classes etc.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void injectIntoClassLoader(LaunchClassLoader classLoader) {
//		tweakClasses.add(loadSBU.class.getName());
	}

	/**
	 * @return ""
	 */
	@Override
	public String getLaunchTarget() {
		return "";
	}

	/**
	 * @return ""
	 */
	@Override
	public String[] getLaunchArguments() {
		return new String[0];
	}

}

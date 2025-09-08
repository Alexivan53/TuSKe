package com.github.tukenuke.tuske.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import com.github.tukenuke.tuske.TuSKe;
import com.github.tukenuke.tuske.documentation.Dependency;
import com.github.tukenuke.tuske.util.ReflectionUtils;
import com.github.tukenuke.tuske.util.Registry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import javax.annotation.Nullable;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

@Name("Minecraft Version")
@Description("Returns the minecraft version of {{types|Player|player}}.")
@Examples({
		"on join:",
		"if minecraft version of player is \"1.9\":",
		"send \"You're joining with version %mc version of player%!\""})
@Since("1.0 (ProtocolSupport), 1.0.5 (ViaVersion)")
@Dependency("ProtocolSupport or ViaVersion")
public class ExprPlayerVersion extends SimplePropertyExpression<Player, String>{
	private static boolean hasViaVersion = false;
	private static boolean hasProtocolSupport = Bukkit.getServer().getPluginManager().isPluginEnabled("ProtocolSupport");
	private static Object api; //ViaVersion API instance

	static {
		JavaPlugin viaversion = (JavaPlugin) Bukkit.getServer().getPluginManager().getPlugin("ViaVersion");
		if (viaversion != null) {
			try {
				Class<?> via = Class.forName("com.viaversion.viaversion.api.Via");
				api = via.getMethod("getAPI").invoke(null);
				hasViaVersion = true;
			} catch (Throwable t) {
				try {
					api = viaversion.getClass().getMethod("getApi").invoke(viaversion);
					hasViaVersion = true;
				} catch (Throwable ignored) {
					TuSKe.log("Couldn't hook with ViaVersion API.", Level.WARNING);
				}
			}
		}
		if (hasViaVersion || hasProtocolSupport)
			Registry.newProperty(ExprPlayerVersion.class, "(mc|minecraft) version", "player");
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	@Nullable
	public String convert(Player p) {
		if (hasViaVersion){
			try {
				int proto;
				try {
					proto = (Integer) api.getClass().getMethod("getPlayerVersion", java.util.UUID.class).invoke(api, p.getUniqueId());
				} catch (NoSuchMethodException e) {
					proto = (Integer) api.getClass().getMethod("getPlayerVersion", Player.class).invoke(api, p);
				}
				try {
					Class<?> pv = Class.forName("com.viaversion.viaversion.api.protocol.version.ProtocolVersion");
					Object obj = pv.getMethod("getProtocol", int.class).invoke(null, proto);
					String name = (String) pv.getMethod("getName").invoke(obj);
					return name != null ? name.replace(".x", "") : null;
				} catch (ClassNotFoundException e) {
					Class<?> pv = Class.forName("us.myles.ViaVersion.api.protocol.ProtocolVersion");
					Object obj = pv.getMethod("getProtocol", int.class).invoke(null, proto);
					String name = (String) pv.getMethod("getName").invoke(obj);
					return name != null ? name.replace(".x", "") : null;
				}
			} catch (Throwable t) {
				TuSKe.log("ViaVersion reflection failed: " + t.getMessage(), Level.WARNING);
			}
		} else if (hasProtocolSupport)
			return protocolsupport.api.ProtocolSupportAPI.getProtocolVersion(p).getName();
		return null;
	}

	@Override
	protected String getPropertyName() {
		return "minecraft version";
	}

}

package com.github.tukenuke.tuske.expressions;

import com.github.tukenuke.tuske.util.Registry;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.event.Event;

import java.util.*;

import javax.annotation.Nullable;

import ch.njol.skript.command.Commands;
import ch.njol.skript.command.ScriptCommand;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.github.tukenuke.tuske.util.CommandUtils;
import com.github.tukenuke.tuske.util.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import java.io.File;

@Name("Command Info")
@Description("Return some info about a command, such as permission, description, usage, who registered it and where it is located.")
@Examples({"send command info of \"broadcast\""})
@Since("1.6.6")
public class ExprCommandInfo extends SimpleExpression<String> {
	static {
		Registry.newSimple(ExprCommandInfo.class, "command info of %string%", "command info %string%", "%string%\'s command info");
	}

	private Expression<String> e;
	private int id;
	@Override
	public boolean isSingle() {
		return true;
	}
	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean b) {
		return "command info of " + e.toString(event, b);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] expr, int mark, Kleenean kleenean, ParseResult parseResult) {
		e = (Expression<String>) expr[0];
		id = parseResult.regexes.size() > 0 ? Integer.valueOf(parseResult.regexes.get(0).group()) : 0;
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nullable
	protected String[] get(Event e) {
		String cmd = this.e.getSingle(e);
		if (cmd == null)
			return null;
		SimpleCommandMap map = ReflectionUtils.getField(Bukkit.getServer().getPluginManager().getClass(), Bukkit.getPluginManager(), "commandMap");
		if (map != null) {
			Command c = map.getCommand(cmd);
			if (c != null){
				switch (id){
					case 1: return new String[]{c.getPermission()};
					case 2: return new String[]{c.getDescription()};
					case 3: return new String[]{c.getUsage()};
					case 4:
						if (c.getPermission() == null)
							return new String[]{"Spigot"};
						if (c.getPermission().startsWith("minecraft"))
							return new String[]{"Minecraft"};
						break;
					case 5: return new String[]{c.getUsage() != null ? c.getUsage().replaceAll("^/?<command>", "/"+ c.getName()) : null};
					case 6: return c.getAliases().toArray(new String[c.getAliases().size()]);
				}
			}
		} else {
			Map<String, ScriptCommand> cmds = ReflectionUtils.getField(Commands.class, null, "commands");
			if (cmds == null)
				cmds = new HashMap<>();
			if (cmds.containsKey(cmd)){
				Object script = cmds.get(cmd).getScript();
				String result = null;
				try {
					File f = (File) script.getClass().getMethod("getFile").invoke(script);
					if (f != null)
						result = f.getAbsolutePath();
				} catch (Throwable ignored) {}
				if (result != null && result.toLowerCase().contains("scripts"))
					result = result.split("scripts")[1].substring(1);
				return new String[]{result};
			}
		}
		return null;
	}
}

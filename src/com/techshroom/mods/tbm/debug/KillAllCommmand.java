package com.techshroom.mods.tbm.debug;

import static com.techshroom.mods.tbm.TBMMod.mod;
import static com.techshroom.mods.tbm.Tutils.cast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class KillAllCommmand extends CommandBase {

    @Override
    public String getCommandName() {
        return "killall";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "/killall [entityNames...] | "
                + "Kills everything that isn't a player. Useful, I guess.";
    }

    @Override
    public void processCommand(ICommandSender p_71515_1_, String[] p_71515_2_) {
        mod().log.info(Arrays.toString(p_71515_2_));
        World w = p_71515_1_.getEntityWorld();
        List<Entity> loaded = cast(w.loadedEntityList);
        loaded = filter(loaded, p_71515_2_);
        for (Entity e : loaded) {
            if (e instanceof EntityPlayer) {
                continue;
            }
            p_71515_1_.addChatMessage(new ChatComponentText("Killed "
                    + e.getCommandSenderName()));
            w.removeEntity(e);
        }
    }

    private List<Entity> filter(List<Entity> loaded, String[] args) {
        List<Entity> copyForMod = new ArrayList<Entity>(loaded);
        if (args.length > 0) {
            List<Entity> temp = new ArrayList<Entity>(copyForMod.size() / 2);
            for (int i = 0; i < args.length; i++) {
                for (Entity entity : copyForMod) {
                    String simp = entity.getCommandSenderName();
                    if (simp.equals(args[i])) {
                        if (!temp.contains(entity)) {
                            temp.add(entity);
                        }
                    }
                }
            }
            copyForMod = temp;
        }
        return copyForMod;
    }

    @Override
    public List<?> addTabCompletionOptions(ICommandSender p_71516_1_,
            String[] p_71516_2_) {
        return g_addTabCompletionOptions(p_71516_1_, p_71516_2_);
    }

    private List<String> g_addTabCompletionOptions(ICommandSender sender,
            String[] args) {
        String prefix = args[args.length - 1];
        List<String> entitynames = new ArrayList<String>();
        World w = sender.getEntityWorld();
        List<Entity> loaded = cast(w.loadedEntityList);
        for (Entity entity : loaded) {
            if (!entitynames.contains(entity.getCommandSenderName())
                    && entity.getCommandSenderName().startsWith(prefix)
                    && !(entity instanceof EntityPlayer)) {
                entitynames.add(entity.getCommandSenderName());
            }
        }
        return entitynames;
    }
}

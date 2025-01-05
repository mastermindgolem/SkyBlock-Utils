package com.golem.skyblockutils.command.commands;

import com.golem.skyblockutils.models.AttributeItemType;
import com.golem.skyblockutils.models.AttributePrice;
import com.golem.skyblockutils.models.AuctionAttributeItem;
import com.golem.skyblockutils.utils.AttributeUtils;
import com.golem.skyblockutils.utils.ChatUtils;
import com.golem.skyblockutils.utils.ToolTipListener;
import logger.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.*;
import java.util.stream.Collectors;

import static com.golem.skyblockutils.Main.*;
import static com.golem.skyblockutils.models.AttributeItemType.*;
import static com.golem.skyblockutils.models.AttributePrice.AttributePrices;
import static com.golem.skyblockutils.utils.AuctionHouse.CheckIfAuctionsSearched;
import static com.golem.skyblockutils.utils.Colors.getRarityCode;

public class MoltenCommand extends CommandBase implements Help {
    private int level;

    private final List<String> helpStrings;

    private final AttributeItemType[] item_types = new AttributeItemType[] {MoltenNecklace, MoltenCloak, MoltenBelt, MoltenBracelet};

    public MoltenCommand() {
        helpStrings = new ArrayList<>();
    }

    @Override
    public Help getHelp() {
        return this;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public List<String> getHelpStrings() {
        return helpStrings;
    }

    @Override
    public List<String> getHoverStrings() {
        return Arrays.asList(
                EnumChatFormatting.BLUE + " ===================molten Equipment help menu!=================== ",
                EnumChatFormatting.RESET + "\n",
                example() + "                  Attribute price but for molten equipment                  ",
                EnumChatFormatting.RESET + "\n",
                EnumChatFormatting.GOLD + "/moltenprice help" +
                        EnumChatFormatting.GRAY + " ⬅ Shows this message.",
                EnumChatFormatting.RESET + "\n",
                EnumChatFormatting.RESET + "\n",
                EnumChatFormatting.GRAY + " " + EnumChatFormatting.STRIKETHROUGH + "/moltenprice ah W.I.P.",
                EnumChatFormatting.RESET + "\n",
                EnumChatFormatting.RESET + "\n",
                EnumChatFormatting.GOLD + "/moltenprice attribute [level]" +
                        EnumChatFormatting.GRAY  + " ⬅ Shows best price for attribute at any level, unless [level] is specified",
                EnumChatFormatting.RESET + "\n",
                example() + "E.g. /moltenprice veteran 1",
                EnumChatFormatting.RESET + "\n",
                EnumChatFormatting.RESET + "\n",
                EnumChatFormatting.GOLD + "/moltenprice attribute1 attribute2" +
                        EnumChatFormatting.GRAY + " ⬅ Shows cheapest equipment of specified combo",
                EnumChatFormatting.RESET + "\n",
                EnumChatFormatting.RESET + "\n",
                EnumChatFormatting.RED + "Legend:" +
                        EnumChatFormatting.RESET + "\n  " +
                        EnumChatFormatting.WHITE + "Attribute: veteran, mana_regeneration, mending" +
                        EnumChatFormatting.RESET + "\n  " +
                        EnumChatFormatting.WHITE + "Level: 1, 10, 5",
                EnumChatFormatting.RESET + "\n",
                EnumChatFormatting.BLUE + " ======================================================== "
        );
    }

    @Override
    public String getHelpMessage() {
        return
                EnumChatFormatting.GRAY + "▶ " +
                        EnumChatFormatting.GOLD + "/" +
                        EnumChatFormatting.BOLD + "moltenprice" +
                        EnumChatFormatting.RESET +
                        EnumChatFormatting.GOLD + "price attribute " +
                        EnumChatFormatting.AQUA +	"level" +
                        example() +	"(Aliases: /mp)" +
                        EnumChatFormatting.RESET + "\n";
    }

    @Override
    public void addHelpString() {
        helpStrings.add(getHelpMessage());
    }

    @Override
    public String getCommandName() {
        return "moltenprice";
    }

    @Override
    public List<String> getCommandAliases() {
        List<String> al = new ArrayList<>();
        al.add("mp");
        return al;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/moltenprice help for more information";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            StringBuilder sb = new StringBuilder();
            for (String str : getHoverStrings()) {
                sb.append(str);
            }
            String hover = sb.toString();
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(getHelpMessage()).setChatStyle(new ChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(hover)))));
        }

        //Check all tiers
        String attribute1;
        if (args.length == 1) {
            attribute1 = AttributeUtils.AttributeAliases(args[0]);

            if (CheckIfAuctionsSearched()) return;

            ChatUtils.addChatMessage(EnumChatFormatting.AQUA + "Auctions for " + EnumChatFormatting.BOLD + EnumChatFormatting.DARK_BLUE + attribute1.toUpperCase());
            getAttributePrice(attribute1, 0);

            mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + "[ARMOR]").setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/attributeprice " + attribute1) {
            })));
        }
        if (args.length == 2) {
            attribute1 = AttributeUtils.AttributeAliases(args[0]);
            boolean combo = false;
            try {
                level = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {
                combo = true;
            }
            Logger.debug("Checking " + auctions.size() + " auctions");
            if (auctions.size() == 0) {
                final IChatComponent msg = new ChatComponentText(EnumChatFormatting.RED + "Auctions not checked yet. If you have logged in more than 5 minutes ago, contact golem.");
                mc.thePlayer.addChatMessage(msg);
                return;
            }
            if (!combo) {
                if (CheckIfAuctionsSearched()) return;

                ChatUtils.addChatMessage(EnumChatFormatting.AQUA + "Auctions for " + EnumChatFormatting.BOLD + EnumChatFormatting.DARK_BLUE + attribute1.toUpperCase() + " " + level);
                getAttributePrice(attribute1, level);

                mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + "[ARMOR]").setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/attributeprice " + attribute1 + " " + level) {
                    @Override
                    public Action getAction() {
                        return Action.RUN_COMMAND;
                    }
                })));
            } else {
                String attribute2 = AttributeUtils.AttributeAliases(args[1]);
                ChatUtils.addChatMessage(EnumChatFormatting.AQUA + "Auctions for " + EnumChatFormatting.DARK_BLUE + attribute1.toUpperCase() + " " + attribute2.toUpperCase());
                Set<String> attributeSet = new HashSet<>();
                attributeSet.add(attribute1);
                attributeSet.add(attribute2);
                for (AttributeItemType key : item_types) {
                    AuctionAttributeItem item = AttributePrice.getComboItem(key, attributeSet);
                    if (item == null) continue;
                    ChatUtils.addChatMessage(getRarityCode(item.tier) + item.item_name + " - " + EnumChatFormatting.GREEN + coolFormat(item.price, 0), new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/viewauction " + item.viewauctionID), getRarityCode(item.tier) + item.item_lore);
                }
                mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + "[ARMOR]").setChatStyle(new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/attributeprice " + attribute1 + " " + attribute2) {
                })));
            }
        }
    }

    public String example() {
        return EnumChatFormatting.GRAY + " " + EnumChatFormatting.ITALIC;
    }



    public void getAttributePrice(String attribute, int level) {
        for (AttributeItemType key : item_types) {
            if (!AttributePrices.get(key).containsKey(attribute)) continue;
            ArrayList<AuctionAttributeItem> items = AttributePrices.get(key).get(attribute);
            if (items == null) continue;
            if (level == 0) {
                items = items.stream()
                        .filter(item -> item.attributes.get(attribute) >= configFile.minArmorTier)
                        .collect(Collectors.toCollection(ArrayList::new));
            } else {
                items = items.stream()
                        .filter(item -> item.attributes.get(attribute) == level)
                        .collect(Collectors.toCollection(ArrayList::new));
            }

            items.sort(Comparator.comparingDouble((AuctionAttributeItem o) -> o.attributeInfo.get(attribute).price_per));
            ChatUtils.addChatMessage(EnumChatFormatting.AQUA + ToolTipListener.TitleCase(key.getDisplay()));
            if (items.size() < 5) {
                for (AuctionAttributeItem item: items) {
                    ChatUtils.addChatMessage(getRarityCode(item.tier) + item.item_name + " - " + EnumChatFormatting.YELLOW + coolFormat(item.attributeInfo.get(attribute).price_per, 0) + EnumChatFormatting.RESET + " - " + EnumChatFormatting.GREEN + coolFormat(item.price,0), new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/viewauction " + item.viewauctionID), getRarityCode(item.tier) + item.item_lore);
                }
            } else {
                for (int i = 0; i < 5; i++) {
                    AuctionAttributeItem item = items.get(i);
                    ChatUtils.addChatMessage(getRarityCode(item.tier) + item.item_name + " - " + EnumChatFormatting.YELLOW + coolFormat(item.attributeInfo.get(attribute).price_per, 0) + EnumChatFormatting.RESET + " - " + EnumChatFormatting.GREEN + coolFormat(item.price,0), new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/viewauction " + item.viewauctionID), getRarityCode(item.tier) + item.item_lore);
                }
            }
        }
    }
}
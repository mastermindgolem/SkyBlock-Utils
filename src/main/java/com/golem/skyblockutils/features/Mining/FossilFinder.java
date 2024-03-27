package com.golem.skyblockutils.features.Mining;

import com.golem.skyblockutils.utils.LocationUtils;
import com.golem.skyblockutils.utils.RenderUtils;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FossilFinder {

    private static boolean started = false;
    private HashMap<Integer, Boolean> clickedSlots = new HashMap<>();
    private int lastClicked = -1;
    private boolean fossilFound = false;
    private List<char[][]> grids = new ArrayList<>();
    private List<char[][]> possibleGrids = new ArrayList<>();

    @SubscribeEvent
    public void guiOpenEvent(GuiOpenEvent event) {
        if (!(event.gui instanceof GuiChest)) return;
        possibleGrids = grids;
        clickedSlots.clear();
        fossilFound = false;
    }

    @SubscribeEvent
    public void onGui(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (!(event.gui instanceof GuiChest) || !Objects.equals(LocationUtils.getLocation(), "mining_3")) return;
        GuiChest gui = (GuiChest) event.gui;
        Container container = gui.inventorySlots;
        String chestName = ((ContainerChest) container).getLowerChestInventory().getDisplayName().getUnformattedText();
        if (!Objects.equals(chestName, "Fossil Excavator")) return;

        int highlightSlot = -1;
        List<Slot> slots = container.inventorySlots;
        if (slots.get(49).getHasStack() && slots.get(49).getStack().getDisplayName().equals("§cClose")) {
            return;
        }
        if (slots.size() > 54) slots = slots.subList(0, 54);
        if (!started) onStart();
        lastClicked = -1;
        for (Slot slot : slots) {
            int slotIndex = slot.getSlotIndex();
            boolean hasStack = slot.getHasStack();
            if (hasStack) {
                String displayName = slot.getStack().getDisplayName();
                if (displayName.equals("§6Dirt")) clickedSlots.remove(slotIndex);
                else if (displayName.equals("§6Fossil")) {
                    fossilFound = true;
                    if (!clickedSlots.containsKey(slotIndex)) lastClicked = slotIndex;
                    clickedSlots.put(slotIndex, true);
                } else {
                    if (!clickedSlots.containsKey(slotIndex)) lastClicked = slotIndex;
                    clickedSlots.put(slotIndex, false);
                }
            } else {
                if (!clickedSlots.containsKey(slotIndex)) lastClicked = slotIndex;
                clickedSlots.put(slotIndex, false);
            }
        }

        if (lastClicked >= 0) {
            possibleGrids = possibleGrids.stream()
                    .filter(grid -> clickedSlots.entrySet().stream()
                            .allMatch(entry -> {
                                int index = entry.getKey();
                                int row = index / 9;
                                int col = index % 9;
                                return grid[row][col] == '#' == entry.getValue();
                            }))
                    .collect(Collectors.toList());
        }
        fossilFound = !possibleGrids.isEmpty();
        if (!possibleGrids.isEmpty()) {
            int[][] board = new int[6][9];
            int maxCount = -1;
            for (char[][] grid : possibleGrids) {
                for (int i = 0; i < 6; ++i) {
                    for (int j = 0; j < 9; ++j) {
                        if (grid[i][j] == '#' && !clickedSlots.containsKey(9 * i + j)) {
                            board[i][j]++;
                            if (board[i][j] > maxCount) {
                                maxCount = board[i][j];
                                highlightSlot = 9 * i + j;
                            }
                        }
                    }
                }
            }
        }

        if (highlightSlot >= 0) {
            RenderUtils.highlight(Color.GREEN, gui, container.getSlot(highlightSlot));
        }

    }

    public void onStart() {
        started = true;
        String[] FOSSIL1 = {"0001000", "1001001", "0101010", "0011100"};
        String[] FOSSIL2 = {"00001", "01001", "10001", "01010", "00100"};
        String[] FOSSIL3 = {"111111", "011110", "001100"};
        String[] FOSSIL4 = {"010100", "101010", "010110", "001111", "000010"};
        String[] FOSSIL5 = {"01111000", "10000100", "01000011", "00000011"};
        String[] FOSSIL6 = {"10101", "10101", "01110", "01110", "00100"};
        String[] FOSSIL7 = {"11111", "10001", "10101", "10111"};
        String[] FOSSIL8 = {"011110", "111111", "011110", "001100"};

        String[][] FOSSILS = {FOSSIL1, FOSSIL2, FOSSIL3, FOSSIL4, FOSSIL5, FOSSIL6, FOSSIL7, FOSSIL8};

        for (String[] fossil : FOSSILS) {
            List<char[][]> positions = generateAllPositions(fossil);
            for (char[][] position : positions) {
                for (int i = 0; i < 7 - position.length; i++) {
                    for (int j = 0; j < 10 - position[0].length; j++) {
                        char[][] grid = new char[6][9];
                        placeFossil(grid, position, i, j);
                        grids.add(grid);
                    }
                }
            }
        }
    }

    private static List<char[][]> generateAllPositions(String[] fossil) {
        List<char[][]> positions = new ArrayList<>();
        char[][] currentFossil = new char[fossil.length][fossil[0].length()];
        for (int i = 0; i < fossil.length; i++) {
            currentFossil[i] = fossil[i].toCharArray();
        }
        for (int i = 0; i < 4; i++) {
            positions.add(currentFossil);
            currentFossil = rotateFossil(currentFossil);
        }
        return positions;
    }

    private static char[][] rotateFossil(char[][] fossil) {
        int rows = fossil.length;
        int cols = fossil[0].length;
        char[][] rotatedFossil = new char[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotatedFossil[j][rows - 1 - i] = fossil[i][j];
            }
        }
        return rotatedFossil;
    }

    private static void placeFossil(char[][] grid, char[][] fossil, int row, int col) {
        for (int i = 0; i < fossil.length; i++) {
            for (int j = 0; j < fossil[0].length; j++) {
                if (fossil[i][j] == '1') {
                    grid[row + i][col + j] = '#';
                }
            }
        }
    }

}

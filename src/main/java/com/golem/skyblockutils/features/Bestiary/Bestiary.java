package com.golem.skyblockutils.features.Bestiary;

import java.util.*;


public class Bestiary {

    public static int romanToInt(String s) {
        Map<Character, Integer> romanValues = new HashMap<>();
        romanValues.put('I', 1);
        romanValues.put('V', 5);
        romanValues.put('X', 10);
        romanValues.put('L', 50);
        romanValues.put('C', 100);
        romanValues.put('D', 500);
        romanValues.put('M', 1000);

        int result = 0;
        int prevValue = 0;

        for (int i = s.length() - 1; i >= 0; i--) {
            int currentValue = romanValues.get(s.charAt(i));
            if (currentValue >= prevValue) {
                result += currentValue;
            } else {
                result -= currentValue;
            }
            prevValue = currentValue;
        }

        return result;
    }

    public static final int[][] killBrackets = {
            {20, 40, 60, 100, 200, 400, 800, 1400, 2000, 3000, 6000, 12000, 20000, 30000, 40000, 50000, 60000, 72000, 86000, 100000, 200000, 400000, 600000, 800000, 1000000},
            {5, 10, 15, 25, 50, 100, 200, 350, 500, 750, 1500, 3000, 5000, 7500, 10000, 12500, 15000, 18000, 21500, 25000, 50000, 100000, 150000, 200000, 250000},
            {4, 8, 12, 16, 20, 40, 80, 140, 200, 300, 600, 1200, 2000, 3000, 4000, 5000, 6000, 7200, 8600, 10000, 20000, 40000, 60000, 80000, 100000},
            {2, 4, 6, 10, 15, 20, 25, 35, 50, 75, 150, 300, 500, 750, 1000, 1350, 1650, 2000, 2500, 3000, 5000, 10000, 15000, 20000, 25000},
            {1, 2, 3, 5, 7, 10, 20, 25, 30, 60, 120, 200, 300, 400, 500, 600, 720, 860, 1000, 2000, 4000, 6000, 8000, 10000},
            {1, 2, 3, 5, 7, 9, 14, 17, 21, 25, 50, 80, 125, 175, 250, 325, 425, 525, 625, 750, 1500, 3000, 4500, 6000, 7500},
            {1, 2, 3, 5, 7, 9, 11, 14, 17, 20, 30, 40, 55, 75, 100, 150, 200, 275, 375, 500, 1000, 1500, 2000, 2500, 3000}
    };

    public static Map<String, Mob> bestiary = new HashMap<String, Mob>() {{
        put("Creeper", new Mob(Collections.singletonList("creeper"), Arrays.asList(1), 1, 5, 5));
        put("Enderman", new Mob(Collections.singletonList("enderman"), Arrays.asList(1, 15), 1, 5, 5));
        put("Skeleton", new Mob(Collections.singletonList("skeleton"), Arrays.asList(1, 15), 1, 5, 5));
        put("Slime", new Mob(Collections.singletonList("slime"), Arrays.asList(1), 1, 5, 5));
        put("Spider", new Mob(Collections.singletonList("spider"), Arrays.asList(1, 15), 1, 5, 5));
        put("Witch", new Mob(Collections.singletonList("witch"), Arrays.asList(1, 15), 1, 5, 60));
        put("Zombie", new Mob(Collections.singletonList("zombie"), Arrays.asList(1, 15), 1, 5, 5));
        put("Crypt Ghoul", new Mob(Collections.singletonList("unburried_zombie"), Arrays.asList(30), 1, 15, 0.5));
        put("Golden Ghoul", new Mob(Collections.singletonList("unburried_zombie"), Arrays.asList(60), 3, 15, 5));
        put("Graveyard Zombie", new Mob(Collections.singletonList("graveyard_zombie"), Arrays.asList(1), 1, 5, 0.5));
        put("Old Wolf", new Mob(Collections.singletonList("old_wolf"), Arrays.asList(50), 3, 15, 10));
        put("Wolf", new Mob(Collections.singletonList("ruin_wolf"), Arrays.asList(15), 1, 15, 0.5));
        put("Zombie Villager", new Mob(Collections.singletonList("zombie_villager"), Arrays.asList(1), 4, 15, 1));
        put("Chicken", new Mob(Collections.singletonList("farming_chicken"), Arrays.asList(1), 1, 5, 0.5));
        put("Cow", new Mob(Collections.singletonList("farming_cow"), Arrays.asList(1), 1, 5, 0.5));
        put("Mushroom Cow", new Mob(Collections.singletonList("mushroom_cow"), Arrays.asList(1), 1, 5, 3));
        put("Pig", new Mob(Collections.singletonList("farming_pig"), Arrays.asList(1), 1, 5, 0.5));
        put("Rabbit", new Mob(Collections.singletonList("farming_rabbit"), Arrays.asList(1), 1, 5, 3));
        put("Sheep", new Mob(Collections.singletonList("farming_sheep"), Arrays.asList(1), 1, 5, 3));
        put("Arachne", new Mob(Collections.singletonList("arachne"), Arrays.asList(300, 500), 7, 20, 30));
        put("Arachne's Brood", new Mob(Collections.singletonList("arachne_brood"), Arrays.asList(100, 200), 4, 15, 3));
        put("Arachne's Keeper", new Mob(Collections.singletonList("arachne_keeper"), Arrays.asList(100), 5, 15, 30));
        put("Brood Mother", new Mob(Collections.singletonList("brood_mother_spider"), Arrays.asList(12), 5, 15, 60));
        put("Dasher Spider", new Mob(Collections.singletonList("dasher_spider"), Arrays.asList(4, 42, 45, 50), 2, 15, 1));
        put("Gravel Skeleton", new Mob(Collections.singletonList("respawning_skeleton"), Arrays.asList(2), 3, 15, 1));
        put("Rain Slime", new Mob(Collections.singletonList("random_slime"), Arrays.asList(8), 4, 15, 5));
        put("Silverfish", new Mob(Arrays.asList("jockey_shot_silverfish", "splitter_spider_silverfish"), Arrays.asList(2, 3, 42, 45, 50), 1, 15, 1));
        put("Spider Jockey", new Mob(Collections.singletonList("spider_jockey"), Arrays.asList(3, 42), 2, 15, 1));
        put("Splitter Spider", new Mob(Collections.singletonList("splitter_spider"), Arrays.asList(4, 42, 45, 50), 2, 15, 1));
        put("Voracious Spider", new Mob(Collections.singletonList("voracious_spider"), Arrays.asList(10, 42, 45, 50), 1, 15, 1));
        put("Weaver Spider", new Mob(Collections.singletonList("weaver_spider"), Arrays.asList(3, 42, 45, 50), 2, 15, 1));
        put("Dragon", new Mob(Arrays.asList("unstable_dragon", "strong_dragon", "superior_dragon", "wise_dragon", "young_dragon", "old_dragon", "protector_dragon"), Arrays.asList(100), 5, 20, 45));
        put("Enderman", new Mob(Collections.singletonList("enderman"), Arrays.asList(42, 45, 50), 4, 25, 0.5));
        put("Endermite", new Mob(Arrays.asList("endermite", "nest_endermite"), Arrays.asList(37, 40, 50), 5, 25, 5));
        put("Endstone Protector", new Mob(Collections.singletonList("corrupted_protector"), Arrays.asList(100), 7, 20, 120));
        put("Obsidian Defender", new Mob(Collections.singletonList("obsidian_wither"), Arrays.asList(55), 4, 25, 2));
        put("Voidling Extremist", new Mob(Collections.singletonList("voidling_extremist"), Arrays.asList(100), 3, 15, 6));
        put("Voidling Fanatic", new Mob(Collections.singletonList("voidling_fanatic"), Arrays.asList(85), 4, 25, 1.5));
        put("Watcher", new Mob(Collections.singletonList("watcher"), Arrays.asList(55), 4, 25, 2));
        put("Zealot", new Mob(Arrays.asList("zealot_enderman", "zealot_bruiser"), Arrays.asList(55, 100), 3, 25, 3));
        put("Ashfang", new Mob(Collections.singletonList("ashfang"), Arrays.asList(200), 5, 20, 90));
        put("Barbarian Duke X", new Mob(Collections.singletonList("barbarian_duke_x"), Arrays.asList(200), 5, 20, 45));
        put("Bladesoul", new Mob(Collections.singletonList("bladesoul"), Arrays.asList(200), 5, 20, 45));
        put("Blaze", new Mob(Arrays.asList("blaze", "bezal", "mutated_blaze"), Arrays.asList(25, 70, 80, 70), 4, 20, 3));
        put("Flaming Spider", new Mob(Collections.singletonList("flaming_spider"), Arrays.asList(80), 3, 20, 3));
        put("Flare", new Mob(Collections.singletonList("flare"), Arrays.asList(90), 1, 20, 0.5));
        put("Ghast", new Mob(Arrays.asList("ghast", "dive_ghast"), Arrays.asList(85, 90), 4, 20, 4));
        put("Mage Outlaw", new Mob(Collections.singletonList("mage_outlaw"), Arrays.asList(200), 5, 20, 45));
        put("Magma Boss", new Mob(Collections.singletonList("magma_boss"), Arrays.asList(500), 5, 20, 90));
        put("Magma Cube", new Mob(Arrays.asList("magma_cube", "pack_magma_cube"), Arrays.asList(75, 90), 3, 20, 2));
        put("Matcho", new Mob(Collections.singletonList("matcho"), Arrays.asList(100), 5, 15, 30));
        put("Millenia-Aged Blaze", new Mob(Collections.singletonList("old_blaze"), Arrays.asList(110), 3, 15, 10));
        put("Mushroom Bull", new Mob(Collections.singletonList("charging_mushroom_cow"), Arrays.asList(80), 3, 20, 1));
        put("Pigman", new Mob(Arrays.asList("magma_cube_rider", "kada_knight"), Arrays.asList(90), 3, 20, 2));
        put("Smoldering Blaze", new Mob(Collections.singletonList("smoldering_blaze"), Arrays.asList(95), 2, 20, 2));
        put("Tentacle", new Mob(Arrays.asList("tentacle", "hellwisp"), Arrays.asList(1, 100), 5, 20, 15));
        put("Vanquisher", new Mob(Collections.singletonList("vanquisher"), Arrays.asList(100), 5, 20, 180));
        put("Wither Skeleton", new Mob(Collections.singletonList("wither_skeleton"), Arrays.asList(70), 3, 20, 3));
        put("Wither Spectre", new Mob(Collections.singletonList("wither_spectre"), Arrays.asList(70), 3, 20, 1.5));
        put("Emerald Slime", new Mob(Collections.singletonList("emerald_slime"), Arrays.asList(5, 10, 15), 1, 10, 1));
        put("Lapis Zombie", new Mob(Collections.singletonList("lapis_zombie"), Arrays.asList(7), 1, 10, 1));
        put("Miner Skeleton", new Mob(Collections.singletonList("diamond_skeleton"), Arrays.asList(15, 20), 1, 10, 1));
        put("Miner Zombie", new Mob(Collections.singletonList("diamond_zombie"), Arrays.asList(15, 20), 1, 10, 1));
        put("Redstone Pigman", new Mob(Collections.singletonList("redstone_pigman"), Arrays.asList(10), 1, 10, 1));
        put("Sneaky Creeper", new Mob(Collections.singletonList("invisible_creeper"), Arrays.asList(3), 3, 10, 2));
        put("Ghost", new Mob(Collections.singletonList("caverns_ghost"), Arrays.asList(250), 2, 25, 0.5));
        put("Goblin", new Mob(Arrays.asList("goblin_weakling_melee", "goblin_weakling_bow", "goblin_creepertamer", "goblin_battler", "goblin_knife_thrower", "goblin_flamethrower", "goblin_murderlover"), Arrays.asList(25, 40, 50, 70, 100, 200), 2, 20, 0.5));
        put("Goblin Raider", new Mob(Arrays.asList("goblin_weakling_melee", "goblin_weakling_bow", "goblin_creepertamer", "goblin_creeper", "goblin_battler", "goblin_murderlover", "goblin_golem"), Arrays.asList(5, 20, 60, 90, 150), 4, 15, 5));
        put("Golden Goblin", new Mob(Collections.singletonList("goblin"), Arrays.asList(50), 5, 15, 15));
        put("Ice Walker", new Mob(Collections.singletonList("ice_walker"), Arrays.asList(45), 2, 15, 3));
        put("Powder Ghast", new Mob(Collections.singletonList("powder_ghast"), Arrays.asList(1), 1, 5, 180));
        put("Star Sentry", new Mob(Collections.singletonList("crystal_sentry"), Arrays.asList(50), 4, 15, 30));
        put("Treasure Hoarder", new Mob(Collections.singletonList("treasure_hoarder"), Arrays.asList(70), 3, 15, 5));
        put("Automaton", new Mob(Collections.singletonList("automaton"), Arrays.asList(100, 150), 2, 15, 2));
        put("Bal", new Mob(Collections.singletonList("bal_boss"), Arrays.asList(100), 6, 15, 90));
        put("Butterfly", new Mob(Collections.singletonList("butterfly"), Arrays.asList(100), 4, 15, 15));
        put("Grunt", new Mob(Arrays.asList("team_treasurite_grunt", "team_treasurite_viper", "team_treasurite_wendy", "team_treasurite_sebastian", "team_treasurite_corleone"), Arrays.asList(50, 100, 200), 3, 15, 5));
        put("Key Guardian", new Mob(Collections.singletonList("key_guardian"), Arrays.asList(100), 6, 15, 45));
        put("Sludge", new Mob(Collections.singletonList("sludge"), Arrays.asList(5, 10, 100), 3, 15, 3));
        put("Thyst", new Mob(Collections.singletonList("thyst"), Arrays.asList(20), 3, 15, 5));
        put("Worm", new Mob(Arrays.asList("worm", "scatha"), Arrays.asList(5, 10), 5, 15, 60));
        put("Yog", new Mob(Collections.singletonList("yog"), Arrays.asList(100), 3, 15, 15));
        put("Howling Spirit", new Mob(Collections.singletonList("howling_spirit"), Arrays.asList(35), 2, 15, 2));
        put("Pack Spirit", new Mob(Collections.singletonList("pack_spirit"), Arrays.asList(30), 2, 15, 2));
        put("Soul of the Alpha", new Mob(Collections.singletonList("soul_of_the_alpha"), Arrays.asList(55), 4, 15, 6));
        put("Crazy Witch", new Mob(Collections.singletonList("batty_witch"), Arrays.asList(60), 2, 10, 10));
        put("Headless Horseman", new Mob(Collections.singletonList("horseman_horse"), Arrays.asList(100), 7, 20, 30));
        put("Phantom Spirit", new Mob(Collections.singletonList("phantom_spirit"), Arrays.asList(35), 2, 10, 10));
        put("Scary Jerry", new Mob(Collections.singletonList("scary_jerry"), Arrays.asList(30), 2, 10, 10));
        put("Trick or Treater", new Mob(Collections.singletonList("trick_or_treater"), Arrays.asList(30), 2, 10, 10));
        put("Wither Gourd", new Mob(Collections.singletonList("wither_gourd"), Arrays.asList(40), 2, 10, 10));
        put("Wraith", new Mob(Collections.singletonList("wraith"), Arrays.asList(50), 2, 10, 10));
        put("Angry Archeologist", new Mob(Arrays.asList("diamond_guy", "master_diamond_guy"), Arrays.asList(80, 90, 100, 110, 120, 130, 140, 150, 160, 170), 5, 25, 30));
        put("Bat", new Mob(Collections.singletonList("dungeon_secret_bat"), Arrays.asList(1), 4, 15, 180));
        put("Cellar Spider", new Mob(Arrays.asList("cellar_spider", "master_cellar_spider"), Arrays.asList(45, 65, 75, 85, 95, 105, 115, 125), 4, 15, 60));
        put("Crypt Dreadlord", new Mob(Arrays.asList("crypt_dreadlord", "master_cryptdreadlord"), Arrays.asList(47, 67, 77, 87, 97, 107, 117, 127), 4, 25, 5));
        put("Crypt Lurker", new Mob(Arrays.asList("crypt_lurker", "master_crypt_lurker"), Arrays.asList(41, 61, 71, 81, 91, 101, 111, 121), 4, 25, 5));
        put("Crypt Souleater", new Mob(Arrays.asList("crypt_souleater", "master_crypt_souleater"), Arrays.asList(45, 65, 75, 85, 95, 105, 115, 125), 4, 25, 5));
        put("Fels", new Mob(Arrays.asList("tentaclees", "master_tentaclees"), Arrays.asList(90, 100, 110), 4, 25, 10));
        put("Golem", new Mob(Arrays.asList("sadan_golem", "master_sadan_golem"), Arrays.asList(1), 4, 15, 45));
        put("King Midas", new Mob(Arrays.asList("king_midas", "master_king_midas"), Arrays.asList(130, 140, 150, 160, 170), 5, 10, 600));
        put("Lonely Spider", new Mob(Arrays.asList("lonely_spider", "master_lonely_spider"), Arrays.asList(35, 55, 65, 75, 85, 95, 105, 115), 4, 25, 15));
        put("Lost Adventurer", new Mob(Arrays.asList("lost_adventurer", "master_lost_adventurer"), Arrays.asList(80, 85, 90, 100, 110, 120, 130, 140, 150, 160), 5, 25, 30));
        put("Mimic", new Mob(Arrays.asList("mimic", "master_mimic"), Arrays.asList(115, 125), 4, 15, 300));
        put("Scared Skeleton", new Mob(Arrays.asList("scared_skeleton", "master_scared_skeleton"), Arrays.asList(42, 60, 62, 70, 72), 3, 15, 5));
        put("Shadow Assassin", new Mob(Arrays.asList("shadow_assassin", "master_shadow_assassin"), Arrays.asList(120, 130, 140, 150, 160, 170, 171), 5, 25, 30));
        put("Skeleton Grunt", new Mob(Arrays.asList("skeleton_grunt", "master_skeleton_grunt"), Arrays.asList(40, 60, 70, 80), 3, 15, 5));
        put("Skeleton Lord", new Mob(Arrays.asList("skeleton_lord", "master_skeleton_lord"), Arrays.asList(150), 5, 20, 10));
        put("Skeleton Master", new Mob(Arrays.asList("skeleton_master", "master_skeleton_master"), Arrays.asList(78, 88, 98, 108, 118, 128), 4, 25, 5));
        put("Skeleton Soldier", new Mob(Arrays.asList("skeleton_soldier", "master_skeleton_soldier"), Arrays.asList(66, 76, 86, 96, 106, 116, 126), 1, 15, 5));
        put("Skeletor", new Mob(Arrays.asList("skeletor", "skeletor_prime", "master_skeletor", "master_skeletor_prime"), Arrays.asList(80, 90, 100, 110), 5, 25, 5));
        put("Sniper", new Mob(Arrays.asList("sniper_skeleton", "master_sniper_skeleton"), Arrays.asList(43, 63, 73, 83, 93, 103, 113, 123), 3, 15, 60));
        put("Super Archer", new Mob(Arrays.asList("super_archer", "master_super_archer"), Arrays.asList(90, 100, 110, 120), 5, 25, 30));
        put("Super Tank Zombie", new Mob(Arrays.asList("super_tank_zombie", "master_super_tank_zombie"), Arrays.asList(90, 100, 110, 120), 4, 25, 5));
        put("Tank Zombie", new Mob(Arrays.asList("crypt_tank_zombie", "master_crypt_tank_zombie"), Arrays.asList(40, 60, 70, 80, 90), 3, 15, 5));
        put("Terracotta", new Mob(Arrays.asList("sadan_statue", "master_sadan_statue"), Arrays.asList(1), 1, 15, 3));
        put("Undead", new Mob(Arrays.asList("watcher_summon_undead", "master_watcher_summon_undead"), Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), 2, 15, 10));
        put("Undead Skeleton", new Mob(Arrays.asList("dungeon_respawning_skeleton", "master_dungeon_respawning_skeleton"), Arrays.asList(40, 60, 61, 70, 71, 80, 81, 90, 91, 100, 101, 110, 111, 120), 4, 25, 5));
        put("Wither Guard", new Mob(Arrays.asList("wither_guard", "master_wither_guard"), Arrays.asList(100), 5, 25, 5));
        put("Wither Husk", new Mob(Collections.singletonList("master_wither_husk"), Arrays.asList(100), 5, 25, 5));
        put("Wither Miner", new Mob(Arrays.asList("wither_miner", "master_wither_miner"), Arrays.asList(100), 4, 25, 5));
        put("Withermancer", new Mob(Arrays.asList("crypt_witherskeleton", "master_crypt_witherskeleton"), Arrays.asList(90, 100, 110, 120), 4, 25, 5));
        put("Zombie Commander", new Mob(Arrays.asList("zombie_commander", "master_zombie_commander"), Arrays.asList(110, 120), 4, 20, 15));
        put("Zombie Grunt", new Mob(Arrays.asList("zombie_grunt", "master_zombie_grunt"), Arrays.asList(40, 60, 70), 3, 15, 5));
        put("Zombie Knight", new Mob(Arrays.asList("zombie_knight", "master_zombie_knight"), Arrays.asList(86, 96, 106, 116, 126), 4, 25, 5));
        put("Zombie Lord", new Mob(Arrays.asList("zombie_lord", "master_zombie_lord"), Arrays.asList(150), 5, 20, 15));
        put("Zombie Soldier", new Mob(Arrays.asList("zombie_soldier", "master_zombie_soldier"), Arrays.asList(83, 93, 103, 113, 123), 1, 15, 5));
        put("Squid", new Mob(Collections.singletonList("pond_squid"), Arrays.asList(1), 2, 15, 7));
        put("Night Squid", new Mob(Collections.singletonList("night_squid"), Arrays.asList(6), 4, 15, 20));
        put("Sea Walker", new Mob(Collections.singletonList("sea_walker"), Arrays.asList(4), 3, 15, 20));
        put("Sea Guardian", new Mob(Collections.singletonList("sea_guardian"), Arrays.asList(10), 3, 15, 20));
        put("Sea Witch", new Mob(Collections.singletonList("sea_witch"), Arrays.asList(15), 3, 15, 20));
        put("Sea Archer", new Mob(Collections.singletonList("sea_archer"), Arrays.asList(15), 3, 15, 20));
        put("Rider of the Deep", new Mob(Collections.singletonList("zombie_deep"), Arrays.asList(20), 3, 15, 20));
        put("Catfish", new Mob(Collections.singletonList("catfish"), Arrays.asList(23), 4, 15, 20));
        put("Carrot King", new Mob(Collections.singletonList("carrot_king"), Arrays.asList(25), 5, 15, 30));
        put("Sea Leech", new Mob(Collections.singletonList("sea_leech"), Arrays.asList(30), 4, 15, 20));
        put("Guardian Defender", new Mob(Collections.singletonList("guardian_defender"), Arrays.asList(45), 4, 15, 20));
        put("Deep Sea Protector", new Mob(Collections.singletonList("deep_sea_protector"), Arrays.asList(60), 4, 15, 20));
        put("Water Hydra", new Mob(Collections.singletonList("water_hydra"), Arrays.asList(100), 5, 15, 180));
        put("The Sea Emperor", new Mob(Collections.singletonList("skeleton_emperor"), Arrays.asList(150), 7, 15, 300));
        put("Oasis Rabbit", new Mob(Collections.singletonList("oasis_rabbit"), Arrays.asList(10), 4, 10, 15));
        put("Oasis Sheep", new Mob(Collections.singletonList("oasis_sheep"), Arrays.asList(10), 4, 10, 15));
        put("Water Worm", new Mob(Collections.singletonList("water_worm"), Arrays.asList(20), 4, 15, 20));
        put("Poisoned Water Worm", new Mob(Collections.singletonList("poisoned_water_worm"), Arrays.asList(25), 4, 15, 20));
        put("Zombie Miner", new Mob(Collections.singletonList("zombie_miner"), Arrays.asList(150), 6, 15, 60));
        put("Scarecrow", new Mob(Collections.singletonList("scarecrow"), Arrays.asList(9), 3, 15, 15));
        put("Nightmare", new Mob(Collections.singletonList("nightmare"), Arrays.asList(24), 4, 15, 15));
        put("Werewolf", new Mob(Collections.singletonList("werewolf"), Arrays.asList(50), 4, 15, 30));
        put("Phantom Fisherman", new Mob(Collections.singletonList("phantom_fisherman"), Arrays.asList(160), 6, 15, 60));
        put("Grim Reaper", new Mob(Collections.singletonList("grim_reaper"), Arrays.asList(190), 7, 15, 300));
        put("Frozen Steve", new Mob(Collections.singletonList("frozen_steve"), Arrays.asList(7), 3, 15, 15));
        put("Frosty The Snowman", new Mob(Collections.singletonList("frosty_the_snowman"), Arrays.asList(13), 3, 15, 15));
        put("Grinch", new Mob(Collections.singletonList("grinch"), Arrays.asList(21), 6, 15, 120));
        put("Yeti", new Mob(Collections.singletonList("yeti"), Arrays.asList(175), 6, 15, 300));
        put("Nutcracker", new Mob(Collections.singletonList("nutcracker"), Arrays.asList(50), 5, 15, 15));
        put("Reindrake", new Mob(Collections.singletonList("reindrake"), Arrays.asList(100), 7, 15, 1800));
        put("Nurse Shark", new Mob(Collections.singletonList("nurse_shark"), Arrays.asList(6), 3, 15, 15));
        put("Blue Shark", new Mob(Collections.singletonList("blue_shark"), Arrays.asList(20), 4, 15, 30));
        put("Tiger Shark", new Mob(Collections.singletonList("tiger_shark"), Arrays.asList(50), 4, 15, 45));
        put("Great White Shark", new Mob(Collections.singletonList("great_white_shark"), Arrays.asList(180), 5, 15, 90));
        put("Plhlegblast", new Mob(Collections.singletonList("pond_squid"), Arrays.asList(300), 7, 5, 1800));
        put("Magma Slug", new Mob(Collections.singletonList("magma_slug"), Arrays.asList(200), 2, 15, 15));
        put("Moogma", new Mob(Collections.singletonList("moogma"), Arrays.asList(210), 3, 15, 15));
        put("Lava Leech", new Mob(Collections.singletonList("lava_leech"), Arrays.asList(220), 3, 15, 15));
        put("Pyroclastic Worm", new Mob(Collections.singletonList("pyroclastic_worm"), Arrays.asList(240), 3, 15, 15));
        put("Lava Flame", new Mob(Collections.singletonList("lava_flame"), Arrays.asList(230), 4, 15, 15));
        put("Fire Eel", new Mob(Collections.singletonList("fire_eel"), Arrays.asList(240), 4, 15, 15));
        put("Taurus", new Mob(Collections.singletonList("pig_rider"), Arrays.asList(250), 4, 15, 15));
        put("Thunder", new Mob(Collections.singletonList("thunder"), Arrays.asList(400), 5, 15, 300));
        put("Lord Jawbus", new Mob(Collections.singletonList("lord_jawbus"), Arrays.asList(600), 6, 15, 600));
        put("Flaming Worm", new Mob(Collections.singletonList("flaming_worm"), Arrays.asList(50), 3, 15, 10));
        put("Lava Blaze", new Mob(Collections.singletonList("lava_blaze"), Arrays.asList(100), 4, 15, 10));
        put("Lava Pigman", new Mob(Collections.singletonList("lava_pigman"), Arrays.asList(100), 4, 15, 10));
        put("Agarimoo", new Mob(Collections.singletonList("agarimoo"), Arrays.asList(35), 3, 15, 15));
        put(// Mythological Creatures
        "Gaia Construct", new Mob(Collections.singletonList("gaia_construct"), Arrays.asList(140, 260), 4, 20, 20));
        put("Minos Champion", new Mob(Collections.singletonList("minos_champion"), Arrays.asList(175, 310), 5, 20, 20));
        put("Minos Hunter", new Mob(Collections.singletonList("minos_hunter"), Arrays.asList(15, 60, 125), 5, 20, 20));
        put("Minos Inquisitor", new Mob(Collections.singletonList("minos_inquisitor"), Arrays.asList(750), 7, 20, 300));
        put("Minotaur", new Mob(Collections.singletonList("minotaur"), Arrays.asList(45, 120, 210), 4, 20, 20));
        put("Siamese Lynx", new Mob(Collections.singletonList("siamese_lynx"), Arrays.asList(25, 85, 155), 4, 20, 20));
        put("Blue Jerry", new Mob(Collections.singletonList("mayor_jerry_blue"), Arrays.asList(2), 5, 10, 1440));
        put("Golden Jerry", new Mob(Collections.singletonList("mayor_jerry_golden"), Arrays.asList(5), 7, 10, 7200));
        put("Green Jerry", new Mob(Collections.singletonList("mayor_jerry_green"), Arrays.asList(1), 4, 10, 720));
        put("Purple Jerry", new Mob(Collections.singletonList("mayor_jerry_purple"), Arrays.asList(3), 6, 10, 2880));
        put("Blazing Golem", new Mob(Collections.singletonList("blazing_golem"), Arrays.asList(100, 200, 300, 400, 500), 3, 10, 30));
        put("Blight", new Mob(Collections.singletonList("blight"), Arrays.asList(100, 200, 300, 400, 500), 3, 20, 15));
        put("Dropship", new Mob(Collections.singletonList("dropship"), Arrays.asList(100, 200, 300, 400, 500), 3, 10, 30));
        put("Explosive Imp", new Mob(Collections.singletonList("explosive_imp"), Arrays.asList(100, 200, 300, 400, 500), 3, 20, 30));
        put("Inferno Magma Cube", new Mob(Collections.singletonList("inferno_magma_cube"), Arrays.asList(100, 200, 300, 400, 500), 3, 20, 15));
        put("Kuudra Berserker", new Mob(Collections.singletonList("kuudra_berserker"), Arrays.asList(100, 200, 300, 400, 500), 3, 20, 15));
        put("Kuudra Follower", new Mob(Collections.singletonList("kuudra_follower"), Arrays.asList(100, 200, 300, 400, 500), 2, 20, 15));
        put("Kuudra Knocker", new Mob(Collections.singletonList("kuudra_knocker"), Arrays.asList(100, 200, 300, 400, 500), 3, 20, 15));
        put("Kuudra Landmine", new Mob(Collections.singletonList("kuudra_landmine"), Arrays.asList(100, 200, 300, 400, 500), 3, 20, 15));
        put("Kuudra Slasher", new Mob(Collections.singletonList("kuudra_slasher"), Arrays.asList(100, 200, 300, 400, 500), 5, 10, 60));
        put("Magma Follower", new Mob(Collections.singletonList("magma_follower"), Arrays.asList(100, 200, 300, 400, 500), 5, 10, 30));
        put("Wandering Blaze", new Mob(Collections.singletonList("wandering_blaze"), Arrays.asList(100, 200, 300, 400, 500), 4, 20, 15));
        put("Wither Sentry", new Mob(Collections.singletonList("wither_sentry"), Arrays.asList(100, 200, 300, 400, 500), 4, 10, 30));
    }};
}

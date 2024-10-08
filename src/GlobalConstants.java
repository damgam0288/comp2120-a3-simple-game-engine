/**
 * A globally accessible utility class containing all important constants
 */
public class GlobalConstants {
    public static final int GAME_MAX_ENEMIES_PER_LEVEL = 3;
    public static final int GAME_MAX_NPC_PER_LEVEL = 3;
    public static final int MAP_MAX_WIDTH = 30;
    public static final int MAP_MAX_HEIGHT = 15;
    public static final int MAP_MIN_WIDTH = 10;
    public static final int MAP_MIN_HEIGHT = 5;
    public static final int PLAYER_MAX_LEVEL = 5;
    public static final int PLAYER_HP_INCREASE_PER_LEVEL = 20;
    public static final int PLAYER_AP_INCREASE_PER_LEVEL = 10;
    public static String PATH_TO_CONFIG_FILE;

    // To allow the Tests to set the variable to a testing config file
    public static void setConfigFilePath(String path) {
        PATH_TO_CONFIG_FILE = path;
    }


}

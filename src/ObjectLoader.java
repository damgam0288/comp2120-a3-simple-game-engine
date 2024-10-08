import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/**
 * Reads JSON files to create objects like NPCs, items etc. This is an abstract class
 * that holds functionality that goes into loading aforementioned objects
 *
 * @author Damian Gamlath
 */
abstract class ObjectLoader {

    /**
     * Method to return the JSON object given the parameters
     * @param targetObject the string value of the object you are looking for
     * @param jsonArray the array key that this object will be found in
     * @param key the Json-key string that is associated with the targetObject value
     * @param filepath the Json's file path
     * @return a JSON object if the object is found or {@code null} if not found
     * @throws JSONException if any parameter is not found in the Json file
     * @throws IOException if the file cannot be found
     */
    public static JSONObject findObject
            (String targetObject, String jsonArray, String key, String filepath)
            throws JSONException,IOException {

        // Read JSON file
        String content = new String(Files.readAllBytes(Paths.get(filepath)));

        // Search through all data in the JSON file
        JSONArray array = new JSONObject(content).getJSONArray(jsonArray);

        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);

            // Find target object
            String keyvalue = object.getString(key);
            if (keyvalue.equalsIgnoreCase(targetObject)) {
                return object;
            }
        }

        // Missing object in a JSON configuration file means game shouldn't run
        throw new JSONException("Could not find " + targetObject +
                " in the given reference: " +
                filepath + "/" + jsonArray + "/" + key);
    }
}

/**
 * A custom version of the Object Loader to find an Item in a JSON file
 *
 * @author Damian Gamlath
 */
class NPCLoader extends ObjectLoader {

    /**
     * Returns new NPC given the target-string and Json file.
     *
     * @param target   name of the NPC you are looking to load
     * @param filepath file containing the target NPC to load
     * @return NPC with x,y,name,char and item assigned
     * @throws JSONException        if wrong Json key reference is used
     * @throws IOException          if the config file can't be found
     * @throws NoSuchFieldException if wrong reference key is used
     *                              or a NoSuchFieldException if the specified item cannot be found.
     */
    public static NPC loadObject(String target, String filepath)
            throws JSONException, IOException {

        JSONObject jsonObject = ObjectLoader.findObject(target, "npcs", "name", filepath);
        int x = jsonObject.getInt("startx");
        int y = jsonObject.getInt("starty");
        char symbol = jsonObject.getString("char").charAt(0);
        String name = jsonObject.getString("name");
        String item = jsonObject.getString("item");

        return new NPC(x, y, symbol, name, item);
    }
}


/**
 * A custom version of the Object Loader to find an Item in a JSON file
 *
 * @author Damian Gamlath
 */
class ItemLoader extends ObjectLoader {

    /**
     * Returns a newly created Item object given the target-string and Json file.
     * Will return JSONExceptions if the KEY-string cannot be found,
     * or a NoSuchFieldException if the specified item cannot be found.
     * @param target string value of the target item e.g. "weapon2"
     * @param filepath file path of the Json file this item will be found
     * @return the specific item if found or {@code null} if not found
     * @throws Exception JSONException if any key is missing or IOException if the file could not be found
     */
    public static Item loadObject(String target, String filepath) throws Exception {
        JSONObject itemRef = findObject(target, "items", "name", filepath);
        ItemType itemType = ItemType.stringToType(itemRef.getString("type"));

        if (itemType==null)
            throw new IllegalArgumentException("Cannot recognise type");

        return switch (itemType) {
            case WEAPON
                    -> new Weapon(itemRef.getString("name"),itemRef.getInt("value"));
            case SHIELD
                    -> new Shield(itemRef.getString("name"),itemRef.getInt("value"));
            case HEALTHPOTION
                    -> new HealthPotion(itemRef.getString("name"),itemRef.getInt("value"));
        };
    }
}

/**
 * A utility class to load the contents of a JSON file, trim leading/trailing spaces and
 * generate a string. Used to help print a "PAUSED" screen or a "YOU WON!" screen
 */
class ScreenLoader {

    public static List<String> jsonContentToStringList(String filepath) throws IOException {

        // Read JSON file
        List<String> lines = Files.readAllLines(Paths.get(filepath));
        lines = lines.stream()
                .map(line -> line.replaceAll("[\\[\\],\"]", "").trim())
                .filter(line -> !line.trim().isEmpty())
                .toList();

        return lines;
    }

}



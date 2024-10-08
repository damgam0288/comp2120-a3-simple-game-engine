import org.json.JSONObject;

import java.util.Objects;

/**
 * NPC class is a specific Entity that can interact with the player
 * through conversation
 */
public class NPC extends Entity {
    private String name = null;
    private Item item = null;

    /**
     * Constructor for the NPC class.
     *
     * @param startX the starting x-coordinate of the NPC.
     * @param startY the starting y-coordinate of the NPC.
     * @param symbol the character symbol representing the NPC on the map.
     */
    public NPC(int startX, int startY, char symbol) {
        super(startX, startY, symbol);
    }

    public NPC(int startX, int startY, char symbol, String name) {
        super(startX, startY, symbol);
        this.name = name;
    }

    public NPC(int startX, int startY, char symbol, String name, String itemName) {
        super(startX, startY, symbol);
        this.name = name;
        try {
            this.item = ItemLoader.loadObject(itemName, GlobalConstants.PATH_TO_CONFIG_FILE);
        } catch (Exception ignored) {
            this.item = null;
        }
    }

    /**
     * Retrieves the name of the entity.
     *
     * @return the name of the entity.
     * @author Rifang Zhou
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the entity to the specified value.
     *
     * @param name the new name to set for the entity.
     * @author Rifang Zhou
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the specified item for the entity.
     *
     * @param item the item to set for the entity.
     * @author Rifang Zhou
     */
    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * Sets the specified item for the entity.
     *
     * @param itemref the string item reference in the "items" Json object
     * @author Rifang Zhou
     */
    public void setItem(String itemref) {
        try {
            this.item = ItemLoader.loadObject(itemref, GlobalConstants.PATH_TO_CONFIG_FILE);
        } catch (Exception ignored) {
            this.item = null;
        }
    }
    /**
     * Gives the specified item from this entity to the specified player.
     * The item is removed from this entity after it is given to the player.
     *
     * @param player the player to whom the item will be given.
     * @author Rifang Zhou
     */
    public void giveItem(Player player) {
        player.receiveItem(item);
        item = null;
    }

    /**
     * Checks whether the entity currently has an item.
     *
     * @return true if the entity has an item, false otherwise.
     * @author Rifang Zhou
     */
    public boolean hasItem() {
        return (Objects.nonNull(item));
    }

    /**
     * Handles the interaction between the NPC and the player based on the current map.
     * <p>
     * If the npc has an item, it will give the item to the Player. Otherwise it will wish
     * the Player well
     *
     * @author Rifang Zhou
     *
     * @param player  The player interacting with the NPC.
     */
    public void interact(Player player) {
        if (hasItem()) {
            System.out.println("NPC: Here's something to help! " + item.getName());
            this.giveItem(player);
        }
        else {
            System.out.println("NPC: Good luck out there!");
        }
    }
}


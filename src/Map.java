import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Conceptually, the game is played on a "playable grid". This class
 * represents the entire "playable grid" including the world, all entities
 * and any inventory items
 */
public class Map {

    private String name;
    private char[][] world;   // Actual map of the world only
    private char[][] grid;          // Playable grid containing game map, player, items, NPCs etc.
    private int width;
    private int height;
    private Player player;
    private List<Entity> entities;


    /**
     * Constructor
     *
     * @param n        - name of the map
     * @param filePath - path to json file containing world
     * @throws IOException - in case cannot find json file
     */
    public Map(String n, String filePath) throws Exception {
        name = n;
        entities = new ArrayList<>();

        MapLoader.loadMapWorldFromFile(filePath,this);
    }

    /**
     * Draws game map together with all entities, items etc. to the terminal
     */
    public void draw() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // First make the "floor"
                grid[x][y] = world[x][y];

                // Then add the player
                if (player.getX() == x && player.getY() == y) grid[x][y] = player.getSymbol();

                // Next add the entities
                for (Entity e : entities) {
                    if (e.getX() == x && e.getY() == y) {
                        grid[x][y] = e.getSymbol();
                        break;
                    }
                }

                // Finally, draw game grid
                System.out.print(grid[x][y]);
            }
            System.out.println();
        }
    }

    /**
     * Returns the char from the grid given x and y coordinates
     * @param x - starts at 0
     * @param y - starts at 0
     */
    public char getGridTile(int x, int y) {
        return grid[x][y];
    }

    /**
     * Sets the char of the play grid given x,y coordinates and the char
     * @param x - starts at 0
     * @param y - starts at 0
     */
    public void setGridTile(int x, int y, char value) {
        grid[x][y] = value;
    }

    public char getWorldTile(int x, int y) {return world[x][y]; }

    public void setWorldTile(int x, int y, char value) {world[x][y] = value; }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public char[][] getWorld() {
        return world;
    }

    public void setWorld(char[][] world) {
        this.world = world;
    }

    public char[][] getGrid() {
        return grid;
    }

    public void setGrid(char[][] grid) {
        this.grid = grid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    /**
     * Checks if given x,y position is not over the game world boundaries
     * or over an obstacle
     */
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width &&
                y >= 0 && y < height &&
                !isObstacle(x, y);
    }

    /**
     * Helper method for checking if given x,y coordinates overlaps a
     * char that stands for an obstacle e.g. a wall
     */
    private boolean isObstacle(int x, int y) {
        return getGridTile(x, y) == '#';
    }

    /**
     * Adds (non-duplicate) entity to the list of entities on this map
     * NOTE: does NOT re-draw the game world to the terminal
     */
    public boolean addEntity(Entity e) {
        if (!entities.contains(e)) {
            entities.add(e);
            return true;
        }
        return false;
    }

    /**
     * Updates entity position on the map IF it exists in this game world
     * and if the move is valid.<br>
     * NOTE: Does NOT re-draw the play grid to the terminal - other classes
     * must do that separately if required
     */
    public void moveEntity(Entity e, int newX, int newY) {
        if (isValidPosition(newX, newY)) {
            e.setX(newX); e.setY(newY);
        }
        else {
            System.out.println("Can't move there!");
        }
    }

    /**
     * @return Null if player is null <br>
     * Null if no entities on the map <br>
     * The entity that the Player is colliding with
     */
    public Entity getCollidingEntity() {
        if (Objects.isNull(player))
            return null;

        if (entities.isEmpty())
            return null;

        for (Entity e : entities) {
            if (isColliding(player,e)) return e;
        }

        return null;
    }

    /**
     * Checks if all enemies on the map have been defeated.
     *
     * @return true if there are no enemies left on the map, false otherwise.
     */
    public boolean allEnemiesDefeated() {
        return entities.stream().noneMatch(e -> e instanceof Enemy);
    }

    /**
     * Determines if the player can move to the next map.
     * The player can only move to the next map if all enemies are defeated
     * and the player is standing on the designated 'O' tile (exit point).
     *
     * @return true if the player can move to the next map, false otherwise.
     */
    public boolean canMoveToNextMap() {
        return allEnemiesDefeated() && getGridTile(player.getX(), player.getY()) == 'O';
    }

    /**
     * Checks if the player has won the game.
     * Victory is achieved if all enemies are defeated and the player is on the 'V' tile (victory point).
     *
     * @return true if the player has won the game, false otherwise.
     */
    public boolean isVictory() {
        return allEnemiesDefeated() && getGridTile(player.getX(), player.getY()) == 'V';
    }

    /**
     * Removes a specified entity from the map and resets the tile where it was located.
     * After removing the entity, the tile it occupied is set back to a default state,
     *
     * @param e The entity to be removed from the map.
     */
    public boolean removeEntity(Entity e) {
        if (entities.contains(e)) {
            entities.remove(e);
            setGridTile(e.getX(), e.getY(), world[e.getX()][e.getY()]);   // Reset the tile to floor value
            return true;
        }
        return false;
    }

    /**
     * Helper method to check NPC / Enemy collision
     * @param e1 first entity
     * @param e2 second entity
     * @return true if first and second entities are colliding
     */
    private boolean isColliding(Entity e1, Entity e2) {
        return (e1.getX() == e2.getX() &&
                e1.getY() == e2.getY());
    }

    public String getMapNumber() {
        return name;  // Placeholder for map number, adjust if necessary
    }
}
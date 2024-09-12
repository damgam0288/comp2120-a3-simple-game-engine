import java.io.IOException;
import java.util.Scanner;

public class Game {

    private final Map currentMap;
    private final Player player;
    private final NPC npc;

    // Game initiation
    public Game() throws IOException {

        player = new Player(1, 2, 'P');

        currentMap = new Map("map2", "assets/map2.json", player);

        // Dummy entities: can move this to a config file later
        npc = new NPC(3, 3, 'N');
        currentMap.addEntity(npc);
    }

    // Main game "loop" - handle user inputs through Scanner
    public void start() {
        currentMap.draw();

        Scanner scanner = new Scanner(System.in);
        String input;
        do {
            System.out.println("Enter move (W for Up, S for Down, A for Left, D for Right, Q to quit): ");
            input = scanner.nextLine();

            handleMovement(input);      // Handle player movement
            handleNPCInteraction();     // Handle interaction with NPCs

            currentMap.draw();
        } while (!input.equalsIgnoreCase("q"));

        scanner.close();
    }

    // Handle player movement within the current map
    private void handleMovement(String move) {
        switch (move.toLowerCase()) {
            case "w" -> player.move(0, -1, currentMap);
            case "s" -> player.move(0, 1, currentMap);
            case "a" -> player.move(-1, 0, currentMap);
            case "d" -> player.move(1, 0, currentMap);
        }
    }

    private void handleNPCInteraction() {
        // Handle  NPCs / enemy interaction here

        // Testing method only - TODO remove later
        System.out.println("TEST: Player & NPC colliding: " + currentMap.isColliding(player,npc));

    }

    public static void main(String[] args) throws IOException {
        new Game().start();
    }
}

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class MainTest {
    String stale = "Repl.it uses operational transformations to keep everyone in a multiplayer repl in sync.";
    String latest = "Repl.it uses operational transformations.";
    ArrayList<String> operations = new ArrayList<>();

    @Test
    public void testInsert() {
        // Example 1
        Main.cursor = 0;
        stale = "";
        operations.add("{\"op\": \"insert\", \"chars\": \"Hello, human!\"}");
        Assert.assertTrue(Main.isValid(stale, "Hello, human!", operations));

    }

    @Test
    public void testDelete() {
        // Example 2
        Main.cursor = 7;
        stale = "What is up?";
        operations.add("{\"op\": \"delete\", \"count\": 3}");
        Assert.assertTrue(Main.isValid(stale, "What is?", operations));
    }

    @Test
    public void testSkip() {
        // Example 3
        String stale = "Nice!";
        Main.cursor = 0;
        operations.add("{\"op\": \"skip\", \"count\": 4}");
        operations.add("{\"op\": \"insert\", \"chars\": \" day\"}");
        Assert.assertTrue(Main.isValid(stale, "Nice day!", operations));
    }

    @Test
    public void testSkipAndDeleteReturnTrue() {
        Main.cursor = 0;
        operations.add("{\"op\": \"skip\", \"count\": 40}");
        operations.add("{\"op\": \"delete\", \"count\": 47}");
        Assert.assertTrue(Main.isValid(stale, latest, operations));
    }

    @Test
    public void testDeletePastEndReturnFalse() {
        Main.cursor = 0;
        operations.add("{\"op\": \"skip\", \"count\": 45}");
        operations.add("{\"op\": \"delete\", \"count\": 47}");
        Assert.assertFalse(Main.isValid(stale, latest, operations));
    }

    @Test
    public void testSkipPastEndReturnFalse() {
        Main.cursor = 0;
        operations.add("{\"op\": \"skip\", \"count\": 40}");
        operations.add("{\"op\": \"delete\", \"count\": 47}");
        operations.add("{\"op\": \"skip\", \"count\": 2}");
        Assert.assertFalse(Main.isValid(stale, latest, operations));
    }

    @Test
    public void testDeleteInsertSkipDeleteReturnTrue() {
        Main.cursor = 0;
        operations.add("{\"op\": \"delete\", \"count\": 7}");
        operations.add("{\"op\": \"insert\", \"chars\": \"We\"}");
        operations.add("{\"op\": \"skip\", \"count\": 4}");
        operations.add("{\"op\": \"delete\", \"count\": 1}");
        latest = "We use operational transformations to keep everyone in a multiplayer repl in sync.";
        Assert.assertTrue(Main.isValid(stale, latest, operations));
    }

    @Test
    public void testDeleteInsertSkipDeleteReturnFalse() {
        Main.cursor = 0;
        latest = "We can use operational transformations to keep everyone in a multiplayer repl in sync.";
        operations.add("{\"op\": \"delete\", \"count\": 7}");
        operations.add("{\"op\": \"insert\", \"chars\": \"We\"}");
        operations.add("{\"op\": \"skip\", \"count\": 4}");
        operations.add("{\"op\": \"delete\", \"count\": 1}");
        Assert.assertFalse(Main.isValid(stale, latest, operations));
    }

    @Test
    public void testNoOperationsReturnTrue() {
        Main.cursor = 0;
        stale = "";
        latest = "";
        Assert.assertTrue(Main.isValid(stale, latest, operations));
    }
}
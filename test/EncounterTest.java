import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.*;
import java.beans.Transient;
import java.lang.*;
/**
 * JUnit Tests for Encounter Builder... Tests core funcionality, manual UI tests are required for GUI portion of the application.
 */
public class EncounterTest {
    private Encounter encounter;
    /**
	 * Reset the base data structures just in case
	 */
	@BeforeEach 
	void reset(){
        encounter = new Encounter("Test");
	}

    @Test
    public void testAddCombatant(){
        Combatant a =
            new Combatant(
                "A",
                20,
                10,
                10,
                "PC"
            );
        encounter.addCombatant(a);
        assertEquals(a, encounter.getCurrentCombatant());
        assertEquals(1, encounter.getCombatants().size());
    }

    @Test
    public void testRemoveCombatant(){
        Combatant a =
            new Combatant(
                "A",
                20,
                10,
                10,
                "PC"
            );

        Combatant b =
            new Combatant(
                "B",
                10,
                10,
                10,
                "Monster"
            );
        encounter.addCombatant(a);
        encounter.addCombatant(b);
        assertEquals(2, encounter.getCombatants().size());
        assertEquals(a, encounter.getCurrentCombatant());
        encounter.removeCombatant("A");
        assertEquals(1, encounter.getCombatants().size());
        assertEquals(b, encounter.getCurrentCombatant());
        assertFalse(encounter.getCombatants().contains(a));
        assertTrue(encounter.getCombatants().contains(b));
        encounter.removeCombatant("B");
        assertEquals(encounter.getCurrentCombatant(), null);


    }

    @Test
    public void testSortInitiative(){
        Combatant a =
            new Combatant(
                "A",
                1,
                10,
                10,
                "PC"
            );

        Combatant b =
            new Combatant(
                "B",
                10,
                10,
                10,
                "Monster"
            );
        Combatant c =
            new Combatant(
                "C",
                20,
                10,
                10,
                "PC"
            );

        Combatant d =
            new Combatant(
                "D",
                5,
                10,
                10,
                "Monster"
            );
        encounter.addCombatant(a);
        encounter.addCombatant(b);
        encounter.addCombatant(c);
        encounter.addCombatant(d);
        assertEquals(a, encounter.getCombatants().get(0));
        assertEquals(b, encounter.getCombatants().get(1));
        assertEquals(c, encounter.getCombatants().get(2));
        assertEquals(d, encounter.getCombatants().get(3));
        encounter.sortInitiative();
        assertEquals(c, encounter.getCombatants().get(0));
        assertEquals(b, encounter.getCombatants().get(1));
        assertEquals(d, encounter.getCombatants().get(2));
        assertEquals(a, encounter.getCombatants().get(3));
    }

    @Test
    public void testSaveLoadEncounter(){
        Encounter encounter =
        new Encounter("Dungeon");

        Combatant goblin =
            new Combatant(
                "Goblin",
                12,
                8,
                10,
                "Monster"
            );

        Condition poison =
            new Condition(
                "Poisoned",
                3
            );

        goblin.addConditions(poison);
        encounter.addCombatant(goblin);
        encounter.saveEncounter( "test.enc");

        Encounter loaded = Encounter.loadEncounter("test.enc");
        assertNotNull(loaded);

        assertEquals("Dungeon", loaded.getEncounterName());
        assertEquals(1, loaded.getCombatants().size());

        Combatant loadedGoblin = loaded.getCombatants().get(0);

        assertEquals("Goblin", loadedGoblin.getName());
        assertEquals(8, loadedGoblin.getCurHP());
        assertEquals(10, loadedGoblin.getMaxHP());
        assertEquals("Monster", loadedGoblin.getType());
        assertEquals(1, loadedGoblin.getConditions().size());
        assertEquals("Poisoned",
                loadedGoblin
                .getConditions()
                .get(0)
                .getconditionName());
    }

    @Test
    public void testHealDamage(){
        Combatant a =
            new Combatant(
                "A",
                20,
                10,
                10,
                "PC"
            );
        a.takeDamage(10);
        assertEquals(0, a.getCurHP());
        a.heal(5);
        assertEquals(5, a.getCurHP());
    }

    @Test
    public void testConditions(){
        Combatant a =
            new Combatant(
                "A",
                20,
                10,
                10,
                "PC"
            );
        Condition poison =
            new Condition(
                "Poisoned",
                3
            );

        a.addConditions(poison);
        encounter.addCombatant(a);
        assertEquals(1, a.getConditions().size());
        assertEquals(3, a.getConditions().get(0).getDuration());
        a.tickconditions();
        assertEquals(2, a.getConditions().get(0).getDuration());
        a.tickconditions();
        assertEquals(1  , a.getConditions().get(0).getDuration());
        a.tickconditions();
        assertEquals(0  , a.getConditions().get(0).getDuration()); //0 duration means its expiring at the end of this turn
        a.tickconditions();
        assertEquals(0, a.getConditions().size());
    }

    @Test
    public void testNextTurnWrapsRound(){
        Combatant a =
            new Combatant(
                "A",
                20,
                10,
                10,
                "PC"
            );

        Combatant b =
            new Combatant(
                "B",
                10,
                10,
                10,
                "Monster"
            );

        encounter.addCombatant(a);
        encounter.addCombatant(b);
        encounter.sortInitiative();

        assertEquals(a, encounter.getCurrentCombatant());

        encounter.nextTurn();

        assertEquals(b, encounter.getCurrentCombatant());

        encounter.nextTurn();

        assertEquals(a, encounter.getCurrentCombatant());
        assertEquals(2, encounter.getCurrentRound());


    }

}

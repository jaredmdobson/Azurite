package ai.statemachine;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Juyas
 * @version 15.07.2021
 * @since 15.07.2021
 */
public class StateMachineTest {

  private StateMachine testMachine1;
  private StateMachine testMachine2;

  private int machine1Counter;
  private String logString;

  @BeforeEach
  public void setUp() throws Exception {
    this.testMachine1 = new StateMachine();
    this.testMachine2 = new StateMachine();

    this.testMachine1.addState("idle", new State() {
      @Override
      public void enterState() {
        logString += "ENTER:idle,";
      }

      @Override
      public void updateState(float dt) {
        machine1Counter++;
        logString += "UPDATE:idle,";
      }

      @Override
      public void exitState() {
        logString += "EXIT:idle,";
      }
    });
    this.testMachine1.addState("other", new State() {
      @Override
      public void enterState() {
        logString += "ENTER:other,";
      }

      @Override
      public void updateState(float dt) {
        machine1Counter--;
        logString += "UPDATE:other,";
      }

      @Override
      public void exitState() {
        logString += "EXIT:other,";
      }
    });
    this.testMachine1.addTransition("idle", "other", state -> true);
    this.testMachine1.addTransition("other", "idle", state -> true);

    this.testMachine2.addState("idle", new State() {
      @Override
      public void enterState() {

      }

      @Override
      public void updateState(float dt) {

      }

      @Override
      public void exitState() {

      }
    });
    this.testMachine2.addState("other", new State() {
      @Override
      public void enterState() {

      }

      @Override
      public void updateState(float dt) {

      }

      @Override
      public void exitState() {

      }
    });
    //only one transition to test validate
    this.testMachine2.addTransition("idle", "other", state -> true);
    this.testMachine1.startMachine("idle");
    this.testMachine2.startMachine("idle");
  }

  @Test
  public void hasTransition() {
    Assertions.assertTrue(this.testMachine1.hasTransition("idle", "other"));
    Assertions.assertTrue(this.testMachine1.hasTransition("other", "idle"));
    Assertions.assertFalse(this.testMachine1.hasTransition("unknown", "idle"));
    Assertions.assertFalse(this.testMachine1.hasTransition("idle", "unknown"));
    Assertions.assertFalse(this.testMachine1.hasTransition("unknown", "other_unknown"));

    Assertions.assertTrue(this.testMachine1.hasTransition("idle"));
    Assertions.assertTrue(this.testMachine1.hasTransition("other"));
    Assertions.assertFalse(this.testMachine1.hasTransition("unknown"));

    Assertions.assertTrue(this.testMachine2.hasTransition("idle", "other"));
    //this transition should be missing
    Assertions.assertFalse(this.testMachine2.hasTransition("other", "idle"));
    Assertions.assertFalse(this.testMachine2.hasTransition("unknown", "idle"));
    Assertions.assertFalse(this.testMachine2.hasTransition("idle", "unknown"));
    Assertions.assertFalse(this.testMachine2.hasTransition("unknown", "other_unknown"));

    Assertions.assertTrue(this.testMachine2.hasTransition("idle"));
    //this should have no transitions
    Assertions.assertFalse(this.testMachine2.hasTransition("other"));
    Assertions.assertFalse(this.testMachine2.hasTransition("unknown"));
  }

  @Test
  public void canTransitionTo() {
    Assertions.assertTrue(this.testMachine1.canTransitionTo("idle"));
    Assertions.assertTrue(this.testMachine1.canTransitionTo("other"));
    Assertions.assertFalse(this.testMachine1.canTransitionTo("unknown"));

    Assertions.assertFalse(this.testMachine2.canTransitionTo("idle"));
    Assertions.assertTrue(this.testMachine2.canTransitionTo("other"));
    Assertions.assertFalse(this.testMachine2.canTransitionTo("unknown"));
  }

  @Test
  public void hasState() {
    Assertions.assertTrue(this.testMachine1.hasState("idle"));
    Assertions.assertTrue(this.testMachine1.hasState("other"));
    Assertions.assertFalse(this.testMachine1.hasState("unknown"));

    Assertions.assertTrue(this.testMachine2.hasState("idle"));
    Assertions.assertTrue(this.testMachine2.hasState("other"));
    Assertions.assertFalse(this.testMachine2.hasState("unknown"));
  }

  @Test
  public void addState() {
    Assertions.assertFalse(this.testMachine1.hasState("no_name"));
    this.testMachine1.addState("no_name", new State() {
      @Override
      public void enterState() {

      }

      @Override
      public void updateState(float dt) {

      }

      @Override
      public void exitState() {

      }
    });
    Assertions.assertTrue(this.testMachine1.hasState("no_name"));
  }

  @Test
  public void addTransition() {
    Assertions.assertFalse(this.testMachine1.addTransition("idle", "newstate", state -> Math.random() < 0.3f));
    Assertions.assertFalse(this.testMachine1.hasTransition("idle", "newstate"));
    this.testMachine1.addState("newstate", new State() {
      @Override
      public void enterState() {

      }

      @Override
      public void updateState(float dt) {

      }

      @Override
      public void exitState() {

      }
    });
    Assertions.assertTrue(this.testMachine1.addTransition("idle", "newstate", state -> Math.random() < 0.3f));
    Assertions.assertTrue(this.testMachine1.hasTransition("idle", "newstate"));
    Assertions.assertTrue(this.testMachine1.doTransition("newstate"));
    Assertions.assertEquals("newstate", this.testMachine1.getCurrentStateName());
    Assertions.assertTrue(this.testMachine1.doTransition("idle"));
    Assertions.assertEquals("idle", this.testMachine1.getCurrentStateName());
  }

  @Test
  public void startMachine() {
    //machines has already started, therefore, it should fail
    Assertions.assertFalse(this.testMachine1.startMachine("idle"));
    Assertions.assertFalse(this.testMachine2.startMachine("idle"));

    StateMachine machine = new StateMachine();
    machine.addState("test", new State() {
      @Override
      public void enterState() {

      }

      @Override
      public void updateState(float dt) {

      }

      @Override
      public void exitState() {

      }
    });

    Assertions.assertTrue(machine.startMachine("test"));
    //start should only work once
    Assertions.assertFalse(machine.startMachine("test"));
  }

  @Test
  public void doTransition() {
    Assertions.assertEquals("idle", this.testMachine1.getCurrentStateName());
    Assertions.assertTrue(this.testMachine1.doTransition("other"));
    Assertions.assertEquals("other", this.testMachine1.getCurrentStateName());
    Assertions.assertTrue(this.testMachine1.doTransition("idle"));
    Assertions.assertEquals("idle", this.testMachine1.getCurrentStateName());
    Assertions.assertFalse(this.testMachine1.doTransition("anyUnknownState"));
    Assertions.assertEquals("idle", this.testMachine1.getCurrentStateName());
  }

  @Test
  public void validate() {
    //machine1 has no deadlock nodes nor orphaned nodes
    Assertions.assertTrue(this.testMachine1.validate());
    //machine2 has a deadlock node: other
    Assertions.assertFalse(this.testMachine2.validate());
  }

  @Test
  public void update() {
    logString = "";
    machine1Counter = 0;
    //run update with any number
    this.testMachine1.update(1.0f);
    Assertions.assertFalse(logString.isEmpty());
    Assertions.assertEquals(-1, machine1Counter);
    Assertions.assertEquals("EXIT:idle,ENTER:other,UPDATE:other,", logString);
    //run update again with any number
    this.testMachine1.update(1.0f);
    Assertions.assertEquals(0, machine1Counter);
    Assertions.assertEquals("EXIT:idle,ENTER:other,UPDATE:other,EXIT:other,ENTER:idle,UPDATE:idle,", logString);
  }
}
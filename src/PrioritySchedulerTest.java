 import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vmthread.VMThread;
import junit.framework.TestCase;

/**
 * Tests for PriorityScheduler.
 * 
 * @author Tim
 * @version Feb 23, 2008
 */
public class PrioritySchedulerTest extends TestCase
{
    /** Test execution. */
    public void testSimpleExec()
    {
        try
        {
            HashMap<String, Integer> symbols = 
                new HashMap<String, Integer>();
            VMThread t1 = new VMThread(
                new BufferedReader(new StringReader(
                    "push 5\n" +
                    "pop x\n" +
                    "push 3\n" +
                    "push x\n" +
                    "add\n" +
                    "pop y\n")), symbols);
            
            List<VMThread> threadList = new ArrayList<VMThread>();
            threadList.add(t1);
            
            PriorityScheduler scheduler = new PriorityScheduler(threadList);
            
            assertSame("Hint: (prio) getCurrentThread returns wrong object",
                t1, scheduler.getCurrentThread());
            assertEquals("Hint: (prio) symbol table should be empty initially",
                0, symbols.size());
            
            scheduler.run(1);
            assertEquals("Hint: (prio) symbol table should still be empty",
                0, symbols.size());
            
            scheduler.run(1);
            assertEquals("Hint: (prio) symbol table contains wrong number " +
                    "of entries", 1, symbols.size());
            assertEquals("Hint: (prio) symbol didn't have expected value",
                5, (int)symbols.get("x"));
            
            scheduler.run(3);
            assertEquals("Hint: (prio) symbol table contains wrong number " +
                    "of entries", 1, symbols.size());
            
            scheduler.run(1);
            assertEquals("Hint: (prio) symbol table contains wrong number " +
                    "of entries", 2, symbols.size());
            assertEquals("Hint: (prio) symbol didn't have expected value",
                5, (int)symbols.get("x"));
            assertEquals("Hint: (prio) symbol didn't have expected value",
                8, (int)symbols.get("y"));
            //scheduler.run(5);
            //assertNull(scheduler.getCurrentThread());
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Hint: (prio) Exception thrown by constructor: " + e);
        }
    }
    
    public void testSimpleExec1()
    {
        try
        {
            HashMap<String, Integer> symbols = 
                new HashMap<String, Integer>();
            VMThread t1 = new VMThread(
                new BufferedReader(new StringReader(
                    "push x\n" +
                    "push 3\n" +
                    "add\n" +
                    "pop x\n" 
                    )), symbols, 1);
            
            VMThread t2 = new VMThread(
                    new BufferedReader(new StringReader(
                        "push 5\n" +
                        "pop x\n" 
                        )), symbols, 2);
            
            List<VMThread> threadList = new ArrayList<VMThread>();
            threadList.add(t2);
            threadList.add(t1);
            
            PriorityScheduler scheduler = new PriorityScheduler(threadList);
            
            assertSame("Hint: (prio) getCurrentThread returns wrong object",
                t2, scheduler.getCurrentThread());
            assertEquals("Hint: (prio) symbol table should be empty initially",
                0, symbols.size());
            
            scheduler.run(3);
            assertSame("Hint: (prio) getCurrentThread returns wrong object",
                    t1, scheduler.getCurrentThread());
            assertEquals("Hint: (prio) symbol table should be 1",
                   1  , symbols.size());
            
            
            
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Hint: (prio) Exception thrown by constructor: " + e);
        }
    }
    
    public void testConcurrentPriority()
    {
        try
        {
            HashMap<String, Integer> symbols = 
                new HashMap<String, Integer>();
            VMThread t1 = new VMThread(
                new BufferedReader(new StringReader(
                    "push x\n" +
                    "push 3\n" +
                    "add\n" +
                    "pop x\n" 
                    )), symbols, 1);
            
            VMThread t2 = new VMThread(
                    new BufferedReader(new StringReader(
                        "push 5\n" +
                        "pop x\n" 
                        )), symbols, 2);
            
            VMThread t3 = new VMThread(
                    new BufferedReader(new StringReader(
                        "push 5\n" +
                        "pop y\n" 
                        )), symbols, 2);
            
            List<VMThread> threadList = new ArrayList<VMThread>();
            threadList.add(t2);
            threadList.add(t1);
            threadList.add(t3);
            
            PriorityScheduler scheduler = new PriorityScheduler(threadList);
            
            assertSame("Hint: (prio) getCurrentThread returns wrong object",
                t2, scheduler.getCurrentThread());
            assertEquals("Hint: (prio) symbol table should be empty initially",
                0, symbols.size());
            
            scheduler.run(5);
            assertSame("Hint: (prio) getCurrentThread returns wrong object",
                    t1, scheduler.getCurrentThread());
            assertEquals("Hint: (prio) symbol table should be 2 (x and y)",
                   2  , symbols.size());
            
            
            
            
            
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Hint: (prio) Exception thrown by constructor: " + e);
        }
    }
    
    public void testConcurrentYield()
    {
        try
        {
            HashMap<String, Integer> symbols = 
                new HashMap<String, Integer>();
            VMThread t1 = new VMThread(
                new BufferedReader(new StringReader(
                    "push x\n" +
                    "push 3\n" +
                    "add\n" +
                    "pop x\n" 
                    )), symbols, 1);
            
            VMThread t2 = new VMThread(
                    new BufferedReader(new StringReader(
                        "push 5\n" +
                        "yield\n" +
                        "pop x\n" 
                        )), symbols, 2);
            
            VMThread t3 = new VMThread(
                    new BufferedReader(new StringReader(
                        "push 5\n" +
                        "yield\n" +
                        "pop y\n" 
                        )), symbols, 2);
            
            List<VMThread> threadList = new ArrayList<VMThread>();
            threadList.add(t2);
            threadList.add(t1);
            threadList.add(t3);
            
            PriorityScheduler scheduler = new PriorityScheduler(threadList);
            
            //make sure we start with t2
            assertSame("Hint: (prio) getCurrentThread returns wrong object",
                t2, scheduler.getCurrentThread());
            assertEquals("Hint: (prio) symbol table should be empty initially",
                0, symbols.size());
            
            scheduler.run(2);
            //run til yield, make sure we are using t3
            assertSame("Hint: (prio) getCurrentThread returns wrong object",
                    t3, scheduler.getCurrentThread());
            
            scheduler.run(2);
            //run yet again, make sure we get t2
            assertSame("Hint: (prio) getCurrentThread returns wrong object (wrong priority?)",
                    t2, scheduler.getCurrentThread());
            
            scheduler.run(2);
            //make sure priority management is done right
            assertSame("Hint: (prio) getCurrentThread returns wrong object (wrong priority?)",
                    t1, scheduler.getCurrentThread());
            
            
            
            
            
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Hint: (prio) Exception thrown by constructor: " + e);
        }
    }
    public void testPriorities()
    {
        try
        {
            HashMap<String, Integer> symbols = 
                new HashMap<String, Integer>();
            VMThread t1 = new VMThread(
                new BufferedReader(new StringReader(
                    "push x\n" +
                    "push 3\n" +
                    "add\n" +
                    "pop x\n" 
                    )), symbols, 1);
            
            VMThread t2 = new VMThread(
                    new BufferedReader(new StringReader(
                        "push 5\n" +
                        "yield\n" +
                        "pop x\n" 
                        )), symbols, 3);

            
            List<VMThread> threadList = new ArrayList<VMThread>();
            threadList.add(t2);
            threadList.add(t1);
            
            PriorityScheduler scheduler = new PriorityScheduler(threadList);
            
            //make sure we start with t2
            assertSame("Hint: (prio) getCurrentThread returns wrong object",
                t2, scheduler.getCurrentThread());
            assertEquals("Hint: (prio) symbol table should be empty initially",
                0, symbols.size());
            
            scheduler.run(2);
            //run til yield, make sure we are using t3
            assertSame("Hint: (prio) getCurrentThread returns wrong object",
                    t2, scheduler.getCurrentThread());
            
            scheduler.run(2);
            assertSame("Hint: (prio) getCurrentThread returns wrong object",
                    t1, scheduler.getCurrentThread());
            
            scheduler.run(4);
            assertNull(scheduler.getCurrentThread());
            
            
            
            
            
            
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Hint: (prio) Exception thrown by constructor: " + e);
        }
    }
    
    //  TODO: Your tests here
}

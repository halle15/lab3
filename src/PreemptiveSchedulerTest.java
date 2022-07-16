import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vmthread.VMThread;
import junit.framework.TestCase;

/**
 * Tests for PreemptiveScheduler.
 * 
 * @author Tim
 * @version Feb 23, 2008
 */
public class PreemptiveSchedulerTest extends TestCase
{    /** Test execution. */
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
            
            PreemptiveScheduler scheduler = 
                new PreemptiveScheduler(threadList, 20);
            
            assertSame("Hint: (preempt) getCurrentThread returns wrong object",
                t1, scheduler.getCurrentThread());
            assertEquals("Hint: (preempt) symbol table should be empty " +
            		"initially", 0, symbols.size());
            
            scheduler.run(1);
            assertEquals("Hint: (preempt) symbol table should still be empty",
                0, symbols.size());
            
            scheduler.run(1);
            assertEquals("Hint: (preempt) symbol table contains wrong number " +
            		"of entries", 1, symbols.size());
            assertEquals("Hint: (preempt) symbol didn't have expected value",
                5, (int)symbols.get("x"));
            
            scheduler.run(3);
            assertEquals("Hint: (preempt) symbol table contains wrong number " +
            		"of entries", 1, symbols.size());
            
            scheduler.run(1);
            assertEquals("Hint: (preempt) symbol table contains wrong number " +
            		"of entries", 2, symbols.size());
            assertEquals("Hint: (preempt) symbol didn't have expected value",
                5, (int)symbols.get("x"));
            assertEquals("Hint: (preempt) symbol didn't have expected value",
                8, (int)symbols.get("y"));
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Hint: (preempt) Exception thrown by constructor: " + e);
        }
    }
    
    
    public void testExec()
    {
        try
        {
            HashMap<String, Integer> symbols = 
                new HashMap<String, Integer>();
            VMThread t1 = new VMThread(
                new BufferedReader(new StringReader(
                    "push 5\n" +
                    "push 3\n" +
                    "add\n" +
                    "pop x\n")), symbols);
            
            VMThread t2 = new VMThread(
                    new BufferedReader(new StringReader(
                        "push 7\n" +
                        "pop y\n")), symbols);
            
            List<VMThread> threadList = new ArrayList<VMThread>();
            threadList.add(t1);
            threadList.add(t2);
            
            PreemptiveScheduler scheduler = 
                new PreemptiveScheduler(threadList, 1);
            
            assertSame("Hint: (preempt) getCurrentThread returns wrong object",
                t1, scheduler.getCurrentThread());
            assertEquals("Hint: (preempt) symbol table should be empty " +
            		"initially", 0, symbols.size());
            
            scheduler.run(1);
            assertSame("Hint: (preempt) getCurrentThread returns wrong object",
                    t2, scheduler.getCurrentThread());
            assertSame("Hint: (preempt) getCurrentThread returns wrong object",
                    t2, scheduler.getCurrentThread()); // double check to make sure there are no changes with the getCurrentThread method
            
            scheduler.run(1);
            assertSame("Hint: (preempt) getCurrentThread returns wrong object",
                    t1, scheduler.getCurrentThread());
            
            scheduler.run(1);
            assertSame("Hint: (preempt) getCurrentThread returns wrong object",
                    t2, scheduler.getCurrentThread());
            assertSame("Hint: (preempt) getCurrentThread returns wrong object",
                    t2, scheduler.getCurrentThread()); // double check to make sure there are no changes with the getCurrentThread method
            scheduler.run(1);
            assertSame("Hint: (preempt) getCurrentThread returns wrong object",
                    t1, scheduler.getCurrentThread()); // double check to make sure there are no changes with the getCurrentThread method

            
           

            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Hint: (preempt) Exception thrown by constructor: " + e);
        }
    }
    
    public void testSleep()
    {
        try
        {
            HashMap<String, Integer> symbols = 
                new HashMap<String, Integer>();
            VMThread t1 = new VMThread(
                    new BufferedReader(new StringReader(
                    		//0
                        "push 5\n" + 	//1
                        "sleep 3\n" +   //3
                        "pop x\n")), symbols); //7
                
                VMThread t2 = new VMThread(
                    new BufferedReader(new StringReader(
                        "push 6\n" +  //2 
                        "sleep 2\n" + //4 (2)
                        			//5 null (1)
                        //6 null 0
                        "pop z\n")), symbols);
            
            List<VMThread> threadList = new ArrayList<VMThread>();
            threadList.add(t1);
            threadList.add(t2);
            
            PreemptiveScheduler scheduler = 
                new PreemptiveScheduler(threadList, 1);
            
            assertSame("Hint: (preempt) getCurrentThread returns wrong object",
                t1, scheduler.getCurrentThread());
            assertEquals("Hint: (preempt) symbol table should be empty " +
            		"initially", 0, symbols.size());
            
            scheduler.run(1);
            assertSame("Hint: (preempt) getCurrentThread returns wrong object",
                    t2, scheduler.getCurrentThread());
            assertSame("Hint: (preempt) getCurrentThread returns wrong object",
                    t2, scheduler.getCurrentThread()); // double check to make sure there are no changes with the getCurrentThread method
            
           scheduler.run(1); // 2
           assertSame("Hint: (preempt) getCurrentThread returns wrong object",
                   t1, scheduler.getCurrentThread());
           
           scheduler.run(1); //3 (sleep 3)
           assertSame("Hint: (preempt) getCurrentThread returns wrong object",
                   t2, scheduler.getCurrentThread());
           
           scheduler.run(4);
           assertSame("Hint: (preempt) getCurrentThread returns wrong object",
                   t2, scheduler.getCurrentThread());
           
           /*scheduler.run(1);
           assertSame("Hint: (preempt) getCurrentThread returns wrong object",
                   t1, scheduler.getCurrentThread());
            */
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Hint: (preempt) Exception thrown by constructor: " + e);
        }
    }
    
    //  TODO: Your tests here
}

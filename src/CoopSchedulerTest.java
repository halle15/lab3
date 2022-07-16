import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;
import vmthread.VMThread;

/**
 * Tests for CoopScheduler.
 * 
 * @author Tim
 * @version Feb 23, 2008
 */
public class CoopSchedulerTest extends TestCase
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
            
            CoopScheduler scheduler = new CoopScheduler(threadList);
            
            assertSame("Hint: (coop) getCurrentThread returns wrong object",
                t1, scheduler.getCurrentThread());
            assertEquals("Hint: (coop) symbol table should be empty initially",
                0, symbols.size());
            
            scheduler.run(1);
            assertEquals("Hint: (coop) symbol table should still be empty",
                0, symbols.size());
            
            scheduler.run(1);
            assertEquals("Hint: (coop) symbol table contains wrong number " +
            		"of entries", 1, symbols.size());
            assertEquals("Hint: (coop) symbol didn't have expected value",
                5, (int)symbols.get("x"));
            
            scheduler.run(3);
            assertEquals("Hint: (coop) symbol table contains wrong number " +
            		"of entries", 1, symbols.size());
            
            scheduler.run(1);
            assertEquals("Hint: (coop) symbol table contains wrong number " +
            		"of entries", 2, symbols.size());
            assertEquals("Hint: (coop) symbol didn't have expected value",
                5, (int)symbols.get("x"));
            assertEquals("Hint: (coop) symbol didn't have expected value",
                8, (int)symbols.get("y"));
            assertNull(scheduler.getCurrentThread());
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Hint: (coop) Exception thrown by constructor: " + e);
        }
    }
    
    public void testSimpleYield()
    {
        try
        {
            HashMap<String, Integer> symbols = 
                new HashMap<String, Integer>();
            VMThread t1 = new VMThread(
                new BufferedReader(new StringReader(
                	"push 5\n" +
                	"push 3\n" +
                    "push x\n" +
                    "yield\n" +
                    "add\n" +
                    "pop y\n")), symbols);
            VMThread t2 = new VMThread(
                    new BufferedReader(new StringReader(
                        "push 5\n" +
                        "pop z\n" +
                        "push 3\n" +
                        "push z\n" +
                        "yield\n" +
                        "add\n" +
                        "pop e\n")), symbols);
            
            List<VMThread> threadList = new ArrayList<VMThread>();
            threadList.add(t1);
            threadList.add(t2);
            
            CoopScheduler scheduler = new CoopScheduler(threadList);
            
           scheduler.run(6);
           assertEquals("Hint: (coop) symbol table contains wrong number " +
           		"of entries", 1, symbols.size());
           
           assertSame("Hint: (coop) getCurrentThread returns wrong object",
                   t2, scheduler.getCurrentThread());
           scheduler.run(3);
           assertSame("Hint: (coop) getCurrentThread returns wrong object",
                   t1, scheduler.getCurrentThread());

            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Hint: (coop) Exception thrown by constructor: " + e);
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
                    "push 5\n" +
                    "pop x\n" +
                    "push 3\n" +
                    "push x\n" +
                    "add\n" +
                    "pop y\n")), symbols);
            VMThread t2 = new VMThread(
                    new BufferedReader(new StringReader(
                        "push 5\n" +
                        "pop z\n" +
                        "push 3\n" +
                        "push z\n" +
                        "add\n" +
                        "pop e\n")), symbols);
            
            List<VMThread> threadList = new ArrayList<VMThread>();
            threadList.add(t1);
            threadList.add(t2);
            
            CoopScheduler scheduler = new CoopScheduler(threadList);
            
            assertSame("Hint: (coop) getCurrentThread returns wrong object",
                t1, scheduler.getCurrentThread());
            assertEquals("Hint: (coop) symbol table should be empty initially",
                0, symbols.size());
            
            scheduler.run(1);
            assertEquals("Hint: (coop) symbol table should still be empty",
                0, symbols.size());
            
            scheduler.run(1);
            assertEquals("Hint: (coop) symbol table contains wrong number " +
            		"of entries", 1, symbols.size());
            assertEquals("Hint: (coop) symbol didn't have expected value",
                5, (int)symbols.get("x"));
            
            scheduler.run(3);
            assertEquals("Hint: (coop) symbol table contains wrong number " +
            		"of entries", 1, symbols.size());
            
            scheduler.run(1);
            assertEquals("Hint: (coop) symbol table contains wrong number " +
            		"of entries", 2, symbols.size());
            assertEquals("Hint: (coop) symbol didn't have expected value",
                5, (int)symbols.get("x"));
            assertEquals("Hint: (coop) symbol didn't have expected value",
                8, (int)symbols.get("y"));
            scheduler.run(6);
            assertEquals("Hint: (coop) symbol table contains wrong number " +
            		"of entries", 4, symbols.size());
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Hint: (coop) Exception thrown by constructor: " + e);
        }
    }
    
    public void testSleepSimple()
    {
        try
        {
            HashMap<String, Integer> symbols = 
                new HashMap<String, Integer>();
            VMThread t1 = new VMThread(
                    new BufferedReader(new StringReader(
                        "push x\n" +
                        "sleep 3\n" +
                        "push 3\n" +
                        "add\n" +
                        "pop x\n" 
                        )), symbols);
                

            
            List<VMThread> threadList = new ArrayList<VMThread>();
            threadList.add(t1);

            
            CoopScheduler scheduler = new CoopScheduler(threadList);
            
            assertSame("Hint: (coop) getCurrentThread returns wrong object",
                t1, scheduler.getCurrentThread());
            assertEquals("Hint: (coop) symbol table should be empty initially",
                0, symbols.size());
            
            scheduler.run(2);
            assertNull(scheduler.getCurrentThread());
            
            scheduler.run(4);
            
            
            
            
            
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Hint: (coop) Exception thrown by constructor: " + e);
        }
    }
    
    public void testSleepToFront()
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
                        )), symbols);
                
            VMThread t2 = new VMThread(
                        new BufferedReader(new StringReader(
                            "push 5\n" +
                            "pop x\n" 
                            )), symbols);
            
            VMThread t3 = new VMThread(
                    new BufferedReader(new StringReader(
                    	"sleep 1\n" +
                        "push 5\n" +
                        "push 3\n" +
                        "add\n" +
                        "pop x\n" 
                        )), symbols);
            
            List<VMThread> threadList = new ArrayList<VMThread>();
            threadList.add(t3);
            threadList.add(t2);
            threadList.add(t1);
            CoopScheduler scheduler = new CoopScheduler(threadList);
            
            assertSame("Hint: (coop) getCurrentThread returns wrong object",
                t3, scheduler.getCurrentThread());
            assertEquals("Hint: (coop) symbol table should be empty initially",
                0, symbols.size());
            
            scheduler.run(2);
            assertSame("Hint: (coop) getCurrentThread returns wrong object",
                    t2, scheduler.getCurrentThread());
            
            scheduler.run(1);
            assertSame("Hint: (coop) getCurrentThread returns wrong object",
                    t1, scheduler.getCurrentThread());
            
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Hint: (coop) Exception thrown by constructor: " + e);
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
                        "push x\n" +
                        "sleep 10\n" +
                        "push 3\n" +
                        "add\n" +
                        "pop x\n" 
                        )), symbols);
                
            VMThread t2 = new VMThread(
                        new BufferedReader(new StringReader(
                            "push 5\n" +
                            "yield\n" +
                            "pop x\n" 
                            )), symbols);
            
            VMThread t3 = new VMThread(
                    new BufferedReader(new StringReader(
                        "push 5\n" +
                        "yield\n" +
                        "pop x\n" 
                        )), symbols);
            
            List<VMThread> threadList = new ArrayList<VMThread>();
            threadList.add(t1);
            threadList.add(t2);
            threadList.add(t3);
            CoopScheduler scheduler = new CoopScheduler(threadList);
            
            assertSame("Hint: (coop) getCurrentThread returns wrong object",
                t1, scheduler.getCurrentThread());
            assertEquals("Hint: (coop) symbol table should be empty initially",
                0, symbols.size());
            
            scheduler.run(2);
            assertSame("Hint: (coop) getCurrentThread returns wrong object",
                    t2, scheduler.getCurrentThread());
            scheduler.run(2);
            assertSame("Hint: (coop) getCurrentThread returns wrong object",
                    t3, scheduler.getCurrentThread());
            scheduler.run(6);
            assertNull(scheduler.getCurrentThread());
            
            scheduler.run(3);
            
            assertSame("Hint: (coop) getCurrentThread returns wrong object",
                    t1, scheduler.getCurrentThread());
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Hint: (coop) Exception thrown by constructor: " + e);
        }
    }
    
    public void testSleepAdv()
    {
        try
        {
            HashMap<String, Integer> symbols = 
                new HashMap<String, Integer>();
            VMThread t1 = new VMThread(
                    new BufferedReader(new StringReader(
                        "push x\n" +
                        "sleep 100\n" +
                        "push 3\n" +
                        "add\n" +
                        "pop x\n" 
                        )), symbols);
                
            VMThread t2 = new VMThread(
                        new BufferedReader(new StringReader(
                            "push 5\n" +
                            "yield\n" +
                            "pop x\n" 
                            )), symbols);
            
            VMThread t3 = new VMThread(
                    new BufferedReader(new StringReader(
                        "push 5\n" +
                        "yield\n" +
                        "pop x\n" 
                        )), symbols);
            
            List<VMThread> threadList = new ArrayList<VMThread>();
            threadList.add(t1);
            threadList.add(t2);
            threadList.add(t3);
            CoopScheduler scheduler = new CoopScheduler(threadList);
            
            assertSame("Hint: (coop) getCurrentThread returns wrong object",
                t1, scheduler.getCurrentThread());
            assertEquals("Hint: (coop) symbol table should be empty initially",
                0, symbols.size());
            
            scheduler.run(2);
            assertSame("Hint: (coop) getCurrentThread returns wrong object",
                    t2, scheduler.getCurrentThread());
            scheduler.run(2);
            assertSame("Hint: (coop) getCurrentThread returns wrong object",
                    t3, scheduler.getCurrentThread());
            scheduler.run(6);
            assertNull(scheduler.getCurrentThread());
            
            scheduler.run(3);
            
            assertNull(scheduler.getCurrentThread());
            
            
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Hint: (coop) Exception thrown by constructor: " + e);
        }
    }
    
    public void testSleepAdv2()
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
                        )), symbols);
                
            VMThread t2 = new VMThread(
                        new BufferedReader(new StringReader(
                        	"sleep 4\n" +
                            "push 5\n" +
                            "push 33\n" +
                            "pop x\n" 
                            )), symbols);
            
            VMThread t3 = new VMThread(
                    new BufferedReader(new StringReader(
                        "push 5\n" +
                        "push 4\n" +
                        "pop x\n" 
                        )), symbols);
            
            List<VMThread> threadList = new ArrayList<VMThread>();
            threadList.add(t3);
            threadList.add(t2);
            threadList.add(t1);
            CoopScheduler scheduler = new CoopScheduler(threadList);
            
            assertSame("Hint: (coop) getCurrentThread returns wrong object",
                t3, scheduler.getCurrentThread());
            assertEquals("Hint: (coop) symbol table should be empty initially",
                0, symbols.size());
            
            scheduler.run(3);
            assertSame("Hint: (coop) getCurrentThread returns wrong object",
                    t2, scheduler.getCurrentThread());

            scheduler.run(2);
            assertSame("Hint: (coop) getCurrentThread returns wrong object",
                    t1, scheduler.getCurrentThread());
            
            scheduler.run(4);
            assertSame("Hint: (coop) getCurrentThread returns wrong object",
                    t2, scheduler.getCurrentThread());
            
            
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Hint: (coop) Exception thrown by constructor: " + e);
        }
    }
    
    public void testSleepAdv3()
    {
        try
        {
            HashMap<String, Integer> symbols = 
                new HashMap<String, Integer>();
            VMThread t1 = new VMThread(
                    new BufferedReader(new StringReader(
                        "sleep 3\n" +
                        "push 3\n" +
                        "add\n" +
                        "pop x\n" 
                        )), symbols);
                
            VMThread t2 = new VMThread(
                        new BufferedReader(new StringReader(
                        	"sleep 4\n" +
                            "push 5\n" +
                            "push 33\n" +
                            "pop x\n" 
                            )), symbols);
            
            VMThread t3 = new VMThread(
                    new BufferedReader(new StringReader(
                        "sleep 5\n" +
                        "push 4\n" +
                        "pop x\n" 
                        )), symbols);
            
            List<VMThread> threadList = new ArrayList<VMThread>();
            threadList.add(t3);
            threadList.add(t2);
            threadList.add(t1);
            CoopScheduler scheduler = new CoopScheduler(threadList);
            
            assertSame("Hint: (coop) getCurrentThread returns wrong object",
                t3, scheduler.getCurrentThread());
            assertEquals("Hint: (coop) symbol table should be empty initially",
                0, symbols.size());
            
            scheduler.run(7);
            assertSame("Hint: (coop) getCurrentThread returns wrong object",
                    t3, scheduler.getCurrentThread());

            
            
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Hint: (coop) Exception thrown by constructor: " + e);
        }
    }
    
    public void testSleepAdv4()
    {
        try
        {
            HashMap<String, Integer> symbols = 
                new HashMap<String, Integer>();
                VMThread t1 = new VMThread(
                    new BufferedReader(new StringReader(
                    		//0
                        "push 5\n" + 	//1
                        "sleep 3\n" +   //2
                        "pop x\n")), symbols);
                
                VMThread t2 = new VMThread(
                    new BufferedReader(new StringReader(
                        "push 6\n" +  //3 (2)
                        "sleep 2\n" + //4 (1)
                        			//5 null (0)
                        "pop z\n")), symbols);
            
            List<VMThread> threadList = new ArrayList<VMThread>();
            threadList.add(t1);
            threadList.add(t2);
            
            CoopScheduler scheduler = new CoopScheduler(threadList);
            
            assertSame("Hint: (coop) getCurrentThread returns wrong object",
                t1, scheduler.getCurrentThread());
            assertEquals("Hint: (coop) symbol table should be empty initially",
                0, symbols.size());
            
            scheduler.run(1);
            assertSame("Hint: (coop) getCurrentThread returns wrong object",
                    t1, scheduler.getCurrentThread());
            
            scheduler.run(1);
            assertSame("Hint: (coop) getCurrentThread returns wrong object",
                    t2, scheduler.getCurrentThread());
            
            scheduler.run(1);
            assertSame("Hint: (coop) getCurrentThread returns wrong object",
                    t2, scheduler.getCurrentThread());
            
            scheduler.run(1);
            assertNull(scheduler.getCurrentThread());
            
            scheduler.run(2);
            assertSame("Hint: (coop) getCurrentThread returns wrong object",
                    t1, scheduler.getCurrentThread());

            
            
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail("Hint: (coop) Exception thrown by constructor: " + e);
        }
    }
    		
    
    //  TODO: Your tests here
}

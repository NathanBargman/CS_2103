import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;

/**
 * Code to test an <tt>LRUCache</tt> implementation.
 */
public class CacheTest {
	
	//Creates a class which conatins a HashMap of Type DataProvider
	public class DataBase<T, U> implements DataProvider<T, U>{
		final protected HashMap<T, U> list;
		private int numMisses;
		public DataBase (int capacity) {
			list = new HashMap<T, U>(capacity);
			numMisses = 0;
		}
		public U get (T key) {
			numMisses += 1;
			return list.get(key);
		}
		public int getNumMisses() {return numMisses;}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initData(DataBase store) {
		store.list.put(2, "Jerry");
		store.list.put(45, "George");
		store.list.put(57, "Kramer");
		store.list.put(9, "Newman");
		store.list.put(12, "Susan");
		store.list.put(28, "Frank");
		store.list.put(73, "Morty");
		store.list.put(67, "Estella");
		store.list.put(91, "Helen");
		store.list.put(32, "Ruthie");
	}
    
	@Test
	public void basicFunction() {
		DataProvider<Integer,String> provider = new DataBase<Integer, String>(10);
		initData((DataBase<Integer, String>) provider);
		Cache<Integer,String> cache = new LRUCache<Integer,String>(provider, 5);
		assert cache.get(2).equals("Jerry");
		assert cache.getNumMisses() == 1; //Jerry does not start in the Cache so the first get misses
		assert cache.get(2).equals("Jerry");
		assert cache.getNumMisses() == 1; //Jerry was added to the Cache so there is a hit
	}
	@Test
	public void leastRecentlyUsedIsCorrect () {
		DataProvider<Integer,String> provider = new DataBase<Integer, String>(10);
		initData((DataBase<Integer, String>) provider);
		Cache<Integer,String> cache = new LRUCache<Integer,String>(provider, 5);
		assert cache.get(2).equals("Jerry");
		assert cache.get(45).equals("George");
		assert cache.get(57).equals("Kramer");
		assert cache.get(9).equals("Newman");
		assert cache.get(12).equals("Susan");
		assert cache.getNumMisses() == 5; //Fill the Cache of 5
		assert cache.get(28).equals("Frank");
		assert cache.getNumMisses() == 6; //Add a 6th member eviction Jerry
		assert cache.get(2).equals("Jerry");
		assert cache.getNumMisses() == 7; //Since Jerry is no longer in the Cache it will be another miss
		assert cache.get(57).equals("Kramer");
		assert cache.getNumMisses() == 7; //Kramer is currently the least used but will not be evicted because he is still in the cache
		assert cache.get(45).equals("George");
		assert cache.getNumMisses() == 8; //If we then search for George who has been evicted it should kick out the least used member which should not be Kramer
		assert cache.get(57).equals("Kramer");
		assert cache.getNumMisses() == 8; //Kramer has returned to the top of cache
	}
	@Test
	public void cacheOfOne() {
		DataProvider<Integer,String> provider = new DataBase<Integer, String>(10);
		initData((DataBase<Integer, String>) provider);
		Cache<Integer,String> cache = new LRUCache<Integer,String>(provider, 1);
		assert cache.get(2).equals("Jerry");
		assert cache.getNumMisses() == 1;
		assert cache.get(2).equals("Jerry");
		assert cache.getNumMisses() == 1;
		assert cache.get(45).equals("George");
		assert cache.getNumMisses() == 2; 
		assert cache.get(2).equals("Jerry");
		assert cache.getNumMisses() == 3; //Since the Cache is only 1 element long it cant store both Jerry and George
	}
	@Test
	public void smallCache() {
		DataProvider<Integer,String> provider = new DataBase<Integer, String>(10);
		initData((DataBase<Integer, String>) provider);
		Cache<Integer,String> cache = new LRUCache<Integer,String>(provider, 2);
		assert cache.get(2).equals("Jerry");
		assert cache.getNumMisses() == 1;
		assert cache.get(2).equals("Jerry");
		assert cache.getNumMisses() == 1;
		assert cache.get(45).equals("George");
		assert cache.getNumMisses() == 2; 
		assert cache.get(2).equals("Jerry");
		assert cache.getNumMisses() == 2;
		assert cache.get(57).equals("Kramer");
		assert cache.getNumMisses() == 3;
		assert cache.get(45).equals("George");
		assert cache.getNumMisses() == 4; //This checks if the cache properly swaps linked list variables with a small cache
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void initEdgeData(DataBase store) {
		store.list.put(0, "Jerry");
		store.list.put(45, "George");
		store.list.put(57, null);
		store.list.put(9, "Newman");
		store.list.put(12, "Susan");
	}
	
	@Test
	public void edgeCases() {
		DataProvider<Integer,String> provider = new DataBase<Integer, String>(5);
		initEdgeData((DataBase<Integer, String>) provider);
		Cache<Integer,String> cache = new LRUCache<Integer,String>(provider, 3);
		assert cache.get(0).equals("Jerry");
		assert cache.getNumMisses() == 1;
		assert cache.get(0).equals("Jerry");
		assert cache.getNumMisses() == 1;
		assert cache.get(57) == null;
		assert cache.getNumMisses() == 2; 
		assert cache.get(57) == null;
		assert cache.getNumMisses() == 2;
	}
}

import java.util.HashMap;

/**
 * An implementation of <tt>Cache</tt> that uses a least-recently-used (LRU)
 * eviction policy.
 */
@SuppressWarnings("unused")
public class LRUCache<T, U> implements Cache<T, U> {
	/**
	 * @param provider the data provider to consult for a cache miss
	 * @param capacity the exact number of (key,value) pairs to store in the cache
	 */
	private final DataProvider<T, U> PROVIDER;
	private final int CAPACITY;
	private int numMisses;
	private HashMap<T, item> list;
	private item head = null;
	private item tail = null;
	//Items are objects that will join to make a doubly linked list inside of the HashMap
	private class item{
		private final U DATA;
		protected final T KEY;
		protected item next;
		protected item last;
		public item (U data, T key, item _next, item _last) {
			DATA = data;
			KEY = key;
			next = _next; //this will be the most resently used item in the list
			last = _last; //this will be the least used item in the list
		}
	}
	public LRUCache (DataProvider<T, U> provider, int capacity) {
		CAPACITY = capacity;
		PROVIDER = provider;
		list = new HashMap<T, item> (CAPACITY);
	}
	/**
	 * Returns the value associated with the specified key.
	 * @param key the key
	 * @return the value associated with the key
	 */
	@SuppressWarnings("unchecked")
	public U get (T key) {
		final item _temp;
		//First check if the key is in the cache
		if (list.containsKey(key)) {
			_temp = (LRUCache<T, U>.item) list.get(key);
			if(head != _temp) {
				if(tail != _temp) {
					//if it is not the head or the tail take it out and patch the hole
					_temp.last.next = _temp.next;
					_temp.next.last = _temp.last;
				}else {
					//if it is the tail only the back needs to be patched
					_temp.next.last = null;
					tail = _temp.next;
				}
				//after somthing is searched for make that the most used i.e. the head of the linked list
				_temp.last = head;
				head.next = _temp;
				head = _temp;
			}
		}else {	
			//if the key is not in the cache it counts as a miss
			numMisses += 1;
			//if the item does not already exist make one
			_temp = new item (PROVIDER.get(key), key, null, null);
			if(list.size() == CAPACITY) {
				//if the cache is full evict the least used one i.e. the tail of the linked list
				item _oldTail = (LRUCache<T, U>.item) list.get(tail.KEY);
				tail = _oldTail.next;
				list.remove(_oldTail.KEY);
			}
			if(head == null) {
				//if this is the  first item to enter the list make it the head and tail
				head = _temp;
				tail = _temp;
				//make sure the item is also in the HashMap
				list.put(_temp.KEY, _temp);
			}else {
				if(CAPACITY > 1) {	
					//if the capacity is greater than one there needs to be a new head and the front must be patched
					head.next = _temp;
					_temp.last = head;
					head = _temp;
					list.put(_temp.KEY, _temp);
				}else {
					//if the capacity is 1 than it only needs to replace the old head
					list.remove(head.KEY);
					head = _temp;
					tail = _temp;
					list.put(_temp.KEY, _temp);
				}
			}
		}
		//return the data of the item found
		return _temp.DATA;
	}
	/**
	 * Returns the number of cache misses since the object's instantiation.
	 * @return the number of cache misses since the object's instantiation.
	 */
	public int getNumMisses () {
		return numMisses;
	}
}

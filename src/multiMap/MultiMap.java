package multiMap;

import java.util.*;
import java.util.function.*;

public class MultiMap<K, V> {

	HashMap<K, HashSet<V>> map = new HashMap<>();

	public void put(K key, V value) {

		HashSet<V> values = map.get(key);

		if(values == null) {

			values = new HashSet<V>();
			map.put(key, values);

		}
		values.add(value);

	}

	public HashSet<V> get(K key) {

		HashSet<V> values = map.get(key);

		return values;

	}

	public void remove(K key, V value) {

		HashSet<V> values = map.get(key);

		values.remove(value);

		if(values.isEmpty()) {
			
			map.remove(key);

		}

	}

	public void removeIf(Predicate<V> predicate) {

		Iterator<K> mapIterator = map.keySet().iterator();

		while(mapIterator.hasNext()) {

			HashSet<V> values = map.get(mapIterator.next());
			Iterator<V> setIterator = values.iterator();

			while(setIterator.hasNext()) {

				V nextItem = setIterator.next();

				if(predicate.test(nextItem)) {

					setIterator.remove();

					if(values.isEmpty()) {

						mapIterator.remove();

					}

				}

			}
		}

	}

	public boolean isEmpty() {

		return map.isEmpty();

	}

	@Override
	public String toString() {
		
		String string = "";

		for(K key : map.keySet()) {

			string += key + ": " + map.get(key) + "\n";

		}

		return string;

	}

}

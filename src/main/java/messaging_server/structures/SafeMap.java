package messaging_server.structures;


import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class SafeMap<K,V> {
    protected final ConcurrentHashMap<K, V> map = new ConcurrentHashMap<>();

    // Adds an element with (key,value) to the map if the key does not exist
    // Will return True if the element was added and false if it was not
    public boolean add(K key, V value) {
        if(map.containsKey(key)){
            return false;
        } else {
            map.put(key, value);
        }
        return true;
    }

    // Remove a specific element from the map by key
    public void removeElementByKey(K key) {
        this.map.remove(key);
    }

    // Check if key exists in map
    public boolean exists(K key) {
        return map.containsKey(key);
    }

    // Export a copy of the keys as a list
    public ArrayList<K> exportKeysAsList() {
        return new ArrayList<>(map.keySet());
    }

    public ArrayList<V> exportValuesAsList() { return new ArrayList<>(map.values());}

    // Check if key exists in map
    public V get(K key) {
        return map.get(key);
    }

}

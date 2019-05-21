package eg.edu.alexu.csd.filestructure.redblacktree;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.management.RuntimeErrorException;

public class TreeMap<T extends Comparable<T>, V> implements ITreeMap<T, V> {
	RedBlackTree<T, V> RBT = new RedBlackTree<>();

	@Override
	public Entry<T, V> ceilingEntry(T key) {
		if (key == null)
			throw new RuntimeErrorException(null);
		INode<T, V> node = RBT.searchKeyCeil(key);
		if (node == null)
			return null;
		Entry<T, V> entry = new AbstractMap.SimpleEntry<T, V>(node.getKey(), node.getValue());
		return entry;
	}

	@Override
	public T ceilingKey(T key) {
		if (key == null)
			throw new RuntimeErrorException(null);
		INode<T, V> node = RBT.searchKeyCeil(key);
		if (node != null)
			return node.getKey();
		return null;
	}

	@Override
	public void clear() {
		RBT.clear();
	}

	@Override
	public boolean containsKey(T key) {
		if (key == null)
			throw new RuntimeErrorException(null);
		return RBT.contains(key);
	}

	@Override
	public boolean containsValue(V value) {
		if(value==null) {
			throw new RuntimeErrorException(null); 
		}
		if(values().contains(value)) {
			return true;
		}
		return false;
	}

	@Override
	public Set<Entry<T, V>> entrySet() {
		Set<Entry<T, V>> data = new LinkedHashSet<>();
		Iterator<INode<T, V>> itt = RBT.getNCollection().iterator();
		while(itt.hasNext()) {
			INode<T, V> node = itt.next();
			Entry<T, V> entry = new AbstractMap.SimpleEntry<T, V>(node.getKey(), node.getValue());
			data.add(entry);
		}
		return data;
	}

	@Override
	public Entry<T, V> firstEntry() {
		if (RBT.isEmpty())
			return null;
		INode<T, V> node = RBT.GetMin(RBT.getRoot());
		Entry<T, V> entry = new AbstractMap.SimpleEntry<T, V>(node.getKey(), node.getValue());
		return entry;
	}

	@Override
	public T firstKey() {
		if (RBT.isEmpty())
			return null;
		return RBT.GetMin(RBT.getRoot()).getKey();
	}

	@Override
	public Entry<T, V> floorEntry(T key) {
		if (key == null)
			throw new RuntimeErrorException(null);
		INode<T, V> node = RBT.searchKeyFloor(key);
		if (node == null)
			return null;
		Entry<T, V> entry = new AbstractMap.SimpleEntry<T, V>(node.getKey(), node.getValue());
		return entry;
	}

	@Override
	public T floorKey(T key) {
		if (key == null)
			throw new RuntimeErrorException(null);
		INode<T, V> node = RBT.searchKeyFloor(key);
		if (node == null)
			return null;
		return node.getKey();
	}

	@Override
	public V get(T key) {
		return RBT.search(key);
	}

	@Override
	public ArrayList<Entry<T, V>> headMap(T toKey) {
		if (RBT.isEmpty())
			return null;
		ArrayList<Entry<T, V>> headMap = new ArrayList<>();
		Iterator<INode<T, V>> itt = RBT.getNCollection().iterator();
		while (itt.hasNext()) {
			INode<T, V> node = itt.next();
			if (node.getKey().compareTo(toKey) >= 0)
				break;
			headMap.add(new AbstractMap.SimpleEntry<T, V>(node.getKey(), node.getValue()));
		}
		return headMap;
	}

	@Override
	public ArrayList<Entry<T, V>> headMap(T toKey, boolean inclusive) {
		if (RBT.isEmpty())
			return null;
		ArrayList<Entry<T, V>> headMap = new ArrayList<>();
		Iterator<INode<T, V>> itt = RBT.getNCollection().iterator();
		while (itt.hasNext()) {
			INode<T, V> node = itt.next();
			if (inclusive) {
				if (node.getKey().compareTo(toKey) > 0)
					break;
			} else {
				if (node.getKey().compareTo(toKey) >= 0)
					break;
			}
			headMap.add(new AbstractMap.SimpleEntry<T, V>(node.getKey(), node.getValue()));
		}
		return headMap;
	}

	@Override
	public Set<T> keySet() {
		if (RBT.isEmpty())
			return null;
		Set<T> set = new LinkedHashSet<>();
		Iterator<INode<T, V>> itt = RBT.getNCollection().iterator();
		while (itt.hasNext()) {
			set.add(itt.next().getKey());
		}
		return set;
	}

	@Override
	public Entry<T, V> lastEntry() {
		if (RBT.isEmpty())
			return null;
		INode<T, V> node = RBT.GetMax(RBT.getRoot());
		Entry<T, V> entry = new AbstractMap.SimpleEntry<T, V>(node.getKey(), node.getValue());
		return entry;
	}

	@Override
	public T lastKey() {
		if (RBT.isEmpty())
			return null;
		return RBT.GetMax(RBT.getRoot()).getKey();
	}

	@Override
	public Entry<T, V> pollFirstEntry() {
		if (RBT.isEmpty())
			return null;
		INode<T, V> node = RBT.GetMin(RBT.getRoot());
		RBT.delete(node.getKey());
		Entry<T, V> entry = new AbstractMap.SimpleEntry<T, V>(node.getKey(), node.getValue());
		return entry;
	}

	@Override
	public Entry<T, V> pollLastEntry() {
		if (RBT.isEmpty())
			return null;
		INode<T, V> node = RBT.GetMax(RBT.getRoot());
		RBT.delete(node.getKey());
		Entry<T, V> entry = new AbstractMap.SimpleEntry<T, V>(node.getKey(), node.getValue());
		return entry;
	}

	@Override
	public void put(T key, V value) {
		RBT.insert(key, value);
	}

	@Override
	public void putAll(Map<T, V> map) {
		if (map == null || map.isEmpty())
			throw new RuntimeErrorException(null);
		for (Map.Entry<T, V> entry : map.entrySet()) {
			RBT.insert(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public boolean remove(T key) {
		return RBT.delete(key);
	}

	@Override
	public int size() {
		return RBT.getSize();
	}

	@Override
	public Collection<V> values() {
		Collection<V> collection = new ArrayList<>();
		Iterator<INode<T, V>> itt = RBT.getNCollection().iterator();
		while (itt.hasNext()) {
			collection.add(itt.next().getValue());
		}
		return collection;
	}

}

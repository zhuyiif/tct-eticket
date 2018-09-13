package com.funenc.eticket.api.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.ws.rs.core.MultivaluedMap;

public class MetadataMap<K, V> implements MultivaluedMap<K, V> {

    private boolean caseInsensitive;
    private Map<K, List<V>> m;

    public MetadataMap() {
        this.m = new LinkedHashMap<K, List<V>>();
    }

    public MetadataMap(int size) {
        this.m = new LinkedHashMap<K, List<V>>(size);
    }

    public MetadataMap(Map<K, List<V>> store) {
        this(store, false, false);
    }

    public MetadataMap(boolean readOnly, boolean caseInsensitive) {
        this(null, readOnly, caseInsensitive);
    }

    public MetadataMap(Map<K, List<V>> store, boolean readOnly, boolean caseInsensitive) {

        this.caseInsensitive = caseInsensitive;

        this.m = new LinkedHashMap<K, List<V>>();
        if (store != null) {
            for (Map.Entry<K, List<V>> entry : store.entrySet()) {
                List<V> values = new ArrayList<V>(entry.getValue());
                m.put(entry.getKey(), readOnly
                        ? Collections.unmodifiableList(values) : values);
            }
        }
        if (readOnly) {
            this.m = Collections.unmodifiableMap(m);
        }

    }

    public void add(K key, V value) {
        List<V> data = this.get(key);
        if (data == null) {
            data = new ArrayList<V>();
            m.put(key, data);
        }
        data.add(value);
    }

    public V getFirst(K key) {
        List<V> data = this.get(key);
        return data == null ? null : data.get(0);
    }

    public void putSingle(K key, V value) {
        List<V> data = new ArrayList<V>();
        data.add(value);
        this.put(key, data);
    }

    public void clear() {
        m.clear();
    }

    public boolean containsKey(Object key) {
        if (!caseInsensitive) {
            return m.containsKey(key);
        }
        return getMatchingKey(key) != null;
    }

    public boolean containsValue(Object value) {
        return m.containsValue(value);
    }

    public Set<Entry<K, List<V>>> entrySet() {
        return m.entrySet();
    }

    public List<V> get(Object key) {
        if (!caseInsensitive) {
            return m.get(key);
        }
        K realKey = getMatchingKey(key);
        return realKey == null ? null : m.get(realKey);
    }

    private K getMatchingKey(Object key) {
        for (K entry : m.keySet()) {
            if (entry.toString().equalsIgnoreCase(key.toString())) {
                return entry;
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return m.isEmpty();
    }

    public Set<K> keySet() {
        if (!caseInsensitive) {
            return m.keySet();
        } else {
            Set<K> set = new TreeSet<K>(new KeyComparator<K>());
            set.addAll(m.keySet());
            return set;
        }
    }

    public List<V> put(K key, List<V> value) {
        K realKey = !caseInsensitive ? key : getMatchingKey(key);
        return m.put(realKey == null ? key : realKey, value);
    }

    public void putAll(Map<? extends K, ? extends List<V>> map) {
        if (!caseInsensitive) {
            m.putAll(map);
        } else {
            for (Map.Entry<? extends K, ? extends List<V>> entry : map.entrySet()) {
                this.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public List<V> remove(Object key) {
        if (caseInsensitive) {
            K realKey = getMatchingKey(key);
            return m.remove(realKey == null ? key : realKey);
        } else {
            return m.remove(key);
        }
    }

    public int size() {
        return m.size();
    }

    public Collection<List<V>> values() {
        return m.values();
    }

    @Override
    public int hashCode() {
        return m.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return m.equals(o);
    }

    public String toString() {
        return m.toString();
    }

    private static class KeyComparator<K> implements Comparator<K> {

        public int compare(K k1, K k2) {
            String s1 = k1.toString();
            String s2 = k2.toString();
            return s1.compareToIgnoreCase(s2);
        }

    }
}


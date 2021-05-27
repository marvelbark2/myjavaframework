package ws.prospeak.myweb.framework.Illuminate.collection;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.collections4.bag.HashBag;
import ws.prospeak.myweb.framework.Illuminate.database.orm.Models;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Collection<T> implements Serializable, Bag<T> {
    private Bag<T> collection;
    private final ObjectMapper mapper = new ObjectMapper();


    public Collection(Object array) {
        collection = new HashBag<>((java.util.Collection<T>)array);
    }

    public Collection<T> where(Object key, Object sort) {
        Bag<T> bag = new HashBag<>();
        for (Object o: collection) {
            Models model = (Models) o;
            Map<Object, Models> hm = model.map();
            if(String.valueOf(hm.get(key)).equals(String.valueOf(sort)))
                bag.add((T) model);
        }
        return new Collection<>(bag);
    }


    public Bag getCollection() {
        return collection;
    }

    @SneakyThrows
    @Override
    public String toString() {
        return mapper.writeValueAsString(this.collection);
    }

    @Override
    public int getCount(Object o) {
        return collection.getCount(o);
    }

    @Override
    public boolean add(T t) {
        return collection.add(t);
    }

    @Override
    public boolean add(T t, int i) {
        return collection.add(t, i);
    }

    @Override
    public boolean remove(Object o) {
        return collection.remove(o);
    }

    @Override
    public boolean remove(Object o, int i) {
        return remove(o, i);
    }

    @Override
    public Set<T> uniqueSet() {
        return collection.uniqueSet();
    }

    @Override
    public int size() {
        return collection.size();
    }

    @Override
    public boolean isEmpty() {
        return collection.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return collection.contains(o);
    }

    @Override
    public boolean containsAll(java.util.Collection<?> collection) {
        return this.collection.containsAll(collection);
    }

    @Override
    public boolean addAll(java.util.Collection<? extends T> c) {
        return collection.addAll(c);
    }

    @Override
    public boolean removeAll(java.util.Collection<?> collection) {
        return this.collection.removeAll(collection);
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        return collection.removeIf(filter);
    }

    @Override
    public boolean retainAll(java.util.Collection<?> collection) {
        return this.collection.retainAll(collection);
    }

    @Override
    public void clear() {
        collection.clear();
    }

    @Override
    public Spliterator<T> spliterator() {
        return collection.spliterator();
    }

    @Override
    public Stream<T> stream() {
        return collection.stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return collection.parallelStream();
    }

    @Override
    public Iterator<T> iterator() {
        return collection.iterator();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        collection.forEach(action);
    }

    @Override
    public Object[] toArray() {
        return collection.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return collection.toArray(a);
    }

    @Override
    public <T1> T1[] toArray(IntFunction<T1[]> generator) {
        return collection.toArray(generator);
    }

    public Collection<T> execpt(Object key) {
        Bag<T> bag = new HashBag<>();
        for (Object o: collection) {
            Models model = (Models) o;
            Map<Object, Models> hm = model.map();
            hm.remove(key);
            T newModel = (T) mapper.convertValue(hm, Object.class);
            bag.add(newModel);
        }
        return new Collection<>(bag);
    }
    public Collection<Object> modelKeys() {
        Bag<Object> bag = new HashBag<>();
        for (Object o: collection) {
            Models model = (Models) o;
            Map<Object, Models> hm = model.map();
            String keyAssigned = model.getPrimaryKey();
            String key = keyAssigned == null ? "id" : keyAssigned;
            bag.add(hm.get(key));
        }
        return new Collection<>(bag);
    }

    public T find(Object id) throws IOException {
        Collection<T> self = this;
        for (T t: self) {
            Models model = (Models) t;
            Map hm = model.map();
            String keyAssigned = model.getPrimaryKey();
            String key = keyAssigned == null ? "id" : keyAssigned;
            if(hm.get(key) != null){
                if(hm.get(key) == id || hm.get(key).equals(id))
                    return (T) model;
            }
        }
        return null;
    }

    public Collection<T> intersaction(Collection<T> b) {
        Bag aCollection = this.getCollection();
        Bag bCollection = b.getCollection();
        Object ab = SetUtils.intersection(aCollection.uniqueSet(), bCollection.uniqueSet());
        return new Collection<>(ab);
    }
    public Collection<T> diff(Collection<T> b) {
        Bag aCollection = this.getCollection();
        Bag bCollection = b.getCollection();
        Object ab = SetUtils.difference(aCollection.uniqueSet(), bCollection.uniqueSet());
        return new Collection<>(ab);
    }
}

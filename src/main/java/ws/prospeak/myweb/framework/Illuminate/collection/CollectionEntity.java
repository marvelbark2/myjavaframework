package ws.prospeak.myweb.framework.Illuminate.collection;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.collections4.comparators.ComparatorChain;
import ws.prospeak.myweb.framework.Illuminate.database.orm.Models;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class CollectionEntity<T> implements Serializable, Collection<T> {
    private static final long serialVersionUID = 941747885320975292L;

    private final Collection<T> collection;
    private final ObjectMapper mapper = new ObjectMapper();


    public CollectionEntity(Object array) {
        collection = new HashBag<>((java.util.Collection<T>)array);
        mapper.registerModule(new JavaTimeModule());
    }
    public CollectionEntity(LinkedList<T> array) {
        mapper.registerModule(new JavaTimeModule());
        collection = CollectionUtils.emptyIfNull(array);
        Set<T> hSet = new HashSet<>();
        for (T x : array)
            hSet.add(x);

        System.out.println("Created HashSet is");
        for (T x : hSet)
            System.out.println(x);
    }

    public CollectionEntity<T> where(Object key, Object sort) {
        Bag<T> bag = new HashBag<>();
        for (Object o: collection) {
            Models model = (Models) o;
            Map<Object, Models> hm = model.map();
            if(String.valueOf(hm.get(key)).equals(String.valueOf(sort)))
                bag.add((T) model);
        }
        return new CollectionEntity<>(bag);
    }


    public Bag<T> toBag() {
        return (Bag<T>) collection;
    }

    public Collection<T> getCollection() {
        return collection;
    }

    @SneakyThrows
    @Override
    public String toString() {
        return mapper.writeValueAsString(this.collection);
    }

    public int getCount(Object o) {
        return ((Bag<T>) collection).getCount(o);
    }

    @Override
    public boolean add(T t) {
        return collection.add(t);
    }

    public boolean add(T t, int i) {
        return ((Bag<T>) collection).add(t, i);
    }

    @Override
    public boolean remove(Object o) {
        return collection.remove(o);
    }

    public boolean remove(Object o, int i) {
        return ((Bag<T>) collection).remove(o, i);
    }

    public Set<T> uniqueSet() {
        return ((Bag<T>) collection).uniqueSet();
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

    public CollectionEntity<T> execpt(Object key) {
        Bag<T> bag = new HashBag<>();
        for (Object o: collection) {
            Models model = (Models) o;
            Map<Object, Models> hm = model.map();
            hm.remove(key);
            T newModel = (T) mapper.convertValue(hm, Object.class);
            bag.add(newModel);
        }
        return new CollectionEntity<>(bag);
    }
    public CollectionEntity<Object> modelKeys() {
        Bag<Object> bag = new HashBag<>();
        for (Object o: collection) {
            Models model = (Models) o;
            Map<Object, Models> hm = model.map();
            String keyAssigned = model.getPrimaryKey();
            String key = keyAssigned == null ? "id" : keyAssigned;
            bag.add(hm.get(key));
        }
        return new CollectionEntity<>(bag);
    }

    public T find(Object id) throws IOException {
        CollectionEntity<T> self = this;
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

    public CollectionEntity<T> intersaction(CollectionEntity<T> b) {
        Bag<T> aCollection = this.toBag();
        Bag<T> bCollection = b.toBag();
        Object ab = SetUtils.intersection(aCollection.uniqueSet(), bCollection.uniqueSet());
        return new CollectionEntity<>(ab);
    }

    public CollectionEntity<T> diff(CollectionEntity<T> b) {
        Bag<T> aCollection = this.toBag();
        Bag<T> bCollection = b.toBag();
        Object ab = SetUtils.difference(aCollection.uniqueSet(), bCollection.uniqueSet());
        return new CollectionEntity<>(ab);
    }

    public CollectionEntity<T> union(CollectionEntity<T> b) {
        Bag<T> aCollection = this.toBag();
        Bag<T> bCollection = b.toBag();
        Object ab = SetUtils.union(aCollection.uniqueSet(), bCollection.uniqueSet());
        return new CollectionEntity<>(ab);
    }
    public CollectionEntity<T> sortBy(Object key) {
        List<T> aCollection = new ArrayList<>(this.getCollection());
        ComparatorChain<T> comparator = new ComparatorChain<>();
        comparator.addComparator(new BeanComparator(String.valueOf(key)));
        aCollection.sort(comparator);
        return new CollectionEntity<>(new LinkedList<>(aCollection));
    }

}

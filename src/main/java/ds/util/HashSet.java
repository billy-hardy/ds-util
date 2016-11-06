package ds.util;

import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

@SuppressWarnings("unchecked")
public class HashSet<T> {

    private List<T>[] buckets;
    private int numBuckets;
    private int size;
    private static final int DEFAULT_NUM_BUCKETS = 100;

    public HashSet() {
	this(DEFAULT_NUM_BUCKETS);
    }

    public HashSet(int numBuckets) {
	this.numBuckets = numBuckets;
	this.buckets = new List[this.numBuckets];
	this.size = 0;
    }

    private int indexOf(T o) {
	int hash = o.hashCode();
	return hash%this.numBuckets;
    }

    private List<T> getBucket(T o) {
	int bucketNum = this.indexOf(o);
	if(this.buckets[bucketNum] == null) {
	    this.buckets[bucketNum] = new LinkedList<>();
	}
	return this.buckets[bucketNum];
    } 

    public boolean add(T value) {
	if(!this.contains(value)) {
	    List<T> bucket = this.getBucket(value);
	    boolean added = bucket.add(value);
	    if(added) {
		this.size++;
	    }
	    return added;
	}
	return false;
    }

    public boolean addAll(Collection<T> c) {
	for(T val: c) {
	    if(!this.contains(val)) {
		return false;
	    }
	}
	for(T val: c) {
	    this.add(val);
	}
	return true;
    }

    public void clear() {
	//TODO: use iterator to do this
    }

    public boolean contains(T value) {
	List<T> bucket = this.getBucket(value);
	for(T o: bucket) {
	    if(o.equals(value)) {
		return true;
	    }
	}
	return false;
    }

    public boolean containsAll(Collection<T> c) {
	for(T val: c) {
	    if(!this.contains(val)) {
		return false;
	    }
	}
	return true;
    }

    public boolean isEmpty() {
	return this.size == 0;
    }

    public Iterator<T> iterator() {
	return new HashIterator();	
    }

    public boolean remove(T o) {
	List<T> bucket = this.getBucket(o);
	boolean removed = bucket.remove(o);
	if(removed) {
	    this.size--;
	}
	return removed;
    }

    public boolean removeAll(Collection<T> c) {
	for(T val: c) {
	    if(!this.remove(val)) {
		return false;
	    }
	}
	return true;
    }

    public boolean retainAll(Collection<T> c) {
	for(T val: c) {
	    if(!this.remove(val)) {
		return false;
	    }
	}
	return true;
    }
    public int size() {
	return this.size;
    }

    public Object[] toArray() {
	Iterator it = this.iterator();
	Object[] retVal = new Object[this.size()];
	int i = 0;
	while(it.hasNext()) {
	    retVal[i] = it.next();
	}
	return retVal; 
    }

    public T[] toArray(T[] a) {
	//TODO: use iterator
	return a;
    }

    private class HashIterator implements Iterator {
	private List<T>[] entries;
	private int currBucket;
	private int currIndex;

	public HashIterator() {
	    this.entries = buckets;
	    this.currBucket = 0;
	    this.currIndex = 0;
	}

	private List<T> getBucket(int bucketNum) {
	    if(this.currBucket < entries.length) {
		return this.entries[this.currBucket];
	    }
	    return null;
	}

	private Container getNext(Container o) {
	    Container c = new Container(o);
	    while(c.element == null) {
		List<T> bucket = this.getBucket(this.currBucket);
		if(bucket == null || bucket.size() < this.currIndex) {
		    c.currIndex = 0;
		    c.currBucket++;
		} else {
		    c.element = bucket.get(this.currIndex);
		}
	    }
	    return c;
	}

	private class Container {
	    public int currBucket;
	    public int currIndex;
	    public T element;

	    public Container() {
		this(0);
	    }
	    public Container(int currBucket) {
		this(currBucket, 0);
	    }
	    public Container(int currBucket, int currIndex) {
		this.currBucket = currBucket;
		this.currIndex = currIndex;
	    }
	    public Container(Container c) {
		this(c.currBucket, c.currIndex); 
	    }
	}

	public boolean hasNext() {
	    Container c = new Container(this.currBucket, this.currIndex);
	    Container next = this.getNext(c);
	    return next.element == null;
	}

	public T next() {
	    Container c = new Container(this.currBucket, this.currIndex);
	    Container next = this.getNext(c);
	    this.currBucket = c.currBucket;
	    this.currIndex = c.currIndex;
	    return next.element;
	}

	public void remove() {

	}
    }
}


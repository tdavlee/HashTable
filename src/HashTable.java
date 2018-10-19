import java.util.ArrayList;
import java.util.List;

public class HashTable<K, V> {
    private List<HashNode<K, V>> bucket;
    private int numBuckets;
    private int size;

    public HashTable(int initialBuckets) {
        bucket = new ArrayList<>();
        numBuckets = initialBuckets;
        size = 0;

        for (int i = 0; i < numBuckets; i++)
            bucket.add(null);
    }

    public void add(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        HashNode<K, V> head = bucket.get(bucketIndex);

        while (head != null) {
            if (head.key.equals(key)) {
                head.value = value;
                return;
            }
            head = head.next;
        }

        size++;
        head = bucket.get(bucketIndex);
        HashNode<K, V> newNode = new HashNode<>(key, value);
        newNode.next = head;
        bucket.set(bucketIndex, newNode);

        // Check if load factor is beyond threshold
        // double the size of the table if it is
        // Default load factor (0.75) offers a good tradeoff between time and space
        if ((1.0 * size)/numBuckets >= 0.75) {
            List<HashNode<K, V>> temp = bucket;
            numBuckets *= 2;
            size = 0;

            for (int i = 0; i < numBuckets; i++)
                bucket.add(null);

            for (HashNode<K, V> headNode : temp) {
                while (headNode != null) {
                    add(headNode.key, headNode.value);
                    headNode = headNode.next;
                }
            }
        }
    }

    public V remove(K key) {
        int bucketIndex = getBucketIndex(key);
        HashNode<K, V> head = bucket.get(bucketIndex);
        HashNode<K, V> prev = null;

        while (head != null) {
            if (head.key.equals(key))
                break;

            prev = head;
            head = head.next;
        }

        if (head == null)
            return null;

        size--;
        if (prev != null) {
            prev.next = head.next;
        } else {
            bucket.set(bucketIndex, head.next);
        }

        return head.value;
    }

    public V poll(K key) {
        int bucketIndex = getBucketIndex(key);
        HashNode<K, V> head = bucket.get(bucketIndex);

        if (head == null)
            return null;

        bucket.set(bucketIndex, head.next);

        return head.value;
    }

    public int getBucketIndex(K key) {
        int hash = key.hashCode();
        return Math.abs(hash) % numBuckets;
    }

    public V get(K key) {
        int bucketIndex = getBucketIndex(key);
        HashNode<K, V> head = bucket.get(bucketIndex);

        while (head != null) {
            if (head.key == key) {
                return head.value;
            }
            head = head.next;
        }
        return null;
    }

    public void printTable() {
        for (HashNode<K, V> hashNode : bucket) {
            while (hashNode != null) {
                System.out.print(hashNode + " | ");
                hashNode = hashNode.next;
            }
            System.out.println();
        }
    }
}

class HashNode<K, V> {
    K key;
    V value;
    HashNode<K, V> next;

    public HashNode(K key, V value) {
        this.key = key;
        this.value = value;
        next = null;
    }

    @Override
    public String toString() {
        return this.key + " " + this.value;
    }
}
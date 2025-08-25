package datastructures;

import java.util.Comparator;
import java.util.Random;
import java.util.SortedMap;


public class UzmaTreap<K,V> extends UzmaTreeMap<K,V> {
    private final Random random = new Random();

    protected static class TreapNode<E> extends BalanceableBinaryTree.BSTNode<E> {
        private int priority;
        public TreapNode(E element, BalanceableBinaryTree.Node<E> parent, BalanceableBinaryTree.Node<E> left, BalanceableBinaryTree.Node<E> right, int priority) {
            super(element, parent, left, right);
            this.priority = priority;
        }
        public int getPriority() {
            return priority;
        }
        public void setPriority(int p) {
            priority = p;
        }
    }

    protected BalanceableBinaryTree.Node<Entry<K,V>> createNode(Entry<K,V> entry, BalanceableBinaryTree.Node<Entry<K,V>> parent, BalanceableBinaryTree.Node<Entry<K,V>> left,
                                                                BalanceableBinaryTree.Node<Entry<K,V>> right) {
        return new TreapNode<>( entry, parent, left, right, random.nextInt(Integer.MAX_VALUE));
    }

    public UzmaTreap() {
        super();
        initTree();
    }

    public UzmaTreap(Comparator<K> comp) {
        super(comp);
        initTree();
    }

    private void initTree() {
        this.tree = new BalanceableBinaryTree<Entry<K,V>>() {
            @Override
            protected Node<Entry<K,V>> createNode(Entry<K,V> entry, Node<Entry<K,V>> parent, Node<Entry<K,V>> left, Node<Entry<K,V>> right) {
                return UzmaTreap.this.createNode(entry, parent, left, right);
            }
        };
        tree.addRoot(null);
    }

    @Override
    public V remove(Object key) {
        try {
            K k = (K) key;
            checkKey(k);

            Position<Entry<K,V>> p = treeSearch(root(), k);
            if (isExternal(p)) {
                rebalanceAccess(p);
                return null;
            }
            V old = p.getElement().getValue();

            while (isInternal(left(p)) && isInternal(right(p))) {
                Position<Entry<K,V>> L = left(p), R = right(p);
                TreapNode<Entry<K,V>> ln =
                        (TreapNode<Entry<K,V>>) tree.validate(L);
                TreapNode<Entry<K,V>> rn =
                        (TreapNode<Entry<K,V>>) tree.validate(R);

                if (ln.getPriority() < rn.getPriority()) {
                    tree.rotate(R);
                } else {
                    tree.rotate(L);
                }

            }

            Position<Entry<K,V>> leaf = isExternal(left(p)) ? left(p) : right(p);
            Position<Entry<K,V>> sib = sibling(leaf);
            tree.remove(leaf);
            tree.remove(p);
            return old;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Wrong key type.");
        }
    }


    @Override
    protected void rebalanceInsert(Position<Entry<K,V>> p) {
        Position<Entry<K,V>> cur = p;
        while (!isRoot(cur)) {
            Position<Entry<K,V>> par = parent(cur);
            TreapNode<Entry<K,V>> curnode = (TreapNode<Entry<K,V>>) tree.validate(cur);
            TreapNode<Entry<K,V>> prenode = (TreapNode<Entry<K,V>>) tree.validate(par);
            if (curnode.getPriority() <= prenode.getPriority()) {
                break;
            }
            tree.rotate(cur);
            cur = par;
        }
    }

    @Override
    protected void rebalanceDelete(Position<Entry<K,V>> p) { }

    @Override
    protected void rebalanceAccess(Position<Entry<K,V>> p) { }

    @Override
    public SortedMap<K,V> headMap(K toKey) {
        checkKey(toKey);
        UzmaTreap<K,V> result = new UzmaTreap<>(internalComparator());
        for (Entry<K,V> e : entrySet()) {
            if (compare(e.getKey(), toKey) < 0) {
                result.put(e.getKey(), e.getValue());
            } else {
                break;
            }
        }
        return result;
    }

    @Override
    public SortedMap<K,V> tailMap(K fromKey) {
        checkKey(fromKey);
        UzmaTreap<K,V> result = new UzmaTreap<>(internalComparator());
        boolean inRange = false;
        for (Entry<K,V> e : entrySet()) {
            if (!inRange) {
                if (compare(e.getKey(), fromKey) >= 0) {
                    inRange = true;
                } else {
                    continue;
                }
            }
            result.put(e.getKey(), e.getValue());
        }
        return result;
    }

    @Override
    public SortedMap<K,V> subMap(K fromKey, K toKey) {
        checkKey(fromKey);
        checkKey(toKey);
        if (compare(fromKey, toKey) > 0)
            throw new IllegalArgumentException("fromKey is greater than toKey");
        UzmaTreap<K,V> result = new UzmaTreap<>(internalComparator());
        for (Entry<K,V> e : entrySet()) {
            K k = e.getKey();
            if (compare(k, fromKey) >= 0 && compare(k, toKey) < 0) {
                result.put(k, e.getValue());
            }
            if (compare(k, toKey) >= 0) {
                break;
            }
        }
        return result;
    }
}

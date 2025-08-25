/**
 * Some code has been taken from Data Structures and Algorithms by Michael T.Goodrich, Roberto Tamassia and Michae; H.Goldwasser
 */
package datastructures;

import java.util.*;

public class UzmaTreeMap<K,V> extends AbstractSortedMap<K,V> {

    protected static class BalanceableBinaryTree<E> extends LinkedBinaryTree<E> {
        protected static class BSTNode<E> extends Node<E> {
            private int aux = 0;

            public BSTNode(E e, Node<E> parent, Node<E> leftChild, Node<E> rightChild) {
                super(e, parent, leftChild, rightChild);
            }

            public int getAux() { return aux; }
            public void setAux(int value) { aux = value; }
        }

        public int getAux(Position<E> p) {
            BSTNode<E> node = (BSTNode<E>) validate(p);
            return node.getAux();
        }
        public void setAux(Position<E> p, int value) {
            BSTNode<E> node = (BSTNode<E>) validate(p);
            node.setAux(value);
        }

        @Override
        protected Node<E> createNode(E e, Node<E> parent, Node<E> left, Node<E> right) {
            return new BSTNode<>(e, parent, left, right);
        }

        private void relink(Node<E> parent, Node<E> child, boolean makeLeftChild) {
            if (child != null) {
                child.setParent(parent);
            }
            if (makeLeftChild) {
                parent.setLeft(child);
            } else {
                parent.setRight(child);
            }
        }

        public void rotate(Position<E> p) {
            if (p == null) {
                return;
            }
            Node<E> x = validate(p);
            Node<E> y = x.getParent();
            if (y == null) {
                return;
            }
            Node<E> z = y.getParent();

            if (z == null) {
                root = x;
                x.setParent(null);
            } else {
                relink(z, x, y == z.getLeft());
            }
            if (x == y.getLeft()) {
                relink(y, x.getRight(), true);
                relink(x, y, false);
            } else {
                relink(y, x.getLeft(), false);
                relink(x, y, true);
            }
        }

        public Position<E> restructure(Position<E> x) {
            if (x == null) {
                return null;
            }
            Position<E> y = parent(x), z = (y==null ? null : parent(y));
            if (y == null || z == null) {
                return x;
            }
            if ((x == right(y)) == (y == right(z))) {
                rotate(y);
                return y;
            } else {
                rotate(x);
                rotate(x);
                return x;
            }
        }
    }

    protected BalanceableBinaryTree<Entry<K,V>> tree = new BalanceableBinaryTree<>();

    public UzmaTreeMap() {
        super();
        tree.addRoot(null);
    }

    public UzmaTreeMap(Comparator<K> comp) {
        super(comp);
        tree.addRoot(null);
    }

    @Override
    public int size() {
        return (tree.size() - 1) / 2;
    }

    protected Position<Entry<K,V>> root() { return tree.root(); }
    protected Position<Entry<K,V>> parent(Position<Entry<K,V>> p) { return tree.parent(p); }
    protected Position<Entry<K,V>> left(Position<Entry<K,V>> p) { return tree.left(p); }
    protected Position<Entry<K,V>> right(Position<Entry<K,V>> p) { return tree.right(p); }
    protected boolean isRoot(Position<Entry<K,V>> p) { return tree.isRoot(p); }
    protected boolean isInternal(Position<Entry<K,V>> p) { return tree.isInternal(p); }
    protected boolean isExternal(Position<Entry<K,V>> p) { return tree.isExternal(p); }
    protected Position<Entry<K,V>> sibling(Position<Entry<K,V>> p) { return tree.sibling(p); }
    protected void set(Position<Entry<K,V>> p, Map.Entry<K,V> e) { tree.set(p, e); }

    private void expandExternal(Position<Entry<K,V>> p, Entry<K,V> entry) {
        tree.set(p, entry);
        tree.addLeft(p, null);
        tree.addRight(p, null);
    }

    protected Position<Entry<K,V>> treeSearch(Position<Entry<K,V>> p, K key) {
        while (isInternal(p)) {
            int comp = compare(key, p.getElement().getKey());
            if (comp == 0) {
                return p;
            }
            p = (comp < 0) ? left(p) : right(p);
        }
        return p;
    }
    protected Position<Entry<K,V>> treeMax(Position<Entry<K,V>> p) {
        if (isExternal(p)) {
            return null;
        }
        Position<Entry<K,V>> walk = p;
        while (isInternal(right(walk))) {
            walk = right(walk);
        }
        return walk;
    }

    protected Position<Entry<K,V>> treeMin(Position<Entry<K,V>> p) {
        if (isExternal(p)) {
            return null;
        }
        Position<Entry<K,V>> walk = p;
        while (isInternal(left(walk))) {
            walk = left(walk);
        }
        return walk;
    }


    @Override
    public V get(Object key) {
        try {
            K k = (K) key;
            checkKey(k);
            Position<Entry<K,V>> p = treeSearch(root(), k);
            rebalanceAccess(p);
            if (isExternal(p)) {
                return null;
            }
            return p.getElement().getValue();
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException("Invalid key type.");
        }
    }

    @Override
    public V put(K key, V value) {
        checkKey(key);
        Entry<K,V> entry = new AbstractMap.SimpleEntry<>(key, value);
        Position<Entry<K,V>> p = treeSearch(root(), key);
        if (isExternal(p)) {
            expandExternal(p, entry);
            rebalanceInsert(p);
            return null;
        } else {
            V old = p.getElement().getValue();
            set(p, entry);
            rebalanceAccess(p);
            return old;
        }
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
            if (isInternal(left(p)) && isInternal(right(p))) {
                Position<Entry<K,V>> r = treeMax(left(p));
                set(p, r.getElement());
                p = r;
            }
            Position<Entry<K,V>> leaf = isExternal(left(p)) ? left(p) : right(p);
            Position<Entry<K,V>> sib  = sibling(leaf);
            tree.remove(leaf);
            tree.remove(p);
            rebalanceDelete(sib);
            return old;
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException("Invalid key type.");
        }
    }

    @Override
    public Set<Entry<K,V>> entrySet() {
        ArrayList<Entry<K,V>> buffer = new ArrayList<>(size());
        for (Position<Entry<K,V>> p : tree.inorder()) {
            if (isInternal(p)) {
                buffer.add(p.getElement());
            }
        }
        return new LinkedHashSet<>(buffer);
    }


    @Override
    public Entry<K,V> firstEntry() {
        if (isEmpty()) {
            throw new NoSuchElementException("Map is empty");
        }
        return treeMin(root()).getElement();
    }

    @Override
    public Entry<K,V> lastEntry() {
        if (isEmpty()) {
            throw new NoSuchElementException("Map is empty");
        }
        return treeMax(root()).getElement();
    }

    @Override
    public K firstKey() {
        return firstEntry().getKey();
    }

    @Override
    public K lastKey() {
        return lastEntry().getKey();
    }

    @Override
    public SortedMap<K,V> headMap(K toKey) {
        throw new UnsupportedOperationException("headMap not implemented");
    }

    @Override
    public SortedMap<K,V> tailMap(K fromKey) {
        throw new UnsupportedOperationException("tailMap not implemented");
    }

    @Override
    public SortedMap<K,V> subMap(K fromKey, K toKey) {
        if (compare(fromKey, toKey) >= 0) {
            throw new IllegalArgumentException("fromKey must be less than toKey");
        }
        UzmaTreeMap<K,V> result = new UzmaTreeMap<>(internalComparator());
        subMapRecurse(fromKey, toKey, root(), result);
        return result;
    }

    private void subMapRecurse(K fromKey, K toKey, Position<Entry<K,V>> p, UzmaTreeMap<K,V> result) {
        if (isInternal(p)) {
            K k = p.getElement().getKey();
            if (compare(k, fromKey) >= 0 && compare(k, toKey) < 0) {
                result.put(k, p.getElement().getValue());
            }
            if (compare(k, fromKey) > 0) {
                subMapRecurse(fromKey, toKey, left(p), result);
            }
            if (compare(k, toKey) < 0) {
                subMapRecurse(fromKey, toKey, right(p), result);
            }
        }
    }

    protected void rebalanceInsert(Position<Entry<K,V>> p)  { }
    protected void rebalanceDelete(Position<Entry<K,V>> p)  { }
    protected void rebalanceAccess(Position<Entry<K,V>> p)  { }
}

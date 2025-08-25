package datastructures;

import java.util.Comparator;

public class AVLTreeMap<K, V> extends UzmaTreeMap<K, V> {

    public AVLTreeMap() {
        super();
        this.tree = new AVLBinaryTree();
        this.tree.addRoot(null);
    }

    public AVLTreeMap(Comparator<K> comp) {
        super(comp);
        this.tree = new AVLBinaryTree();
        this.tree.addRoot(null);
    }

    protected class AVLBinaryTree extends BalanceableBinaryTree<Entry<K,V>> {
        private int height(Position<Entry<K, V>> p) {
            return isExternal(p) ? 0 : getAux(p);
        }
        private void recomputeHeight(Position<Entry<K, V>> p) {
            setAux(p, 1 + Math.max(height(left(p)), height(right(p))));
        }
        private boolean isBalanced(Position<Entry<K, V>> p) {
            return Math.abs(height(left(p)) - height(right(p))) <= 1;
        }
        private Position<Entry<K, V>> tallerChild(Position<Entry<K, V>> p) {
            int hl = height(left(p)), hr = height(right(p));
            if (hl > hr) {
                return left(p);
            }
            if (hr > hl) {
                return right(p);
            }
            if (isRoot(p)) {
                return left(p);
            }
            return (p == left(parent(p))) ? left(p) : right(p);
        }

        public void rebalance(Position<Entry<K, V>> p) {
            while (p != null) {
                recomputeHeight(p);
                if (!isBalanced(p)) {
                    Position<Entry<K, V>> y = tallerChild(p);
                    Position<Entry<K, V>> x = tallerChild(y);
                    p = restructure(x);
                    recomputeHeight(left(p));
                    recomputeHeight(right(p));
                    recomputeHeight(p);
                }
                p = parent(p);
            }
        }
    }

    @Override
    protected void rebalanceInsert(Position<Entry<K, V>> p) {
        ((AVLBinaryTree) tree).rebalance(p);
    }

    @Override
    protected void rebalanceDelete(Position<Entry<K, V>> p) {
        if (p == null) {
            return;
        }
        Position<Entry<K, V>> start = parent(p);
        if (start != null) {
            ((AVLBinaryTree) tree).rebalance(start);
        }
    }

    @Override
    protected void rebalanceAccess(Position<Entry<K, V>> p) {
        // meant for Splay
    }
}

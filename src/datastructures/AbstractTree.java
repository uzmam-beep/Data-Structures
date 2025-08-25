/**
 * Code taken from Data Structures and Algorithms by Michael T.Goodrich, Roberto Tamassia and Michae; H.Goldwasser
 */

package datastructures;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractTree<E> implements Tree<E> {

    @Override
    public boolean isInternal(Position<E> p) {
        return numChildren(p) > 0;
    }

    @Override
    public boolean isExternal(Position<E> p) {
        return numChildren(p) == 0;
    }

    @Override
    public boolean isRoot(Position<E> p) {
        return p == root();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    private void preorderSubtree(Position<E> p, List<Position<E>> snapshot) {
        snapshot.add(p);
        for (Position<E> c : children(p)) {
            preorderSubtree(c, snapshot);
        }
    }

    @Override
    public Iterable<Position<E>> preorder() {
        List<Position<E>> snapshot = new ArrayList<>();
        if (!isEmpty()) {
            preorderSubtree(root(), snapshot);
        }
        return snapshot;
    }

    private void postorderSubtree(Position<E> p, List<Position<E>> snapshot) {
        for (Position<E> c : children(p)) {
            postorderSubtree(c, snapshot);
        }
        snapshot.add(p);
    }

    @Override
    public Iterable<Position<E>> postorder() {
        List<Position<E>> snapshot = new ArrayList<>();
        if (!isEmpty()) {
            postorderSubtree(root(), snapshot);
        }
        return snapshot;
    }

    @Override
    public Iterable<Position<E>> positions() {
        return preorder();
    }

}

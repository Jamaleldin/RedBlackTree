package eg.edu.alexu.csd.filestructure.redblacktree;

import java.util.ArrayList;
import java.util.Collection;
import javax.management.RuntimeErrorException;

public class RedBlackTree<T extends Comparable<T>, V> implements IRedBlackTree<T, V> {

	private INode<T, V> root = new Node<>();
	private INode<T, V> NIL = new Node<>();
	private int size = 0;
	private ArrayList<INode<T, V>> Ncollection = new ArrayList<>();

	public RedBlackTree() {
		// constructor
		NIL.setColor(INode.BLACK);
		root = NIL;
	}

	@Override
	public INode<T, V> getRoot() {
		return root;
	}

	@Override
	public boolean isEmpty() {
		return root == NIL;
	}

	@Override
	public void clear() {
		root = NIL;
		size = 0;
	}

	@Override
	public V search(T key) {
		if (key == null) {
			throw new RuntimeErrorException(null);
		}
		INode<T, V> temp = getRoot();
		while (temp != NIL && temp.getKey().compareTo(key) != 0) {
			if (temp.getKey().compareTo(key) > 0) {
				temp = temp.getLeftChild();
			} else {
				temp = temp.getRightChild();
			}
		}
		return temp.getValue();
	}

	@Override
	public boolean contains(T key) {
		return (search(key) != null);
	}

	@Override
	public void insert(T key, V value) {
		if (key == null || value == null) {
			throw new RuntimeErrorException(null);
		}

		// the inserted node
		INode<T, V> z = new Node<>();
		z.setKey(key);
		z.setValue(value);

		// normal insertion of BST
		INode<T, V> y = NIL;
		INode<T, V> x = getRoot();
		while (x != NIL) {
			y = x;
			if (x.getKey().compareTo(key) == 0) {
				x.setValue(value);
				return;
			} else if (x.getKey().compareTo(key) > 0) {
				x = x.getLeftChild();
			} else {
				x = x.getRightChild();
			}
		}
		z.setParent(y);
		if (y == NIL) {
			root = z;
		} else if (y.getKey().compareTo(key) > 0) {
			y.setLeftChild(z);
		} else {
			y.setRightChild(z);
		}
		z.setLeftChild(NIL);
		z.setRightChild(NIL);
		z.setColor(INode.RED);
		InsertFixUp(z);
		size++;
	}

	private void InsertFixUp(INode<T, V> z) {
		while (z.getParent().getColor() == INode.RED) {
			if (z.getParent() == z.getParent().getParent().getLeftChild()) {
				// left
				INode<T, V> y = z.getParent().getParent().getRightChild();// y is uncle
				if (y.getColor() == INode.RED) { // case1
					z.getParent().setColor(INode.BLACK);
					y.setColor(INode.BLACK);
					z.getParent().getParent().setColor(INode.RED);
					z = z.getParent().getParent();
				} else {
					if (z == z.getParent().getRightChild()) { // case2(Triangle)
						z = z.getParent();
						leftRotate(z);
					}
					z.getParent().setColor(INode.BLACK); // case3(Line)
					z.getParent().getParent().setColor(INode.RED);
					rightRotate(z.getParent().getParent());
				}

			} else {
				// right
				INode<T, V> y = z.getParent().getParent().getLeftChild();// y is uncle
				if (y.getColor() == INode.RED) { // case1
					z.getParent().setColor(INode.BLACK);
					y.setColor(INode.BLACK);
					z.getParent().getParent().setColor(INode.RED);
					z = z.getParent().getParent();
				} else {
					if (z == z.getParent().getLeftChild()) { // case2(Triangle)
						z = z.getParent();
						rightRotate(z);
					}
					z.getParent().setColor(INode.BLACK); // case3(Line)
					z.getParent().getParent().setColor(INode.RED);
					leftRotate(z.getParent().getParent());
				}

			}
		}
		root.setColor(INode.BLACK);
	}

	private void leftRotate(INode<T, V> x) {
		INode<T, V> y = x.getRightChild();
		x.setRightChild(y.getLeftChild());
		if (y.getLeftChild() != NIL) {
			y.getLeftChild().setParent(x);
		}
		y.setParent(x.getParent());
		if (x.getParent() == NIL) {
			root = y;
		} else if (x == x.getParent().getLeftChild()) {
			x.getParent().setLeftChild(y);
		} else {
			x.getParent().setRightChild(y);
		}
		y.setLeftChild(x);
		x.setParent(y);

	}

	private void rightRotate(INode<T, V> x) {
		INode<T, V> y = x.getLeftChild();
		x.setLeftChild(y.getRightChild());
		if (y.getRightChild() != NIL) {
			y.getRightChild().setParent(x);
		}
		y.setParent(x.getParent());
		if (x.getParent() == NIL) {
			root = y;
		} else if (x == x.getParent().getLeftChild()) {
			x.getParent().setLeftChild(y);
		} else {
			x.getParent().setRightChild(y);
		}
		y.setRightChild(x);
		x.setParent(y);
	}

	@Override
	public boolean delete(T key) {
		if (key == null) {
			throw new RuntimeErrorException(null);
		}
		INode<T, V> z = GetNode(key);
		if (z == NIL) {
			return false;
		}
		INode<T, V> y = z;
		INode<T, V> x = new Node<>();
		boolean ycolor = y.getColor();
		if (z.getLeftChild() == NIL) {
			x = z.getRightChild();
			Transplant(z, z.getRightChild());
		} else if (z.getRightChild() == NIL) {
			x = z.getLeftChild();
			Transplant(z, z.getLeftChild());
		} else {
			y = GetMin(z.getRightChild());
			ycolor = y.getColor();
			x = y.getRightChild();
			if (y.getParent() == z) {
				x.setParent(y);
			} else {
				Transplant(y, y.getRightChild());
				y.setRightChild(z.getRightChild());
				y.getRightChild().setParent(y);
			}
			Transplant(z, y);
			y.setLeftChild(z.getLeftChild());
			y.getLeftChild().setParent(y);
			y.setColor(z.getColor());
		}

		if (ycolor == INode.BLACK) {
			DeleteFixUp(x);
		}
		size--;
		return true;
	}

	private void DeleteFixUp(INode<T, V> x) {
		while (x != getRoot() && x.getColor() == INode.BLACK) {
			if (x == x.getParent().getLeftChild()) {
				// left
				INode<T, V> w = x.getParent().getRightChild();
				if (w.getColor() == INode.RED) {
					w.setColor(INode.BLACK);
					x.getParent().setColor(INode.RED);
					leftRotate(x.getParent());
					w = x.getParent().getRightChild();
				}

				if (w.getLeftChild().getColor() == INode.BLACK && w.getRightChild().getColor() == INode.BLACK) {
					w.setColor(INode.RED);
					x = x.getParent();
				}

				else {
					if (w.getRightChild().getColor() == INode.BLACK) {
						w.getLeftChild().setColor(INode.BLACK);
						w.setColor(INode.RED);
						rightRotate(w);
						w = x.getParent().getRightChild();
					}
					w.setColor(x.getParent().getColor());
					x.getParent().setColor(INode.BLACK);
					w.getRightChild().setColor(INode.BLACK);
					leftRotate(x.getParent());
					x = getRoot();
				}

			} else {
				// right
				INode<T, V> w = x.getParent().getLeftChild();
				if (w.getColor() == INode.RED) {
					w.setColor(INode.BLACK);
					x.getParent().setColor(INode.RED);
					rightRotate(x.getParent());
					w = x.getParent().getLeftChild();
				}

				if (w.getLeftChild().getColor() == INode.BLACK && w.getRightChild().getColor() == INode.BLACK) {
					w.setColor(INode.RED);
					x = x.getParent();
				}

				else {
					if (w.getLeftChild().getColor() == INode.BLACK) {
						w.getRightChild().setColor(INode.BLACK);
						w.setColor(INode.RED);
						leftRotate(w);
						w = x.getParent().getLeftChild();
					}
					w.setColor(x.getParent().getColor());
					x.getParent().setColor(INode.BLACK);
					w.getLeftChild().setColor(INode.BLACK);
					rightRotate(x.getParent());
					x = getRoot();
				}

			}
		}
		x.setColor(INode.BLACK);
	}

	private INode<T, V> GetNode(T key) {
		INode<T, V> temp = getRoot();
		while (temp != NIL && temp.getKey().compareTo(key) != 0) {
			if (temp.getKey().compareTo(key) > 0) {
				temp = temp.getLeftChild();
			} else {
				temp = temp.getRightChild();
			}
		}
		return temp;
	}

	public INode<T, V> GetMin(INode<T, V> x) {
		while (x.getLeftChild() != NIL) {
			x = x.getLeftChild();
		}
		return x;
	}

	private void Transplant(INode<T, V> u, INode<T, V> v) {
		if (u.getParent() == NIL) {
			root = v;
		} else if (u == u.getParent().getLeftChild()) {
			u.getParent().setLeftChild(v);
		} else {
			u.getParent().setRightChild(v);
		}
		v.setParent(u.getParent());
	}

	public INode<T, V> searchKeyCeil(T key) {
		INode<T, V> temp = getRoot();
		while (temp.getKey().compareTo(key) != 0) {
			if (temp.getKey().compareTo(key) > 0) {
				if (temp.getLeftChild() == NIL)
					break;
				temp = temp.getLeftChild();
			} else {
				if (temp.getRightChild() == NIL)
					break;
				temp = temp.getRightChild();
			}
		}
		if (temp == temp.getParent().getLeftChild()) {
			if (temp.getKey().compareTo(key) < 0)
				return temp.getParent();
			else
				return temp;

		} else {
			if (temp.getKey().compareTo(key) < 0) {
				while (temp != NIL) {
					if (temp == temp.getParent().getLeftChild() && temp.getParent().getKey().compareTo(key) >= 0) {
						return temp.getParent();
					}
					temp = temp.getParent();
				}
				return null;
			} else
				return temp;
		}
	}

	public int getSize() {
		return size;
	}

	public INode<T, V> GetMax(INode<T, V> x) {
		// INode<T, V> temp = clone(x);
		while (x.getRightChild() != NIL) {
			x = x.getRightChild();
		}
		return x;
	}

	public INode<T, V> searchKeyFloor(T key) {
		INode<T, V> temp = getRoot();
		while (temp.getKey().compareTo(key) != 0) {
			if (temp.getKey().compareTo(key) > 0) {
				if (temp.getLeftChild() == NIL)
					break;
				temp = temp.getLeftChild();
			} else {
				if (temp.getRightChild() == NIL)
					break;
				temp = temp.getRightChild();
			}
		}
		if (temp == temp.getParent().getLeftChild()) {
			if (temp.getKey().compareTo(key) > 0) {
				while (temp != NIL) {
					if (temp == temp.getParent().getRightChild() && temp.getParent().getKey().compareTo(key) <= 0) {
						return temp.getParent();
					}
					temp = temp.getParent();
				}
				return null;
			} else
				return temp;

		} else {
			if (temp.getKey().compareTo(key) > 0)
				return temp.getParent();
			else
				return temp;
		}
	}

	private void inorderTrav(INode<T, V> root) {
		if (root == NIL) {
			return;
		}
		inorderTrav(root.getLeftChild());
		Ncollection.add(root);
		inorderTrav(root.getRightChild());
	}

	public Collection<INode<T, V>> getNCollection() {
		inorderTrav(getRoot());
		return Ncollection;
	}
}

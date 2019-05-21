package eg.edu.alexu.csd.filestructure.redblacktree;

import java.util.HashSet;
import java.util.Random;

public class Main {

	public static void main(String[] args) {
		
		IRedBlackTree<Integer, String> tree = new RedBlackTree<>();
		Random r = new Random();
		HashSet<Integer> list = new HashSet<Integer>();
		for (int i = 0; i < 100000; ++i) {
			int key = r.nextInt(10000);
			if (r.nextInt(5) % 4 == 0) {
				list.add(key);
			}
			tree.insert(key, "soso" + key);
		}
		for (Integer i : list) {
			tree.delete(i);
		}
		boolean[] ans = new boolean[] { true };
		inOrder(tree.getRoot(), ans);
		System.out.println(ans[0]);
	}

	public static int inOrder(INode<Integer, String> node, boolean[] ans) {
		if (node == null) {
			return 1;
		}
		int leftCount = inOrder(node.getLeftChild(), ans);
		int rightCount = inOrder(node.getRightChild(), ans);
		ans[0] = ans[0] && (leftCount == rightCount);
		return leftCount + ((!node.getColor() == INode.RED) ? 1 : 0);
	}

}

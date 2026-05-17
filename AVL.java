package application;

public class AVL {
	private AVLTreeNode root;

	public AVL() {
		root = null;
	}

	private int height(AVLTreeNode e) {
		if (e == null) 
			return -1;
		return e.getHeight();
	}

	public AVLTreeNode getRoot() {
		return root;
	}

	public void setRoot(AVLTreeNode root) {
		this.root = root;
	}

	private AVLTreeNode rotateWithLeftChild(AVLTreeNode k2) {
		AVLTreeNode k1 = k2.getLeft();
		k2.setLeft(k1.getRight());
		k1.setRight(k2);

		k2.setHeight(Math.max(height(k2.getLeft()), height(k2.getRight())) + 1);
		k1.setHeight(Math.max(height(k1.getLeft()), k2.getHeight()) + 1);

		return k1;
	}

	private AVLTreeNode rotateWithRightChild(AVLTreeNode k1) {
		AVLTreeNode k2 = k1.getRight();
		k1.setRight(k2.getLeft());
		k2.setLeft(k1);

		k1.setHeight(Math.max(height(k1.getLeft()), height(k1.getRight())) + 1);
		k2.setHeight(Math.max(height(k2.getRight()), k1.getHeight()) + 1);

		return k2;
	}

	private AVLTreeNode DoubleWithLeftChild(AVLTreeNode k3) {
		k3.setLeft(rotateWithRightChild(k3.getLeft()));
		return rotateWithLeftChild(k3);
	}

	private AVLTreeNode DoubleWithRightChild(AVLTreeNode k1) {
		k1.setRight(rotateWithLeftChild(k1.getRight()));
		return rotateWithRightChild(k1);
	}

	private AVLTreeNode balance(AVLTreeNode node) {
		if (node == null)
			return null;

		int balanceFactor = height(node.getLeft()) - height(node.getRight());

		if (balanceFactor > 1) {
			if (height(node.getLeft().getLeft()) >= height(node.getLeft().getRight())) {
				node = rotateWithLeftChild(node);
			} else {
				node = DoubleWithLeftChild(node);
			}
		} else if (balanceFactor < -1) {
			if (height(node.getRight().getRight()) >= height(node.getRight().getLeft())) {
				node = rotateWithRightChild(node);
			} else {
				node = DoubleWithRightChild(node);
			}
		}

		node.setHeight(Math.max(height(node.getLeft()), height(node.getRight())) + 1);
		return node;
	}

	public void insert(Object value) { 
		root = insert(value, root);
	}

	private int compare(Object a, Object b) {
		return ((Comparable) a).compareTo(b);
	}

	private AVLTreeNode insert(Object value, AVLTreeNode node) {
		if (node == null)
			return new AVLTreeNode(value);

		if (compare(value, node.getElement()) < 0) {
			node.setLeft(insert(value, node.getLeft()));
		} else if (compare(value, node.getElement()) > 0) {
			node.setRight(insert(value, node.getRight()));
		}

		return balance(node);
	}

	public void remove(Object value) {
		root = remove(value, root);
	}

	private AVLTreeNode remove(Object value, AVLTreeNode node) {
		if (node == null)
			return null;

		if (compare(value, node.getElement()) < 0) {
			node.setLeft(remove(value, node.getLeft()));
		} else if (compare(value, node.getElement()) > 0) {
			node.setRight(remove(value, node.getRight()));
		} else {
			if (node.getLeft() == null || node.getRight() == null) {
				node = (node.getLeft() != null) ? node.getLeft() : node.getRight();
			} else {
				AVLTreeNode min = findMin(node.getRight());
				node.setElement(min.getElement());
				node.setRight(remove(min.getElement(), node.getRight()));
			}
		}

		if (node != null)
			node = balance(node);

		return node;
	}

	private AVLTreeNode findMin(AVLTreeNode node) {
		while (node.getLeft() != null)
			node = node.getLeft();
		return node;
	}

	private void inOrder(AVLTreeNode node) {
		if (node == null)
			return;
		inOrder(node.getLeft());
		System.out.print(node.getElement() + " ");
		inOrder(node.getRight());
	}

	public void printInOrder() {
		System.out.print("InOrder: ");
		inOrder(root);
		System.out.println();
	}

	private void preOrder(AVLTreeNode node) {
		if (node == null)
			return;
		System.out.print(node.getElement() + " ");
		preOrder(node.getLeft());
		preOrder(node.getRight());
	}

	public void printPreOrder() {
		System.out.print("PreOrder: ");
		preOrder(root);
		System.out.println();
	}

	private void postOrder(AVLTreeNode node) {
		if (node == null)
			return;
		postOrder(node.getLeft());
		postOrder(node.getRight());
		System.out.print(node.getElement() + " ");
	}

	public void printPostOrder() {
		System.out.print("PostOrder: ");
		postOrder(root);
		System.out.println();
	}

	

	private int getBalance(AVLTreeNode node) {
		if (node == null)
			return 0;
		return height(node.getLeft()) - height(node.getRight());
	}

	public int getBalanceOfNode(AVLTreeNode node) {
		return getBalance(node);
	}
	
	
	public WordEA searchEnglishWord(String word) {
	    AVLTreeNode R = root;

	    while (R != null) {
	        WordEA wEA = (WordEA) R.getElement();
	        int C = word.compareToIgnoreCase(wEA.getWord());

	        if (C == 0) {
	            return wEA;
	        } 
	        else if (C < 0) {
	            R = R.getLeft();
	        } 
	        else {
	            R = R.getRight();
	        }
	    }
	    return null;
	}
  
	
	public WordEA searchArabicWord(String wa) {
	    return searchArabic(root, wa);
	}

	private WordEA searchArabic(AVLTreeNode atn, String wa) {
	    if (atn == null) {
	        return null;
	    }

	    WordEA w = (WordEA) atn.getElement();

	    if (w.getArMeaning().equals(wa)) {
	        return w;
	    }

	    WordEA result = searchArabic(atn.getLeft(), wa);

	    if (result != null) {
	        return result;
	    }

	    return searchArabic(atn.getRight(), wa);
	}

	public int countWordsInTree() {
	    return countWordsInTreeInTheSameChar(root);
	}

	private int countWordsInTreeInTheSameChar(AVLTreeNode node) {
	    if (node == null)
	        return 0;
	    return 1 + countWordsInTreeInTheSameChar(node.getLeft()) + countWordsInTreeInTheSameChar(node.getRight());
	}

}

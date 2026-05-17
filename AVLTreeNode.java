package application;



public class AVLTreeNode {
	private Object element;
	private AVLTreeNode left;
	private AVLTreeNode right;
	private int height;

	public Object getElement() {
		return element;
	}

	public void setElement(Object element) {
		this.element = element;
	}

	public AVLTreeNode getLeft() {
		return left;
	}

	public void setLeft(AVLTreeNode left) {
		this.left = left;
	}

	public AVLTreeNode getRight() {
		return right;
	}

	public void setRight(AVLTreeNode right) {
		this.right = right;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public AVLTreeNode(Object element) {
		this(element, null, null);
	}

	public AVLTreeNode(Object element, AVLTreeNode left, AVLTreeNode right) {
		this.element = element;
		this.left = left;
		this.right = right;
		this.height = 0;
	}

	

}

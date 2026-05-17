package application;



public class LinkedList {
	
	
		private Node front, back;
		private int size;

		public LinkedList() {
			front = back = null;
			size = 0;
		}

		public void addFirst(Object element) {
			Node newNode = new Node(element);
			if (size == 0) {
				front = back = newNode;
			} else {
				newNode.setNext(front);
				front = newNode;
			}
			size++;
		}

		public void addLast(Object element) {
			Node newNode = new Node(element);
			if (size == 0) {
				front = back = newNode;
			} else {
				back.setNext(newNode);
				back = newNode;
			}
			size++;
		}

		public Object getFirst() {
			if (size == 0) {
				return null;
			}
			return front.getElement();
		}

		public Object getLast() {
			if (size == 0) {
				return null;
			}
			return back.getElement();
		}

		public int getSize() {
			return size;
		}

		public void printList() {
			Node current = front;
			while (current != null) {
				System.out.println(current.getElement());
				current = current.getNext();
			}
		}
		/*
		 * public Object get(int index) { if (index < 0 || index >= size) { return null;
		 * } else if (index == 0) { return getFirst(); } else if (index == size - 1) {
		 * return getLast(); } else { Node current = front; for (int i = 0; i < index;
		 * i++) { current = current.getNext(); } return current.getElement(); } }
		 */

		public Object get(int index) {
			if (size == 0) {
				return null;
			} else if (index == 0)
				return getFirst();
			else if (index == size - 1)
				return getLast();
			else if (index > 0 && index < size - 1) {
				Node current = front;
				for (int i = 0; i < index; i++)
					current = current.getNext();
				return current.getElement();
			} else {
				return null;
			}
		}

		public void add(int index, Object element) {
			if (index < 0 || index > size) {
				throw new IndexOutOfBoundsException("Invalid index");
			}
			if (index == 0) {
				addFirst(element);
			} else if (index == size) {
				addLast(element);
			} else {
				Node newNode = new Node(element);
				Node current = front;
				for (int i = 0; i < index - 1; i++) {
					current = current.getNext();
				}
				newNode.setNext(current.getNext());
				current.setNext(newNode);
				size++;
			}
		}

		/*
		 * public void add(int index, Object element) { if (index ==0) {
		 * addFirst(element); } else if (index >= size) { addLast(element); }else { Node
		 * newNode= new Node(element); Node current = front; for (int i=0; i<index-1 ;
		 * i++) current =current.getNext();
		 * 
		 * 
		 * 
		 * } }
		 */

		public boolean removeFirst() {
			if (size == 0) {
				return false;
			} else if (size == 1) {
				front = back = null;
				size = 0;
			} else {
				front = front.getNext();
				size--;
			}
			return true;
		}

		public boolean removeLast() {
			if (size == 0) {
				return false;
			} else if (size == 1) {
				front = back = null;
				size = 0;
			} else {
				Node current = front;
				for (int i = 0; i < size - 2; i++) {
					current = current.getNext();
				}
				current.setNext(null);
				back = current;
				size--;
			}
			return true;
		}

		public boolean remove(int index) {
			if (index < 0 || index >= size) {
				return false;
			} else if (index == 0) {
				return removeFirst();
			} else if (index == size - 1) {
				return removeLast();
			} else {
				Node current = front;
				for (int i = 0; i < index - 1; i++) {
					current = current.getNext();
				}
				current.setNext(current.getNext().getNext());
				size--;
				return true;
			}
		}

		public void traverse(Node cuurent) {
			if (cuurent != null) {
				System.out.println(cuurent.getElement());
				traverse(cuurent.getNext());
			}
		}


}

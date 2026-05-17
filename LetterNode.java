package application;

public class LetterNode {
    char letter;
    AVL tree;
    LetterNode next;
    
    // Main constructor
    public LetterNode(char letter, AVL tree, LetterNode next) {
        this.letter = letter;
        this.tree = tree;
        this.next = next;
    }
    
    // Simple constructor - FIXED: Initialize tree
    public LetterNode(char c) {
        this.letter = c;
        this.tree = new AVL(); // IMPORTANT: Initialize the tree
        this.next = null;
    }
    
    public char getLetter() {
        return letter;
    }
    
    public void setLetter(char letter) {
        this.letter = letter;
    }
    
    public AVL getTree() {
        return tree;
    }
    
    public void setTree(AVL tree) {
        this.tree = tree;
    }
    
    public LetterNode getNext() {
        return next;
    }
    
    public void setNext(LetterNode next) {
        this.next = next;
    }
    
    @Override
    public String toString() {
        return "LetterNode [letter=" + letter + ", tree=" + tree + ", next=" + next + "]";
    }
}
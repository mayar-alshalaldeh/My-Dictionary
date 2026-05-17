package application;

public class DictionaryWords {

    private LetterNode head;

    public DictionaryWords() {
        head = new LetterNode('A');
        LetterNode cur = head;
        for (char c = 'B'; c <= 'Z'; c++) {
            cur.next = new LetterNode(c);
            cur = cur.next;
        }
    }

    public LetterNode getNode(char c) {
        LetterNode cur = head;
        while (cur != null) {
            if (cur.letter == c) return cur;
            cur = cur.next;
        }
        return null;
    }

    public boolean addWord(WordEA w) {
        LetterNode e = getNode(Character.toUpperCase(w.getWord().charAt(0)));
        if (e == null) return false;
        if (e.tree.searchEnglishWord(w.getWord()) != null)
            return false;
        e.tree.insert(w);
        return true;
    }

    public WordEA searchEnglishWords(String w) {
        if (w == null || w.isEmpty()) return null;
        LetterNode node = getNode(Character.toUpperCase(w.charAt(0)));
        if (node == null) return null;
        return node.tree.searchEnglishWord(w);
    }

    // FIXED: Added method alias for consistency
    public WordEA searchEnglish(String w) {
        return searchEnglishWords(w);
    }

    public WordEA searchArabic(String a) {
        LetterNode cur = head;
        while (cur != null) {
            WordEA w = cur.tree.searchArabicWord(a);
            if (w != null) return w;
            cur = cur.next;
        }
        return null;
    }

   
    public boolean deleteWord(String word) {
        if (word == null || word.isEmpty()) return false;
        
        LetterNode node = getNode(Character.toUpperCase(word.charAt(0)));
        if (node == null) return false;
        
        WordEA wordToDelete = node.tree.searchEnglishWord(word);
        if (wordToDelete == null) return false;
        
        node.tree.remove(wordToDelete);
        return true;
    }

    public LetterNode getHead() { 
        return head; 
    }
    
   
    public LetterNode gettingH() {
        return head;
    }
}
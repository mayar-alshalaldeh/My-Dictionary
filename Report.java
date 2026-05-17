package application;

public class Report {

    private DictionaryWords dictionary;

    public Report(DictionaryWords d) {
        this.dictionary = d;
    }

   
    public String countPerLetter() {
        StringBuilder sb = new StringBuilder();
        LetterNode cur = dictionary.getHead();
        int total = 0;

        while (cur != null) {
            int count = cur.getTree().countWordsInTree();
            sb.append(cur.getLetter())
              .append(" : ")
              .append(count)
              .append("\n");
            total += count;
            cur = cur.getNext();
        }

        sb.append("\nTotal Words = ").append(total);
        return sb.toString();
    }

    // ===============================
    // Count types
    // ===============================
    public String countWordTypes() {
        int noun = 0, verb = 0, adj = 0;

        LetterNode cur = dictionary.getHead();
        while (cur != null) {
            noun += countType(cur.getTree().getRoot(), "noun");
            verb += countType(cur.getTree().getRoot(), "verb");
            adj  += countType(cur.getTree().getRoot(), "adjective");
            cur = cur.getNext();
        }

        return "Nouns = " + noun +
               "\nVerbs = " + verb +
               "\nAdjectives = " + adj;
    }

    private int countType(AVLTreeNode node, String type) {
        if (node == null) return 0;

        WordEA w = (WordEA) node.getElement();
        int c = w.getType().equalsIgnoreCase(type) ? 1 : 0;

        return c
                + countType(node.getLeft(), type)
                + countType(node.getRight(), type);
    }

    // ===============================
    // Words for specific letter
    // ===============================
    public String wordsForLetter(char letter) {
        LetterNode node = dictionary.getNode(Character.toUpperCase(letter));
        if (node == null) return "No words";

        StringBuilder sb = new StringBuilder();
        printTree(node.getTree().getRoot(), sb);
        return sb.toString();
    }

    private void printTree(AVLTreeNode node, StringBuilder sb) {
        if (node == null) return;
        printTree(node.getLeft(), sb);
        sb.append(node.getElement()).append("\n");
        printTree(node.getRight(), sb);
    }
}

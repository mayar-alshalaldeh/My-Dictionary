package application;

import java.util.Random;

public class SentenceWords {

    private DictionaryWords dictionary;

    public SentenceWords(DictionaryWords d) {
        this.dictionary = d;
    }

    // FIXED: Using custom LinkedList instead of Java's LinkedList
    public String generateEnglish() {

        LinkedList nouns = new LinkedList();
        LinkedList verbs = new LinkedList();

        LetterNode cur = dictionary.getHead();
        while (cur != null) {
            collect(cur.getTree().getRoot(), nouns, verbs);
            cur = cur.getNext();
        }

        if (nouns.getSize() == 0 || verbs.getSize() == 0)
            return "Not enough words to generate sentence";

        Random r = new Random();
        WordEA subject = (WordEA) nouns.get(r.nextInt(nouns.getSize()));
        WordEA verb = (WordEA) verbs.get(r.nextInt(verbs.getSize()));
        WordEA object = (WordEA) nouns.get(r.nextInt(nouns.getSize()));

        return subject.getWord() + " " +
               verb.getWord() + " " +
               object.getWord();
    }

    // FIXED: Added Arabic sentence generation
    public String generateArabic() {

        LinkedList nouns = new LinkedList();
        LinkedList verbs = new LinkedList();

        LetterNode cur = dictionary.getHead();
        while (cur != null) {
            collect(cur.getTree().getRoot(), nouns, verbs);
            cur = cur.getNext();
        }

        if (nouns.getSize() == 0 || verbs.getSize() == 0)
            return "لا توجد كلمات كافية لتوليد جملة";

        Random r = new Random();
        WordEA verb = (WordEA) verbs.get(r.nextInt(verbs.getSize()));
        WordEA subject = (WordEA) nouns.get(r.nextInt(nouns.getSize()));
        WordEA object = (WordEA) nouns.get(r.nextInt(nouns.getSize()));

        // Arabic sentence order: Verb + Subject + Object
        return verb.getArMeaning() + " " +
               subject.getArMeaning() + " " +
               object.getArMeaning();
    }

    private void collect(AVLTreeNode node,
                         LinkedList nouns,
                         LinkedList verbs) {

        if (node == null) return;

        WordEA w = (WordEA) node.getElement();

        if (w.getType().equalsIgnoreCase("noun"))
            nouns.addLast(w);
        else if (w.getType().equalsIgnoreCase("verb"))
            verbs.addLast(w);

        collect(node.getLeft(), nouns, verbs);
        collect(node.getRight(), nouns, verbs);
    }
}
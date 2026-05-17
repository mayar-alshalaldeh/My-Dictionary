package application;

import java.io.*;

public class FileHandler {

    // FIXED: Renamed to loadFromFile for consistency
    public int loadFromFile(String fileN, DictionaryWords dict) throws IOException {

        BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileN), "UTF-8")
        );

        String ln;
        int ct = 0;

        while ((ln = br.readLine()) != null) {
            ln = ln.trim();
            if (ln.isEmpty())
                continue;

            String[] partsArr = ln.split(";");

            if (partsArr.length != 5)
                continue;

            String word = partsArr[0].trim();
            String eng = partsArr[1].trim();
            String ar = partsArr[2].trim();
            String example = partsArr[3].trim();
            String type = partsArr[4].trim();

            WordEA w = new WordEA(word, eng, ar, example, type);

            if (dict.addWord(w))
                ct++;
        }

        br.close();
        return ct;
    }

    // FIXED: Added readFromFile as alias
    public int readFromFile(String fileN, DictionaryWords dict) throws IOException {
        return loadFromFile(fileN, dict);
    }

    // Save dictionary to file
    public void saveToFile(String fileName, DictionaryWords dict) throws IOException {

        BufferedWriter bWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8")
        );

        LetterNode cur = dict.getHead();

        while (cur != null) {
            writingTree(cur.getTree().getRoot(), bWriter);
            cur = cur.getNext();
        }

        bWriter.close();
    }

    private void writingTree(AVLTreeNode nd, BufferedWriter bw) throws IOException {

        if (nd == null)
            return;

        writingTree(nd.getLeft(), bw);

        WordEA w = (WordEA) nd.getElement();
        bw.write(
                w.getWord() + ";" +
                w.getEngMeaning() + ";" +
                w.getArMeaning() + ";" +
                w.getExample() + ";" +
                w.getType()
        );
        bw.newLine();

        writingTree(nd.getRight(), bw);
    }
}
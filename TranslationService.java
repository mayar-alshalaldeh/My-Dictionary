package application;

public class TranslationService {
    private DictionaryWords dictionary;

    public TranslationService(DictionaryWords dictionary) {
        this.dictionary = dictionary;
    }

    // Translate English text to Arabic
    public String translateToArabic(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "";
        }

        String[] words = text.split("\\s+");
        StringBuilder translation = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            // Remove punctuation for lookup
            String cleanWord = words[i].replaceAll("[^a-zA-Z]", "");
            
            if (cleanWord.isEmpty()) {
                translation.append(words[i]);
            } else {
                WordEA found = dictionary.searchEnglish(cleanWord);
                if (found != null) {
                    translation.append(found.getArMeaning());
                } else {
                    translation.append("[").append(words[i]).append("]");
                }
            }

            if (i < words.length - 1) {
                translation.append(" ");
            }
        }

        return translation.toString();
    }

    // Translate Arabic text to English
    public String translateToEnglish(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "";
        }

        String[] words = text.split("\\s+");
        StringBuilder translation = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String word = words[i].trim();
            
            if (word.isEmpty()) {
                continue;
            }

            WordEA found = dictionary.searchArabic(word);
            if (found != null) {
                translation.append(found.getWord());
            } else {
                translation.append("[").append(word).append("]");
            }

            if (i < words.length - 1) {
                translation.append(" ");
            }
        }

        return translation.toString();
    }
}
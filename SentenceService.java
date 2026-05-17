package application;

public class SentenceService {
    private SentenceWords sentenceWords;

    public SentenceService(DictionaryWords dictionary) {
        this.sentenceWords = new SentenceWords(dictionary);
    }

    public String generateEnglish() {
        return sentenceWords.generateEnglish();
    }

    public String generateArabic() {
        return sentenceWords.generateArabic();
    }
}
package application;

public class AddWords {
	   private DictionaryWords dw;
	   
	    public AddWords(DictionaryWords dw) { 
	    	this.dw = dw;
	    	}

	    public boolean add(String words, String english, String arabic,String example, String type) {
	        return dw.addWord(new WordEA(words,  english,  arabic, example, type));
	    }
}

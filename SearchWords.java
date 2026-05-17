package application;



public class SearchWords {
	  private DictionaryWords dsearch;
	  
	  public SearchWords(DictionaryWords d) { 
		  this.dsearch = d;
		  }

	    public WordEA searchEnglish(String wE) {
	        return dsearch.searchEnglish(wE);
	    }

	    public WordEA searchArabic(String wA) {
	        return dsearch.searchArabic(wA);
	    }
	  
}

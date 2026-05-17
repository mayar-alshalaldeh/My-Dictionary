package application;

public class DeleteWords {
    private DictionaryWords d;
    public DeleteWords(DictionaryWords d){ 
    	this.d = d; 
    	}
    public boolean delete(String w){
      
        return d.deleteWord(w);
    }

}

package application;

public class WordEA implements Comparable<WordEA> {
	  private String word;
	  private  String engMeaning;
	  private  String arMeaning;
	  private  String example;
	  private  String type;
	  public WordEA(String word, String engMeaning, String arMeaning, String example, String type) {
		super();
		this.word = word;
		this.engMeaning = engMeaning;
		this.arMeaning = arMeaning;
		this.example = example;
		this.type = type;
	  }
	  public String getWord() {
		  return word;
	  }
	  public void setWord(String word) {
		  this.word = word;
	  }
	  public String getEngMeaning() {
		  return engMeaning;
	  }
	  public void setEngMeaning(String engMeaning) {
		  this.engMeaning = engMeaning;
	  }
	  public String getArMeaning() {
		  return arMeaning;
	  }
	  public void setArMeaning(String arMeaning) {
		  this.arMeaning = arMeaning;
	  }
	  public String getExample() {
		  return example;
	  }
	  public void setExample(String example) {
		  this.example = example;
	  }
	  public String getType() {
		  return type;
	  }
	  public void setType(String type) {
		  this.type = type;
	  }
	  @Override
	  public String toString() {
		return "WordEA [word=" + word + ", engMeaning=" + engMeaning + ", arMeaning=" + arMeaning + ", example="
				+ example + ", type=" + type + "]";
	  }
	  @Override
	  public int compareTo(WordEA w) {
		  return this.word.compareToIgnoreCase(w.word);
		
	  }
	  
	  
	  

}

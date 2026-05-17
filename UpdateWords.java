package application;

public class UpdateWords {
    private DictionaryWords d;
    public UpdateWords(DictionaryWords d){ this.d = d; }
    public boolean update(String word,String eng,String arb,String exa,String tp){
        WordEA x = d.searchEnglishWords(word);
        if(x==null) return false;
        x.setEngMeaning(eng);
        x.setArMeaning(arb);
        x.setExample(exa);
        x.setType(tp);
        return true;
    }
}

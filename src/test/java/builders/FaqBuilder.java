package builders;


import demo.models.Faq;

public class FaqBuilder {
  private Faq faq = new Faq();
  
  public FaqBuilder id(int id) {
    faq.setId(id);
    return this;
  }

  public FaqBuilder category(String category) {
    faq.setCategory(category);
    return this;
  }

  public FaqBuilder question(String question) {
    faq.setQuestion(question);
    return this;
  }
  public FaqBuilder answer(String Answer) {
    faq.setAnswer(Answer);
    return this;
  }
  

  
  public Faq build() {
    return faq;
  }
}

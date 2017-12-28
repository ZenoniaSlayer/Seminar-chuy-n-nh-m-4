package AST;

@SuppressWarnings("serial")
public class ErrorToken extends RuntimeException {
  public String s;

  public ErrorToken(String s) {
    this.s = s;
  }

  @Override
  public String toString() {
    return "Error token: " + this.s;
  }
}

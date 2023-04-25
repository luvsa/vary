package org.luvsa.html;

/**
 * @author Aglet
 * @create 2023/2/18 9:43
 */
public class ScriptDataDoubleEscapedDash implements CharHandler{
    @Override
    public void read(Tokenizer t, CharReader r) {
        char c = r.consume();
        switch (c) {
            case '-' -> {
                t.emit(c);
                t.transition("ScriptDataDoubleEscapedDashDash");
            }
            case '<' -> {
                t.emit(c);
                t.transition("ScriptDataDoubleEscapedLessthanSign");
            }
            case nullChar -> {
                t.error(this);
                t.emit(replacementChar);
                t.transition("ScriptDataDoubleEscaped");
            }
            case eof -> {
                t.eofError(this);
                t.transition("Data");
            }
            default -> {
                t.emit(c);
                t.transition("ScriptDataDoubleEscaped");
            }
        }
    }
}
package org.luvsa.html;

/**
 * @author Aglet
 * @create 2023/2/18 10:24
 */
public class AfterDoctypeName implements CharHandler{
    @Override
    public void read(Tokenizer t, CharReader r) {
        if (r.isEmpty()) {
            t.eofError(this);
            t.doctypePending.forceQuirks = true;
            t.emitDoctypePending();
            t.transition("Data");
            return;
        }
//        if (r.matchesAny('\t', '\n', '\r', '\f', ' '))
//            r.advance(); // ignore whitespace
//        else if (r.matches('>')) {
//            t.emitDoctypePending();
//            t.advanceTransition("Data");
//        } else if (r.matchConsumeIgnoreCase(DocumentType.PUBLIC_KEY)) {
//            t.doctypePending.pubSysKey = DocumentType.PUBLIC_KEY;
//            t.transition("AfterDoctypePublicKeyword");
//        } else if (r.matchConsumeIgnoreCase(DocumentType.SYSTEM_KEY)) {
//            t.doctypePending.pubSysKey = DocumentType.SYSTEM_KEY;
//            t.transition("AfterDoctypeSystemKeyword");
//        } else {
//            t.error(this);
//            t.doctypePending.forceQuirks = true;
//            t.advanceTransition("BogusDoctype");
//        }
    }
}

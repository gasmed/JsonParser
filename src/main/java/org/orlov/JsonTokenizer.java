package org.orlov;

import java.util.ArrayList;
import java.util.List;

public class JsonTokenizer {

    public List<String> tokenize(String json) {
        List<String> tokens = new ArrayList<>();
        StringBuilder token = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < json.length(); i++) {
            char ch = json.charAt(i);

            if (ch == '"') {
                token.append(ch);
                inQuotes =!inQuotes;
            } else if (inQuotes) {
                token.append(ch);
            } else {
                if (Character.isWhitespace(ch)) {
                    continue;
                }
                switch (ch) {
                    case '{':
                    case '}':
                    case '[':
                    case ']':
                    case ',':
                    case ':':
                        if (token.length() > 0) {
                            tokens.add(token.toString());
                            token.setLength(0);
                        }
                        tokens.add(String.valueOf(ch));
                        break;
                    default:
                        token.append(ch);
                }
            }
        }

        if (token.length() > 0) {
            tokens.add(token.toString());
        }

        return tokens;
    }
}

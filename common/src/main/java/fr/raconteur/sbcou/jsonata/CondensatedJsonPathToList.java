/*
package fr.raconteur.sbcou.jsonata;

import java.io.IOException;
import java.util.List;
import com.dashjoin.jsonata.Jsonata;
import org.apache.commons.text.StringEscapeUtils;

public class CondensatedJsonPathToList extends Transformer<List<String>> {

    public */
/*CondensatedJsonPathToList*//*
() throws IOException {
        super("condensated_path_to_list", (Class<List<String>>)(Class<?>)List.class, true);
        // Ajout de la fonction unescape à l'initialisation
        expression.registerFunction("unescape", StringEscapeUtils::unescapeJava);
    }

    public List<String> query(String data) {
        Jsonata.Frame env = createEnv();
        return execute(data, env);
    }
}*/

/*
package fr.raconteur.sbcou.jsonata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import com.dashjoin.jsonata.Jsonata;

public abstract class Transformer<T> {
    protected Jsonata expression;
    private Class<T> type;

    public Transformer(String jsonataExpression, Class<T> type) {
        this.expression = Jsonata.jsonata(jsonataExpression);
        this.type = type;
    }
    
    public Transformer(String fileName, Class<T> type, boolean fromFile) throws IOException {
        this(loadExpressionFromFile(fileName + ".jsonata"), type);
    }
    
    private static String loadExpressionFromFile(String fileName) throws IOException {
        InputStream resource = Transformer.class.getClassLoader().getResourceAsStream("jsonata/" + fileName);
        if (resource == null) {
            throw new IOException("Fichier non trouvé : " + fileName);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
    
    public T execute(Object data, Jsonata.Frame env) {
        Object result = expression.evaluate(data, env);
        if (!type.isInstance(result)) {
            throw new IllegalArgumentException("Le type du résultat ne correspond pas à " + type.getName());
        }
        return type.cast(result);
    }

    protected Jsonata.Frame createEnv() {
        return expression.createFrame();
    }
}
*/

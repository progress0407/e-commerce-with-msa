package codereview.simpleorder.support;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class JsonFileConverter {

    private static final Gson gson = new Gson();

    private JsonFileConverter() {
    }

    public static <T> List<T> fromJsonFile(String path, Class<T> clazz) {

        try {
            return innerFromJsonFile(path, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> List<T> innerFromJsonFile(String path, Class<T> clazz) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(new ClassPathResource(path).getFile()));
        return JsonFileConverter.gson.fromJson(bufferedReader, createTypeToken(clazz));
    }

    private static <T> Type createTypeToken(Class<T> clazz) {

        return TypeToken.getParameterized(List.class, clazz).getType();
    }
}

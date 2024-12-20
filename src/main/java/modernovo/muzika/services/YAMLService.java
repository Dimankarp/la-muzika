package modernovo.muzika.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class YAMLService {

    private ObjectMapper mapper;

    public YAMLService() {
        mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
    }

    public <T> T parse(InputStream inputStream, Class<T> type) throws IOException {
        return mapper.readValue(inputStream, type);
    }


}

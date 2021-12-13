import com.config.Parameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class YamlTest {

    @Test
    public void saveYaml() throws IOException {
        Parameters parameters = new Parameters();
        parameters.setCurrentData(new Date());
        parameters.setFolder("resources");
        parameters.setPath("large.txt");
        parameters.setOwner("Boris");
        YAMLFactory yamlFactory = new YAMLFactory();

        ObjectMapper objectMapper = new ObjectMapper(yamlFactory);
        objectMapper.writeValue(new File("src/main/resources/application1.yaml"), parameters);
    }
    @Test
    public void saveYamlToJson() throws IOException {
        Parameters parameters = new Parameters();
        parameters.setCurrentData(new Date());
        parameters.setFolder("resources");
        parameters.setPath("large.txt");
        parameters.setOwner("Boris");
        YAMLFactory yamlFactory = new YAMLFactory();

        ObjectMapper objectMapper = new ObjectMapper(yamlFactory);
        objectMapper.writeValue(new File("src/main/resources/application1.yaml"), parameters);
    }

    @Test
    public void readYaml() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        Parameters params = objectMapper.readValue(new File("src/main/resources/application1.yaml"), Parameters.class);
        Assert.assertNotNull(params);
        Assert.assertTrue(params.getOwner().equalsIgnoreCase("root"));
    }

    @Test
    public void readObject() throws Exception {
        String path = "src/main/resources/" + "application2.yaml";
        try (InputStream in = new FileInputStream(path);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in))) {
            String strLine;
            boolean descriptor = false;
            Map<String, Object> objectData = new HashMap<>();
            while ((strLine = bufferedReader.readLine()) != null) {

                if (strLine.equals("#class")) {
                    break;
                }

                if (descriptor) {
                    objectData.put(strLine.split(":")[0].trim(), strLine.split(":")[1].trim());
                }

                if (strLine.startsWith("class")) {
                    if (strLine.split(":")[1].trim().equalsIgnoreCase("Parameters")) {
                        descriptor = true;
                    }
                }
            }

            Assert.assertTrue(objectData.entrySet().size() > 0);
            Parameters p = new Parameters();
            for (String key : objectData.keySet()) {
                 Object value = objectData.get(key);
                 String setter = "set"+ key.substring(0,1).toUpperCase()+ key.substring(1);
                 String getter= setter.replace("set","get");

                 Method getterMethod=  p.getClass().getMethod(getter);
                 Class<?> returnType=  getterMethod.getReturnType();
                 Method setterMethod = p.getClass().getDeclaredMethod(setter,  returnType);
                 setterMethod.invoke(p,value);
                 Assert.assertSame(setterMethod.getReturnType().getName(),"void");

            }
        }
    }

    @Test
    public void saveObjectToBytes() throws IOException {
        Parameters parameters = new Parameters();
        parameters.setOwner("root");
        parameters.setCurrentData(new Date());
        try(
            OutputStream bos = new FileOutputStream("src/main/resources/app.bin");
            ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(parameters);
            oos.flush();
        }

    }
        @Test
        public void readObjectToBytes() throws IOException , ClassNotFoundException{
        File file;
        try(
        InputStream fis = new FileInputStream("src/main/resources/app.bin")  ;
        ObjectInputStream ois = new ObjectInputStream(fis)) {
            Parameters params = (Parameters) ois.readObject();
            Assert.assertNotNull(params);
            file = new File("src/main/resources/app.bin");
        }
           Files.delete(file.toPath());
    }
}

package hamburg.cypp;

import com.thinkaurelius.titan.core.attribute.AttributeSerializer;
import com.thinkaurelius.titan.diskstorage.ScanBuffer;
import com.thinkaurelius.titan.diskstorage.WriteBuffer;
import com.thinkaurelius.titan.graphdb.database.serialize.attribute.ByteArraySerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import clojure.lang.Keyword;

public class KeywordSerializer implements AttributeSerializer<Keyword> {
    private ByteArraySerializer serializer;

    public KeywordSerializer() {
        serializer = new ByteArraySerializer();
    }

    @Override
    public Keyword read(ScanBuffer buffer) {
        Keyword result = null;

        byte[] data = serializer.read(buffer);

        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            result = (Keyword) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    @Override
    public void write(WriteBuffer buffer, Keyword attribute) {

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(attribute);
            byte[] data = baos.toByteArray();
            serializer.write(buffer, data);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

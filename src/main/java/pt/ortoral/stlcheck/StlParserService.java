package pt.ortoral.stlcheck;

import org.springframework.stereotype.Service;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@Service
public class StlParserService {

    public StlInfo parse(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        
        buffer.position(80);

        int triangleCount = buffer.getInt();

        float minX = Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        float minZ = Float.MAX_VALUE;
        float maxX = - Float.MAX_VALUE;
        float maxY = - Float.MAX_VALUE;
        float maxZ = - Float.MAX_VALUE;

        for (int i = 0; i < triangleCount; i++) {
            buffer.getFloat();
            buffer.getFloat();
            buffer.getFloat();

            for (int v = 0; v < 3; v++) {
                float x = buffer.getFloat();
                float y = buffer.getFloat();
                float z = buffer.getFloat();

                minX = Math.min(minX, x);
                minY = Math.min(minY, y);
                minZ = Math.min(minZ, z);
                maxX = Math.max(maxX, x);
                maxY = Math.max(maxY, y);
                maxZ = Math.max(maxZ, z);
            }

            buffer.getShort();
        }
        
        return new StlInfo(triangleCount, minX, minY, minZ, maxX, maxY, maxZ);
    }
}

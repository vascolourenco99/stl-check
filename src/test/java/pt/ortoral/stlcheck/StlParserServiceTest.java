package pt.ortoral.stlcheck;

import org.junit.jupiter.api.Test;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import static org.junit.jupiter.api.Assertions.*;

class StlParserServiceTest {

    @Test
    void parsesTriangleCountAndBoundingBox() {
        ByteBuffer buffer = ByteBuffer.allocate(134).order(ByteOrder.LITTLE_ENDIAN);

        // 80 bytes de header (deixa a zeros)
        buffer.position(80);

        // nº de triângulos
        buffer.putInt(1);

        // normal (ignorada)
        buffer.putFloat(0f).putFloat(0f).putFloat(0f);

        // vértice 1
        buffer.putFloat(0f).putFloat(0f).putFloat(0f);
        // vértice 2
        buffer.putFloat(1f).putFloat(2f).putFloat(3f);
        // vértice 3
        buffer.putFloat(-1f).putFloat(5f).putFloat(0f);

        // attribute byte count
        buffer.putShort((short) 0);

        byte[] stlBytes = buffer.array();

        StlParserService service = new StlParserService();
        StlInfo info = service.parse(stlBytes);

        assertEquals(1, info.triangleCount());
        assertEquals(-1f, info.minX());
        assertEquals(0f, info.minY());
        assertEquals(0f, info.minZ());
        assertEquals(1f, info.maxX());
        assertEquals(5f, info.maxY());
        assertEquals(3f, info.maxZ());
    }
}

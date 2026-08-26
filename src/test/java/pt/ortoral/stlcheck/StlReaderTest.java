package pt.ortoral.stlcheck;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.junit.jupiter.api.Test;

class StlReaderTest {

    /** Two triangles spanning (0,0,0)..(10,20,30). */
    private static byte[] binaryStl() {
        float[][] tris = {
            {0, 0, 0, 10, 0, 0, 0, 20, 0},
            {0, 0, 0, 0, 0, 30, 10, 20, 30},
        };
        var buf = ByteBuffer.allocate(80 + 4 + tris.length * 50).order(ByteOrder.LITTLE_ENDIAN);
        buf.position(80).putInt(tris.length);
        for (float[] t : tris) {
            buf.putFloat(0).putFloat(0).putFloat(1); // normal, ignored by the reader
            for (float f : t) buf.putFloat(f);
            buf.putShort((short) 0); // attribute byte count
        }
        return buf.array();
    }

    @Test
    void readsCountAndBoundingBox() throws IOException {
        var s = StlReader.read(new ByteArrayInputStream(binaryStl()));
        assertEquals(2, s.triangles());
        assertEquals(10f, s.width());
        assertEquals(20f, s.height());
        assertEquals(30f, s.depth());
        assertEquals(0f, s.minX());
    }

    @Test
    void rejectsAsciiStl() {
        var ascii = "solid cube\nfacet normal 0 0 1\n".repeat(10).getBytes();
        var e = assertThrows(IOException.class, () -> StlReader.read(new ByteArrayInputStream(ascii)));
        assertEquals("ASCII STL not supported, binary only", e.getMessage());
    }

    @Test
    void rejectsTruncatedFile() {
        var full = binaryStl();
        var cut = java.util.Arrays.copyOf(full, full.length - 20);
        assertThrows(IOException.class, () -> StlReader.read(new ByteArrayInputStream(cut)));
    }
}

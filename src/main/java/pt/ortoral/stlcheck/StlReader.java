package pt.ortoral.stlcheck;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/** Reads binary STL: 80-byte header, uint32 triangle count, then 50 bytes per triangle. */
public final class StlReader {

    private static final int HEADER_BYTES = 80;
    private static final int TRIANGLE_BYTES = 50; // 12 floats (normal + 3 vertices) + 2 padding

    public record Stats(
            long triangles,
            float minX, float minY, float minZ,
            float maxX, float maxY, float maxZ) {

        public float width()  { return maxX - minX; }
        public float height() { return maxY - minY; }
        public float depth()  { return maxZ - minZ; }
    }

    private StlReader() {}

    public static Stats read(InputStream in) throws IOException {
        var head = in.readNBytes(HEADER_BYTES + 4);
        if (head.length < HEADER_BYTES + 4) {
            throw new IOException("File too short to be a binary STL");
        }
        if (new String(head, 0, 5).equalsIgnoreCase("solid")) {
            // ponytail: ASCII STL rejected, not parsed. Add a text parser if real files show up as ASCII.
            throw new IOException("ASCII STL not supported, binary only");
        }

        long count = ByteBuffer.wrap(head, HEADER_BYTES, 4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .getInt() & 0xFFFFFFFFL;

        float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE, minZ = Float.MAX_VALUE;
        float maxX = -Float.MAX_VALUE, maxY = -Float.MAX_VALUE, maxZ = -Float.MAX_VALUE;

        var buf = ByteBuffer.allocate(TRIANGLE_BYTES).order(ByteOrder.LITTLE_ENDIAN);
        for (long t = 0; t < count; t++) {
            var bytes = in.readNBytes(TRIANGLE_BYTES);
            if (bytes.length < TRIANGLE_BYTES) {
                throw new IOException("Truncated at triangle " + t + " of " + count);
            }
            buf.clear().put(bytes).flip().position(12); // skip the normal vector

            for (int v = 0; v < 3; v++) {
                float x = buf.getFloat(), y = buf.getFloat(), z = buf.getFloat();
                minX = Math.min(minX, x); maxX = Math.max(maxX, x);
                minY = Math.min(minY, y); maxY = Math.max(maxY, y);
                minZ = Math.min(minZ, z); maxZ = Math.max(maxZ, z);
            }
        }

        if (count == 0) {
            return new Stats(0, 0, 0, 0, 0, 0, 0);
        }
        return new Stats(count, minX, minY, minZ, maxX, maxY, maxZ);
    }
}

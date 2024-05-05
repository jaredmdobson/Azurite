package org.azurite.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class DataConverter {

  public static ByteBuffer loadInputStreamToByteBuffer(InputStream inputStream) throws IOException {
    byte[] buffer = new byte[1024];
    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

    int bytesRead;
    while ((bytesRead = inputStream.read(buffer)) != -1) {
      byteStream.write(buffer, 0, bytesRead);
    }

    byte[] byteArray = byteStream.toByteArray();
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(byteArray.length);

    for (byte b : byteArray) {
      byteBuffer.put(b);
    }
    byteBuffer.flip(); // Prepare the buffer for reading

    return byteBuffer;
  }
}

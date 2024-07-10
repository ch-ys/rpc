package org.example.serializer;

import java.io.IOException;


public interface Serializer {

    <T> byte[] doSerialize(T object) throws IOException;

    <T> T deSerialize(byte[] bytes, Class<T> type) throws IOException;
}

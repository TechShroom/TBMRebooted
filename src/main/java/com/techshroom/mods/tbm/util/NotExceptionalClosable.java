package com.techshroom.mods.tbm.util;


public interface NotExceptionalClosable extends AutoCloseable {
    
    @Override
    void close();

}

package org.zeromq.jni;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

public final class PollItems
{
    private static final Unsafe unsafe;
    static
    {
        try
        {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private PollItem flyweight = new PollItem();
    private long address;
    private int length;

    private PollItems()
    {
    }

    public static PollItems allocatePollItems(int num)
    {
        PollItems items = new PollItems();
        items.address = unsafe.allocateMemory(PollItem.OBJECT_SIZE * num);
        items.length = num;
        items.flyweight.setObjectOffset(items.address);
        return items;
    }

    public int length()
    {
        return length;
    }
    
    public long address()
    {
        return address;
    }

    public PollItem get(int index)
    {
        final long offset = address + (index * PollItem.OBJECT_SIZE);
        flyweight.setObjectOffset(offset);
        return flyweight;
    }
    
    public void destroy() {
        unsafe.freeMemory(address);
    }

    public static class PollItem
    {
        private static int offset = 0;
        private static final int SOCKET_OFFSET = offset += 0;
        private static final int FD_OFFSET = offset += 8;
        private static final int EVENTS_OFFSET = offset += 4;
        private static final int REVENTS_OFFSET = offset += 2;
        private static final int OBJECT_SIZE = offset += 2;
        
        private long objectOffset;
        
        public PollItem()
        {
        }
        
        private void setObjectOffset(long offset)
        {
            this.objectOffset = offset;
        }

        public long getSocket()
        {
            return unsafe.getLong(objectOffset + SOCKET_OFFSET);
        }
        
        public void setSocket(long socket)
        {
            unsafe.putLong(objectOffset + SOCKET_OFFSET, socket);
        }

        public int getFD()
        {
            return unsafe.getInt(objectOffset + FD_OFFSET);
        }

        public void setFD(int fd)
        {
            unsafe.putInt(objectOffset + SOCKET_OFFSET, fd);
        }

        public short getEvents()
        {
            return unsafe.getShort(objectOffset + EVENTS_OFFSET);
        }

        public void setEvents(short events)
        {
            unsafe.putShort(objectOffset + EVENTS_OFFSET, events);
        }
        
        public short getREvents()
        {
            return unsafe.getShort(objectOffset + REVENTS_OFFSET);
        }

        public void setREvents(short revents)
        {
            unsafe.putShort(objectOffset + REVENTS_OFFSET, revents);
        }
    }
}

package cn.yaolianhua.juc.semaphore;

import java.util.concurrent.Semaphore;

/**
 * @author yaolianhua789@gmail.com
 * @date 2019-11-07 16:20
 **/
public class Pool {

    private static final int MAX_AVAILABLE = 6;
    private final Semaphore available = new Semaphore(MAX_AVAILABLE,true);

    protected Object[] items = new Object[]{"item-1","item-2","item-3","item-4","item-5","item-6"};
    protected boolean[] used = new boolean[MAX_AVAILABLE];

    public Object getItem(int acquire) throws InterruptedException {
        available.acquire(acquire);
        return getNextAvailableItem();
    }

    public void putItem(Object item,int acquire){
        if (markAsUnused(item))
            available.release(acquire);
    }

    public int getAvailablePermits(){
        return available.availablePermits();
    }


    private synchronized Object getNextAvailableItem(){
        for (int i = 0; i < MAX_AVAILABLE; i++) {
            if (!used[i]){
                used[i] = true;
                return items[i];
            }
        }
        return null;
    }

    private synchronized boolean markAsUnused(Object item){
        for (int i = 0; i < MAX_AVAILABLE; i++) {
            if (item == items[i]){
                if (used[i]) {
                    used[i] = false;
                    return true;
                }
            }else
                return false;
        }
        return false;
    }
}

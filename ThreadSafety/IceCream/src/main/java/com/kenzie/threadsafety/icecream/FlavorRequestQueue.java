package com.kenzie.threadsafety.icecream;

import com.kenzie.threadsafety.icecream.model.Flavor;

import java.util.LinkedList;
import java.util.Queue;

public final class FlavorRequestQueue {
    private final Queue<Flavor> flavorQueue;

    public FlavorRequestQueue() {
        flavorQueue = new LinkedList<>();
    }

    public synchronized void needFlavor(Flavor flavor) {
        flavorQueue.add(flavor);
    }

    public Flavor nextNeededFlavor() {
        Flavor flavor = pollFlavor(flavorQueue);
        while (flavor == null) {
            try {
                Thread.sleep(10L);
                flavor = pollFlavor(flavorQueue);
            } catch (InterruptedException e) {
                System.out.println("!!!Interrupted waiting for flavor request!!!");
                e.printStackTrace();
                throw new RuntimeException("Interrupted waiting for flavor request!", e);
            }
        }
        return flavor;
    }

    public synchronized Flavor pollFlavor(Queue<Flavor> flavors) {
        return flavors.poll();
    }

    public int requestCount() {
        return flavorQueue.size();
    }
}

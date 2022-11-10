package com.kenzie.threadsafety.icecream;

import com.kenzie.threadsafety.icecream.FlavorRequestQueue;
import com.kenzie.threadsafety.icecream.IceCreamMaker;
import com.kenzie.threadsafety.icecream.dao.CartonDao;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.kenzie.threadsafety.icecream.model.Flavor.CHOCOLATE;
import static com.kenzie.threadsafety.icecream.model.Flavor.STRAWBERRY;
import static com.kenzie.threadsafety.icecream.model.Flavor.VANILLA;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Phase2Test {
    CartonDao cartonDao = new CartonDao();
    FlavorRequestQueue flavorRequestQueue = new FlavorRequestQueue();

    @Test
    public void flavorRequestQueue_10000Requests_handles10000RequestsForEachFlavor() throws Exception {
        // GIVEN
        // 20 ice cream makers, executing in their own threads
        List<IceCreamMaker> iceCreamMakers = IntStream.range(0, 20)
            .mapToObj(i -> new IceCreamMaker(cartonDao, flavorRequestQueue))
            .collect(Collectors.toList());
        ExecutorService executorService = Executors.newCachedThreadPool();
        iceCreamMakers.forEach(executorService::submit);
        executorService.shutdown();
        // 10K requests to be made at a time
        int numRequests = 10000;

        // WHEN
        // We actually make the requests
        IntStream.range(0, numRequests)
            .forEach(i -> flavorRequestQueue.needFlavor(CHOCOLATE));
        IntStream.range(0, numRequests)
                .forEach(i -> flavorRequestQueue.needFlavor(VANILLA));
        IntStream.range(0, numRequests)
                .forEach(i -> flavorRequestQueue.needFlavor(STRAWBERRY));
        Thread.sleep(6000L);

        // THEN
        // The service processes all the requests without deadlocking
        assertEquals(0, flavorRequestQueue.requestCount(),
                String.format("All requests not fulfilled! Completed - %d chocolate, %d vanilla, %d strawberry.",
                        cartonDao.inventoryOfFlavor(CHOCOLATE),
                        cartonDao.inventoryOfFlavor(VANILLA),
                        cartonDao.inventoryOfFlavor(STRAWBERRY)));
        assertEquals(numRequests, cartonDao.inventoryOfFlavor(CHOCOLATE),
                "Completed - " + cartonDao.inventoryOfFlavor(CHOCOLATE) + " chocolate cartons.");
        assertEquals(numRequests, cartonDao.inventoryOfFlavor(VANILLA),
                "Completed - " + cartonDao.inventoryOfFlavor(VANILLA) + " vanilla cartons.");
        assertEquals(numRequests, cartonDao.inventoryOfFlavor(STRAWBERRY),
                "Completed - " + cartonDao.inventoryOfFlavor(STRAWBERRY) + " strawberry cartons.");
    }

}

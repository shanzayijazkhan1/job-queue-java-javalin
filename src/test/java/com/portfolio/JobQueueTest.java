package com.portfolio;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JobQueueTest {
    @Test
    public void enqueueAndTake() throws Exception {
        JobQueue jq = new JobQueue();
        long id = jq.enqueue("hi");
        assertEquals("queued", jq.status(id));
        Job j = jq.take();
        assertEquals(id, j.id);
        assertEquals("taken", jq.status(id));
    }
}

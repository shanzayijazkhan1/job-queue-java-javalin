package com.portfolio;

import io.javalin.Javalin;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class App {
    public static void main(String[] args) {
        JobQueue queue = new JobQueue();
        Javalin app = Javalin.create().start(8080);
        app.post("/jobs", ctx -> {
            String payload = ctx.body();
            long id = queue.enqueue(payload);
            ctx.json(Map.of("id", id));
        });
        app.post("/jobs/take", ctx -> {
            Job j = queue.take();
            ctx.json(Map.of("id", j.id, "payload", j.payload));
        });
        app.get("/jobs/{id}", ctx -> {
            long id = Long.parseLong(ctx.pathParam("id"));
            String status = queue.status(id);
            ctx.json(Map.of("id", id, "status", status));
        });
    }
}

class Job {
    final long id;
    final String payload;
    Job(long id, String payload){ this.id = id; this.payload = payload; }
}

class JobQueue {
    private final AtomicLong counter = new AtomicLong();
    private final BlockingQueue<Job> q = new LinkedBlockingQueue<>();
    private final ConcurrentHashMap<Long, String> status = new ConcurrentHashMap<>();

    public long enqueue(String payload) {
        long id = counter.incrementAndGet();
        Job j = new Job(id, payload);
        status.put(id, "queued");
        q.add(j);
        return id;
    }
    public Job take() throws InterruptedException {
        Job j = q.take();
        status.put(j.id, "taken");
        return j;
    }
    public String status(long id) {
        return status.getOrDefault(id, "unknown");
    }
}

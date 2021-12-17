package de.andrena.tools.staticcodeanalysis.application;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

class TestAppender extends AbstractAppender {

    private final List<LogEvent> events = new ArrayList<>();

    protected TestAppender() {
        super(TestAppender.class.getSimpleName(), null, null, false, null);
        setStarted();
    }

    @Override
    public void append(LogEvent event) {
        events.add(event.toImmutable());
    }

    public List<LogEvent> getLoggedEvents() {
        return unmodifiableList(events);
    }

    public void clearLogEvents() {
        events.clear();
    }
}
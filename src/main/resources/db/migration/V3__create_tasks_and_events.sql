CREATE TABLE tasks (
    id SERIAL PRIMARY KEY,
    task_title TEXT NOT NULL,
    task_description TEXT,
    calendar_id BIGINT NOT NULL,
    CONSTRAINT fk_tasks_calendar FOREIGN KEY (calendar_id) REFERENCES calendars (id)
);

CREATE INDEX idx_tasks_calendar_id ON tasks (calendar_id);

CREATE TABLE events (
    id SERIAL PRIMARY KEY,
    event_title TEXT NOT NULL,
    event_description TEXT,
    task_priority VARCHAR(255) NOT NULL CHECK (
        task_priority IN ('LOW', 'MEDIUM', 'HIGH')
    ),
    calendar_id BIGINT NOT NULL,
    CONSTRAINT fk_events_calendar FOREIGN KEY (calendar_id) REFERENCES calendars (id)
);

CREATE INDEX idx_events_calendar_id ON events (calendar_id);
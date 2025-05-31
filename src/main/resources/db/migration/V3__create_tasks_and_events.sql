CREATE TABLE events (
    id BIGSERIAL PRIMARY KEY,
    event_title TEXT NOT NULL,
    event_description TEXT,
    task_priority VARCHAR(255),
    calendar_id BIGINT NOT NULL,
    event_date TIMESTAMP,
    CONSTRAINT chk_events_priority CHECK (
        task_priority IN ('LOW', 'MEDIUM', 'HIGH')
    ),
    CONSTRAINT fk_events_calendar FOREIGN KEY (calendar_id) REFERENCES calendars (id)
);

CREATE INDEX idx_events_calendar_id ON events (calendar_id);

CREATE TABLE tasks (
    id BIGSERIAL PRIMARY KEY,
    task_title TEXT NOT NULL,
    task_description TEXT,
    calendar_id BIGINT NOT NULL,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    task_date TIMESTAMP,
    task_priority VARCHAR(255),
    CONSTRAINT chk_tasks_priority CHECK (
        task_priority IN ('LOW', 'MEDIUM', 'HIGH')
    ),
    CONSTRAINT fk_tasks_calendar FOREIGN KEY (calendar_id) REFERENCES calendars (id)
);

CREATE INDEX idx_tasks_calendar_id ON tasks (calendar_id);
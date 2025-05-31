CREATE TABLE calendars (
    id SERIAL PRIMARY KEY,
    calendar_name TEXT NOT NULL
);

CREATE TABLE calendars_users (
    calendar_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_calendars_users_calendar FOREIGN KEY (calendar_id) REFERENCES calendars (id),
    CONSTRAINT fk_calendars_users_user FOREIGN KEY (user_id) REFERENCES users (id),
    PRIMARY KEY (calendar_id, user_id)
);

CREATE INDEX idx_calendars_users_calendar_id ON calendars_users (calendar_id);

CREATE INDEX idx_calendars_users_user_id ON calendars_users (user_id);
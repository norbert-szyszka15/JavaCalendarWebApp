CREATE TABLE calendars (
    id BIGSERIAL PRIMARY KEY,
    calendar_name TEXT NOT NULL
);

CREATE TABLE calendars_users (
    calendar_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_calendars_users PRIMARY KEY (calendar_id, user_id),
    CONSTRAINT fk_calendars_users_calendar FOREIGN KEY (calendar_id) REFERENCES calendars (id),
    CONSTRAINT fk_calendars_users_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE INDEX idx_calendars_users_calendar ON calendars_users (calendar_id);

CREATE INDEX idx_calendars_users_user ON calendars_users (user_id);
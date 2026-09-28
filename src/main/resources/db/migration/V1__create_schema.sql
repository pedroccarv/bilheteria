CREATE TABLE events (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    venue VARCHAR(255) NOT NULL,
    starts_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE lots (
    id UUID PRIMARY KEY,
    event_id UUID NOT NULL REFERENCES events (id),
    name VARCHAR(255) NOT NULL,
    price NUMERIC(10, 2) NOT NULL CHECK (price > 0),
    total_quantity INTEGER NOT NULL CHECK (total_quantity > 0),
    available_quantity INTEGER NOT NULL,
    sales_start TIMESTAMP,
    sales_end TIMESTAMP,
    version BIGINT NOT NULL,
    CONSTRAINT lots_available_quantity_check CHECK (available_quantity BETWEEN 0 AND total_quantity)
);

CREATE TABLE reservations (
    id UUID PRIMARY KEY,
    lot_id UUID NOT NULL REFERENCES lots (id),
    customer_email VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity BETWEEN 1 AND 4),
    unit_price NUMERIC(10, 2) NOT NULL CHECK (unit_price > 0),
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'PAID', 'EXPIRED', 'CANCELLED')),
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    paid_at TIMESTAMP
);

CREATE INDEX idx_reservations_pending_expiry
    ON reservations (expires_at)
    WHERE status = 'PENDING';

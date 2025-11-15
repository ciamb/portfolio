-- Create enum type for status
CREATE TYPE contact_me_status AS ENUM ('PENDING', 'PROCESSED', 'ERROR');

-- Create contact_me table
CREATE TABLE contact_me (
                            id BIGSERIAL PRIMARY KEY,
                            email VARCHAR(255) NOT NULL,
                            name VARCHAR(150) NOT NULL,
                            message TEXT NOT NULL,
                            contact_back BOOLEAN NOT NULL,
                            status contact_me_status NOT NULL DEFAULT 'PENDING',
                            attempts INTEGER NOT NULL DEFAULT 0,
                            last_attempt_at TIMESTAMP WITH TIME ZONE,
                            error_reason TEXT,
                            created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_contact_me_status ON contact_me(status);
CREATE INDEX idx_contact_me_created_at ON contact_me(created_at);
CREATE INDEX idx_contact_me_email ON contact_me(email);



-- Re-create the schema for the application

DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;


-- Authors table

CREATE TABLE authors (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT NOT NULL UNIQUE,
    bio TEXT,
    olid TEXT UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);


-- Books table

CREATE TABLE books (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title TEXT NOT NULL,
    subtitle TEXT,
    edition TEXT,
    description TEXT,
    olid TEXT UNIQUE,
    isbn_10 TEXT[],
    isbn_13 TEXT[],
    publishers TEXT[],
    publish_year INT,
    number_of_pages INT,
    languages TEXT[],
    cover_urls TEXT[],
    subjects TEXT[],
    format TEXT,
    dimensions TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE book_authors (
    book_id UUID REFERENCES books(id) ON DELETE CASCADE,
    author_id UUID REFERENCES authors(id) ON DELETE CASCADE,
    role TEXT,
    PRIMARY KEY (book_id, author_id)
);

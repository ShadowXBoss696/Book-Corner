--
-- Enum: Author Roles
--

CREATE TYPE author_role AS ENUM (
	'AUTHOR',
	'EDITOR',
	'ILLUSTRATOR',
	'TRANSLATOR'
);


--
-- Table: Books
--

CREATE SEQUENCE books_id_seq AS BIGINT;

CREATE TABLE books (
	id BIGINT NOT NULL DEFAULT nextval('books_id_seq'),
	title VARCHAR(1024) NOT NULL,
	summary VARCHAR(4096),
	publication_year INTEGER,
	page_count INTEGER,
	olid VARCHAR(20),
	isbn_10 VARCHAR(10),
	isbn_13 VARCHAR(13),
	language_code VARCHAR(10) DEFAULT 'en',
	created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
	updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
	CONSTRAINT books_pk PRIMARY KEY (id)
);

CREATE UNIQUE INDEX idx_books_uk_olid ON books (olid);
CREATE UNIQUE INDEX idx_books_uk_isbn_10 ON books (isbn_10);
CREATE UNIQUE INDEX idx_books_uk_isbn_13 ON books (isbn_13);


--
-- Table: Authors
--

CREATE SEQUENCE authors_id_seq AS BIGINT;

CREATE TABLE authors (
	id BIGINT NOT NULL DEFAULT nextval('authors_id_seq'),
	name VARCHAR(1024) NOT NULL,
	bio VARCHAR(4096),
	olid VARCHAR(20),
	created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
	updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
	CONSTRAINT authors_pk PRIMARY KEY (id)
);

CREATE UNIQUE INDEX idx_authors_uk_olid ON authors (olid);


--
-- Join: Book + Authors
--

CREATE SEQUENCE book_authors_id_seq AS BIGINT;

CREATE TABLE book_authors (
	id BIGINT NOT NULL DEFAULT nextval('book_authors_id_seq'),
	book_id BIGINT NOT NULL,
	author_id BIGINT NOT NULL,
	role author_role NOT NULL DEFAULT 'AUTHOR',
	ordinal INTEGER NOT NULL DEFAULT 0,
	CONSTRAINT book_authors_pk PRIMARY KEY (id),
	CONSTRAINT book_authors_reln_books_fk FOREIGN KEY (book_id) REFERENCES books (id)
		ON DELETE CASCADE,
	CONSTRAINT book_authors_reln_authors_fk FOREIGN KEY (author_id) REFERENCES authors (id)
		ON DELETE CASCADE
);

CREATE UNIQUE INDEX idx_book_authors_uk_book_id_and_author_id ON book_authors (book_id, author_id, role);

CREATE INDEX idx_book_authors_book_id_role_ordinal ON book_authors (book_id, role, ordinal);
CREATE INDEX idx_book_authors_author_id ON book_authors (author_id);

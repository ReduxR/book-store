INSERT INTO categories (id, name)
VALUES
    (1, 'Fantasy'),
    (2, 'Adventure'),
    (3, 'Sci-Fi');

INSERT INTO books (
    id,
    title,
    author,
    isbn,
    price,
    description,
    cover_image,
    is_deleted
)
VALUES
    (
        1,
        'The Hobbit',
        'J. R. R. Tolkien',
        '9780261102217',
        29.95,
        'Lorem ipsum',
        'http://example.com/hobbit.jpg',
        0
    ),
    (
        2,
        'Dune',
        'Frank Herbert',
        '9780441172719',
        24.95,
        'Lorem ipsum',
        'http://example.com/dune.jpg',
        0
    ),
    (
        3,
        '1984',
        'G. Orwell',
        '9781122334451',
        19.95,
        'Lorem ipsum',
        'http://example.com/1984.jpg',
        0
    );

INSERT INTO books_categories (book_id, category_id)
VALUES
    (1, 1), -- Hobbit -> Fantasy
    (1, 2), -- Hobbit -> Adventure
    (2, 3), -- Dune -> Sci-Fi
    (3, 1); -- 1984 -> Fantasy
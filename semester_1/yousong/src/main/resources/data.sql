INSERT INTO artist(id,name)
VALUES (1,'Dave'),
       (2,'Ken Carson'),
       (3,'Nemzz'),
       (4,'goddard'),
       (5,'Mozart'),
       (6,'brakence'),
       (7,'Boondawg'),
       (8,'Playboy Carti'),
       (9,'Metro Boomin'),
       (10,'Lil Baby');


INSERT INTO song(id, title, artist_id, length)
VALUES (1,'Starlight', 1, 208),
       (2,'Vampire Hour', 2, 189),
       (3,'Evicted', 3, 181),
       (4,'Escape', 3, 148),
       (5,'Wasted Youth', 4, 168),
       (6,'Divertimento In D Allegro', 5, 239),
       (7,'bugging', 6, 197),
       (8,'GANGSIGNS RMX', 7, 242),
       (9,'Vamp Anthem', 2, 180),
       (10,'I Cant Save You', 8, 210),
       (11,'Me or Sum', 9, 248);

INSERT INTO song_genre(song_id, genre)
values (1, 'UK Rap'),
       (2, 'Rap'),
       (3, 'UK Rap'),
       (4, 'UK Rap'),
       (5, 'D&B'),
       (6, 'Classical'),
       (7, 'Hyperpop'),
       (8, 'Deutsch Rap'),
       (9, 'Rap'),
       (10, 'Hip Hop'),
       (11, 'Melodic Rap');



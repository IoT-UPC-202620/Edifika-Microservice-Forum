-- Datos de prueba locales (solo corre con SEED_MODE=always). Idempotente.
-- resident_id = usuarios de IAM (2..5).

INSERT INTO posts (id, content, created_at, image_url, resident_id, title, author_role) VALUES
  (1, 'Alguien sabe a que hora abre el gimnasio los domingos?', now() - interval '6 days', NULL, 4, 'Horario del gimnasio', 'TENANT'),
  (2, 'Propongo organizar una parrillada de integracion el proximo mes en la zona BBQ.', now() - interval '4 days', NULL, 3, 'Parrillada de integracion', 'OWNER'),
  (3, 'Encontre unas llaves en el ascensor del piso 2, las deje en recepcion.', now() - interval '2 days', NULL, 2, 'Llaves encontradas', 'OWNER'),
  (4, 'La luz del pasillo del piso 3 de la torre parpadea desde ayer.', now() - interval '10 hours', NULL, 5, 'Luz del pasillo piso 3', 'OWNER')
ON CONFLICT DO NOTHING;

-- Rol del autor para posts sembrados antes de existir la columna
UPDATE posts SET author_role = 'OWNER'  WHERE author_role IS NULL AND resident_id IN (2, 3, 5);
UPDATE posts SET author_role = 'TENANT' WHERE author_role IS NULL AND resident_id = 4;
UPDATE posts SET author_role = 'ADMIN'  WHERE author_role IS NULL AND resident_id = 1;

SELECT setval(pg_get_serial_sequence('posts', 'id'), (SELECT MAX(id) FROM posts));

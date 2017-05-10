CREATE OR REPLACE FUNCTION acquire_resource_lock(
  p_resource_name text,
  p_lock_name text,
  p_expires_at TIMESTAMP WITH TIME ZONE)
  RETURNS void AS
$BODY$
DELETE FROM resource_lock
      WHERE lock_name = p_lock_name
        AND expires_at IS NOT NULL
        AND expires_at < now();

INSERT INTO resource_lock(
  resource_name,
  lock_name,
  created_at,
  expires_at
)
VALUES (
  p_resource_name,
  p_lock_name,
  now(),
  p_expires_at
);
$BODY$
LANGUAGE sql VOLATILE SECURITY DEFINER;
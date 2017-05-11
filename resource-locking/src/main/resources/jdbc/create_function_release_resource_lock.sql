CREATE OR REPLACE FUNCTION release_resource_lock(
  p_resource_name text,
  p_lock_name text)
  RETURNS void AS
$BODY$
DELETE FROM resource_lock
      WHERE resource_name = p_resource_name
        AND lock_name = p_lock_name;
$BODY$
LANGUAGE sql VOLATILE SECURITY DEFINER;
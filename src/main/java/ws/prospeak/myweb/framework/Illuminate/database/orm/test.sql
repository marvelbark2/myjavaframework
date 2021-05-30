CREATE OR REPLACE FUNCTION public.iscardaccessibletoequip(cardid integer, positionid integer)
              RETURNS boolean
              LANGUAGE plpgsql
              AS $function$
              declare
              spaceid int;
begin
    IF (SELECT active from access_card where card_id = cardid) THEN
IF EXISTS(SELECT * FROM position_ WHERE position_id = positionid) THEN
SELECT space_id into spaceid FROM position_ WHERE position_id = positionid;
IF isCardAccessibleToSpace(cardid, spaceid) THEN
IF EXISTS(SELECT * FROM card_position WHERE card_id = cardid AND position_id = positionid) THEN
return (SELECT enabled FROM card_position WHERE card_id = cardid AND position_id = positionid);
ELSE IF EXISTS(SELECT * FROM access_card JOIN person p on access_card.person_id = p.person_id JOIN roles r on p.role_id = r.role_id JOIN role_position rp on p.role_id = rp.role_id
WHERE access_card.card_id = cardid AND rp.position_id = positionid) THEN
return (SELECT rp.enabled FROM access_card JOIN person p on access_card.person_id = p.person_id JOIN roles r on p.role_id = r.role_id JOIN role_position rp on p.role_id = rp.role_id
WHERE access_card.card_id = cardid AND rp.position_id = positionid);
ELSE
return false;
end if;
end if;
ELSE
return false;
end if;
ELSE
return false;
end if;
ELSE
return false;
end if;

end
$function$
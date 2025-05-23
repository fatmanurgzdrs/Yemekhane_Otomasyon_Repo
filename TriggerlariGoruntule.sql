SELECT 
    tr.name AS Trigger_Adi,
    OBJECT_NAME(tr.parent_id) AS Tablo_Adi,
    tr.type_desc AS Trigger_Tipi,
    m.definition AS Trigger_Kodu
FROM 
    sys.triggers tr
JOIN 
    sys.sql_modules m ON tr.object_id = m.object_id;
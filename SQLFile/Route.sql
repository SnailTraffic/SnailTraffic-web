create or replace PROCEDURE ROUTE 
    (
      STARTSITE IN VARCHAR2 
    , ENDSITE IN VARCHAR2 
    , linename in Varchar2
    , runleft in Number
    , sp_coesor out sys_refcursor
    ) AS 
    -- 根据起始站点，终止站点，线路名，是否左行->获得线路路径
    -- 注意：需要截断临时表

    interMediary Varchar(64);-- 中间站点名
    insertStr Varchar(128);

    BEGIN
      interMediary := '';
      insertStr := 'insert into Temp_Route values(''' || STARTSITE || ''')';
      execute immediate insertStr; 
      intermediary := startSite;
  
      While intermediary != endSite 
        LOOP
          --DBMS_OUTPUT.PUT_LINE(endSite);
          -- 获得中间站点名
          Select ENDSNAME Into interMediary
            From View_NEXTSITE
            Where STARTSNAME = intermediary
              AND Lname = linename
              AND isleft = runleft;
          
          --DBMS_OUTPUT.PUT_LINE(interMediary);
          insertStr := 'insert into Temp_Route values(''' || interMediary || ''')';
          execute immediate insertStr; 
        END LOOP;
      Open sp_coesor FOR 
        SELECT * FROM TEMP_Route;
     execute immediate 'delete from TEMP_Route'; 
    END ROUTE;
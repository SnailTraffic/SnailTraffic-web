create or replace PROCEDURE INITIALIZATE_GROUP7DB AS 
-- 初始化数据库
tem smallint;   --临时变量

BEGIN
  --删除表------------------------------
  
  -- 判断删除下一站表*************************************
  SELECT COUNT(1) INTO tem FROM ALL_TABLES WHERE TABLE_NAME = 'NEXTSITE';
  IF tem = 1 THEN
    EXECUTE IMMEDIATE'
      alter table NEXTSITE drop constraint PK_UNINO_KEY cascade';
    EXECUTE IMMEDIATE'
      alter table NEXTSITE drop constraint FK_START_SID cascade';
    EXECUTE IMMEDIATE'
      alter table NEXTSITE drop constraint FK_END_SID cascade';
    EXECUTE IMMEDIATE'
      alter table NEXTSITE drop constraint FK_BY_LID cascade'; 
    EXECUTE IMMEDIATE'
      DROP TABLE NEXTSITE';
  END IF;
  
  --判断站点到线路信息
  SELECT COUNT(1) INTO tem FROM ALL_TABLES WHERE TABLE_NAME = 'SITETOLINE';
  IF tem = 1 THEN
  --删除约束
    EXECUTE IMMEDIATE'
    alter table SITETOLINE drop constraint FK_SID cascade';
    EXECUTE IMMEDIATE'
    DROP TABLE SITETOLINE';
  END IF;
  
  --判断线路到站点信息
  SELECT COUNT(1) INTO tem FROM ALL_TABLES WHERE TABLE_NAME = 'LINETOSITE';
  IF tem = 1 THEN
  --删除约束
    EXECUTE IMMEDIATE'
    alter table LINETOSITE drop constraint FK_LID cascade';
    EXECUTE IMMEDIATE'
    DROP TABLE LINETOSITE';
  END IF;
  
  -- 判断删除站点信息表
  SELECT COUNT(1) INTO tem FROM ALL_TABLES WHERE TABLE_NAME = 'SITEINFO';
  IF tem = 1 THEN
   EXECUTE IMMEDIATE'
    alter table SITEINFO drop constraint PK_SID cascade';
    EXECUTE IMMEDIATE'
    DROP TABLE SITEINFO';
  END IF;
  
  -- 判断删除线路信息表
  SELECT COUNT(1) INTO tem FROM ALL_TABLES WHERE TABLE_NAME = 'LINEINFO';
  IF tem = 1 THEN
    EXECUTE IMMEDIATE'
    alter table LINEINFO drop constraint PK_LID cascade';
    EXECUTE IMMEDIATE'
    DROP TABLE LINEINFO';
  END IF;

  -- 判断删除临时表
  EXECUTE IMMEDIATE'
    DROP TABLE Temp_DIRECTACCESS';
    -- 判断删除临时表
  EXECUTE IMMEDIATE'
    DROP TABLE TEMP_ROUTE';  
  --------------------------------------------------
  --创建表
  --站点信息表：站点id、站点名、站点地址、站点附近标志建筑1、站点附近标志建筑2
  EXECUTE IMMEDIATE'
  CREATE TABLE SITEINFO
  (
    Sid smallint,
    Sname varchar2(30) NOT NULL,
    Saddress varchar2(50),
    Slandmark1 varchar2(50),
    Slandmark2 varchar2(50),
    CONSTRAINT PK_SID PRIMARY KEY (Sid)
  )';
  
  --线路信息表：线路id、线路名、线路区间、首站开班、末站开班、首站收班、末站收班、票价、刷卡、公司
   EXECUTE IMMEDIATE'
  CREATE TABLE LINEINFO
  (
    Lid smallint,
    Lname varchar2(30) NOT NULL,
    Linterval varchar2(50),
    Lfirstopen varchar2(15),
    Llastopen varchar2(15),
    Lfirstclose varchar2(15),
    Llastclose varchar2(15),
    Lprice varchar2(35),
    Lcardprice varchar2(35),
    Lcompany varchar2(50),
    remark varchar2(50),
    CONSTRAINT PK_LID PRIMARY KEY (Lid)
  )';
  
  --线路站点表：线路id、站点id序列
   EXECUTE IMMEDIATE'
  CREATE TABLE LINETOSITE
  (
    Lid smallint,
    LSidseq varchar2(256),
    RSidseq varchar2(256),
    CONSTRAINT FK_LID FOREIGN KEY (Lid) REFERENCES LINEINFO(Lid)  
    ON DELETE CASCADE
  )';
  
  --站点线路表：站点id、线路id序列
   EXECUTE IMMEDIATE'
  CREATE TABLE SITETOLINE
  (
    Sid smallint,
    LLidseq varchar2(128),
    RLidseq varchar2(128),
    CONSTRAINT FK_SID FOREIGN KEY (Sid) REFERENCES SITEINFO(Sid)
    ON DELETE CASCADE
  )';

  --站点线路视图：站点名、线路id序列
  EXECUTE IMMEDIATE'
  CREATE OR REPLACE VIEW View_SiteLine(sname, llidseq, rlidseq) AS 
    SELECT SITEINFO.sname, SITETOLINE.llidseq, SITETOLINE.rlidseq
    FROM SITEINFO, SITETOLINE
    WHERE SITEINFO.sid = SITETOLINE.sid
  ';
  
  --线路站点视图：线路名、站点id序列
  EXECUTE IMMEDIATE'
  CREATE OR REPLACE VIEW View_LineSite(lname, lsidseq, rsidseq) AS 
    SELECT LINEINFO.lname, LINETOSITE.lsidseq, LINETOSITE.rsidseq
    FROM LINEINFO, LINETOSITE
    WHERE LINEINFO.lid = LINETOSITE.lid
  ';
   
  --下一站点表：站点id, 线路id, 下一站id, 时间，距离
  EXECUTE IMMEDIATE'
  CREATE TABLE NEXTSITE
  (
    StartSid smallint,
    Lid smallint,
    IsLeft number(1),
    EndSid smallint,
    RunTime smallint,
    Distance smallint,
    
    CONSTRAINT FK_START_SID FOREIGN KEY (StartSid) REFERENCES SITEINFO(Sid)
    ON DELETE CASCADE ,
    CONSTRAINT FK_END_SID FOREIGN KEY (EndSid) REFERENCES SITEINFO(Sid)
    ON DELETE CASCADE ,
    CONSTRAINT FK_BY_LID FOREIGN KEY (Lid) REFERENCES LINEINFO(Lid)
    ON DELETE CASCADE ,
    CONSTRAINT PK_UNINO_KEY PRIMARY KEY (StartSid, Lid, IsLeft, EndSid)
  )';
  
  --下一站点视图：站点名，线路名，下一个站点名，时间，距离
  EXECUTE IMMEDIATE'
  CREATE OR REPLACE VIEW View_NextSite(StartSname, Lname,IsLeft, EndSname, RunTime, Distance) AS 
    SELECT s1.Sname, LINEINFO.lname, NEXTSITE.IsLeft, s2.Sname, NEXTSITE.RunTime, NEXTSITE.Distance
    FROM SITEINFO s1, LINEINFO, SITEINFO s2, NEXTSITE
    WHERE  NEXTSITE.StartSid = s1.Sid   
      AND NEXTSITE.Lid = LINeINFO.Lid
      AND NEXTSITE.ENDSid = s2.Sid 
    WITH READ ONLY
  ';
  -- 创建临时表
  EXECUTE IMMEDIATE'
  CREATE  GLOBAL  TEMPORARY  TABLE  Temp_DIRECTACCESS  (  
                      sname Varchar(32),
                      lname Varchar(8),
                      runLeft NUMBER(38),
                      runTime  NUMBER(38),
                      distance NUMBER(38))On Commit Preserve Rows
  ';
  -- 创建路径临时表
  EXECUTE IMMEDIATE'
  CREATE GLOBAL TEMPORARY TABLE TEMP_ROUTE (
    SNAME VARCHAR2(64)
   ) On Commit Preserve Rows';

  -- 创建自定义类
  EXECUTE IMMEDIATE'
    create or replace type varTableType as table of NUMBER
  ';
  
  -- 创建程序包
  EXECUTE IMMEDIATE'
    create or replace PACKAGE PACKAGE AS 
    -- 自定义记录类型
    Type RecType Is Record
    (
      sname Varchar(32),
      lname Varchar(8),
      runLeft NUMBER(38),
      runTime  NUMBER(38),
      distance NUMBER(38)
    );
    -- 自定义表类型
    Type TabType Is Table Of  RecType  Index By Binary_Integer;
  END PACKAGE;
  ';
  
  
  --=============================================================函数
  -- 创建字符串分割函数
  EXECUTE IMMEDIATE'
    create or replace function str2numList
    (
      p_string in varchar2 
    ) RETURN varTableType AS
    -- 字符串转换数字数组函数
    v_str long default p_string || '','';
    v_n number;-- 数量
    v_data varTableType := varTableType();  --自定义类型
    BEGIN
      LOOP
        v_n := to_number(instr( v_str, '','' ));
        exit when (nvl(v_n,0) = 0);
        v_data.extend;
        SELECT to_number(ltrim(rtrim(substr(v_str,1,v_n-1)))) INTO　v_data( v_data.count )　
          FROM DUAL;
        --v_data( v_data.count ) := ltrim(rtrim(substr(v_str,1,v_n-1)));
        v_str := substr( v_str, v_n+1 );
      END LOOP;
    RETURN v_data;
  END;
  ';
  --=========================================================存储过程
  -- 创建删除线路对下一站点表存储过程
   EXECUTE IMMEDIATE'
    create or replace PROCEDURE DELETE_NEXTSITE_LINE 
    (
      LINENAME IN VARCHAR2 
    ) AS 
    --通过线路名删除关系
    lineid NUMBER;
    BEGIN
      --找到站点对应的id
      SELECT Lid INTO lineid 
        FROM LINEINFO 
        WHERE Lname = LINENAME;
    
      --删除起始站点id或终止站点id为siteid的站点
      DELETE FROM NEXTSITE
        WHERE Lid = lineid;
END DELETE_NEXTSITE_LINE;
';
  
  -- 创建删除站点对下一站点表
   EXECUTE IMMEDIATE'
    create or replace PROCEDURE DELETE_NEXTSITE_SITE 
    (
      SITENAME IN VARCHAR2 
    ) AS 
    --根据站点名，删除NEXTSITE表中所有包含次站点名的关系

    siteid NUMBER; --站点名对应的站点id
    BEGIN
      --找到站点对应的id
      SELECT Sid INTO siteid 
        FROM SITEINFO 
        WHERE Sname = SITENAME;
        
      --删除起始站点id或终止站点id为siteid的站点
      DELETE FROM NEXTSITE
        WHERE (startsid = siteid OR　endsid = siteid);
    END DELETE_NEXTSITE_SITE;
   ';
  
  EXECUTE IMMEDIATE'
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
      interMediary := '''';
      insertStr := ''insert into Temp_Route values('''' || STARTSITE || '''')'';
      execute immediate insertStr; 
      intermediary := startSite;
  
      While intermediary != endSite 
        LOOP
          DBMS_OUTPUT.PUT_LINE(endSite);
          -- 获得中间站点名
          Select ENDSNAME Into interMediary
            From View_NEXTSITE
            Where STARTSNAME = intermediary
              AND Lname = linename
              AND isleft = runleft;
          
          --DBMS_OUTPUT.PUT_LINE(interMediary);
          insertStr := ''insert into Temp_Route values('''' || interMediary || '''')'';
          execute immediate insertStr; 
        END LOOP;
      Open sp_coesor FOR 
        SELECT * FROM TEMP_Route;
      execute immediate ''delete from TEMP_Route''; 
    END ROUTE;
';

END INITIALIZATE_GROUP7DB;
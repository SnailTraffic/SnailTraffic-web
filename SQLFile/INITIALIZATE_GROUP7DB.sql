create or replace PROCEDURE INITIALIZATE_GROUP7DB AS 
-- ��ʼ�����ݿ�
tem smallint;   --��ʱ����

BEGIN
  --ɾ����------------------------------
  
  -- �ж�ɾ����һվ��*************************************
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
  
  --�ж�վ�㵽��·��Ϣ
  SELECT COUNT(1) INTO tem FROM ALL_TABLES WHERE TABLE_NAME = 'SITETOLINE';
  IF tem = 1 THEN
  --ɾ��Լ��
    EXECUTE IMMEDIATE'
    alter table SITETOLINE drop constraint FK_SID cascade';
    EXECUTE IMMEDIATE'
    DROP TABLE SITETOLINE';
  END IF;
  
  --�ж���·��վ����Ϣ
  SELECT COUNT(1) INTO tem FROM ALL_TABLES WHERE TABLE_NAME = 'LINETOSITE';
  IF tem = 1 THEN
  --ɾ��Լ��
    EXECUTE IMMEDIATE'
    alter table LINETOSITE drop constraint FK_LID cascade';
    EXECUTE IMMEDIATE'
    DROP TABLE LINETOSITE';
  END IF;
  
  -- �ж�ɾ��վ����Ϣ��
  SELECT COUNT(1) INTO tem FROM ALL_TABLES WHERE TABLE_NAME = 'SITEINFO';
  IF tem = 1 THEN
   EXECUTE IMMEDIATE'
    alter table SITEINFO drop constraint PK_SID cascade';
    EXECUTE IMMEDIATE'
    DROP TABLE SITEINFO';
  END IF;
  
  -- �ж�ɾ����·��Ϣ��
  SELECT COUNT(1) INTO tem FROM ALL_TABLES WHERE TABLE_NAME = 'LINEINFO';
  IF tem = 1 THEN
    EXECUTE IMMEDIATE'
    alter table LINEINFO drop constraint PK_LID cascade';
    EXECUTE IMMEDIATE'
    DROP TABLE LINEINFO';
  END IF;

  -- �ж�ɾ����ʱ��
  EXECUTE IMMEDIATE'
    DROP TABLE Temp_DIRECTACCESS';
    -- �ж�ɾ����ʱ��
  EXECUTE IMMEDIATE'
    DROP TABLE TEMP_ROUTE';  
  --------------------------------------------------
  --������
  --վ����Ϣ��վ��id��վ������վ���ַ��վ�㸽����־����1��վ�㸽����־����2
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
  
  --��·��Ϣ����·id����·������·���䡢��վ���ࡢĩվ���ࡢ��վ�հࡢĩվ�հࡢƱ�ۡ�ˢ������˾
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
  
  --��·վ�����·id��վ��id����
   EXECUTE IMMEDIATE'
  CREATE TABLE LINETOSITE
  (
    Lid smallint,
    LSidseq varchar2(256),
    RSidseq varchar2(256),
    CONSTRAINT FK_LID FOREIGN KEY (Lid) REFERENCES LINEINFO(Lid)  
    ON DELETE CASCADE
  )';
  
  --վ����·��վ��id����·id����
   EXECUTE IMMEDIATE'
  CREATE TABLE SITETOLINE
  (
    Sid smallint,
    LLidseq varchar2(128),
    RLidseq varchar2(128),
    CONSTRAINT FK_SID FOREIGN KEY (Sid) REFERENCES SITEINFO(Sid)
    ON DELETE CASCADE
  )';

  --վ����·��ͼ��վ��������·id����
  EXECUTE IMMEDIATE'
  CREATE OR REPLACE VIEW View_SiteLine(sname, llidseq, rlidseq) AS 
    SELECT SITEINFO.sname, SITETOLINE.llidseq, SITETOLINE.rlidseq
    FROM SITEINFO, SITETOLINE
    WHERE SITEINFO.sid = SITETOLINE.sid
  ';
  
  --��·վ����ͼ����·����վ��id����
  EXECUTE IMMEDIATE'
  CREATE OR REPLACE VIEW View_LineSite(lname, lsidseq, rsidseq) AS 
    SELECT LINEINFO.lname, LINETOSITE.lsidseq, LINETOSITE.rsidseq
    FROM LINEINFO, LINETOSITE
    WHERE LINEINFO.lid = LINETOSITE.lid
  ';
   
  --��һվ���վ��id, ��·id, ��һվid, ʱ�䣬����
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
  
  --��һվ����ͼ��վ��������·������һ��վ������ʱ�䣬����
  EXECUTE IMMEDIATE'
  CREATE OR REPLACE VIEW View_NextSite(StartSname, Lname,IsLeft, EndSname, RunTime, Distance) AS 
    SELECT s1.Sname, LINEINFO.lname, NEXTSITE.IsLeft, s2.Sname, NEXTSITE.RunTime, NEXTSITE.Distance
    FROM SITEINFO s1, LINEINFO, SITEINFO s2, NEXTSITE
    WHERE  NEXTSITE.StartSid = s1.Sid   
      AND NEXTSITE.Lid = LINeINFO.Lid
      AND NEXTSITE.ENDSid = s2.Sid 
    WITH READ ONLY
  ';
  -- ������ʱ��
  EXECUTE IMMEDIATE'
  CREATE  GLOBAL  TEMPORARY  TABLE  Temp_DIRECTACCESS  (  
                      sname Varchar(32),
                      lname Varchar(8),
                      runLeft NUMBER(38),
                      runTime  NUMBER(38),
                      distance NUMBER(38))On Commit Preserve Rows
  ';
  -- ����·����ʱ��
  EXECUTE IMMEDIATE'
  CREATE GLOBAL TEMPORARY TABLE TEMP_ROUTE (
    SNAME VARCHAR2(64)
   ) On Commit Preserve Rows';

  -- �����Զ�����
  EXECUTE IMMEDIATE'
    create or replace type varTableType as table of NUMBER
  ';
  
  -- ���������
  EXECUTE IMMEDIATE'
    create or replace PACKAGE PACKAGE AS 
    -- �Զ����¼����
    Type RecType Is Record
    (
      sname Varchar(32),
      lname Varchar(8),
      runLeft NUMBER(38),
      runTime  NUMBER(38),
      distance NUMBER(38)
    );
    -- �Զ��������
    Type TabType Is Table Of  RecType  Index By Binary_Integer;
  END PACKAGE;
  ';
  
  
  --=============================================================����
  -- �����ַ����ָ��
  EXECUTE IMMEDIATE'
    create or replace function str2numList
    (
      p_string in varchar2 
    ) RETURN varTableType AS
    -- �ַ���ת���������麯��
    v_str long default p_string || '','';
    v_n number;-- ����
    v_data varTableType := varTableType();  --�Զ�������
    BEGIN
      LOOP
        v_n := to_number(instr( v_str, '','' ));
        exit when (nvl(v_n,0) = 0);
        v_data.extend;
        SELECT to_number(ltrim(rtrim(substr(v_str,1,v_n-1)))) INTO��v_data( v_data.count )��
          FROM DUAL;
        --v_data( v_data.count ) := ltrim(rtrim(substr(v_str,1,v_n-1)));
        v_str := substr( v_str, v_n+1 );
      END LOOP;
    RETURN v_data;
  END;
  ';
  --=========================================================�洢����
  -- ����ɾ����·����һվ���洢����
   EXECUTE IMMEDIATE'
    create or replace PROCEDURE DELETE_NEXTSITE_LINE 
    (
      LINENAME IN VARCHAR2 
    ) AS 
    --ͨ����·��ɾ����ϵ
    lineid NUMBER;
    BEGIN
      --�ҵ�վ���Ӧ��id
      SELECT Lid INTO lineid 
        FROM LINEINFO 
        WHERE Lname = LINENAME;
    
      --ɾ����ʼվ��id����ֹվ��idΪsiteid��վ��
      DELETE FROM NEXTSITE
        WHERE Lid = lineid;
END DELETE_NEXTSITE_LINE;
';
  
  -- ����ɾ��վ�����һվ���
   EXECUTE IMMEDIATE'
    create or replace PROCEDURE DELETE_NEXTSITE_SITE 
    (
      SITENAME IN VARCHAR2 
    ) AS 
    --����վ������ɾ��NEXTSITE�������а�����վ�����Ĺ�ϵ

    siteid NUMBER; --վ������Ӧ��վ��id
    BEGIN
      --�ҵ�վ���Ӧ��id
      SELECT Sid INTO siteid 
        FROM SITEINFO 
        WHERE Sname = SITENAME;
        
      --ɾ����ʼվ��id����ֹվ��idΪsiteid��վ��
      DELETE FROM NEXTSITE
        WHERE (startsid = siteid OR��endsid = siteid);
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
    -- ������ʼվ�㣬��ֹվ�㣬��·�����Ƿ�����->�����··��
    -- ע�⣺��Ҫ�ض���ʱ��

    interMediary Varchar(64);-- �м�վ����
    insertStr Varchar(128);

    BEGIN
      interMediary := '''';
      insertStr := ''insert into Temp_Route values('''' || STARTSITE || '''')'';
      execute immediate insertStr; 
      intermediary := startSite;
  
      While intermediary != endSite 
        LOOP
          DBMS_OUTPUT.PUT_LINE(endSite);
          -- ����м�վ����
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
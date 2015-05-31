create or replace PROCEDURE BEDIRECTACCESS 
(
  ENDSITE IN VARCHAR2 
, sp_coesor out sys_refcursor
) AS 
  -- ͨ������վ�����õ����о�����վ����ֱ���վ����
  -- ����
  siteId NUMBER(16);-- ����վ��id
  leftStr VARCHAR2(256);-- ���ַ���
  rightStr VARCHAR2(256);-- ���ַ���
  left_numList varTableType;-- ��ߵ���·id����
  right_numList varTableType;-- �ұߵ���·id����
  left_site_numList varTableType;-- ����վ��id����
  right_site_numList varTableType;-- ����վ��id����
  visit_numList varTableType := varTableType();-- �ѷ�����·id

  isBegin NUMBER;-- �Ƿ�ʼվ
  rightLineId NUMBER(38);-- �ұ���·ID
  --lastSid Number(38);-- ��¼��һ��վ��id
  --last_runTime Number(38);-- ��¼��ʼվ�㵽��һ��վ���ʱ��
  --last_distance NUMBER(38);-- ��¼��ʼվ�㵽��һ��վ��ľ���
  MyTab package.TabType;-- �Զ����¼�����Ͷ���
  vN Number;-- �±�
  lineName Varchar(8);-- ��·��
  createStr Varchar(128);-- ������ʱ�����
  insertStr Varchar(128);-- �����������
  
BEGIN
  isBegin := 0;-- ��ʼ��Ϊ��
  vN := 1;
  --last_runTime := 0;
  --last_distance := 0;
  
  -- ���վ��id
  SELECT sid INTO siteId
    FROM SITEINFO
    WHERE SNAME = endsite;
    
  -- ��þ�����վ���������·
  SELECT LLIDSEQ, RLIDSEQ INTO leftStr, rightStr
    FROM SITETOLINE
    WHERE sid = siteId;
    
  -- ��þ���վ�����·id����
  left_numList := str2numList(leftStr);
  right_numList := str2numList(rightStr);
  
  -- ѭ������·id����
  for i in 1 .. left_numList.count 
    LOOP
      dbms_output.put_line(left_numList(i));
      -- �����·��
      SELECT Lname INTO lineName
        FROM LINEINFO
        WHERE Lid = left_numList(i);
        
      visit_numList.extend;
      visit_numList(visit_numList.count) := left_numList(i);-- ��¼�ѷ�����·
      
      -- �����·��������վ������
      SELECT LSIDSEQ, RSIDSEQ INTO leftStr, rightStr
        FROM LineToSite
        WHERE lid = left_numList(i);
      -- �ָ��ַ���
      left_site_numList := str2numList(leftStr); 
      right_site_numList := str2numList(rightStr);
      
      -- ������ߵ�վ������
      for j in 1 .. left_site_numList.count 
        LOOP
          --dbms_output.put_line(left_site_numList(j));-- ������� 
          -- �ӱ���·���ҵ���ʼվ��
          IF left_site_numList(j) = siteId THEN 
            isBegin := 1;-- ��ʼͳ��
          END IF;
          IF isBegin = 0 THEN
            -- ��ÿ�ֱ��վ����
            SELECT Sname INTO MyTab(vN).sname
              FROM SITEINFO
              WHERE sid = left_site_numList(j);
            -- �����·��
            MyTab(vN).lname := lineName;
            -- ��������б�־
            MyTab(vN).runleft := 1;
            -- �������ʱ�������
            /*SELECT RUNTIME, DISTANCE INTO MyTab(vN).runTime, MyTab(vN).distance
              FROM NEXTSITE
              WHERE STARTSID = lastSid
                AND Lid = left_numList(i)
                AND ISLEFT = MyTab(vN).runleft
                AND endsid = left_site_numList(j);
          
            MyTab(vN).runTime := MyTab(vN).runTime + last_runTime;-- ����ʼվ�㵽��վ���ʱ��
            MyTab(vN).distance := MyTab(vN).distance + last_distance;-- ����ʼվ�㵽��վ��ľ���          
            -- ��¼��ʼվ�㵽��һ��վ�����Ϣ
            last_runTime := MyTab(vN).runTime;
            last_distance := MyTab(vN).distance;	*/	
            vN := vN + 1;
          END IF; 
          
          --lastSid := left_site_numList(j);    
        END LOOP;
        isBegin := 0;
        --last_runTime := 0;
        --last_distance := 0; 
      ------------------------------  
      -- �����ұ�վ������
      for j in 1 .. right_site_numList.count 
        LOOP
          --dbms_output.put_line(right_site_numList(j));
          -- �ӱ���·���ҵ���ʼվ��
          IF right_site_numList(j) = siteId THEN 
            isBegin := 1;-- ��ʼͳ��
          END IF;
          IF isBegin = 0 THEN
            -- ��ÿ�ֱ��վ����
            SELECT Sname INTO MyTab(vN).sname
              FROM SITEINFO
              WHERE sid = right_site_numList(j);
            -- �����·��
            MyTab(vN).lname := lineName;
            -- ��������б�־
            MyTab(vN).runleft := 0;
            -- dbms_output.put_line(lastSid || ',' || left_numList(i) || ',' || MyTab(vN).runleft || ',' || right_site_numList(j));
            -- �������ʱ�������
            /*SELECT RUNTIME, DISTANCE INTO MyTab(vN).runTime, MyTab(vN).distance
              FROM NEXTSITE
              WHERE STARTSID = lastSid
                AND Lid = left_numList(i)
                AND ISLEFT = MyTab(vN).runleft
                AND endsid = right_site_numList(j);
				
            MyTab(vN).runTime := MyTab(vN).runTime + last_runTime;-- ����ʼվ�㵽��վ���ʱ��
            MyTab(vN).distance := MyTab(vN).distance + last_distance;-- ����ʼվ�㵽��վ��ľ���          
            -- ��¼��ʼվ�㵽��һ��վ�����Ϣ
            last_runTime := MyTab(vN).runTime;
            last_distance := MyTab(vN).distance;		*/
            vN := vN + 1;
          END IF; 
         
          --lastSid := right_site_numList(j);
        END LOOP;
        isBegin := 0;
        --last_runTime := 0;
        --last_distance := 0; 
    END LOOP;
   
  --------------------------------------------------------  
  dbms_output.put_line('rightLineId');
  -- ѭ���ұ���·
  for i in 1 .. right_numList.count 
    LOOP
      --dbms_output.put_line(right_numList(i));
      rightLineId := right_numList(i);
      
      -- �ж���·�Ƿ��Ѿ�������
      for k in 1 .. visit_numList.count
        LOOP
          IF right_numList(i) = visit_numList(k) THEN
            rightLineId := 0;
          END IF;
        END LOOP;
        
      -- ����·idδ������(��֤ÿ����·ֻ���һ��)
      IF rightLineId != 0 THEN
        dbms_output.put_line(rightLineId);
        SELECT LSIDSEQ, RSIDSEQ INTO leftStr, rightStr
          FROM LineToSite
          WHERE lid = rightLineId;
        -- �ָ��ַ���
        left_site_numList := str2numList(leftStr); 
        right_site_numList := str2numList(rightStr);
        
        -- ������ߵ�վ������
        for j in 1 .. left_site_numList.count 
          LOOP
            --dbms_output.put_line(left_site_numList(j));
             -- �ӱ���·���ҵ���ʼվ��
            IF left_site_numList(j) = siteId THEN 
              isBegin := 1;-- ��ʼͳ��
            END IF;
            IF isBegin = 0 THEN
              -- ��ÿ�ֱ��վ����
              SELECT Sname INTO MyTab(vN).sname
                FROM SITEINFO
                WHERE sid = left_site_numList(j);
              -- �����·��
              MyTab(vN).lname := lineName;
              -- ��������б�־
              MyTab(vN).runleft := 1;
              -- dbms_output.put_line(lastSid || ',' || left_numList(i) || ',' || MyTab(vN).runleft || ',' || left_site_numList(j));
              -- �������ʱ�������
              /*SELECT RUNTIME, DISTANCE INTO MyTab(vN).runTime, MyTab(vN).distance
                FROM NEXTSITE
                WHERE STARTSID = lastSid
                  AND Lid = rightLineId
                  AND ISLEFT = MyTab(vN).runleft
                  AND endsid = left_site_numList(j);
				
              MyTab(vN).runTime := MyTab(vN).runTime + last_runTime;-- ����ʼվ�㵽��վ���ʱ��
              MyTab(vN).distance := MyTab(vN).distance + last_distance;-- ����ʼվ�㵽��վ��ľ���          
              -- ��¼��ʼվ�㵽��һ��վ�����Ϣ
              last_runTime := MyTab(vN).runTime;
              last_distance := MyTab(vN).distance;	*/	
              vN := vN + 1;
            END IF; 
           
            --lastSid := left_site_numList(j);	
          END LOOP;
          isBegin := 0;
          --last_runTime := 0;
          --last_distance := 0; 
		----------------------------  
        -- �����ұ�վ������
        for j in 1 .. right_site_numList.count 
          LOOP
            --dbms_output.put_line(right_site_numList(j));
            -- �ӱ���·���ҵ���ʼվ��
            IF right_site_numList(j) = siteId THEN 
            isBegin := 1;-- ��ʼͳ��
            END IF;
            IF isBegin = 0 THEN
              -- ��ÿ�ֱ��վ����
              SELECT Sname INTO MyTab(vN).sname
                FROM SITEINFO
                WHERE sid = right_site_numList(j);
              -- �����·��
              MyTab(vN).lname := lineName;
              -- ��������б�־
              MyTab(vN).runleft := 0;
              -- �������ʱ�������
              -- dbms_output.put_line(lastSid || ',' || left_numList(i) || ',' || MyTab(vN).runleft || ',' || right_site_numList(j));
              /*SELECT RUNTIME, DISTANCE INTO MyTab(vN).runTime, MyTab(vN).distance
                FROM NEXTSITE
                WHERE STARTSID = lastSid
                  AND Lid = rightLineId
                  AND ISLEFT = MyTab(vN).runleft
                  AND endsid = right_site_numList(j);
				
              MyTab(vN).runTime := MyTab(vN).runTime + last_runTime;-- ����ʼվ�㵽��վ���ʱ��
              MyTab(vN).distance := MyTab(vN).distance + last_distance;-- ����ʼվ�㵽��վ��ľ���          
              -- ��¼��ʼվ�㵽��һ��վ�����Ϣ
              last_runTime := MyTab(vN).runTime;
              last_distance := MyTab(vN).distance;	*/	
              vN := vN + 1;
            END IF; 
          
          --lastSid := right_site_numList(j);
        END LOOP;
        isBegin := 0;
        --last_runTime := 0;
        --last_distance := 0; 
      END IF; 
    END LOOP;
    DBMS_OUTPUT.PUT_LINE('okok');
    --����
    vN := MyTab.First;
    For varR In vN..MyTab.count
      Loop
        --DBMS_OUTPUT.PUT_LINE(vN ||'   '||MyTab(vN).sname||'   '||MyTab(vN).lname||'   '||MyTab(vN).runleft);
        insertStr := 'insert into Temp_DIRECTACCESS values(''' 
              || MyTab(vN).sname || ''','''
              || MyTab(vN).lname || ''','
              || MyTab(vN).runleft || ',0, 0)';

        execute immediate insertStr; ----ʹ�ö�̬SQL�����ִ��
        vN := MyTab.Next(vN);
      End Loop;
    Open sp_coesor FOR 
      SELECT * FROM TEMP_DIRECTACCESS;
    -- �ֶ������ʱ������
    execute immediate 'delete from TEMP_DIRECTACCESS';
END BEDIRECTACCESS;
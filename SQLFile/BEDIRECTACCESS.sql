create or replace PROCEDURE BEDIRECTACCESS 
(
  ENDSITE IN VARCHAR2 
, sp_coesor out sys_refcursor
) AS 
  -- 通过输入站点名得到所有经过该站点能直达的站点名
  -- 变量
  siteId NUMBER(16);-- 输入站点id
  leftStr VARCHAR2(256);-- 左字符串
  rightStr VARCHAR2(256);-- 右字符串
  left_numList varTableType;-- 左边的线路id数组
  right_numList varTableType;-- 右边的线路id数组
  left_site_numList varTableType;-- 左行站点id数组
  right_site_numList varTableType;-- 右行站点id数组
  visit_numList varTableType := varTableType();-- 已访问线路id

  isBegin NUMBER;-- 是否开始站
  rightLineId NUMBER(38);-- 右边线路ID
  --lastSid Number(38);-- 记录上一个站点id
  --last_runTime Number(38);-- 记录起始站点到上一个站点的时间
  --last_distance NUMBER(38);-- 记录起始站点到上一个站点的距离
  MyTab package.TabType;-- 自定义记录表类型对象
  vN Number;-- 下标
  lineName Varchar(8);-- 线路名
  createStr Varchar(128);-- 创建临时表语句
  insertStr Varchar(128);-- 插入数据语句
  
BEGIN
  isBegin := 0;-- 初始化为零
  vN := 1;
  --last_runTime := 0;
  --last_distance := 0;
  
  -- 获得站点id
  SELECT sid INTO siteId
    FROM SITEINFO
    WHERE SNAME = endsite;
    
  -- 获得经过此站点的所有线路
  SELECT LLIDSEQ, RLIDSEQ INTO leftStr, rightStr
    FROM SITETOLINE
    WHERE sid = siteId;
    
  -- 获得经过站点的线路id数组
  left_numList := str2numList(leftStr);
  right_numList := str2numList(rightStr);
  
  -- 循环左线路id数组
  for i in 1 .. left_numList.count 
    LOOP
      dbms_output.put_line(left_numList(i));
      -- 获得线路名
      SELECT Lname INTO lineName
        FROM LINEINFO
        WHERE Lid = left_numList(i);
        
      visit_numList.extend;
      visit_numList(visit_numList.count) := left_numList(i);-- 记录已访问线路
      
      -- 获得线路的左、右行站点序列
      SELECT LSIDSEQ, RSIDSEQ INTO leftStr, rightStr
        FROM LineToSite
        WHERE lid = left_numList(i);
      -- 分割字符串
      left_site_numList := str2numList(leftStr); 
      right_site_numList := str2numList(rightStr);
      
      -- 分析左边的站点序列
      for j in 1 .. left_site_numList.count 
        LOOP
          --dbms_output.put_line(left_site_numList(j));-- 输出看看 
          -- 从本线路中找到起始站点
          IF left_site_numList(j) = siteId THEN 
            isBegin := 1;-- 开始统计
          END IF;
          IF isBegin = 0 THEN
            -- 获得可直达站点名
            SELECT Sname INTO MyTab(vN).sname
              FROM SITEINFO
              WHERE sid = left_site_numList(j);
            -- 获得线路名
            MyTab(vN).lname := lineName;
            -- 获得左右行标志
            MyTab(vN).runleft := 1;
            -- 获得运行时间与距离
            /*SELECT RUNTIME, DISTANCE INTO MyTab(vN).runTime, MyTab(vN).distance
              FROM NEXTSITE
              WHERE STARTSID = lastSid
                AND Lid = left_numList(i)
                AND ISLEFT = MyTab(vN).runleft
                AND endsid = left_site_numList(j);
          
            MyTab(vN).runTime := MyTab(vN).runTime + last_runTime;-- 从起始站点到本站点的时间
            MyTab(vN).distance := MyTab(vN).distance + last_distance;-- 从起始站点到本站点的距离          
            -- 记录起始站点到上一个站点的信息
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
      -- 分析右边站点序列
      for j in 1 .. right_site_numList.count 
        LOOP
          --dbms_output.put_line(right_site_numList(j));
          -- 从本线路中找到起始站点
          IF right_site_numList(j) = siteId THEN 
            isBegin := 1;-- 开始统计
          END IF;
          IF isBegin = 0 THEN
            -- 获得可直达站点名
            SELECT Sname INTO MyTab(vN).sname
              FROM SITEINFO
              WHERE sid = right_site_numList(j);
            -- 获得线路名
            MyTab(vN).lname := lineName;
            -- 获得左右行标志
            MyTab(vN).runleft := 0;
            -- dbms_output.put_line(lastSid || ',' || left_numList(i) || ',' || MyTab(vN).runleft || ',' || right_site_numList(j));
            -- 获得运行时间与距离
            /*SELECT RUNTIME, DISTANCE INTO MyTab(vN).runTime, MyTab(vN).distance
              FROM NEXTSITE
              WHERE STARTSID = lastSid
                AND Lid = left_numList(i)
                AND ISLEFT = MyTab(vN).runleft
                AND endsid = right_site_numList(j);
				
            MyTab(vN).runTime := MyTab(vN).runTime + last_runTime;-- 从起始站点到本站点的时间
            MyTab(vN).distance := MyTab(vN).distance + last_distance;-- 从起始站点到本站点的距离          
            -- 记录起始站点到上一个站点的信息
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
  -- 循环右边线路
  for i in 1 .. right_numList.count 
    LOOP
      --dbms_output.put_line(right_numList(i));
      rightLineId := right_numList(i);
      
      -- 判断线路是否已经被访问
      for k in 1 .. visit_numList.count
        LOOP
          IF right_numList(i) = visit_numList(k) THEN
            rightLineId := 0;
          END IF;
        END LOOP;
        
      -- 此线路id未被访问(保证每条线路只检查一次)
      IF rightLineId != 0 THEN
        dbms_output.put_line(rightLineId);
        SELECT LSIDSEQ, RSIDSEQ INTO leftStr, rightStr
          FROM LineToSite
          WHERE lid = rightLineId;
        -- 分割字符串
        left_site_numList := str2numList(leftStr); 
        right_site_numList := str2numList(rightStr);
        
        -- 分析左边的站点序列
        for j in 1 .. left_site_numList.count 
          LOOP
            --dbms_output.put_line(left_site_numList(j));
             -- 从本线路中找到起始站点
            IF left_site_numList(j) = siteId THEN 
              isBegin := 1;-- 开始统计
            END IF;
            IF isBegin = 0 THEN
              -- 获得可直达站点名
              SELECT Sname INTO MyTab(vN).sname
                FROM SITEINFO
                WHERE sid = left_site_numList(j);
              -- 获得线路名
              MyTab(vN).lname := lineName;
              -- 获得左右行标志
              MyTab(vN).runleft := 1;
              -- dbms_output.put_line(lastSid || ',' || left_numList(i) || ',' || MyTab(vN).runleft || ',' || left_site_numList(j));
              -- 获得运行时间与距离
              /*SELECT RUNTIME, DISTANCE INTO MyTab(vN).runTime, MyTab(vN).distance
                FROM NEXTSITE
                WHERE STARTSID = lastSid
                  AND Lid = rightLineId
                  AND ISLEFT = MyTab(vN).runleft
                  AND endsid = left_site_numList(j);
				
              MyTab(vN).runTime := MyTab(vN).runTime + last_runTime;-- 从起始站点到本站点的时间
              MyTab(vN).distance := MyTab(vN).distance + last_distance;-- 从起始站点到本站点的距离          
              -- 记录起始站点到上一个站点的信息
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
        -- 分析右边站点序列
        for j in 1 .. right_site_numList.count 
          LOOP
            --dbms_output.put_line(right_site_numList(j));
            -- 从本线路中找到起始站点
            IF right_site_numList(j) = siteId THEN 
            isBegin := 1;-- 开始统计
            END IF;
            IF isBegin = 0 THEN
              -- 获得可直达站点名
              SELECT Sname INTO MyTab(vN).sname
                FROM SITEINFO
                WHERE sid = right_site_numList(j);
              -- 获得线路名
              MyTab(vN).lname := lineName;
              -- 获得左右行标志
              MyTab(vN).runleft := 0;
              -- 获得运行时间与距离
              -- dbms_output.put_line(lastSid || ',' || left_numList(i) || ',' || MyTab(vN).runleft || ',' || right_site_numList(j));
              /*SELECT RUNTIME, DISTANCE INTO MyTab(vN).runTime, MyTab(vN).distance
                FROM NEXTSITE
                WHERE STARTSID = lastSid
                  AND Lid = rightLineId
                  AND ISLEFT = MyTab(vN).runleft
                  AND endsid = right_site_numList(j);
				
              MyTab(vN).runTime := MyTab(vN).runTime + last_runTime;-- 从起始站点到本站点的时间
              MyTab(vN).distance := MyTab(vN).distance + last_distance;-- 从起始站点到本站点的距离          
              -- 记录起始站点到上一个站点的信息
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
    --访问
    vN := MyTab.First;
    For varR In vN..MyTab.count
      Loop
        --DBMS_OUTPUT.PUT_LINE(vN ||'   '||MyTab(vN).sname||'   '||MyTab(vN).lname||'   '||MyTab(vN).runleft);
        insertStr := 'insert into Temp_DIRECTACCESS values(''' 
              || MyTab(vN).sname || ''','''
              || MyTab(vN).lname || ''','
              || MyTab(vN).runleft || ',0, 0)';

        execute immediate insertStr; ----使用动态SQL语句来执行
        vN := MyTab.Next(vN);
      End Loop;
    Open sp_coesor FOR 
      SELECT * FROM TEMP_DIRECTACCESS;
    -- 手动清空临时表内容
    execute immediate 'delete from TEMP_DIRECTACCESS';
END BEDIRECTACCESS;
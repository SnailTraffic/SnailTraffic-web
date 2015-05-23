<%@ page language="java" contentType="text/html; UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="device-width, initial-scale=1.0, user-scalable=no">
<meta name="keywords" content="traffic, Wuhan, bus" >
<meta name="description" content="Snailtraffic - Hang out by Wuhan Traffic with Convenience">
<script type="text/javascript" src="js/jquery/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="js/index.js"></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=h4yPyHGeReyzF66u1yIL22fU" ></script>
<link rel="stylesheet" href="css/index.css" />
<link rel="stylesheet" href="css/formstyle.css" />
<script type="text/javascript" src="js/formstyle.js"></script>
<link rel="stylesheet" href="css/map.css" />
<script type="text/javascript" src="js/map.js"></script>

<title>蜗牛公交</title>
</head>
<body>
<div class="mainbody">
	<div class="logo __temp"></div>
	<div class="top-list">
		<div><img class="tab-icon" src="i/icons/bus-exchange-white.png" width="32" >公交换乘<input type="hidden" value="0" /></div>
		<div>线路查询<input type="hidden" value="1" /></div>
		<div>站点查询<input type="hidden" value="2" /></div>
		<div>一键回家<input type="hidden" value="3" /></div>
	</div>
	<div class="_content-wrapper">
		<div class="page" id="_page1">
			<form class="ben-form" name="bus-exchange" action="search.jsp" method="post">
				<input type="hidden" name="queryType" value="0" />
				<div>
				<table>
				<tr>
				<td><label>出发站点</label></td>
				<td>
				<input class="ben-text" type="text" name="start" value="" maxlength="100" autocomplete="off" />
				<div class="ben-texthint">
				<ul><li class="ben-texthint-spec"><a>关闭</a></li></ul>
				</div>
				</td>
				</tr>
				<tr>
				<td><label>目的站点</label></td>
				<td>
				<input class="ben-text" type="text" name="dest" value="" maxlength="100" autocomplete="off" />
				<div class="ben-texthint">
				<ul></ul>
				</div>
				<td>
				</tr>
				<tr><td colspan="2"><input class="ben-button" type="submit" name="bus-exchange-submit" value="走！" /></td></tr>
				</table>
				</div>
			</form>
		</div>
		<div class="page" id="_page2">
			<form class="ben-form" name="bus-line" action="search.jsp" method="post">
				<input type="hidden" name="queryType" value="1" />
				<label>线路</label>
				<input class="ben-text" type="text" name="line" value="" maxlength="40" autocomplete="off" />
				<input class="ben-button" type="submit" name="bus-line-submit" value="走！" />
			</form>
		</div>
		<div class="page" id="_page3">
			<form class="ben-form" name="bus-station" action="search.jsp" method="post">
				<input type="hidden" name="queryType" value="2" />
				<label>站点名称</label>
				<input class="ben-text" type="text" name="station" value="" maxlength="100" autocomplete="off" />
				<input class="ben-button" type="submit" name="bus-station-submit" value="走！" />
			</form>
		</div>
		<div class="page" id="_page4">
			<form class="ben-form" name="bus-oneclickhome" action="search.jsp" method="post">
				<input type="hidden" name="queryType" value="3" />
				<table>
				<tr>
				<td style="text-align: left;"><label>您的当前位置</label></td>
				<td><input class="ben-text" type="text" name="currentLoc" value="" maxlength="100" autocomplete="off" /></td>
				</tr>
				<tr>
				<td style="text-align: left;"><label>家的位置</label></td>
				<td><input class="ben-text" type="text" name="homeLoc" value="" maxlength="100" autocomplete="off" /></td>
				<tr>
				<td colspan="2"><input class="ben-button" type="submit" name="bus-go-home" value="走！" /></td>
				</tr>
				</table>
			</form>
		</div>
	</div>
</div>
<div class="map_wrapper __temp">
	<div id="_sidebar">
		<div id="_sidebar-content-wrapper">
			<div id="_sidebar-collapse-button">&times;</div>
			<div id="_sidebar-content">
				<span id="_sidebar-title">[TITLE]</span>
				<ul id="_sidebar-list">
				<li>
					<table><tr>
					<td><div class="item-index _vwrap"><div class="_vcontent">24</div></div></td>
					<td width="100%">
						<div class="item-title">643</div>
						<div class="item-text">A->B->C->D->E</div>
					</td>
					</tr>
					</table>
				</li>
				</ul>
			</div>
		</div>
		<div id="_sidebar-expand-button">&raquo;</div>
	</div>
	<div id="_map"></div>
	<div id="_nomap">无法加载地图<p>请刷新页面</div>
</div>
<div class="footer">
	<div style="border-top: solid 2px #1f6fff; padding: 8px 16px;">
		<span>蜗牛公交</span><b>|</b><span>页面设计 - 李诺格</span><b>|</b><span>算法设计 - 钟佳杰</span><b>|</b><span>数据库设计 - 刘国维</span>
	</div>
</div>
</body>
</html>
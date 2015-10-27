
var lz_poll_server = "https://linuxhostsupport.com/chat/";
var lz_poll_url = "https://linuxhostsupport.com/chat/server.php";
var lz_poll_website = "";
var lz_poll_frequency = 60;
var lz_poll_file_chat = "chat.php";
var lz_window_width = "590";
var lz_window_height = "760";

var lz_area_code = "";
var lz_user_name = "";
var lz_user_email = "";
var lz_user_company = "";
var lz_user_question = "";
var lz_user_phone = "";
var lz_user_customs = new Array('','','','','','','','','','');
var lz_user_language = "";
var lz_user_header = "";
var lz_user_website = "";

var lz_getp_track = "fbpos=MjI_&fbml=MA__&fbmt=MA__&fbmr=MjU_&fbmb=MA__&fbw=MjQz&fbh=MjY_&fboo=MQ__";
var lz_alert_html = 'PGRpdiBpZD0ibHpfY2hhdF9hbGVydF9ib3giPg0KICAgIDxkaXYgaWQ9Imx6X2NoYXRfYWxlcnRfaGVhZGVyIj4mbmJzcDtMaXZlIENoYXQgU3VwcG9ydDwvZGl2Pg0KICAgIDxkaXYgaWQ9Imx6X2NoYXRfYWxlcnRfYm94X3RleHRfZnJhbWUiPg0KICAgICAgICA8c3BhbiBpZD0ibHpfY2hhdF9hbGVydF9ib3hfdGV4dCI+PC9zcGFuPg0KICAgIDwvZGl2Pg0KICAgIDxkaXYgc3R5bGU9InRleHQtYWxpZ246cmlnaHQ7cGFkZGluZzo2cHggOHB4IDZweCAwOyI+DQogICAgICAgIDxpbnB1dCB0eXBlPSJidXR0b24iIGNsYXNzPSJsel9mb3JtX2J1dHRvbiIgaWQ9Imx6X2NoYXRfYWxlcnRfYnV0dG9uIiB2YWx1ZT0iT0siPg0KICAgIDwvZGl2Pg0KPC9kaXY+DQo8ZGl2IGlkPSJsel9jaGF0X2FsZXJ0X2JnIj48L2Rpdj4NCg0KDQoNCg==';
var lz_is_ie = false;

var lz_overlay_chat_available = false;
var lz_overlays_possible = true;
var lz_direct_login = false;
var lz_geo_error_span = 30;
var lz_geo_data_count = 6;
var lz_geo_resolution = null;
var lz_geo_resolution;
var lz_geo_resolution_needed = false;
var lz_user_id = "5cc3ecbdcf";
var lz_browser_id = "2c483951fa";
var lz_server_id = "54022";
var lz_geo_url = "https://ssl.livezilla.net/geo/resolute/?aid=&sid=OTQ4YTc1ZTI=&dbp=1";
var lz_mip = "189.5.xxx.xxx";
var lz_oak = '';
var lz_is_tablet = false;

var chars = new Array('3','4','9','b','2','0','1','4','3','6','5','9','4','c','c','2','8','d','b','4','c','1','4','a','4','b','d','8','5','1','e','7','a','d','2','8','f','d','3','2',0);
var order = new Array(27,23,35,31,4,30,12,32,13,9,2,6,39,25,33,19,26,28,10,38,7,37,17,5,15,0,16,24,36,14,29,18,11,8,21,1,3,34,22,20,0);
while(lz_oak.length < (chars.length-1))for(var f in order)if(order[f] == lz_oak.length)lz_oak += chars[f];


var lz_resources = new Array(false,false,false,false,false,false);
LazyLoad=function(x,h){function r(b,a){var c=h.createElement(b),d;for(d in a)a.hasOwnProperty(d)&&c.setAttribute(d,a[d]);return c}function k(b){var a=i[b],c,d;if(a){c=a.callback;d=a.urls;d.shift();l=0;if(!d.length){c&&c.call(a.context,a.obj);i[b]=null;j[b].length&&m(b)}}}function w(){if(!e){var b=navigator.userAgent;e={async:h.createElement("script").async===true};(e.webkit=/AppleWebKit\//.test(b))||(e.ie=/MSIE/.test(b))||(e.opera=/Opera/.test(b))||(e.gecko=/Gecko\//.test(b))||(e.unknown=true)}}function m(b,
a,c,d,s){var n=function(){k(b)},o=b==="css",f,g,p;w();if(a){a=typeof a==="string"?[a]:a.concat();if(o||e.async||e.gecko||e.opera)j[b].push({urls:a,callback:c,obj:d,context:s});else{f=0;for(g=a.length;f<g;++f)j[b].push({urls:[a[f]],callback:f===g-1?c:null,obj:d,context:s})}}if(!(i[b]||!(p=i[b]=j[b].shift()))){q||(q=h.head||h.getElementsByTagName("head")[0]);a=p.urls;f=0;for(g=a.length;f<g;++f){c=a[f];if(o)c=r("link",{charset:"utf-8","class":"lazyload",href:c,rel:"stylesheet",type:"text/css"});else{c=
r("script",{charset:"utf-8","class":"lazyload",src:c});c.async=false}if(e.ie)c.onreadystatechange=function(){var t=this.readyState;if(t==="loaded"||t==="complete"){this.onreadystatechange=null;n()}};else if(o&&(e.gecko||e.webkit))if(e.webkit){p.urls[f]=c.href;u()}else setTimeout(n,50*g);else c.onload=c.onerror=n;q.appendChild(c)}}}function u(){var b=i.css,a;if(b){for(a=v.length;a&&--a;)if(v[a].href===b.urls[0]){k("css");break}l+=1;if(b)l<200?setTimeout(u,50):k("css")}}var e,q,i={},l=0,j={css:[],js:[]},
v=h.styleSheets;return{css:function(b,a,c,d){m("css",b,a,c,d)},js:function(b,a,c,d){m("js",b,a,c,d)}}}(this,this.document);

LazyLoad.js([lz_poll_server + "templates/jscript/jsbox.js",lz_poll_server + "templates/jscript/jsglobal.js",lz_poll_server + "templates/jscript/jstrack.js"], function () {lz_resources[0]=true;lz_resources[1]=true;lz_resources[2]=true;});

if(lz_overlay_chat_available)
{
	LazyLoad.css(lz_poll_server + "templates/overlays/chat/style.css", function (arg) {}, '');
	LazyLoad.js(lz_poll_server + "templates/overlays/chat/jscript/jsextern.js", function () {lz_resources[4]=true;});
}

LazyLoad.css(lz_poll_server + "templates/style.css", function (arg) {}, '');

lz_tracking_start_system();
function lz_tracking_start_system()
{
	if(!lz_resources[0] || !lz_resources[1] || !lz_resources[2] || (lz_overlay_chat_available && (!lz_resources[4])))
	{
		setTimeout(lz_tracking_start_system, 50);
		return;
	}

	lz_geo_resolution = new lz_geo_resolver();
	window.onerror=lz_global_handle_exception;
	
	if(location.search.indexOf("lzcobrowse") != -1)
		return;
		
	lz_session = new lz_jssess();
	lz_session.Load();
	
	try
	{
		if(window.opener != null && typeof(window.opener.lz_get_session) != 'undefined')
		{
			lz_session.UserId = window.opener.lz_get_session().UserId;
			lz_session.GeoResolved = window.opener.lz_get_session().GeoResolved;
		}
	}
	catch(ex)
	{
		// ACCESS DENIED
	}
	
	lz_session.Save();
	if(!lz_tracking_geo_resolute())
		lz_tracking_poll_server();
}

window._pa = window._pa || {};
_pa.segments = [{"name":"All visitors","id":957777,"regex":".*"},{"name":"users in one year","id":957779,"regex":".*"}];
_pa.conversions = [{"name":"order","id":"186515","regex":".*cart.*step=5"}];
_pa.conversionEvents = [];
_pa.segmentEvents = [];
_pa.rtbId = '15604';
_pa.siteId = '521a7a15dd179b238d000055';
_pa.crossDevice = true;
!function(){function e(e,a,t){if(null==t||isNaN(t))var n=_pa.pixelHost+"seg?t=2&add="+e;else var n=_pa.pixelHost+"seg?t=2&add="+e+":"+t;_pa.createImageTag("segments",e,n,a)}function a(e,a){var t=_pa.paRtbHost+"seg/?add="+e;_pa.productId&&(t+=":"+encodeURIComponent(_pa.productId)),_pa.crossDevice&&(t+="&cd=1"),_pa.obscureIP&&(t+="&obscure_ip=1"),d?_pa.createImageTag("paRtbSegments",e,t,a):_.push({id:e,name:a})}function t(){if(d=!0,0!==_.length){for(var e=[],a=[],t=0;t<_.length;t++){var n=_[t],r=n.id,p=n.name;_pa.productId&&(r+=":"+encodeURIComponent(_pa.productId)),e.push(r),a.push(p)}var r=e.join(","),p=a.join(","),o=_pa.paRtbHost+"seg/?add="+r;_pa.crossDevice&&(o+="&cd=1"),_pa.obscureIP&&(o+="&obscure_ip=1"),_pa.createImageTag("paRtbSegments",r,o,p)}}function n(e,a,t){a=a||_pa.orderId,t=t||_pa.revenue;var n=e.id,p=e.name,o=_pa.rtbId;if(r(n,p,a,t,o),e.cofires)for(var i=0;i<e.cofires.length;i++){var c=e.cofires[i];r(c.appnexus_id,c.name,a,t,c.rtb_id)}}function r(e,a,t,n,r){var p="";t&&""!==t&&(t=t.toString().replace(/@.*/,"@"),p+="&order_id="+encodeURIComponent(t)),n&&""!==n&&(p+="&value="+encodeURIComponent(n)),p+="&other="+function(){for(var e="",a="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789",t=0;16>t;t++){var n=Math.floor(Math.random()*a.length);e+=a.charAt(n)}return e}();var o=_pa.pixelHost+"px?t=2&id="+e+p,i=_pa.paRtbHost+"px/?id="+e+p;r&&(i+="&a_id="+r),_pa.obscureIP&&(i+="&obscure_ip=1"),_pa.createImageTag("conversions",e,o,a),_pa.createImageTag("paRtbConversions",e,i,a)}function p(e){for(var a=e.shift(),t=a.split("."),n=_pa,r=0;r<t.length;r++)n=n[t[r]];var p=n.apply(_pa,e);return o(a,e),p}function o(e,a){var t=_pa.callbacks[e];if("undefined"!=typeof t)for(var n=0;n<t.length;n++)t[n].apply(_pa,a)}function i(){for(var e,a=Array.prototype.slice.call(arguments,0),t=a.shift(),n=t.split("."),r=_pa,p=0;p<n.length;p++)r=r[n[p]],e=n[p];r.apply(_pa,a),o(e,a)}function c(){var e=window.navigator.userAgent;(/MSIE 7/.test(e)||/(iPod|iPhone|iPad)/.test(e)&&/AppleWebKit/.test(e))&&(_pa.skip=!0)}function s(){var e=document.createElement("script");e.type="text/javascript",e.async=!0,e.src=("https:"==document.location.protocol?"https:":"http:")+"//pixel.prfct.co/tagjs",_pa.head.appendChild(e)}_pa.head=document.getElementsByTagName("head")[0]||document.getElementsByTagName("body")[0],s();var d=!1,_=[];_pa.ready={looper:!1,onload:!1};var f=["conversions","paRtbConversions"];_pa.init=function(){_pa.fired={segments:[],conversions:[]},_pa.url=document.location.href,_pa.pixelHost=("https:"===document.location.protocol?"https://secure":"http://ib")+".adnxs.com/",_pa.paRtbHost=("https:"===document.location.protocol?"https://":"http://")+"pixel.prfct.co/",_pa.callbacks={},c(),_pa.detect(),_pa.initQ(),t()},_pa.addFired=function(e,a){"undefined"==typeof _pa.fired[e]&&(_pa.fired[e]=[]),_pa.fired[e].push(a)},_pa.detect=function(){for(var e=0;e<_pa.segments.length;e++){var a=_pa.segments[e];_pa.url.match(new RegExp(a.regex,"i"))&&i("fireSegment",a)}for(var e=0;e<_pa.conversions.length;e++){var t=_pa.conversions[e];_pa.url.match(new RegExp(t.regex,"i"))&&n(t)}},_pa.track=function(e,a){a="undefined"!=typeof a?a:{};var t=_pa.trackSegments(e,a),n=_pa.trackConversions(e,a);return t||n},_pa.trackSegments=function(e,a){for(var t=!1,n=0;n<_pa.segmentEvents.length;n++){var r=_pa.segmentEvents[n];r.name===e&&(t=!0,i("fireSegment",r,a.segment_value))}return t},_pa.trackConversions=function(e,a){for(var t=!1,r=0;r<_pa.conversionEvents.length;r++){var p=_pa.conversionEvents[r];p.name===e&&(t=!0,n(p,a.orderId,a.revenue))}return t},_pa.trackProduct=function(e){_pa.productId=e;for(var t=_pa.fired.segments,n={},r=0;r<t.length;r++){var p=t[r],o=p.id;n[o]=!0}for(var i in n)a(i,"product refire")},_pa.fireLoadEvents=function(){if("undefined"!=typeof _pa.onLoadEvent)if("function"==typeof _pa.onLoadEvent)_pa.onLoadEvent();else if("string"==typeof _pa.onLoadEvent)for(var e=_pa.onLoadEvent.split(","),a=0;a<e.length;a++){var t=e[a];_pa.track(t)}},_pa.createImageTag=function(e,a,t,n){if(!_pa.skip){for(var r=!1,p=0;p<f.length;p++)e===f[p]&&(r=!0);_pa.pixelPlacer.place(t,r),_pa.addFired(e,{id:a,name:n})}},_pa.looperReady=function(){_pa.ready.looper=!0,_pa.fireWhenReady()},_pa.fireWhenReady=function(){_pa.ready.looper&&_pa.ready.onload&&(_pa.fireLoadEvents(),_pa.pixelPlacer.activate())},_pa.fireSegment=function(t,n){e(t.id,t.name,n),a(t.id,t.name)},_pa.initQ=function(){if("undefined"!=typeof window._pq)for(var e=0;e<_pq.length;e++){var a=_pq[e];p(a)}window._pq={push:function(e){return p(e)}}},_pa.addListener=function(e,a){"undefined"==typeof _pa.callbacks[e]&&(_pa.callbacks[e]=[]),_pa.callbacks[e].push(a)},_pa.removeListener=function(e,a){for(var t=_pa.callbacks[e],n=t.length;n--;)t[n]===a&&t.splice(n,1)},_pa.pixelPlacer=function(){function e(){r=!0,t()}function a(e,a){r||a?n(e):p.push(e)}function t(){for(var e;e=p.pop();)n(e)}function n(e){var a=document.createElement("img");a.src=e,a.width=1,a.height=1,a.border=0,_pa.head.appendChild(a)}var r=!1,p=[];return{activate:e,place:a}}();var u={cd:function(){return _pa.crossDevice}};_pa.setPartners=function(e){var a,t;for(var n in e){if(a=e[n],t=!0,"object"==typeof a)for(var r;r<a.length;r++){var p=a[r];criteriaFunction=u[p],t=t&&criteriaFunction()}t&&_pa.pixelPlacer.place(_pa.paRtbHost+"cs/?partnerId="+n)}},_pa.init()}();	(function() {
	_pa.partner_tags = {"pa_rtb":true};
	function createPartnerTags(partner_tags){
		if (partner_tags.canned_banners != null){
			loadCannedBanners(partner_tags.canned_banners);
		}
	}
	
	
	
	
	
	
	createPartnerTags(_pa.partner_tags);
})();

(function(){
	if (_pa.initAfterLoad) {
		if (window.document && window.document.readyState === "complete") {
			_pa.ready.onload = true;
			_pa.fireWhenReady();
		} else {
			function hookLoad(handler) {
				if(window.addEventListener) {
					window.addEventListener("load", handler, false);
				} else if(window.attachEvent) {
					window.attachEvent("onload", handler);
				}
			}
			hookLoad(function() {
				_pa.ready.onload = true;
				_pa.fireWhenReady();
			});
		}
	} else {
		_pa.ready.onload = true;
		_pa.fireWhenReady();
	}
})();

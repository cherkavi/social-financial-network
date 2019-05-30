/*
 * elementId - id of element, which need to zoom
 * koef - zoom factor ( multiplier ) will be multiplied to zoom 
 */
function zoom_element(elementId, koef){
	var windowWidth=jQuery(window).width();
	var windowHeight=jQuery(window).height();
	var element=jQuery("#"+elementId);
	var elementWidth=element.width();
	var elementHeight=element.height();
	var zoom=1;
	if(windowHeight>windowWidth){
		// width
		zoom=windowWidth/elementWidth;
	}else{
		// height
		zoom=windowHeight/elementHeight;
	}
	zoom=zoom*koef;
	element.css('zoom',zoom);
	element.css('-moz-transform','scale('+zoom+')');
	element.css('-moz-transform-origin','0 0');
}

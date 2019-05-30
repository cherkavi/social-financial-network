/**
 * download icons from server into "img" element with attribute src
 * @var element - 
 */
function downloadImage(element, fileName){
	// TODO add logic for determinate file depends on size of screen 
	element.src="ImageIconsDownloader?filename="+encodeURIComponent(fileName);
}
/**
 *  move focus between elements
 */


function findElement(id){
	var sourceElement=document.getElementById(id);
	if(sourceElement){
		return sourceElement;
	}
	
	// check for new name
	sourceElement=document.getElementsByName(id);
	if(!sourceElement || !sourceElement[0]){
		return null;
	}
	return sourceElement[0];
}

/**
 * move focus to the next element after "Enter" key was pressed 
 */
function moveFocusAfterEnterButton(currentTextElementId, nextElementId){
	// find element
	var sourceElement=findElement(currentTextElementId);
	if(!sourceElement){
		console.log("can't find source element with ID: "+currentTextElementId);
		return;
	}
	
	var nextElement=findElement(nextElementId);
	if(!nextElement){
		console.log("can't find destination element with ID: "+nextElementId);
		return;
	}
	
	// console.log('assign function');
	$(sourceElement).bind('keydown', function(e) {
		if (e.keyCode == 13) {
			sourceElement.blur();
			nextElement.focus();
			return false;
		}		
	    return true;
	});

}

/**
 * press click on next element, after source element got "enter"
 */
function clickAfterEnterButton(currentTextElementId, nextElementId){
	// find element
	var sourceElement=findElement(currentTextElementId);
	if(!sourceElement){
		console.log("can't find source element with ID: "+currentTextElementId);
		return;
	}
	
	var nextElement=findElement(nextElementId);
	if(!nextElement){
		console.log("can't find destination element with ID: "+nextElementId);
		return;
	}
	
	// console.log('assign function');
	$(sourceElement).bind('keydown', function(e) {
		if (e.keyCode == 13) {
			nextElement.click();
			return false;
		}		
	    return true;
	});

}

/**
 * suppress "enter" button 
 * @param id
 */
function suppressEnterButton(id){
	// find element
	var sourceElement=document.getElementById(id);
	if(!sourceElement){
		console.log("can't find source element with ID: "+currentTextElementId);
		return;
	}

	$(sourceElement).bind('keydown', function(e) {
		if (e.keyCode == 13) {
			return false;
		}		
	    return true;
	});
	
}2
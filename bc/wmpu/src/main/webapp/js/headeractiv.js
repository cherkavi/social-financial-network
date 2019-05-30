var currElement;
function markElement(newElement){
  	 if (currElement) {
		currElement.style.backgroundColor = 'transparent'; 
		currElement.style.fontStyle = 'normal';
		currElement.style.fontWeight = 'normal';
		currElement.style.fontSize = '100%';
		currElement.style.border = '0px';
	}   	 
	 newElement.style.backgroundColor = '#F0F8FF';
	 newElement.style.fontWeight = 'bold';
  	 newElement.style.fontStyle = 'italic';
	 newElement.style.fontSize = '105%';
	 newElement.style.border = '1px inset maroon';	 
  	 currElement = newElement;
	 top.document.currBankElement = currElement;
}

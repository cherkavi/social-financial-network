function set_payer_receiver(element) {
			  var com_type = element.value;
			  var payer = element_payer[element.selectedIndex];
			  var receiver = element_receiver[element.selectedIndex];

			  var id_payer = "";
			  var name_payer = "";
			  var id_receiver = "";
			  var name_receiver = "";
			
			  if (cd_rel_type == 'SOCIETY-SHAREHOLDER' || cd_rel_type == 'SOCIETY-OTHER') {
				  if (payer == 'SOCIETY') {
				  	  id_payer = id_party1;
				      name_payer = name_party1;
				  } else {
					  id_payer = "";
					  name_payer = "";  
				  }
				  if (receiver == 'SOCIETY') {
					  id_receiver = id_party1;
					  name_receiver = name_party1;
				  } else {
					  id_receiver = "";
					  name_receiver = "";
				  }
			  } else if (cd_rel_type == 'SOCIETY-CARD_SELLER') {
				  if (payer == 'SOCIETY') {
				  	  id_payer = id_party1;
				      name_payer = name_party1;
				  } else if (payer == 'CARD_SELLER') {
					  id_payer = id_party2;
					  name_payer = name_party2;  
				  }
				  if (receiver == 'SOCIETY') {
					  id_receiver = id_party1;
					  name_receiver = name_party1;
				  } else if (receiver == 'CARD_SELLER') {
					  id_receiver = id_party2;
					  name_receiver = name_party2;
				  }
			  } else if (cd_rel_type == 'SOCIETY-DEALER') {
				  if (payer == 'SOCIETY') {
				  	  id_payer = id_party1;
				      name_payer = name_party1;
				  } else if (payer == 'DEALER') {
					  id_payer = id_party2;
					  name_payer = name_party2;  
				  }
				  if (receiver == 'SOCIETY') {
					  id_receiver = id_party1;
					  name_receiver = name_party1;
				  } else if (receiver == 'DEALER') {
					  id_receiver = id_party2;
					  name_receiver = name_party2;
				  }
			  } else if (cd_rel_type == 'SOCIETY-FINANCE_ACQUIRER') {
				  if (payer == 'SOCIETY') {
				  	  id_payer = id_party1;
				      name_payer = name_party1;
				  } else if (payer == 'FINANCE_ACQUIRER') {
					  id_payer = id_party2;
					  name_payer = name_party2;  
				  }
				  if (receiver == 'SOCIETY') {
					  id_receiver = id_party1;
					  name_receiver = name_party1;
				  } else if (receiver == 'FINANCE_ACQUIRER') {
					  id_receiver = id_party2;
					  name_receiver = name_party2;
				  }
			  } else if (cd_rel_type == 'SOCIETY-ISSUER') {
				  if (payer == 'SOCIETY') {
				  	  id_payer = id_party1;
				      name_payer = name_party1;
				  } else if (payer == 'ISSUER') {
					  id_payer = id_party2;
					  name_payer = name_party2;  
				  }
				  if (receiver == 'SOCIETY') {
					  id_receiver = id_party1;
					  name_receiver = name_party1;
				  } else if (receiver == 'ISSUER') {
					  id_receiver = id_party2;
					  name_receiver = name_party2;
				  }
			  } else if (cd_rel_type == 'SOCIETY-PARTNER') {
				  if (payer == 'SOCIETY') {
				  	  id_payer = id_party1;
				      name_payer = name_party1;
				  } else if (payer == 'PARTNER') {
					  id_payer = id_party2;
					  name_payer = name_party2;  
				  }
				  if (receiver == 'SOCIETY') {
					  id_receiver = id_party1;
					  name_receiver = name_party1;
				  } else if (receiver == 'PARTNER') {
					  id_receiver = id_party2;
					  name_receiver = name_party2;
				  }
			  } else if (cd_rel_type == 'SOCIETY-TECHNICAL_ACQUIRER') {
				  if (payer == 'SOCIETY') {
				  	  id_payer = id_party1;
				      name_payer = name_party1;
				  } else if (payer == 'TECHNICAL_ACQUIRER') {
					  id_payer = id_party2;
					  name_payer = name_party2;  
				  }
				  if (receiver == 'SOCIETY') {
					  id_receiver = id_party1;
					  name_receiver = name_party1;
				  } else if (receiver == 'TECHNICAL_ACQUIRER') {
					  id_receiver = id_party2;
					  name_receiver = name_party2;
				  }
			  } else if (cd_rel_type == 'SOCIETY-TERMINAL_MANUFACTURER') {
				  if (payer == 'SOCIETY') {
				  	  id_payer = id_party1;
				      name_payer = name_party1;
				  } else if (payer == 'TERMINAL_MANUFACTURER') {
					  id_payer = id_party2;
					  name_payer = name_party2;  
				  }
				  if (receiver == 'SOCIETY') {
					  id_receiver = id_party1;
					  name_receiver = name_party1;
				  } else if (receiver == 'TERMINAL_MANUFACTURER') {
					  id_receiver = id_party2;
					  name_receiver = name_party2;
				  }
			  } else if (cd_rel_type == 'DEALER-FINANCE_ACQUIRER') {
				  if (payer == 'DEALER') {
				  	  id_payer = id_party1;
				      name_payer = name_party1;
				  } else if (payer == 'FINANCE_ACQUIRER') {
					  id_payer = id_party2;
					  name_payer = name_party2;  
				  }
				  if (receiver == 'DEALER') {
					  id_receiver = id_party1;
					  name_receiver = name_party1;
				  } else if (receiver == 'FINANCE_ACQUIRER') {
					  id_receiver = id_party2;
					  name_receiver = name_party2;
				  }
			  } else if (cd_rel_type == 'DEALER-TECHNICAL_ACQUIRER') {
				  if (payer == 'DEALER') {
				  	  id_payer = id_party1;
				      name_payer = name_party1;
				  } else if (payer == 'TECHNICAL_ACQUIRER') {
					  id_payer = id_party2;
					  name_payer = name_party2;  
				  }
				  if (receiver == 'DEALER') {
					  id_receiver = id_party1;
					  name_receiver = name_party1;
				  } else if (receiver == 'TECHNICAL_ACQUIRER') {
					  id_receiver = id_party2;
					  name_receiver = name_party2;
				  }
			  }
			  document.getElementById('id_jur_prs_payer').value = id_payer;
			  document.getElementById('name_jur_prs_payer').value = name_payer;
			  document.getElementById('id_jur_prs_receiver').value = id_receiver;
			  document.getElementById('name_jur_prs_receiver').value = name_receiver;
			  
		  }
function lettersAndDigitsAndSlashOnly(evt) {
	evt = (evt) ? evt : event;
	var charCode = (evt.charCode) ? evt.charCode : ((evt.keyCode) ? evt.keyCode
			: ((evt.which) ? evt.which : -1));
	if (charCode == 8 || charCode == 47 || charCode == 32 || charCode == 37 || charCode == 39) {
		return true;
	} else if (charCode > 31 && (charCode < 48 || charCode > 57)
			&& (charCode < 65 || charCode > 90)
			&& (charCode < 97 || charCode > 122)) {
		// alert("Enter letters only.");
		return false;
	}
	return true;
};

function lettersAndDigitsOnly(evt) {
	evt = (evt) ? evt : event;
	var charCode = (evt.charCode) ? evt.charCode : ((evt.keyCode) ? evt.keyCode
			: ((evt.which) ? evt.which : -1));
	if (charCode == 8 || charCode == 32 || charCode == 37 || charCode == 39) {
		return true;
	} else if (charCode > 31 && (charCode < 48 || charCode > 57)
			&& (charCode < 65 || charCode > 90)
			&& (charCode < 97 || charCode > 122)) {
		// alert("Enter letters only.");
		return false;
	}
	return true;
};

function lettersAndSlashOnly(evt) {
	evt = (evt) ? evt : event;
	var charCode = (evt.charCode) ? evt.charCode : ((evt.keyCode) ? evt.keyCode
			: ((evt.which) ? evt.which : -1));
	if (charCode == 8 || charCode == 47 || charCode == 32 || charCode == 37 || charCode == 39) {
		return true;
	} else if (charCode > 31 && (charCode < 65 || charCode > 90)
			&& (charCode < 97 || charCode > 122)) {
		return false;
	}
	return true;
};

function digitsOnly(evt) {
	evt = (evt) ? evt : event;
	var charCode = (evt.charCode) ? evt.charCode : ((evt.keyCode) ? evt.keyCode : ((evt.which) ? evt.which : -1));
	
	if (charCode != 8 && charCode != 43 && charCode != -1 && evt.charCode != 0 && (charCode < 48 || charCode > 57)) {
		return false;
	}
	return true;
};

function isNumber(evt) {
	evt = (evt) ? evt : window.event;
	var charCode = (evt.which) ? evt.which : evt.keyCode;

	if (charCode > 31 && ((charCode < 48) || (charCode > 57)) && charCode!=46) {
		return false;
	}
	return true;
}

function digitsAndHypheOnly(evt) {
	evt = (evt) ? evt : event;
	var charCode = (evt.charCode) ? evt.charCode : ((evt.keyCode) ? evt.keyCode
			: ((evt.which) ? evt.which : -1));
	if (charCode != 8 && charCode != -1 && evt.charCode != 0 && charCode != 45 && charCode != 37 && charCode != 39 && (charCode < 48 || charCode > 57)) {
		return false;
	}
	return true;
};

function digitsAndDotOnly(evt) {
	evt = (evt) ? evt : event;
	var charCode = (evt.charCode) ? evt.charCode : ((evt.keyCode) ? evt.keyCode
			: ((evt.which) ? evt.which : -1));
	if (charCode != 8 && charCode != -1 && evt.charCode != 0 && charCode != 46 && charCode != 37 && charCode != 39 && (charCode < 48 || charCode > 57)) {
		return false;
	}
	return true;
};

function AlphaOnly(evt) {
	evt = (evt) ? evt : event;
	var charCode = (evt.charCode) ? evt.charCode : ((evt.keyCode) ? evt.keyCode
			: ((evt.which) ? evt.which : -1));
	if (charCode == 8 || charCode == 32 || charCode == 37 || charCode == 39) {
		return true;
	} else if (charCode > 31 && (charCode < 65 || charCode > 90)
			&& (charCode < 97 || charCode > 122)) {
		// alert("Enter letters only.");
		return false;
	}
	return true;
};

function lettersAndHyphenOnly(evt) {
	evt = (evt) ? evt : event;
	var charCode = (evt.charCode) ? evt.charCode : ((evt.keyCode) ? evt.keyCode
			: ((evt.which) ? evt.which : -1));
	if (charCode == 8 || charCode == 45 || charCode == 32 || charCode == 37 || charCode == 39 || charCode == 40 || charCode == 41) {
		return true;
	} else if (charCode > 31 && (charCode < 65 || charCode > 94)
			&& (charCode < 97 || charCode > 125) && (charCode ==41 || charCode==40 || charCode==91 || charCode==93 || charCode==123 || charCode==125  ) ) {
		// alert("Enter letters only.");
		return false;
	}
	return true;
};

function lettersAndHyphenAndSlashOnly(evt) {
	evt = (evt) ? evt : event;
	var charCode = (evt.charCode) ? evt.charCode : ((evt.keyCode) ? evt.keyCode
			: ((evt.which) ? evt.which : -1));
	if (charCode == 8 || charCode == 45 || charCode == 47 || charCode == 32 || charCode == 37 || charCode == 39) {
		return true;
	} else if (charCode > 31 && (charCode < 65 || charCode > 90)
			&& (charCode < 97 || charCode > 122)) {
		// alert("Enter letters only.");
		return false;
	}
	return true;
};

function fixDate(dtFrom, dtTo) {
	$(dtTo).datepicker('option', 'minDate', $(dtFrom).datepicker("getDate"));
	var date2 = $(dtFrom).datepicker("getDate");
	date2.setDate(date2.getDate() + 1);
	$(dtTo).datepicker('setDate', date2);
};

function executeOtherFunc(id) {

}
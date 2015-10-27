 $(document).ready(function() {
	$('#object_countryId').change(function() {
		var elementCountry = document.getElementById("object_countryId");
		var countryIdValue = elementCountry.value;
		var selectActionStates = #{jsAction @CEPController.getStatesByCountryId(':countryId') /};
    	$('#object_stateId').load(
    		selectActionStates({countryId: countryIdValue}));
	});						
	$('#object_stateId').change(function() {
		var elementState = document.getElementById("object_stateId");
		var stateIdValue = elementState.value;
		var selectActionCities = #{jsAction @CEPController.getCitiesByCountryIdAndStateId(':stateId') /};
    	$('#object_cityId').load(
    		selectActionCities({stateId: stateIdValue}));
	});
});
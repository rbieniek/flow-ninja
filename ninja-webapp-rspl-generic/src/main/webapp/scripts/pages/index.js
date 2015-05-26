/**
 * 
 */
angular.module('org.flowninja.rpsl.lookup', ['restangular', 'ui.bootstrap']
).provider('LookupService', function() {
    var config = {
            restUri: ""
    };
	return {
        setRestUri : function(restUri) {
            config.restUri = restUri || config.restUri;
        },
		$get : function() {
			return {
				restUri : function() {
                    return config.restUri;
                }
			};
		}
	};
}).controller('LookupController', function($scope, Restangular, LookupService) {
});
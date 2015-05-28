/**
 * 
 */
angular.module('org.flowninja.rpsl.lookup', ['restangular', 'ui.bootstrap']
).provider('LookupService', function() {
    var config = {
            restUri: "",
            status200: "",
            status400: "",
            status404: "",
            statusAny: "",
    };
	return {
        setRestUri : function(restUri) {
            config.restUri = restUri || config.restUri;
        },
        setStatus200 : function(status) {
        	config.status200 = status || config.status200;
        },
        setStatus400 : function(status) {
        	config.status400 = status || config.status400;
        },
        setStatus404 : function(status) {
        	config.status404 = status || config.status404;
        },
        setStatusAny : function(status) {
        	config.statusAny = status || config.statusAny;
        },
		$get : function() {
			return {
				restUri : function() {
                    return config.restUri;
                },
                status200 : function() {
                	return config.status200;
                },
                status400 : function() {
                	return config.status400;
                },
                status404 : function() {
                	return config.status404;
                },
                statusAny : function() {
                	return config.statusAny;
                }
			};
		}
	};
}).controller('LookupController', function($scope, Restangular, LookupService) {
	$scope.ipAddress = null;
	$scope.networkInfo = {
			status: null,
			cidr: null,
			networkName: null,
			country: null,
			rir: null,
			source: null
	};

	$scope.getFormGroupCssClasses = function(ngModelController) {
		return {
	      'has-error': ngModelController.$invalid && ngModelController.$dirty,
	      'has-success': ngModelController.$valid && ngModelController.$dirty
	    };
	};
	$scope.getFormFeedbackCssClasses= function(ngModelController) {
		return {
			'glyphicon-remove': ngModelController.$invalid && ngModelController.$dirty,
			'glyphicon-ok': ngModelController.$valid && ngModelController.$dirty
		};
	};
	$scope.getFormFeedbackVisible= function(ngModelController) {
		return ngModelController.$invalid;
	}
	$scope.canSave = function() {
	    return $scope.addrForm.$dirty && $scope.addrForm.$valid;
	};
	$scope.clearNetworkInfo = function() {
		$scope.networkInfo = {
				status: null,
				cidr: null,
				networkName: null,
				country: null,
				rir: null,
				source: null
		};
	};
	
	$scope.resolveNetworkInfo = function() {
		
	};
});
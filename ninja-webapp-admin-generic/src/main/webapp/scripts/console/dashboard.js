/**
 * 
 */
angular.module('org.flowninja.admin.console.dashboard', ['restangular', 'ui.bootstrap']
).provider('DashboardService', function() {
	return {
		$get : function() {
			return {
			};
		}
	};
}).controller('DashboardController', 
		function($scope, Restangular, DashboardService) {
	$scope.reload = function() {
	};
	
	$scope.reload();
});
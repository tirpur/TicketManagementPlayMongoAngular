/*
 * Created by shri on 12/12/15.
 */
var app = angular.module("app", ["ngResource"])
	.constant("apiUrl", "http://localhost:9000\:9000/api") // to tell AngularJS that 9000 is not a dynamic parameter
	.config(["$routeProvider", function($routeProvider) {
		return $routeProvider.when("/", {
			templateUrl: "/views/main",
			controller: "ListCtrl"
		}).when("/create", {
			templateUrl: "/views/detail",
			controller: "CreateCtrl"
	    }).when("/edit/:id", {
			templateUrl: "/views/detail",
			controller: "EditCtrl"
	    }).otherwise({
			redirectTo: "/"
		});
	}
	]).config([
	"$locationProvider", function($locationProvider) {
		return $locationProvider.html5Mode(true).hashPrefix("!"); // enable the new HTML5 routing and histoty API
	}
]);

// the global controller
app.controller("AppCtrl", ["$scope", "$location", "$resource", "apiUrl", function($scope, $location, $resource, apiUrl) {
	$scope.go = function (path) {
		$location.path(path);
	};
		var TicketStates = $resource(apiUrl+"/states");
        $scope.ticketStates = TicketStates.query();
}]);

// the list controller
app.controller("ListCtrl", ["$scope", "$resource", "apiUrl", function($scope, $resource, apiUrl) {
	var Tickets = $resource(apiUrl + "/tickets"); // a RESTful-capable resource object
	$scope.tickets = Tickets.query(); // for the list of tickets in public/html/main.html
}]);

// the create controller
app.controller("CreateCtrl", ["$scope", "$resource", "$timeout", "apiUrl", function($scope, $resource, $timeout, apiUrl) {
    $scope.closeAllowed = function() {
    	return angular.equals($scope.ticket.status.name,'Closed'); //Allow closing the tickets in edit i.e after issue is New, Open
    }
//    $scope.ticket = {};
//    $scope.ticket.status = {};
    var Assignees = $resource(apiUrl+"/assignees");
    $scope.assignees = Assignees.query();
    var Csrs = $resource(apiUrl+"/csrs");
    $scope.csrs = Csrs.query();
    var Categories = $resource(apiUrl+"/categories");
    $scope.categories = Categories.query();

//	var TicketStates = $resource(apiUrl+"/states");
//    $scope.ticketStates = TicketStates.query();
//      $scope.ticket.status.setDefault = $scope.ticketStates[1];

//    $scope.ticket.status = $scope.ticketStates[0]

//    $scope.$watch($scope.ticketStates, function(newVal) {
//        if (angular.isUndefined(newVal) || newVal == null) return;
//              $scope.ticket.status.setDefault = $scope.ticketStates[1];

//        $scope.ticket.status = $scope.ticketStates[0];
//        alert($scope.ticket.status.name)
//    });
//
	$scope.newOrOpen = function() {
//		return angular.equals($scope.ticket.status.name, 'Open') &&
		//Disable saving the data when it is assigned to a person and state changed to New
		//Or without assigning to a person state is changed to Open
		return (angular.isUndefined($scope.ticket.assignedto) && angular.equals($scope.ticket.status.name,'Open')) ||
			   (angular.isUndefined($scope.ticket.assignedto) && angular.equals($scope.ticket.status.name,'Closed')) ||
		        (angular.isDefined($scope.ticket.assignedto) && angular.equals($scope.ticket.status.name,'New'));
	};
//    $scope.ticket.status = $scope.ticketStates[0]
	// to save a ticket
	$scope.save = function() {
		var CreateTicket = $resource(apiUrl + "/tickets/new"); // a RESTful-capable resource object
		CreateTicket.save($scope.ticket); // $scope.ticket comes from the detailForm in public/html/detail.html
		$timeout(function() { $scope.go('/'); }); // go back to public/html/main.html
	};
}]);

// the edit controller
app.controller("EditCtrl", ["$scope", "$resource", "$routeParams", "$timeout", "apiUrl", function($scope, $resource, $routeParams, $timeout, apiUrl) {
    var Assignees = $resource(apiUrl+"/assignees");
    $scope.isClosed = function() {
    	return angular.equals($scope.ticket.status.name,'Closed'); //Allow closing the tickets in edit i.e after issue is New, Open
    }
    //make the user allow to close the issue
    $scope.closeAllowed = function() {
    	return false; //Allow closing the tickets in edit i.e after issue is New, Open
    }
//    $scope.ticketClosed = true
    $scope.assignees = Assignees.query();
    var Csrs = $resource(apiUrl+"/csrs");
    $scope.csrs = Csrs.query();
    var Categories = $resource(apiUrl+"/categories");
    $scope.categories = Categories.query();



//	var TicketStates = $resource(apiUrl+"/states");
//    $scope.ticketStates = TicketStates.query();


	var ShowTicket = $resource(apiUrl + "/tickets/:id", {id:"@id"}); // a RESTful-capable resource object
	if ($routeParams.id) {
		// retrieve the corresponding ticket from the database
		// $scope.ticket.id.$oid is now populated so the Delete button will appear in the detailForm in public/html/detail.html
		$scope.ticket = ShowTicket.get({id: $routeParams.id});
		$scope.dbContent = ShowTicket.get({id: $routeParams.id}); // this is used in the noChange function
	}
	
	// decide whether to enable or not the button Save in the detailForm in public/html/detail.html 
	$scope.noChange = function() {
		return angular.equals($scope.ticket, $scope.dbContent);
	};

	$scope.newOrOpen = function() {
//		return angular.equals($scope.ticket.status.name, 'Open') &&
		//Disable saving the data when it is assigned to a person and state changed to New
		//Or without assigning to a person state is changed to Open
		return (angular.isUndefined($scope.ticket.assignedto) && angular.equals($scope.ticket.status.name,'Open')) ||
			   (angular.isUndefined($scope.ticket.assignedto) && angular.equals($scope.ticket.status.name,'Closed')) ||
		        (angular.isDefined($scope.ticket.assignedto) && angular.equals($scope.ticket.status.name,'New'));
	};

	$scope.ticketClosed = function() {
		return angular.equals($scope.ticket.status.name, 'Closed');
	};
	// to update a ticket
	$scope.save = function() {
		var UpdateTicket = $resource(apiUrl + "/tickets/" + $routeParams.id); // a RESTful-capable resource object
		UpdateTicket.save($scope.ticket); // $scope.ticket comes from the detailForm in public/html/detail.html
		$timeout(function() { $scope.go('/'); }); // go back to public/html/main.html
	};

	// to close a ticket
	$scope.close = function() {
		var UpdateTicket = $resource(apiUrl + "/tickets/" + $routeParams.id); // a RESTful-capable resource object
		$scope.$apply(function(){
        $scope.ticket.status = 'Closed';
        }
        )
//		$scope.ticket.status = 'Closed';
//		$scope.ticketClosed = true;
		UpdateTicket.save($scope.ticket); // $scope.ticket comes from the detailForm in public/html/detail.html
		$timeout(function() { $scope.go('/'); }); // go back to public/html/main.html
	};
	
	// to delete a ticket
	$scope.delete = function() {
		var DeleteTicket = $resource(apiUrl + "/tickets/" + $routeParams.id); // a RESTful-capable resource object
		DeleteTicket.delete(); // $scope.ticket comes from the detailForm in public/html/detail.html
		$timeout(function() { $scope.go('/'); }); // go back to public/html/main.html
	};
}]);
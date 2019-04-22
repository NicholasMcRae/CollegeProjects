(function(app) {
var CustomerCtrl = function($scope, $location) {
    $scope.myCustomer = {
      name: 'David',
      street: '123 Anywhere St.',
      combined: ''
    };
    
    $scope.showName = function() {
        $scope.myCustomer.combined = $scope.myCustomer.name + ', ' + $scope.myCustomer.street;
        alert($scope.myCustomer.combined);
    }
};
app.controller('CustomerCtrl', ['$scope', CustomerCtrl]);
}(angular.module('case1')));

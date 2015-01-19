(function(app) {
var MenuCtrl = function($scope, $location) {
    var init = function() {
        $scope.isActive = function(viewLocation) {
            return viewLocation === $location.path();
        };
    }; //init
    init();
};
app.controller('MenuCtrl', ['$scope', '$location', MenuCtrl]);
}(angular.module('case1')));

